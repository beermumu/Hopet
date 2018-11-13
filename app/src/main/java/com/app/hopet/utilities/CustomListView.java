package com.app.hopet.utilities;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.bumptech.glide.Glide;

public class CustomListView extends ArrayAdapter<Animal>{

    public CustomListView(Context context, int resource, List<Animal> objects) {
        super(context, R.layout.item_row, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_row,null);

            Animal animal = getItem(position);
            TextView topic = convertView.findViewById(R.id.topicTextView);
            topic.setText(animal.getTopic());

            TextView name = convertView.findViewById(R.id.userNameTextView);
            name.setText("Post By : " + animal.getUser().getFirstName() +" "+ animal.getUser().getLastName());

            TextView description = convertView.findViewById(R.id.descriptionTextView);
            description.setText("Description : "+animal.getDescription());

            ImageView imageView1 = convertView.findViewById(R.id.postImageView);
            Glide.with(getContext()).load(animal.getPhotoOne()).into(imageView1);


        }
        return convertView;
    }
}