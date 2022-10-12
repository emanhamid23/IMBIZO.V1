package learnprogramming.academy.imbizo_wil3_v2.Create;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class CreateLectureDoc extends AppCompatActivity {

    //Variables initialized for accepting images from user
    private ImageView profilePic;
    public Uri imageUri;
    public Bitmap imageBitmap;

    //Variables initialized for Button Submit
    Button btnCreateLecture, btnCP;
    EditText edtxLectureName;

    //Database related variables
    FirebaseDatabase databaseCategories;
    FirebaseStorage storage;
    DatabaseReference refCategories;
    StorageReference storageRef;
    ProgressDialog progressDialog;

    String usernameEmail;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    static String linkPDF;
    static String lectureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture_doc);

        storage = FirebaseStorage.getInstance();

        //Assigning image view to variable below
        profilePic = findViewById(R.id.imageUploadLecture);

        //viewPic = findViewById(R.id.imageView);
        btnCP = findViewById(R.id.btnCP);
        btnCreateLecture = findViewById(R.id.btnCreateLecture);
        edtxLectureName = findViewById(R.id.edtxLectureNameID);
        progressDialog = new ProgressDialog(this);

        //Setting a event action on image view.

        //Setting a event action on image view.
        btnCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();

            }
        });

        btnCreateLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lectureName = edtxLectureName.getText().toString();
                try{
                    InsertData();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateLectureDoc.this, "Please Add A Name", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //ALL THE STRINGS ARE NOT SUPPOSED TO BE HARDCODED BUT NEEDS TO COME FROM A VARIABLE ***************
    public void InsertData() throws Exception
    {
        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Lectures");

        String[] tokens = HomeFragment.usernameEmail.split("@");



        if(lectureName.equals(""))
        {
            throw new Exception();
        }

        progressDialog.setTitle("Uploading");
        progressDialog.show();

        try {

            storageRef = FirebaseStorage.getInstance().getReference().child("LecturePDF").child(imageUri.getLastPathSegment());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String t = task.getResult().toString();
                                            linkPDF = t;

                                            DatabaseReference newPost = refCategories.child(lectureName+tokens[0]+ HomeFragment.categoryName);

                                            //Code Attributtion
                                            //Author: GeeksforGeeks
                                            //Website:GeeksforGeeks
                                            //Topic:How to Save Data to the Firebase Realtime Database in Android?
                                            //Link:https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/#:~:text=Run%20the%20app%20and%20make,added%20to%20our%20Firebase%20Database.


                                            newPost.child("categoryType").setValue(HomeFragment.categoryName);
                                            newPost.child("userName").setValue(HomeFragment.usernameEmail.toString());
                                            newPost.child("lectureName").setValue(lectureName);
                                            newPost.child("typeOfContent").setValue("Doc");
                                            newPost.child("lecturePDF").setValue(t);

                                            progressDialog.dismiss();

                                            finish();

                                            Intent intent = new Intent(CreateLectureDoc.this, MainActivity.class);
                                            startActivity(intent);


                                        }
                                    });
                        }
                    });
        }
        catch (NullPointerException e)
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Add A Picture!", Toast.LENGTH_LONG).show();
        }
    }

//    public void InsertData2()
//    {
//
//        databaseCategories = FirebaseDatabase.getInstance();
//        refCategories = databaseCategories.getReference("ContentDetails");
//
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        usernameEmail = currentUser.getEmail();
//
//        String[] tokens = usernameEmail.split("@");
//
//        DatabaseReference newPost = refCategories.child(lectureName+"Content"+tokens[0]+ HomeFragment.categoryName);
//
//        //Code Attributtion
//        //Author: GeeksforGeeks
//        //Website:GeeksforGeeks
//        //Topic:How to Save Data to the Firebase Realtime Database in Android?
//        //Link:https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/#:~:text=Run%20the%20app%20and%20make,added%20to%20our%20Firebase%20Database.
//
//
//        newPost.child("userName").setValue(HomeFragment.usernameEmail.toString());
//        newPost.child("link").setValue(linkPDF);
//        newPost.child("lectureName").setValue(lectureName);
//        newPost.child("categoryType").setValue(HomeFragment.categoryName);
//    }



    private void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            Toast.makeText(this, "Photo added", Toast.LENGTH_LONG).show();
        }

    }

}