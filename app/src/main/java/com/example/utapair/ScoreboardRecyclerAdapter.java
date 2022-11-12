package com.example.utapair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/* this class is about adapter in scoreboard
 * this class will put data from ScoreboardUser
 * in to recyclerView */
public class ScoreboardRecyclerAdapter extends RecyclerView.Adapter<ScoreboardRecyclerAdapter.MyViewHolder>{
    private ArrayList<ScoreboardUser> scoreboardUserArrayList;

    /* constructor */
    public ScoreboardRecyclerAdapter(ArrayList<ScoreboardUser> userList){
        this.scoreboardUserArrayList=userList;
    }

    /* hold all elements in layout file */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numberText;
        private TextView usernameText;
        private TextView timeText;

        /* set elements with findViewById */
        public MyViewHolder(final View view){
            super(view);
            numberText = view.findViewById(R.id.number_text);
            usernameText = view.findViewById(R.id.username_text);
            timeText = view.findViewById(R.id.time_text);
        }
    }


    @NonNull
    @Override
    /* set View from layout file */
    public ScoreboardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_list_item,parent,false);
        /* set view from layout file and all elements that in layout file */
        return new MyViewHolder(itemView);
    }

    @Override
    /* get data in to all of elements */
    public void onBindViewHolder(@NonNull ScoreboardRecyclerAdapter.MyViewHolder holder, int position) {
        int number = scoreboardUserArrayList.get(position).getNumber();
        String num = String.valueOf(number);
        String username = scoreboardUserArrayList.get(position).getUsername();
        String time = scoreboardUserArrayList.get(position).getTime();
        /* holder all data that we want to show in recyclerView */
        holder.numberText.setText(num);
        holder.usernameText.setText(username);
        holder.timeText.setText(time);
    }

    @Override
    /* method to return count of item in scoreboardUserArrayList */
    public int getItemCount() {
        return scoreboardUserArrayList.size();
    }
}
