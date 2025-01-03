package com.example.footballtickets.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballtickets.R;

import java.util.ArrayList;
import java.util.List;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private final List<DataModel> dataSet;
    private final OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(DataModel dataModel);
    }

    public CustomeAdapter(ArrayList<DataModel> dataSet, OnItemClickListener onItemClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataModel item = dataSet.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewVersion.setText(item.getDescription());
        holder.imageViewTeam1.setImageResource(item.getImage1());
        holder.textViewPrice.setText(item.getCurrencySymbol());
        holder.textViewPrice.append(String.valueOf(item.getPrice()));
        holder.imageViewTeam2.setImageResource(item.getImage2());
        holder.imageViewLeague.setImageResource(item.getLeagueImage());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewVersion;
        TextView textViewPrice;
        ImageView imageViewTeam1;
        ImageView imageViewTeam2;
        ImageView imageViewLeague;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textView);
            this.textViewVersion = itemView.findViewById(R.id.textView2);
            this.textViewPrice = itemView.findViewById(R.id.textView5);
            this.imageViewTeam1 = itemView.findViewById(R.id.team1);
            this.imageViewTeam2 = itemView.findViewById(R.id.team2);
            this.imageViewLeague = itemView.findViewById(R.id.league);
        }
    }
}
