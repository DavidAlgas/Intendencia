package com.example.david.intendencia.Ajustes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.david.intendencia.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Ventana_AjustesCuenta extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_cuenta);
        ButterKnife.bind(this);
    }

    //HACEMOS CLIC EN AJUSTES -> CAMBIAR CORREO
    @OnClick(R.id.btnCambiarCorreo)
    public void Vetana_Cambio_Correo() {
        intent = new Intent(this, Ventana_CambioCorreo.class);
        startActivity(intent);
    }

    //HACEMOS CLIC EN AJUSTES -> CAMBIAR PASS
    @OnClick(R.id.btnCambiarPass)
    public void Vetana_Cambio_Pass() {
        intent = new Intent(this, Ventana_CambioPassword.class);
        startActivity(intent);
    }

    //HACEMOS CLIC EN AJUSTES -> BORRAR USUARIO
    @OnClick(R.id.btnEliminarCuenta)
    public void Vetana_Eliminar_Usuario() {
        intent = new Intent(this, Ventana_BorrarUsuario.class);
        startActivity(intent);
    }

    //HACEMOS CLIC EN AJUSTES -> MODIFICAR INFORMACION
    @OnClick(R.id.btnModificarInformacion)
    public void Vetana_Modificar_Info() {
        intent = new Intent(this, Ventana_AjustesModificar.class);
        startActivity(intent);
    }
}
