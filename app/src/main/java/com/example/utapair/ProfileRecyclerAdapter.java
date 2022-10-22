package com.example.utapair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.MyViewHolder> {
    private ArrayList<ProfileUser> ProfileUserArrayList;

    public ProfileRecyclerAdapter(ArrayList<ProfileUser> ProfileList){
        this.ProfileUserArrayList = ProfileList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numberText;
        private TextView scoreboardText;
        private TextView timeText;

        public MyViewHolder(final View view){
            super(view);
            numberText = view.findViewById(R.id.number_text);
            scoreboardText = view.findViewById(R.id.scoreboard_text);
            timeText = view.findViewById(R.id.time_text);
        }
    }

    @NonNull
    @Override
    public ProfileRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item,parent,false);
        return new ProfileRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerAdapter.MyViewHolder holder, int position) {
        int number = ProfileUserArrayList.get(position).getNumber();
        String num = String.valueOf(number);
        int scoreboard = ProfileUserArrayList.get(position).getScoreboard();
        String scboard = String.valueOf(scoreboard);
        String time = ProfileUserArrayList.get(position).getTime();
        holder.numberText.setText(num);
        holder.scoreboardText.setText(scboard);
        holder.timeText.setText(time);
    }

    @Override
    public int getItemCount() {
        return ProfileUserArrayList.size();
    }
}