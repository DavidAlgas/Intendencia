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

public class Ventana_CambioCorreo extends AppCompatActivity {

    @Bind(R.id.txtCorreoNuevo)
    EditText NEWCorreo;

    public ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_correo);
        ButterKnife.bind(this);
    }


    //HACEMOS CLIC EN CANCELAR
    @OnClick(R.id.btnCancelarCambioCorreo)
    public void CANCELAR_CORREO() {
        finish();
    }

    //HACEMOS CLIC EN "CAMBIAR CORREO"
    @OnClick(R.id.btnConfirmarCambioCorreo)
    public void CAMBIAR_CORREO() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String NEW = NEWCorreo.getText().toString();

        if (VALIDATE(NEW)) {
            progreso = ProgressDialog.show(this, "", "Modificando Correo", true);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            user.updateEmail(NEW).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progreso.dismiss();
                        Toast.makeText(Ventana_CambioCorreo.this, "Correo Modificado", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (!task.isSuccessful()) {
                        progreso.dismiss();
                        Toast.makeText(Ventana_CambioCorreo.this, "ERROR Modificado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //METODO VALIDATOR
    public boolean VALIDATE(String NEW) {
        int todoOK = 0;

        if (NEW.isEmpty() || NEW.length() == 0) {
            NEWCorreo.setError("Rellene el campo");
            todoOK += 0;
        } else {
            NEWCorreo.setError(null);
            todoOK += 1;

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(NEW).matches()) {
                NEWCorreo.setError("Formato Invalido");
                todoOK += 0;
            } else {
                NEWCorreo.setError(null);
                todoOK += 1;
            }
        }

        return todoOK == 2;
    }
}
