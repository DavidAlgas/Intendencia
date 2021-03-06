package com.example.david.intendencia.Ajustes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.david.intendencia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_Ajustes extends AppCompatActivity {

    @Bind(R.id.lblCuentaActualCorreo)
    TextView CuentaActualCorreo;
    @Bind(R.id.lblCuentaActualAlias)
    TextView CuentaActualAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
                CuentaActualAlias.setText(user.getEmail());
                CuentaActualCorreo.setText("");
            } else {
                CuentaActualAlias.setText(user.getDisplayName());
                CuentaActualCorreo.setText(user.getEmail());
            }
        }

    }

    //HACEMOS CLIC EN AJUSTES -> CUENTA
    @OnClick(R.id.btnAjustes)
    public void Ventana_AjustesCuenta() {
        Intent intent = new Intent(this, Ventana_AjustesCuenta.class);
        startActivity(intent);
    }

    //HACEMOS CLIC EN AJUSTES -> INFORMACION
    @OnClick(R.id.btnAyuda)
    public void Ventana_AjustesAyuda() {
        new AlertDialog.Builder(this).setTitle(R.string.menuAcerca)
                .setMessage("Gestión de Intendencia.\nDavid Algás Calavia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info).show();
    }
}
