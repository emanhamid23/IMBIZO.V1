package learnprogramming.academy.imbizo_wil3_v2.Create;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class CreateLecture extends AppCompatActivity {

    //Variables initialized for accepting images from user
    private ImageView profilePic, viewPic;
    public Uri imageUri;

    //Variables initialized for Button Submit
    Button btnCreateLecture , btnTP, btnCP;
    EditText edtxLectureName, edtxLink;

    //Database related variables
    FirebaseDatabase databaseCategories;
    FirebaseStorage storage;
    DatabaseReference refCategories;
    StorageReference storageRef;
    ProgressDialog progressDialog;

    String usernameEmail;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    static String link;
    static String lectureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);



        storage = FirebaseStorage.getInstance();

        //Assigning image view to variable below
        profilePic = findViewById(R.id.imageUploadLecture);

        //viewPic = findViewById(R.id.imageView);
        btnCP = findViewById(R.id.btnCP);
        btnTP =  findViewById(R.id.btnTP);
        btnCreateLecture = findViewById(R.id.btnCreateLecture);
        edtxLectureName = findViewById(R.id.edtxLectureNameID);
        edtxLink = findViewById(R.id.edtxLink);
        progressDialog = new ProgressDialog(this);

        //Setting a event action on image view.
        btnTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

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

                link = edtxLink.getText().toString();
                lectureName = edtxLectureName.getText().toString();
                try{
                    InsertData();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateLecture.this, "Please Add A Name", Toast.LENGTH_LONG).show();
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

            storageRef = FirebaseStorage.getInstance().getReference().child("LectureImage").child(imageUri.getLastPathSegment());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String t = task.getResult().toString();

                                            DatabaseReference newPost = refCategories.child(lectureName+tokens[0]+HomeFragment.categoryName);

                                            //Code Attributtion
                                            //Author: GeeksforGeeks
                                            //Website:GeeksforGeeks
                                            //Topic:How to Save Data to the Firebase Realtime Database in Android?
                                            //Link:https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/#:~:text=Run%20the%20app%20and%20make,added%20to%20our%20Firebase%20Database.


                                            newPost.child("categoryType").setValue(HomeFragment.categoryName);
                                            newPost.child("userName").setValue(HomeFragment.usernameEmail.toString());
                                            newPost.child("lectureName").setValue(lectureName);
                                            newPost.child("lectureImage").setValue(t);
                                            newPost.child("typeOfContent").setValue("Video");
                                            newPost.child("link").setValue(link);

                                            progressDialog.dismiss();

                                            finish();

                                            Intent intent = new Intent(CreateLecture.this, MainActivity.class);
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
//        newPost.child("link").setValue(link);
//        newPost.child("lectureName").setValue(lectureName);
//        newPost.child("categoryType").setValue(HomeFragment.categoryName);
//    }



    private void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    private void takePicture()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateLecture.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    //processes the image from the phone to the application.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            Toast.makeText(this, "Photo added", Toast.LENGTH_LONG).show();
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getApplicationContext(),photo);
            Picasso.get().load(getImageUri(getApplicationContext(),photo)).into(profilePic);
            Toast.makeText(this, "Photo added", Toast.LENGTH_LONG).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}