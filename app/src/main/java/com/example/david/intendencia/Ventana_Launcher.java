package com.example.david.intendencia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Ventana_Launcher extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    // User is signed in
                    abrimosMAIN();
                } else {
                    // No user is signed in
                    abrimosLOGIN();
                }
            }
        }, 1500);
    }

    public void abrimosLOGIN() {
        Intent intent = new Intent(this, Ventana_Login.class);
        startActivity(intent);
        finish();
    }

    public void abrimosMAIN() {
        Intent intent = new Intent(this, Ventana_Main.class);
        startActivity(intent);
        finish();
    }
}
