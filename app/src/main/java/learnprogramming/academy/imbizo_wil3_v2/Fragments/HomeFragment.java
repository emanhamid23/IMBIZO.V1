package learnprogramming.academy.imbizo_wil3_v2.Fragments;

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
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import learnprogramming.academy.imbizo_wil3_v2.Adapters.CategoryAdapter;
import learnprogramming.academy.imbizo_wil3_v2.Create.CreateCategory;
import learnprogramming.academy.imbizo_wil3_v2.Main.MainLectures;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.OOP.Category;
import learnprogramming.academy.imbizo_wil3_v2.OOP.RecyclerItemClickListener;
import learnprogramming.academy.imbizo_wil3_v2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener  {



    FirebaseDatabase databaseCategories, databaseCategoryItems, databaseItemDetails;
    DatabaseReference refCategories, refItems, refDetails;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    List<Category> categoryDList;
    public static String usernameEmail;
    public static String categoryName;

    EditText searchCategory;
    Button buttonAsc, buttonDesc;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.checkView = 0;
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usernameEmail = currentUser.getEmail();

        loadData();


        FloatingActionButton b = (FloatingActionButton)  getActivity().findViewById(R.id.fab);
        if(b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getActivity(), CreateCategory.class));
                    getActivity().finish();

                }
            });
        }



    }

    public void loadData() {

        databaseCategories = FirebaseDatabase.getInstance();
        refCategories = databaseCategories.getReference("Categories");

        refCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Code Attributtion
                //Author: Uknown
                //Website: Firebase Documentation
                //Topic:Read and Write Data on Android
                //Link:https://firebase.google.com/docs/database/android/read-and-write

                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.child("username").getValue().equals(usernameEmail)) {
                        Category cat = ds.getValue(Category.class);
                        categoryDList.add(cat);
                        categoryAdapter.notifyDataSetChanged();
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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Code Attributtion
        //Author: Unknown
        //Website: CODEPATH
        //Topic:Using the RecyclerView
        //Link:https://guides.codepath.com/android/using-the-recyclerview

        searchCategory = rootView.findViewById(R.id.searchCat);
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



        searchCategory.addTextChangedListener(new TextWatcher() {
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

        recyclerView = rootView.findViewById(R.id.catRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, this));

        categoryDList = new ArrayList<Category>();
        categoryAdapter = new CategoryAdapter(this.getActivity(), categoryDList);
        recyclerView.setAdapter(categoryAdapter);



        // Inflate the layout for this fragment
        return rootView;


    }

    private void filter(String text){
        ArrayList<Category> filteredList = new ArrayList<Category>();

        for(Category item : categoryDList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        categoryAdapter.filterList(filteredList);
    }

    private void filterAsc()
    {
        ArrayList<Category> filteredList = new ArrayList<Category>();

        for(Category item : categoryDList){

            filteredList.add(item);
            Collections.sort(filteredList, Category.StuNameComparatorAsc);

        }
        categoryAdapter.filterList(filteredList);
    }

    private void filterDesc()
    {
        ArrayList<Category> filteredList = new ArrayList<Category>();

        for(Category item : categoryDList){

            filteredList.add(item);
            Collections.sort(filteredList, Category.StuNameComparatorDesc);

        }
        categoryAdapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(View view, int position) {

        Category cat = CategoryAdapter.categoryList.get(position);
        categoryName = cat.getName();

        replaceFragment(new MainLectures());

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

        deleteConfirm.setMessage("Do you to delete this category?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Category cat = CategoryAdapter.categoryList.get(position);
                        categoryName = cat.getName();
                        String[] tokens = usernameEmail.split("@");

                        databaseCategories = FirebaseDatabase.getInstance();
                        refCategories = databaseCategories.getReference("Categories");
                        refCategories.child(categoryName+tokens[0]).removeValue();

                        refItems.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    if(ds.child("categoryType").getValue().equals(categoryName) && ds.child("username").getValue().equals(usernameEmail))
                                    {
                                        String test = ds.getKey().toString();
                                        refItems.child(test).removeValue();
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