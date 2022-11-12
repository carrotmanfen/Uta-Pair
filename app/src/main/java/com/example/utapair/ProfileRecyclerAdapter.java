package com.example.utapair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/* this class is about adapter in profile
* this class will put data from ProfileUser
* in to recyclerView */
public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.MyViewHolder> {
    private ArrayList<ProfileUser> ProfileUserArrayList;

    /* constructor */
    public ProfileRecyclerAdapter(ArrayList<ProfileUser> ProfileList){
        this.ProfileUserArrayList = ProfileList;
    }

    /* hold all elements in layout file */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numberText;
        private TextView scoreboardText;
        private TextView timeText;

        /* set elements with findViewById */
        public MyViewHolder(final View view){
            super(view);
            numberText = view.findViewById(R.id.number_text);
            scoreboardText = view.findViewById(R.id.scoreboard_text);
            timeText = view.findViewById(R.id.time_text);
        }
    }

    @NonNull
    @Override
    /* set View from layout file */
    public ProfileRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item,parent,false);
        /* set view from layout file and all elements that in layout file */
        return new ProfileRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    /* get data in to all of elements */
    public void onBindViewHolder(@NonNull ProfileRecyclerAdapter.MyViewHolder holder, int position) {
        int number = ProfileUserArrayList.get(position).getNumber();
        String num = String.valueOf(number);
        int scoreboard = ProfileUserArrayList.get(position).getScoreboard();
        String scboard = String.valueOf(scoreboard);
        String time = ProfileUserArrayList.get(position).getTime();
        /* holder all data that we want to show in recyclerView */
        holder.numberText.setText(num);
        holder.scoreboardText.setText(scboard);
        holder.timeText.setText(time);
    }

    @Override
    /* method to return count of item in ProfileUserArrayList */
    public int getItemCount() {
        return ProfileUserArrayList.size();
    }
}