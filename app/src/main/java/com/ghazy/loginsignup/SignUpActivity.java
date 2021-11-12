package com.ghazy.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private FirebaseServices fbs;
    private Utilities utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsernameSignup);
        etPassword = findViewById(R.id.etPasswordSignup);
        fbs = FirebaseServices.getInstance();
        utils = Utilities.getInstance();
    }


    public void signup(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!utils.EmailValidation(this, username) || !utils.PasswordValidation(this, password))
            return;

        fbs.getAuth().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // TODO: commands if successful
                        } else {

                            // TODO: commands if failed
                            Toast.makeText(SignUpActivity.this, "Username or password is empty!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
    }

    public void goBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}