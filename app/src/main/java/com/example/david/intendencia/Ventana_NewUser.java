package com.example.david.intendencia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_NewUser extends AppCompatActivity {
    @Bind(R.id.txtNuevoCorreo)
    EditText txtNuevoCorreo;

    @Bind(R.id.txtNuevaPass)
    EditText txtNuevaPass;

    @Bind(R.id.txtNuevaPass2)
    EditText txtNuevaPass2;

    private FirebaseAuth mAuth;
    public ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newusuario);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //Pulsamos en REGISTRAR
    @OnClick(R.id.btnAceptar)
    public void NUEVO_USUARIO() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String NCorreo = txtNuevoCorreo.getText().toString();
        String NPass1 = txtNuevaPass.getText().toString();
        String NPass2 = txtNuevaPass2.getText().toString();

        if (VALIDATE(NCorreo, NPass1, NPass2)) {
            progreso = ProgressDialog.show(this, "", "Creando Usuario", true);
            mAuth.createUserWithEmailAndPassword(NCorreo, NPass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Ventana_NewUser.this, "Exito al crear el usuario", Toast.LENGTH_LONG).show();
                        progreso.dismiss();
                        finish();
                    }

                    if (!task.isSuccessful()) {
                        progreso.dismiss();
                        Log.w("ERROR", "ERROR CREAR", task.getException());
                        FirebaseCrash.log("Error al crear Usuario: " + task.getException().getMessage());
                        FirebaseCrash.report(new Exception("Error al crear Usuario: " + task.getException().getMessage()));
                        Toast.makeText(Ventana_NewUser.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean VALIDATE(String NCorreo, String NPass1, String NPass2) {
        int todoOK = 0;

        if (NCorreo.isEmpty() || NCorreo.length() == 0) {
            txtNuevoCorreo.setError("Rellene el campo");
            todoOK += 0;
        } else {
            txtNuevoCorreo.setError(null);
            todoOK += 1;

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(NCorreo).matches()) {
                txtNuevoCorreo.setError("Formato Invalido");
                todoOK += 0;
            } else {
                txtNuevoCorreo.setError(null);
                todoOK += 1;
            }
        }

        if (NPass1.isEmpty() || NPass1.length() == 0) {
            txtNuevaPass.setError("Rellene el campo");
            todoOK += 0;
        } else {
            txtNuevaPass.setError(null);
            todoOK += 1;

            if (NPass1.length() < 6) {
                txtNuevaPass.setError("6 Caracteres Min.");
                todoOK += 0;
            } else {
                txtNuevaPass.setError(null);
                todoOK += 1;
            }
        }

        if (NPass2.isEmpty() || NPass2.length() == 0) {
            txtNuevaPass2.setError("Rellene el campo");
            todoOK += 0;
        } else {
            txtNuevaPass2.setError(null);
            todoOK += 1;

            if (!NPass2.equals(NPass1)) {
                txtNuevaPass2.setError("Las contraseñas no coinciden");
                todoOK += 0;
            } else {
                txtNuevaPass2.setError(null);
                todoOK += 1;
            }
        }


        return todoOK == 6;
    }

    //Pulsamos en CANCELAR
    @OnClick(R.id.btnCancelar)
    public void CANCELAR() {
        finish();
    }

}