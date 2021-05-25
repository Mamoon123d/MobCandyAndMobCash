package com.mobcash.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.MoreTask;
import com.mobcash.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MoreTaskListAdapter extends RecyclerView.Adapter<MoreTaskListAdapter.ViewHolder> {

    ArrayList<MoreTask> moreTasks;
    Context context;

    public MoreTaskListAdapter(ArrayList<MoreTask> moreTasks, Context context) {
        this.moreTasks = moreTasks;
        this.context = context;
    }

    @NonNull
    @Override
    public MoreTaskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starter_task_view_for_api, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreTaskListAdapter.ViewHolder holder, int position) {

        final MoreTask moreTaskList = moreTasks.get(position);

        String truncateString = moreTaskList.getTaskDescription();
        final String MoreTaskId = String.valueOf(moreTaskList.getMoreTaskId());
        final String actionUrl = moreTaskList.getActionUrl();
        Log.e(TAG, "onBindViewHolder: "+actionUrl );

        holder.moreTaskName.setText(moreTaskList.getTaskTitle());
        holder.moreTaskDec.setText(moreTaskList.getTaskDescription());
        holder.taskAmount.setText(""+moreTaskList.getTaskAmount()+" "+"Coins");

        Picasso.get().load(moreTaskList.getImgUrl()).into(holder.moreTaskImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(actionUrl);
            }
        });



    }


    @Override
    public int getItemCount() {
        return moreTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView moreTaskName, moreTaskDec, taskAmount;
        ImageView moreTaskImage;

        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            moreTaskName = itemView.findViewById(R.id.moreTaskName);
            moreTaskDec = itemView.findViewById(R.id.moreTaskDec);
            taskAmount = itemView.findViewById(R.id.taskAmount);
            moreTaskImage = itemView.findViewById(R.id.moreTaskImage);
            linearLayout = itemView.findViewById(R.id.linearLayout);


        }

    }
    public void openWebPage(String url) {

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
