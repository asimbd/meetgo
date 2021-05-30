package com.asim.vidgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailBox, passwordBox, nameBox, confirmPass;
    Button loginBtn, signupBtn;

    ProgressDialog dialog;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
            startActivity(intent);
            finishAffinity();
        }

        emailBox = findViewById(R.id.emailBox);
        nameBox = findViewById(R.id.namebox);
        passwordBox = findViewById(R.id.passwordBox);
        confirmPass = findViewById(R.id.ConpasswordBox);

        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.createBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email, pass, name, confirmPassword;
                email = emailBox.getText().toString().trim();
                pass = passwordBox.getText().toString();
                confirmPassword = confirmPass.getText().toString();
                name = nameBox.getText().toString();

                final User user = new User();
                user.setEmail(email);
                user.setPass(pass);
                user.setName(name);

                if (name.isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Email Address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length()<6){
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (confirmPassword.isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.equals(confirmPassword)){
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                database.collection("Users")
                                        .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
                                        finishAffinity();
                                    }
                                });
//                            Toast.makeText(SignupActivity.this, "Account is created.", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Password is not match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }
}