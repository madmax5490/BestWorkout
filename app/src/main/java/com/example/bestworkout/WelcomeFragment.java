// WelcomeFragment.java
package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class WelcomeFragment extends Fragment {


    private EditText username;
    private EditText password;
    private Button logInButton;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHomeActivity();
        }

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        logInButton = view.findViewById(R.id.enter_button);
        Button registerButton = view.findViewById(R.id.registration_button);


        setupLogInButton();

        registerButton.setOnClickListener(v -> {
            // Navigate to register fragment
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).navigateToRegister();
            }
        });

        return view;
    }

    private void setupLogInButton() {
        logInButton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getActivity(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(user + "@example.com", pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            navigateToHomeActivity();
                        } else {
                            Toast.makeText(getActivity(), "Invalid login credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}