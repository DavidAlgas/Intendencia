package com.example.david.intendencia.Ajustes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.david.intendencia.R;
import com.example.david.intendencia.Ventana_Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Ventana_BorrarUsuario extends AppCompatActivity {

    public ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_usuario);
        ButterKnife.bind(this);
    }

    //Pulsamos en CANCELAR
    @OnClick(R.id.btnCancelarBorrado)
    public void CANCELAR() {
        finish();
    }

    //Pulsamos en ELIMINAR
    @OnClick(R.id.btnConfirmarBorrado)
    public void Eliminar_Usuario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Ventana_BorrarUsuario.this);
        builder.setMessage("¿Desea borrar su cuenta?\nEsta acción sera irreversible").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                progreso = ProgressDialog.show(Ventana_BorrarUsuario.this, "", "Eliminando Cuenta", true);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progreso.dismiss();
                            Toast.makeText(Ventana_BorrarUsuario.this, "Cuenta Eliminada", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Ventana_BorrarUsuario.this, Ventana_Login.class);
                            intent.putExtra("disconnet", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
                            startActivity(intent);
                        }

                        if (!task.isSuccessful()) {
                            progreso.dismiss();
                            Toast.makeText(Ventana_BorrarUsuario.this, "Error Eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        }).show();
    }
}
