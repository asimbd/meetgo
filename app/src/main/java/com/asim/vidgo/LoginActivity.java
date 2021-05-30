package com.asim.vidgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    Button loginBtn, signupBtn;

    FirebaseAuth auth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finishAffinity();
        }


        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.createBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email, password;
                email = emailBox.getText().toString().trim();
                password = passwordBox.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email Address is empty", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                if (!email.isEmpty() && !password.isEmpty()){
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finishAffinity();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}