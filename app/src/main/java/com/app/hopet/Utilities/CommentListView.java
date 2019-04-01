package com.app.hopet.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hopet.Models.Comment;
import com.app.hopet.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CommentListView extends RecyclerView.Adapter<CommentListView.ViewHolder> {

    public Context context;
    public List<Comment> LCommment;

    public CommentListView(Context context, List<Comment> LCommment) {
        this.context = context;
        this.LCommment = LCommment;
        Log.i("kakak", "Adding CommentListView");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_row, viewGroup, false);
        Log.i("kakak", "Adding ViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comment comment = LCommment.get(i);
        Log.i("kakak","Ka: "+viewHolder.commentDataText);
        viewHolder.commentDataText.setText(comment.getText());
        viewHolder.commentNameText.setText(comment.getUser().getName());
        viewHolder.commentDateTimeText.setText(comment.getTime());
        Glide.with(viewHolder.itemView.getContext()).load(comment.getUser().getPhoto()).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.not_found_image)).into(viewHolder.commentPic);
    }

    @Override
    public int getItemCount() {
        Log.i("kakak","SIZE: "+LCommment.size());
        return LCommment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentNameText,commentDataText,commentDateTimeText;
        public ImageView commentPic;

        public ViewHolder(View itemView) {
            super(itemView);
            commentDataText = itemView.findViewById(R.id.commentDataText);
            commentDateTimeText = itemView.findViewById(R.id.commentDateTImeText);
            commentNameText = itemView.findViewById(R.id.commentNameText);
            commentPic = itemView.findViewById(R.id.commentPicture);
            Log.i("kakak", "Adding Listener");
        }
    }
}
