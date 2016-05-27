package com.example.david.intendencia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.david.intendencia.Ajustes.Ventana_Ajustes;
import com.example.david.intendencia.Tabs.SwipeAdapter;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Ventana_Main extends AppCompatActivity {

    @BindView(R.id.elementoPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }


    //--------------------------------------------------------------------------------------------
    //-------------------------------------- MENU ------------------------------------------------
    //--------------------------------------------------------------------------------------------

    //Inflamos el Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Le damos funcionalidad al menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tareas:
                Intent abrirTareas = new Intent(this, Ventana_Tareas.class);
                startActivity(abrirTareas);
                return true;
            case R.id.addTienda:
                Intent nuevaTienda = new Intent(this, Ventana_NewTienda.class);
                startActivity(nuevaTienda);
                return true;
            case R.id.ajustes:
                Intent abrirAjustes = new Intent(this, Ventana_Ajustes.class);
                startActivity(abrirAjustes);
                return true;
            case R.id.desconectar:
                AlertDialog.Builder builder = new AlertDialog.Builder(Ventana_Main.this);
                builder.setMessage("¿Desconectar?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent meDesconecto = new Intent(Ventana_Main.this, Ventana_Login.class);
                        meDesconecto.putExtra("disconnet", true);
                        startActivity(meDesconecto);

                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
