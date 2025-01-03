package com.example.footballtickets.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.footballtickets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference usernamesRef;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        usernamesRef = FirebaseDatabase.getInstance().getReference("usernames");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button registerBtn = view.findViewById(R.id.RegisterButton);
        registerBtn.setOnClickListener(v -> register(view));

        return view;
    }

    public void register(View view) {
        EditText emailInput = view.findViewById(R.id.email);
        EditText usernameInput = view.findViewById(R.id.username);
        EditText passwordInput = view.findViewById(R.id.password);
        EditText rePasswordInput = view.findViewById(R.id.repassword);
        EditText phoneInput = view.findViewById(R.id.phone);

        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String rePassword = rePasswordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            return;
        }
        if (username.length() < 4) {
            usernameInput.setError("Username must be at least 4 characters");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(rePassword)) {
            rePasswordInput.setError("Passwords do not match");
            return;
        }
        if (phone.isEmpty()) {
            phoneInput.setError("Phone number is required");
            return;
        } else if (phone.length() != 10) {
            phoneInput.setError("Phone number is not valid");
            return;
        }

        usernamesRef.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                usernameInput.setError("Username is already taken");
            } else {
                registerUser(email, username, password, phone, view, emailInput);
            }
        });
    }

    private void registerUser(String email,
                              String username,
                              String password,
                              String phone,
                              View view,
                              EditText emailInput) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        Map<String, String> userDetails = new HashMap<>();
                        userDetails.put("username", username);
                        userDetails.put("email", email);
                        userDetails.put("phone", phone);

                        usersRef.child(userId).setValue(userDetails)
                                .addOnSuccessListener(aVoid -> {
                                    usernamesRef.child(username).setValue(userId)
                                            .addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(getActivity(),
                                                        "Registration Successful",
                                                        Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(view)
                                                        .navigate(R.id.action_registerFragment_to_loginFragment);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getActivity(),
                                                        "Failed to register account: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            emailInput.setError("Email is already in use");

                        } else {
                            Toast.makeText(getActivity(),
                                    "Error: " + (task.getException() != null
                                            ? task.getException().getMessage()
                                            : "Unknown error"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
