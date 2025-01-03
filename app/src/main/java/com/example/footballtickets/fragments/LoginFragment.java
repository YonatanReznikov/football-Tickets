package com.example.footballtickets.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footballtickets.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    EditText email;
    EditText username;
    EditText password;
    Button loginButton;
    TextView createButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        createButton = view.findViewById(R.id.signupText);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(view);
            }
        });

        return view;
    }

    public void login(View view) {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if (emailInput.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Enter a valid email");
            return;
        }

        if (passwordInput.isEmpty()) {
            password.setError("Password cannot be empty");
            return;
        }

        loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(getActivity(), task -> {
                    loginButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            FirebaseDatabase.getInstance().getReference("users").child(userId)
                                    .get().addOnCompleteListener(dataTask -> {
                                        if (dataTask.isSuccessful() && dataTask.getResult().exists()) {
                                            Map<String, Object> userDetails = (Map<String, Object>) dataTask.getResult().getValue();
                                            String username = (String) userDetails.get("username");
                                            Toast.makeText(getContext(), "Welcome, " + username, Toast.LENGTH_SHORT).show();

                                            Bundle args = new Bundle();
                                            args.putString("email", (String) userDetails.get("email"));
                                            args.putString("username", username);

                                            if (getActivity() != null && Navigation.findNavController(view)
                                                    .getCurrentDestination().getId() == R.id.loginFragment) {
                                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homePageFragment, args);
                                            }
                                        } else
                                            Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }
}