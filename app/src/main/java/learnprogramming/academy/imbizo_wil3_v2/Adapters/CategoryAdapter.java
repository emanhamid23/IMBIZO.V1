package learnprogramming.academy.imbizo_wil3_v2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import learnprogramming.academy.imbizo_wil3_v2.OOP.Category;
import learnprogramming.academy.imbizo_wil3_v2.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    public static List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_cat_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category cat = categoryList.get(position);
        holder.catName.setText(cat.getName());

        String imageUri = null;
        imageUri = cat.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void filterList(ArrayList<Category> filteredList){
        categoryList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //here declare design
        ImageView imageView;
        TextView catName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_recyclerView_id);
            catName = itemView.findViewById(R.id.edtxViewNameR);
            cardView = itemView.findViewById(R.id.cardViewCat);

        }
    }
}

