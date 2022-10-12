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

public class CreateCategory extends AppCompatActivity {

    //Variables initialized for accepting images from user
    private ImageView profilePic;
    public Uri imageUri;
    public Bitmap imageBitmap;

    //Variables initialized for Button Submit
    Button btnCreateCat, btnTP, btnCP;
    EditText edtxCatName;

    //Database related variables
    FirebaseDatabase databaseCategories;
    FirebaseStorage storage;
    DatabaseReference refCategories;
    StorageReference storageRef;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Categories");
        storage = FirebaseStorage.getInstance();


        //viewPic = findViewById(R.id.imageView);
        profilePic = findViewById(R.id.imageUpload);
        btnCP = findViewById(R.id.btnCP);
        btnTP =  findViewById(R.id.btnTP);
        btnCreateCat = findViewById(R.id.btnCreateCat);
        edtxCatName = findViewById(R.id.edtxCatNameID);
        progressDialog = new ProgressDialog(this);

        //Setting a event action on image view.
        btnTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Code Attributtion
                //Author: Deep Singh
                //Website: Youtube
                //Topic:How to upload image using camera in Firebase storage
                //Link:https://www.youtube.com/watch?v=ofO21jEKeig&list=PLC1Om_XWjGDVdbC5X7AemWKth0m82zNtT&index=3&t=503s

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


        btnCreateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    InsertData();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateCategory.this, "Please Add A Name", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //ALL THE STRINGS ARE NOT SUPPOSED TO BE HARDCODED BUT NEEDS TO COME FROM A VARIABLE ***************
    public void InsertData() throws Exception
    {

        String[] tokens = HomeFragment.usernameEmail.split("@");


        String name = (edtxCatName.getText().toString());

            if(name.equals(""))
            {
                throw new Exception();
            }

            progressDialog.setTitle("Uploading");
            progressDialog.show();
        try {
            storageRef = FirebaseStorage.getInstance().getReference().child("CatImage").child(imageUri.getLastPathSegment());


            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String t = task.getResult().toString();

                                            DatabaseReference newPost = refCategories.child(name + tokens[0]);

                                            //Code Attributtion
                                            //Author: GeeksforGeeks
                                            //Website:GeeksforGeeks
                                            //Topic:How to Save Data to the Firebase Realtime Database in Android?
                                            //Link:https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/#:~:text=Run%20the%20app%20and%20make,added%20to%20our%20Firebase%20Database.

                                            newPost.child("name").setValue(name);
                                            newPost.child("image").setValue(t);
                                            newPost.child("username").setValue(HomeFragment.usernameEmail.toString());

                                            progressDialog.dismiss();

                                            finish();

                                            Intent intent = new Intent(CreateCategory.this, MainActivity.class);
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



    //Allows the user to select images from current device
    private void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    private void takePicture()
    {
        //Code Attributtion
        //Author: Deep Singh
        //Website: Youtube
        //Topic:How to upload image using camera in Firebase storage

        //Link:https://www.youtube.com/watch?v=ofO21jEKeig&list=PLC1Om_XWjGDVdbC5X7AemWKth0m82zNtT&index=3&t=503s
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateCategory.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    //processes the image from the phone to the application.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            Toast.makeText(this, "Photo added", Toast.LENGTH_LONG);
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {


            //Code Attributtion
            //Author: Deep Singh
            //Website: Youtube
            //Topic:How to upload image using camera in Firebase storage
            //Link:https://www.youtube.com/watch?v=ofO21jEKeig&list=PLC1Om_XWjGDVdbC5X7AemWKth0m82zNtT&index=3&t=503s

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getApplicationContext(),photo);
            Picasso.get().load(getImageUri(getApplicationContext(),photo)).into(profilePic);
            Toast.makeText(this, "Photo added", Toast.LENGTH_LONG);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        //Code Attributtion
        //Author: Deep Singh
        //Website: Youtube
        //Topic:How to upload image using camera in Firebase storage
        //Link:https://www.youtube.com/watch?v=ofO21jEKeig&list=PLC1Om_XWjGDVdbC5X7AemWKth0m82zNtT&index=3&t=503s

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}