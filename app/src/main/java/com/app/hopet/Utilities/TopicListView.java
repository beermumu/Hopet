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
import com.bumptech.glide.request.RequestOptions;

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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Animal animal = animals.get(i);
        viewHolder.topic.setText(animal.getTopic());
        viewHolder.name.setText(animal.getUser().getName());
        viewHolder.description.setText(viewHolder.itemView.getContext().getString(R.string.description)+" : " + animal.getDescription());
        viewHolder.dateTime.setText(animal.getDateTime());
        Glide.with(viewHolder.itemView.getContext()).load(animal.getPhotoOne()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.imageView1);
        Glide.with(viewHolder.itemView.getContext()).load(animal.getPhotoTwo()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.imageView2);
        Glide.with(viewHolder.itemView.getContext()).load(animal.getPhotoThree()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.imageView3);
        Glide.with(viewHolder.itemView.getContext()).load(animal.getUser().getPhoto()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.profilePic);


        Button editBtn = viewHolder.itemView.findViewById(R.id.yourTopicEditButton);
        Button deleteBtn = viewHolder.itemView.findViewById(R.id.yourTopicDeleteButton);
        Button updateBtn = viewHolder.itemView.findViewById(R.id.yourTopicUpdateButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), TopicActivity.class);
                intent.putExtra("key", firebaseKey.get(i));
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setCancelable(true);
                builder.setTitle(viewHolder.itemView.getContext().getString(R.string.confirm_update));
                builder.setMessage(viewHolder.itemView.getContext().getString(R.string.confirm_update_des));
                builder.setPositiveButton(viewHolder.itemView.getContext().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TopicActivity topicActivity = new TopicActivity();
                                topicActivity.changeUpdate(firebaseKey.get(i));
                            }
                        });
                builder.setNegativeButton(viewHolder.itemView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setCancelable(true);
                builder.setTitle(viewHolder.itemView.getContext().getString(R.string.confirm_delete));
                builder.setMessage(viewHolder.itemView.getContext().getString(R.string.confirm_delete_des));
                builder.setPositiveButton(viewHolder.itemView.getContext().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TopicActivity topicActivity = new TopicActivity();
                                topicActivity.changeStatus(firebaseKey.get(i));
                            }
                        });
                builder.setNegativeButton(viewHolder.itemView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
        public ImageView imageView1 , imageView2 , imageView3 , profilePic;

        public ViewHolder(View convertview) {
            super(convertview);
            topic = convertview.findViewById(R.id.yourTopicShowView);
            name = convertview.findViewById(R.id.yourTopicUserNameTextView);
            description = convertview.findViewById(R.id.yourTopicDescriptionTextView);
            imageView1 = convertview.findViewById(R.id.yourTopicPostImageView);
            imageView2 = convertview.findViewById(R.id.yourTopicPostImageView2);
            imageView3 = convertview.findViewById(R.id.yourTopicPostImageView3);
            dateTime = convertview.findViewById(R.id.yourTopicTimeTextView);
            profilePic = convertview.findViewById(R.id.yourTopicUserPhotoImageView);
        }
    }
}
