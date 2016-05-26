package com.example.david.intendencia.Ajustes;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.intendencia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_CambioPassword extends AppCompatActivity {
    @Bind(R.id.txtContraseñaNEW)
    EditText passwordNEW;

    public ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);
        ButterKnife.bind(this);
    }


    //HACEMOS CLIC EN "CAMBIAR CONTRASEÑA"
    @OnClick(R.id.btnConfirmarCambioPass)
    public void CAMBIAR_PASS() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String passNEW = passwordNEW.getText().toString();

        if (VALIDATE(passNEW)) {
            progreso = ProgressDialog.show(this, "", "Modificando Contraseña", true);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            user.updatePassword(passNEW).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progreso.dismiss();
                        Toast.makeText(Ventana_CambioPassword.this, "Pass Modificada", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (!task.isSuccessful()) {
                        progreso.dismiss();
                        Toast.makeText(Ventana_CambioPassword.this, "ERROR Cambio Pass", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //METODO VALIDATOR
    public boolean VALIDATE(String passNEW) {
        int todoOK = 0;

        if (passNEW.isEmpty() || passNEW.length() == 0) {
            passwordNEW.setError("Rellene el campo");
            todoOK += 0;
        } else {
            passwordNEW.setError(null);
            todoOK += 1;

            if (passNEW.length() < 6) {
                passwordNEW.setError("6 caracteres Min.");
                todoOK += 0;
            } else {
                passwordNEW.setError(null);
                todoOK += 1;
            }
        }

        return todoOK == 2;
    }
}
