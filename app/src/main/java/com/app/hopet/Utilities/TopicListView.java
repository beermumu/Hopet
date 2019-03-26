package com.app.hopet.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Activities.TopicActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class TopicListView extends ArrayAdapter<Animal> {

    private List<String> firebaseKey;

    public TopicListView(Context context, int resource, List<Animal> objects, List<String> key) {
        super(context, R.layout.yourtopic_row, objects);
        this.firebaseKey = key;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TopicListView.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yourtopic_row, null);
            viewHolder = new TopicListView.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Animal animal = getItem(position);
        viewHolder.topic.setText(animal.getTopic());
        viewHolder.name.setText("Post By : " + animal.getUser().getName());
        viewHolder.description.setText("Description : " + animal.getDescription());
        viewHolder.dateTime.setText("Time : " + animal.getDateTime());
        Glide.with(getContext()).load(animal.getPhotoOne()).into(viewHolder.imageView1);
        Glide.with(getContext()).load(animal.getUser().getPhoto()).into(viewHolder.profilePic);


        Button editBtn = convertView.findViewById(R.id.yourTopicEditButton);
        Button deleteBtn = convertView.findViewById(R.id.yourTopicDeleteButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("key", firebaseKey.get(position));
                getContext().startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setMessage("If you delete your topic will gone always.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TopicActivity topicActivity = new TopicActivity();
                                topicActivity.changeStatus(firebaseKey.get(position));
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

        return convertView;
    }

    public class ViewHolder {
        public TextView topic, name, description, dateTime ;
        public ImageView imageView1 , profilePic;

        public ViewHolder(View convertview) {
            topic = convertview.findViewById(R.id.yourTopicShowView);
            name = convertview.findViewById(R.id.yourTopicUserNameTextView);
            description = convertview.findViewById(R.id.yourTopicDescriptionTextView);
            imageView1 = convertview.findViewById(R.id.yourTopicPostImageView);
            dateTime = convertview.findViewById(R.id.yourTopicTimeTextView);
            profilePic = convertview.findViewById(R.id.yourTopicUserPhotoImageView);
        }
    }
}
