package com.app.hopet.Utilities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.app.hopet.Activities.PostActivity;
import com.app.hopet.Models.Animal;
import com.app.hopet.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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
        viewHolder.description.setText(viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription());
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


                final Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "text");

                List<String> appNames = new ArrayList<String>();
                appNames.add("Facebook");
                appNames.add("Messenger");
                appNames.add("Line");
                appNames.add("Direct Instagram");
                appNames.add("Twitter");
                appNames.add("More");


                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setTitle(viewHolder.itemView.getContext().getString(R.string.share_to));
                builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (appNames.get(item).equals("Facebook")) {
                            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                                    .setQuote(viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv))
                                    .setContentUrl(Uri.parse(animal.getPhotoOne()))
                                    .build();
                            ShareDialog.show((Activity) viewHolder.itemView.getContext(), shareLinkContent);
                        }else if (appNames.get(item).equals("Messenger")) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setPackage("com.facebook.orca");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo) + " : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                            viewHolder.itemView.getContext().startActivity(intent);
                        } else if (appNames.get(item).equals("Twitter")) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setPackage("com.twitter.android");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo) + " : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                            viewHolder.itemView.getContext().startActivity(intent);
                        }else if (appNames.get(item).equals("Direct Instagram")) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setPackage("com.instagram.android");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo) + " : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                            viewHolder.itemView.getContext().startActivity(intent);
                        }else if (appNames.get(item).equals("Line")) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setPackage("jp.naver.line.android");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo) + " : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                            viewHolder.itemView.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, viewHolder.itemView.getContext().getString(R.string.topic) + " : " + animal.getTopic() + "\n" + viewHolder.itemView.getContext().getString(R.string.breed) + " : " + animal.getBreed() + " " + animal.getType() + "\n" + viewHolder.itemView.getContext().getString(R.string.age) + " : " + animal.getAge() + "\n" + viewHolder.itemView.getContext().getString(R.string.description) + " : " + animal.getDescription() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.photo) + " : " + " " + animal.getPhotoOne() + "\n" + "\n" + viewHolder.itemView.getContext().getString(R.string.share_inv));
                            viewHolder.itemView.getContext().startActivity(intent);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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