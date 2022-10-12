package learnprogramming.academy.imbizo_wil3_v2.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import learnprogramming.academy.imbizo_wil3_v2.Adapters.LectureAdapter;
import learnprogramming.academy.imbizo_wil3_v2.Create.AddContent;
import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.OOP.Lecture;
import learnprogramming.academy.imbizo_wil3_v2.OOP.RecyclerItemClickListener;
import learnprogramming.academy.imbizo_wil3_v2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLectures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLectures extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener {

    FirebaseDatabase databaseCategories, databaseItemDetails;
    FirebaseStorage storage;
    DatabaseReference refCategories, refDetails;
    StorageReference storageRef;
    RecyclerView recyclerView;
    LectureAdapter lectureAdapter;
    List<Lecture> lectureList;
    private final String TEXT_CONTENTS = "TextContents";
    public static String lectureName;
    EditText searchItem;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.checkView = 1;
    }


    public MainLectures() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainLectures.
     */
    // TODO: Rename and change types and number of parameters
    public static MainLectures newInstance(String param1, String param2) {
        MainLectures fragment = new MainLectures();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadData();

        FloatingActionButton b = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getActivity(), AddContent.class));
                    getActivity().finish();

                }
            });
        }

    }


    public void loadData() {

        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Lectures");
        storage = FirebaseStorage.getInstance();

        String test = HomeFragment.categoryName;

        refCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (ds.child("categoryType").getValue().equals(test) && ds.child("userName").getValue().equals(HomeFragment.usernameEmail.toString())) {
                        Lecture l = ds.getValue(Lecture.class);
                        lectureList.add(l);
                        lectureAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_lectures, container, false);

        searchItem = rootView.findViewById(R.id.searchItem);
        MaterialToolbar topAppBar = rootView.findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sortAsc:
                        filterAsc();
                        return true;
                    case R.id.sortDesc:
                        filterDesc();
                        return true;
                    default:
                        return true;
                }
            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        recyclerView = rootView.findViewById(R.id.lectureRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, this));

        lectureList = new ArrayList<Lecture>();
        lectureAdapter = new LectureAdapter(getContext(), lectureList);
        recyclerView.setAdapter(lectureAdapter);

        // Inflate the layout for this fragment
        return rootView;


    }

    private void filter(String text){
        ArrayList<Lecture> filteredList = new ArrayList<Lecture>();

        for(Lecture item : lectureList){
            if(item.getLectureName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        lectureAdapter.filterList(filteredList);
    }

    private void filterAsc()
    {
        ArrayList<Lecture> filteredList = new ArrayList<Lecture>();

        for(Lecture item : lectureList){

            filteredList.add(item);
            Collections.sort(filteredList, Lecture.StuNameComparatorAsc);

        }
        lectureAdapter.filterList(filteredList);
    }

    private void filterDesc()
    {
        ArrayList<Lecture> filteredList = new ArrayList<Lecture>();

        for(Lecture item : lectureList){

            filteredList.add(item);
            Collections.sort(filteredList, Lecture.StuNameComparatorDesc);

        }
        lectureAdapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(View view, int position) {

        Lecture l = LectureAdapter.lectureList.get(position);
        lectureName = l.getLectureName();

        if(l.getTypeOfContent().equals("Video")) {
            startActivity(new Intent(getActivity(), MainContent.class));
            getActivity().finish();
        }
        if(l.getTypeOfContent().equals("Doc")) {
            startActivity(new Intent(getActivity(), MainContentDoc.class));
            getActivity().finish();

        }

    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onItemLongClick(View view, int position) {

        AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(getActivity());

        deleteConfirm.setMessage("Do you to delete this item?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Lecture l = LectureAdapter.lectureList.get(position);
                        lectureName = l.getLectureName();
                        String[] tokens = HomeFragment.usernameEmail.split("@");

                        databaseCategories = FirebaseDatabase.getInstance();
                        refCategories = databaseCategories.getReference("CategoryItems");
                        refCategories.child(lectureName+tokens[0]+HomeFragment.categoryName).removeValue();

                        databaseItemDetails = FirebaseDatabase.getInstance();
                        refDetails = databaseItemDetails.getReference("ItemDetails");

                        refDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    if(ds.child("CategoryItem").getValue().equals(lectureName) && ds.child("username").getValue().equals(HomeFragment.usernameEmail))
                                    {
                                        String test = ds.getKey().toString();
                                        refDetails.child(test).removeValue();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        getActivity().recreate();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });



        AlertDialog alert = deleteConfirm.create();
        alert.setTitle("DELETE");
        alert.show();


    }

}