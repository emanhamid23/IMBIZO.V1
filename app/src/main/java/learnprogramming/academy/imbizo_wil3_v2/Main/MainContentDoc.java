package learnprogramming.academy.imbizo_wil3_v2.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.source.UriSource;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.errorprone.annotations.Var;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import learnprogramming.academy.imbizo_wil3_v2.Create.CreateLecture;
import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class MainContentDoc extends AppCompatActivity {

    FirebaseDatabase databaseCategories;
    FirebaseStorage storage;
    DatabaseReference refCategories;
    StorageReference storageRef;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_content_doc);

        pdfView = findViewById(R.id.pdfView);

        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Lectures");
        storage = FirebaseStorage.getInstance();

        refCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot s : snapshot.getChildren()){
                    if(s.child("userName").getValue().toString().equals(HomeFragment.usernameEmail.toString()) && s.child("lectureName").getValue().toString().equals(MainLectures.lectureName) && s.child("categoryType").getValue().toString().equals(HomeFragment.categoryName) ) {

                        new RetrivePDFfromUrl().execute(s.child("lecturePDF").getValue().toString());



                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.checkView = 2;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainContentDoc.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }


    }
