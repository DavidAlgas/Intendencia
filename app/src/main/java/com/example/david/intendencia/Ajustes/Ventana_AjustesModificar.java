package com.example.david.intendencia.Ajustes;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.intendencia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_AjustesModificar extends AppCompatActivity {

    @Bind(R.id.txtALIAS)
    EditText txtALIAS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_cuenta);
        ButterKnife.bind(this);
    }

    //HACEMOS CLIC EN "CAMBIAR CORREO"
    @OnClick(R.id.btnModificaCuenta)
    public void CAMBIAR_CORREO() {
        String Alias = txtALIAS.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(Alias).build();

        assert user != null;
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Ventana_AjustesModificar.this, "Cuenta Modificada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Ventana_AjustesModificar.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
