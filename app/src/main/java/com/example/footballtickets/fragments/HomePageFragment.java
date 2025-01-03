package com.example.footballtickets.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footballtickets.R;
import com.example.footballtickets.activities.BasketAdapter;
import com.example.footballtickets.activities.CustomeAdapter;
import com.example.footballtickets.activities.DataModel;
import com.example.footballtickets.models.Cart;
import com.example.footballtickets.activities.myData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userName;
    private String userEmail;
    private RecyclerView recyclerView;
    private ArrayList<DataModel> dataSet;
    private CustomeAdapter adapter;
    private BasketAdapter basketAdapter;


    public HomePageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(requireActivity());

        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
            userName = getArguments().getString("username");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        TextView userNameText = view.findViewById(R.id.userNameText);
        userNameText.setText(userName);

        ImageView basketIcon = view.findViewById(R.id.basketIcon);

        basketIcon.setOnClickListener(v -> showBasketDialog());

        recyclerView = view.findViewById(R.id.resView);

        if (myData.matches == null || myData.matches.length == 0) {
            Toast.makeText(requireActivity(), "No data available", Toast.LENGTH_SHORT).show();
            return view;
        }

        dataSet = new ArrayList<>();
        for (int i = 0; i < myData.matches.length; i++) {
            dataSet.add(new DataModel(
                    myData.matches[i],
                    myData.descriptionArray[i],
                    myData.prices[i],
                    myData.drawableArrayTeam1[i],
                    myData.drawableArrayTeam2[i],
                    myData.drawableArrayLeague[i],
                    myData.currencySymbols[i],
                    myData.id_[i]
            ));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomeAdapter(dataSet, this::showOrderPopup);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void showOrderPopup(DataModel dataModel) {
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_order_ticket, null);

        TextView textMatchName = dialogView.findViewById(R.id.text_match_name);
        TextView textPrice = dialogView.findViewById(R.id.text_price);
        TextView textTicketCount = dialogView.findViewById(R.id.text_ticket_count);
        ImageView imageTeam1 = dialogView.findViewById(R.id.image_team1);
        ImageView imageTeam2 = dialogView.findViewById(R.id.image_team2);
        ImageView imageLeague = dialogView.findViewById(R.id.image_league);
        ImageButton incrementButton = dialogView.findViewById(R.id.button_increment);
        ImageButton decrementButton = dialogView.findViewById(R.id.button_decrement);

        imageTeam1.setImageResource(dataModel.getImage1());
        imageTeam2.setImageResource(dataModel.getImage2());
        imageLeague.setImageResource(dataModel.getLeagueImage());
        textMatchName.setText(dataModel.getName());
        textPrice.setText("Price: " + dataModel.getCurrencySymbol() + dataModel.getPrice());

        final int[] ticketCount = {1};
        incrementButton.setOnClickListener(v -> textTicketCount.setText(String.valueOf(++ticketCount[0])));
        decrementButton.setOnClickListener(v -> {
            if (ticketCount[0] > 1) {
                textTicketCount.setText(String.valueOf(--ticketCount[0]));
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .setPositiveButton("Add to Cart", (dialog1, which) -> updateFirebaseDatabase(dataModel, ticketCount[0]))
                .setNegativeButton("Cancel", null)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(getResources().getColor(R.color.green, null));
        }

        if (negativeButton != null) {
            negativeButton.setTextColor(getResources().getColor(R.color.red, null));
        }
    }


    private void updateFirebaseDatabase(DataModel dataModel, int ticketCount) {
        String sanitizedEmail = userEmail.replace(".", ",");

        DatabaseReference userCartRef = FirebaseDatabase.getInstance()
                .getReference("carts")
                .child(sanitizedEmail);

        userCartRef.child("username").setValue(userName);
        DatabaseReference cartsRef = userCartRef.child("items");

        String cartId = cartsRef.push().getKey();

        Cart cart = new Cart(
                cartId,
                dataModel.getName(),
                dataModel.getCurrencySymbol(),
                dataModel.getPrice(),
                ticketCount,
                System.currentTimeMillis()
        );

        if (cartId != null) {
            cartsRef.child(cartId).setValue(cart)
                    .addOnSuccessListener(aVoid -> Toast.makeText(requireActivity(), "Tickets added to cart!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(requireActivity(), "Failed to add tickets: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void showBasketDialog() {
        String sanitizedEmail = userEmail.replace(".", ",");
        DatabaseReference cartsRef = FirebaseDatabase.getInstance()
                .getReference("carts")
                .child(sanitizedEmail)
                .child("items");

        cartsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot result = task.getResult();

                ArrayList<Cart> carts = new ArrayList<>();
                for (DataSnapshot snapshot : result.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    if (cart != null && cart.getCartId() != null) {
                        carts.add(cart);
                    }
                }

                if (carts.isEmpty()) {
                    Toast.makeText(requireActivity(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_basket, null);
                RecyclerView basketRecyclerView = dialogView.findViewById(R.id.basketRecyclerView);
                basketRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

                basketAdapter = new BasketAdapter(carts, new BasketAdapter.OnCartItemActionListener() {
                    @Override
                    public void onRemoveItem(Cart cart) {
                        cartsRef.child(cart.getCartId()).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireActivity(), "Item removed!", Toast.LENGTH_SHORT).show();
                                    carts.remove(cart);
                                    basketAdapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> Toast.makeText(requireActivity(), "Failed to remove item!", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onUpdateQuantity(Cart cart, int newQuantity) {
                        if (newQuantity > 0) {
                            cartsRef.child(cart.getCartId()).child("ticketCount").setValue(newQuantity)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(requireActivity(), "Quantity updated!", Toast.LENGTH_SHORT).show();
                                        cart.setTicketCount(newQuantity);
                                        basketAdapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(requireActivity(), "Failed to update quantity!", Toast.LENGTH_SHORT).show());
                        } else {
                            onRemoveItem(cart);
                        }
                    }
                });

                basketRecyclerView.setAdapter(basketAdapter);

                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setView(dialogView)
                        .setPositiveButton("Close", (dialog1, which) -> dialog1.dismiss())
                        .create();

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                }
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(getResources().getColor(R.color.green, null));
                }
            } else {
                Toast.makeText(requireActivity(), "Failed to load cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}