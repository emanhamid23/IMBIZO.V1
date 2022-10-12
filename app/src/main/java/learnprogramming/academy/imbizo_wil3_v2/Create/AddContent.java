package learnprogramming.academy.imbizo_wil3_v2.Create;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class AddContent extends AppCompatActivity {

    //Variables initialized for Button Submit
    Button btnAddProperty;
    RadioButton rbVideo, rbPDF;

    //Database related variables
    FirebaseDatabase databaseCategories;
    DatabaseReference refCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        //method that makes a popup window
        popUpWindow();

        //viewPic = findViewById(R.id.imageView);
        btnAddProperty = findViewById(R.id.btnAddProperty);
        rbVideo = findViewById(R.id.rbVideo);
        rbPDF = findViewById(R.id.rbPDF);


        btnAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(rbVideo.isChecked())
                {
                    try
                    {

                        finish();

                        Intent intent = new Intent(AddContent.this, CreateLecture.class);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AddContent.this, "Please Add A Name", Toast.LENGTH_LONG).show();
                    }


                }
                if(rbPDF.isChecked())
                {
                    try
                    {


                        finish();

                        Intent intent = new Intent(AddContent.this, CreateLectureDoc.class);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AddContent.this, "Please Add A Name", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddContent.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }


    public void popUpWindow()
    {
        //Code below for pop up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width* .8), (int) (height*.6));
    }


}