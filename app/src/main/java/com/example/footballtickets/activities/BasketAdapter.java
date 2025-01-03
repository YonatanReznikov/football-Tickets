package com.example.footballtickets.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballtickets.R;
import com.example.footballtickets.models.Cart;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {
    private final ArrayList<Cart> carts;
    private final OnCartItemActionListener actionListener;

    public BasketAdapter(ArrayList<Cart> carts, OnCartItemActionListener actionListener) {
        this.carts = carts;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basket, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.matchName.setText(cart.getMatchName());
        holder.ticketCount.setText("Tickets: " + cart.getTicketCount());
        holder.price.setText("Price: " +cart.getCurrencySymbol() +cart.getPrice());
        holder.incrementButton.setOnClickListener(v -> {
            int newQuantity = cart.getTicketCount() + 1;
            actionListener.onUpdateQuantity(cart, newQuantity);
        });
        holder.decrementButton.setOnClickListener(v -> {
            int newQuantity = cart.getTicketCount() - 1;
            actionListener.onUpdateQuantity(cart, newQuantity);
        });
        holder.removeButton.setOnClickListener(v -> actionListener.onRemoveItem(cart));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    static class BasketViewHolder extends RecyclerView.ViewHolder {
        TextView matchName, ticketCount, price;
        ImageButton incrementButton, decrementButton, removeButton;

        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            matchName = itemView.findViewById(R.id.matchName);
            ticketCount = itemView.findViewById(R.id.ticketCount);
            price = itemView.findViewById(R.id.price);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public interface OnCartItemActionListener {
        void onRemoveItem(Cart cart);

        void onUpdateQuantity(Cart cart, int newQuantity);
    }
}