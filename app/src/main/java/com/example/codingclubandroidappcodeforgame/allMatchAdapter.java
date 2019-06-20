package com.example.codingclubandroidappcodeforgame;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class allMatchAdapter extends RecyclerView.Adapter<allMatchAdapter.ViewHolder> {
    private List<allMatchModel> itemList;
    private Context context;

    public allMatchAdapter(List<allMatchModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public allMatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_match_details,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull allMatchAdapter.ViewHolder viewHolder, int i) {
        allMatchModel ne=itemList.get(i);
        viewHolder.team1.setText(ne.getTeam1());
        viewHolder.team2.setText(ne.getTeam2());
        viewHolder.MatchStatus.setText(ne.getMatchStatus());
        viewHolder.matchType.setText(ne.getMatchType());
        viewHolder.date.setText(ne.getDate());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView team1,team2,matchType,MatchStatus,date;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team1=itemView.findViewById(R.id.tv1);
            team2=itemView.findViewById(R.id.tv2);
            matchType=itemView.findViewById(R.id.tv3);
            MatchStatus=itemView.findViewById(R.id.tv4);
            date=itemView.findViewById(R.id.tv5);
            cardView=itemView.findViewById(R.id.single_match);
        }
    }
}
