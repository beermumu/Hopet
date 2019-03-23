package com.app.hopet.Utilities;


import android.content.Context;
import android.content.Intent;
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

public class CustomListView extends ArrayAdapter<Animal> {

    private List<String> firebaseKey;

    public CustomListView(Context context, int resource, List<Animal> objects, List<String> key) {
        super(context, R.layout.item_row, objects);
        this.firebaseKey = key;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_row, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Animal animal = getItem(position);
        viewHolder.topic.setText(animal.getTopic());
        viewHolder.name.setText("Post By : " + animal.getUser().getFirstName() + " " + animal.getUser().getLastName());
        viewHolder.description.setText("Description : " + animal.getDescription());
        viewHolder.dateTime.setText("Time : " + animal.getDateTime());
        Glide.with(getContext()).load(animal.getPhotoOne()).into(viewHolder.imageView1);


        //Comment and Share Button
        final Button commentBtn = convertView.findViewById(R.id.commentPostButton);
        Button shareBtn = convertView.findViewById(R.id.sharedPostButton);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra("key", firebaseKey.get(position));
                getContext().startActivity(intent);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, animal.getTopic() + "\n" + animal.getBreed() + "\n" + animal.getAge() + "\n" + animal.getDescription() + "\n" + "Contact in Hopet App");
                intent.setType("text/plain");
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public TextView topic, name, description, dateTime;
        public ImageView imageView1;

        public ViewHolder(View convertview) {
            topic = convertview.findViewById(R.id.topicTextView);
            name = convertview.findViewById(R.id.userNameTextView);
            description = convertview.findViewById(R.id.descriptionTextView);
            imageView1 = convertview.findViewById(R.id.postImageView);
            dateTime = convertview.findViewById(R.id.timeTextView);
//            Log.i("kakak", "Adding Listener Home");
        }
    }

}