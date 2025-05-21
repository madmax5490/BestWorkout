// RegisterFragment.java
package com.example.bestworkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class RegisterFragment extends Fragment {

    private EditText username;
    private EditText password;
    private EditText repPassword;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        repPassword = view.findViewById(R.id.repeat_password);
        AppCompatImageButton returnButton = view.findViewById(R.id.return_button);
        Button registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this::registerUser);
        returnButton.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).navigateToWelcome();
            }
        });


    }

    private void registerUser(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String repPass = repPassword.getText().toString();

        if (user.isEmpty()) {
            showToast("Write username");
        } else if (pass.isEmpty() || repPass.isEmpty()) {
            showToast("Write password");
        } else if (!pass.equals(repPass)) {
            showToast("Passwords do not match");
        } else if (!isValidPassword(pass)) {
            showToast("Password must be at least 9 characters long and include both letters and numbers");
        } else {
            createUserInFirebase(user, pass);
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 9) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }

    private void createUserInFirebase(String user, String pass) {
        mAuth.createUserWithEmailAndPassword(user + "@example.com", pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            navigateToHome();
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                        Log.d("RegisterActivity.class", task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }



//    public void returnButton(View view) {
//        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//    }
}