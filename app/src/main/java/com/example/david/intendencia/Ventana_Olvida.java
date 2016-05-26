package com.example.david.intendencia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_Olvida extends AppCompatActivity {

    @Bind(R.id.txtCorreoOlvidado)
    EditText ResetCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    //HACEMOS CLIC EN "OLVIDADO LA CONTRASEÑA"
    @OnClick(R.id.btnPassReset)
    public void Enviar_Correo() {

        String RCorreo = ResetCorreo.getText().toString();

        if (VALIDATE(RCorreo)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(RCorreo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Ventana_Olvida.this, "Correo Enviado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (!task.isSuccessful()) {
                        Log.w("ERROR", "ERROR RESET", task.getException());
                        Toast.makeText(Ventana_Olvida.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean VALIDATE(String RCorreo) {
        int todoOK = 0;

        if (RCorreo.isEmpty() || RCorreo.length() == 0) {
            ResetCorreo.setError("Rellene el campo");
            todoOK += 0;
        } else {
            ResetCorreo.setError(null);
            todoOK += 1;

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(RCorreo).matches()) {
                ResetCorreo.setError("Formato Invalido");
                todoOK += 0;
            } else {
                ResetCorreo.setError(null);
                todoOK += 1;
            }
        }

        return todoOK == 2;
    }
}
