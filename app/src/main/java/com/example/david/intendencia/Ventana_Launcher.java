package com.example.david.intendencia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Ventana_Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                abrimosMAIN();
            }
        }, 1500);
    }

    public void abrimosMAIN() {
        Intent intent = new Intent(this, Ventana_Login.class);
        startActivity(intent);
        finish();
    }
}
