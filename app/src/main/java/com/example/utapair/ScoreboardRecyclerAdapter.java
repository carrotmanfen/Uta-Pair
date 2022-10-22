package com.example.utapair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreboardRecyclerAdapter extends RecyclerView.Adapter<ScoreboardRecyclerAdapter.MyViewHolder>{
    private ArrayList<ScoreboardUser> scoreBoardUserArrayList;

    public ScoreboardRecyclerAdapter(ArrayList<ScoreboardUser> userList){
        this.scoreBoardUserArrayList=userList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numberText;
        private TextView usernameText;
        private TextView timeText;

        public MyViewHolder(final View view){
            super(view);
            numberText = view.findViewById(R.id.number_text);
            usernameText = view.findViewById(R.id.username_text);
            timeText = view.findViewById(R.id.time_text);
        }
    }


    @NonNull
    @Override
    public ScoreboardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreboardRecyclerAdapter.MyViewHolder holder, int position) {
        int number = scoreBoardUserArrayList.get(position).getNumber();
        String num = String.valueOf(number);
        String username = scoreBoardUserArrayList.get(position).getUsername();
        String time = scoreBoardUserArrayList.get(position).getTime();
        holder.numberText.setText(num);
        holder.usernameText.setText(username);
        holder.timeText.setText(time);
    }

    @Override
    public int getItemCount() {
        return scoreBoardUserArrayList.size();
    }
}
