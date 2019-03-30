package com.app.hopet.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Activities.TopicActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class TopicListView extends RecyclerView.Adapter<TopicListView.ViewHolder> {

    private Context context;
    private List<Animal> animals;
    private List<String> firebaseKey;

    public TopicListView(Context context, List<Animal> objects, List<String> key) {
        this.context=context;
        this.animals=objects;
        this.firebaseKey = key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.yourtopic_row, viewGroup, false);
        return new TopicListView.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Animal animal = animals.get(i);
        viewHolder.topic.setText(animal.getTopic());
        viewHolder.name.setText("Post By : " + animal.getUser().getName());
        viewHolder.description.setText("Description : " + animal.getDescription());
        viewHolder.dateTime.setText("Time : " + animal.getDateTime());
        Glide.with(viewHolder.itemView.getContext()).load(animal.getPhotoOne()).into(viewHolder.imageView1);
        Glide.with(viewHolder.itemView.getContext()).load(animal.getUser().getPhoto()).into(viewHolder.profilePic);


        Button editBtn = viewHolder.itemView.findViewById(R.id.yourTopicEditButton);
        Button deleteBtn = viewHolder.itemView.findViewById(R.id.yourTopicDeleteButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), TopicActivity.class);
                intent.putExtra("key", firebaseKey.get(i));
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setMessage("If you delete your topic will gone always.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TopicActivity topicActivity = new TopicActivity();
                                topicActivity.changeStatus(firebaseKey.get(i));
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView topic, name, description, dateTime ;
        public ImageView imageView1 , profilePic;

        public ViewHolder(View convertview) {
            super(convertview);
            topic = convertview.findViewById(R.id.yourTopicShowView);
            name = convertview.findViewById(R.id.yourTopicUserNameTextView);
            description = convertview.findViewById(R.id.yourTopicDescriptionTextView);
            imageView1 = convertview.findViewById(R.id.yourTopicPostImageView);
            dateTime = convertview.findViewById(R.id.yourTopicTimeTextView);
            profilePic = convertview.findViewById(R.id.yourTopicUserPhotoImageView);
        }
    }
}
