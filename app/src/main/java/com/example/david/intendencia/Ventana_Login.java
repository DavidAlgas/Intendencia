package com.example.david.intendencia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_Login extends AppCompatActivity {
    @Bind(R.id.txtCorreo)
    EditText txtCorreo;

    @Bind(R.id.txtPassword)
    EditText txtPass;

    @Bind(R.id.checkRecordar)
    Switch checkRecordar;

    public ProgressDialog progreso;
    private SharedPreferences preferencias;
    private boolean desconectado = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        //Cargamos las preferencias guardadas
        preferencias = getSharedPreferences("checkRecordar", Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            desconectado = extras.getBoolean("disconnet", true);

            SharedPreferences.Editor editor = preferencias.edit();
            editor.putBoolean("recordar", false);
            editor.apply();
        }


        checkRecordar.setChecked(preferencias.getBoolean("recordar", false));
        if (checkRecordar.isChecked() && !desconectado) {
            //Cargamos el ultimo email, pass y estado de recordar
            txtCorreo.setText(preferencias.getString("correo", null));
            txtPass.setText(preferencias.getString("pass", null));
            Abrimos_Main();
        } else {
            txtCorreo.setText(preferencias.getString("correo", null));
            txtPass.setText("");
            checkRecordar.setChecked(false);
        }
    }

    //**********************************************************************************************
    //**********************************************************************************************
    //HACEMOS CLIC EN EL BOTON LOGIN
    @OnClick(R.id.btnLogin)
    public void Abrimos_Main() {
        String Correo = txtCorreo.getText().toString();
        String Pass = txtPass.getText().toString();
        VALIDATE(Correo, Pass);
        //Si algun campo esta vacio mostramos error
        if (VALIDATE(Correo, Pass)) {
            progreso = ProgressDialog.show(this, "", "Conectando...", true);

            mAuth.signInWithEmailAndPassword(Correo, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Guaramos el correo, la contraseña para no tener que volverlo a escribir
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putString("correo", txtCorreo.getText().toString());
                        editor.putString("pass", txtPass.getText().toString());
                        if (checkRecordar.isChecked()) {
                            editor.putBoolean("recordar", true);
                        } else {
                            editor.putBoolean("recordar", false);
                        }
                        editor.apply();

                        Intent intent = new Intent(getBaseContext(), Ventana_Main.class);
                        startActivity(intent);
                        progreso.dismiss();
                        finish();
                    }


                    if (!task.isSuccessful()) {
                        progreso.dismiss();
                        Log.w("ERROR", "ERROR LOGIN", task.getException());
                        Toast.makeText(Ventana_Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean VALIDATE(String Correo, String Pass) {
        int todoOK = 0;

        if (Correo.isEmpty() || Correo.length() == 0) {
            txtCorreo.setError("Rellene el campo");
            todoOK += 0;
        } else {
            txtCorreo.setError(null);
            todoOK += 1;

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Correo).matches()) {
                txtCorreo.setError("Formato Invalido");
                todoOK += 0;
            } else {
                txtCorreo.setError(null);
                todoOK += 1;
            }
        }

        if (Pass.isEmpty() || Pass.length() == 0) {
            txtPass.setError("Rellene el campo");
            todoOK += 0;
        } else {
            txtPass.setError(null);
            todoOK += 1;
        }

        return todoOK == 3;
    }

    //**********************************************************************************************
    //**********************************************************************************************
    //HACEMOS CLIC EN "OLVIDADO LA CONTRASEÑA"
    @OnClick(R.id.lblOlvidado)
    public void Ventana_olvidaPass() {
        Intent intent = new Intent(this, Ventana_Olvida.class);
        startActivity(intent);
    }

    //HACEMOS CLIC EN "CREAR NUEVO USUARIO"
    @OnClick(R.id.lblNuevoUser)
    public void Ventana_NuevoUser() {
        Intent intent = new Intent(getBaseContext(), Ventana_NewUser.class);
        startActivity(intent);
    }
}