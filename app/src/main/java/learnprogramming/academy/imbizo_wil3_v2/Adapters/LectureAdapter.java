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

import learnprogramming.academy.imbizo_wil3_v2.OOP.Lecture;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {

    Context context;
    public static List<Lecture> lectureList;

    public LectureAdapter(Context context, List<Lecture> lList)
    {
        this.context = context;
        this.lectureList = lList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView lectureName;
        CardView lectureCardView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_recyclerView_id);
            lectureName = itemView.findViewById(R.id.edtxViewNameR);
            lectureCardView = itemView.findViewById(R.id.cardViewCat);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row_cat_recycler,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Lecture l = lectureList.get(position);
        holder.lectureName.setText(l.getLectureName());

        String imageUri = null;
        imageUri = l.getLectureImage();
        Picasso.get().load(imageUri).into(holder.imageView);

        holder.lectureCardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));


    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    public  void filterList(ArrayList<Lecture> filteredList)
    {
        lectureList = filteredList;
        notifyDataSetChanged();
    }


}
