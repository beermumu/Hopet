package com.app.hopet.Utilities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.app.hopet.Activities.PostActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class CustomListView extends RecyclerView.Adapter<CustomListView.ViewHolder> {

    private Context context;
    private List<Animal> animals;
    private List<String> firebaseKey;

    public CustomListView(Context context, List<Animal> objects, List<String> key) {
        this.context = context;
        this.animals = objects;
        this.firebaseKey = key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Animal animal = animals.get(i);
        viewHolder.topic.setText(animal.getTopic());
        viewHolder.name.setText(animal.getUser().getName());
        viewHolder.description.setText(viewHolder.itemView.getContext().getString(R.string.description)+" : " + animal.getDescription());
        viewHolder.dateTime.setText(animal.getDateTime());
        Glide.with(viewHolder.itemView.getContext()).load(animal.getPhotoOne()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.imageView1);
        Glide.with(viewHolder.itemView.getContext()).load(animal.getUser().getPhoto()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.profilePic);


        //Comment and Share Button
        Button commentBtn = viewHolder.itemView.findViewById(R.id.commentPostButton);
        Button shareBtn = viewHolder.itemView.findViewById(R.id.sharedPostButton);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), PostActivity.class);
                intent.putExtra("key", firebaseKey.get(i));
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic)+" : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed)+" : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age)+" : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description)+" : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo)+" : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                intent.setType("text/plain");
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView topic, name, description, dateTime;
        public ImageView imageView1, profilePic;

        public ViewHolder(View convertview) {
            super(convertview);
            topic = convertview.findViewById(R.id.topicTextView);
            name = convertview.findViewById(R.id.userNameTextView);
            description = convertview.findViewById(R.id.descriptionTextView);
            imageView1 = convertview.findViewById(R.id.postImageView);
            dateTime = convertview.findViewById(R.id.timeTextView);
            profilePic = convertview.findViewById(R.id.userPhotoImageView);

        }
    }

}