package com.asim.vidgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {

    EditText secretCodeBox;
    Button joinBtn, shareBtn;
    BottomNavigationView bottomNav;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNav = findViewById(R.id.navDash);
        secretCodeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        shareBtn = findViewById(R.id.shareBtn);

        URL serverURL;


        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setVideoMuted(true)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }





        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeBox = secretCodeBox.getText().toString().trim();

                if (codeBox.isEmpty()) {
                    Toast.makeText(DashboardActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (codeBox.length() < 8) {
                    Toast.makeText(DashboardActivity.this, "Please Enter 8 digit Code", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(codeBox)
                            .setWelcomePageEnabled(false)
                            .build();

                    JitsiMeetActivity.launch(DashboardActivity.this, options);
                }
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        logoutDialog();
                        break;
                }
                return false;
            }
        });

    }

    private void logoutDialog() {
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Logout")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}