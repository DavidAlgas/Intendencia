package com.example.david.intendencia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Ventana_InfoItem extends AppCompatActivity {

    @Bind(R.id.imgInfoITEM)
    ImageView imagenITEM;
    @Bind(R.id.txtInfoItemTipo)
    TextView txtTipo;
    @Bind(R.id.txtInfoItemMarca)
    TextView txtMarca;
    @Bind(R.id.txtInfoItemModelo)
    TextView txtModelo;
    @Bind(R.id.txtInfoItemCantidad)
    TextView txtCantidad;
    @Bind(R.id.txtInfoItemDetalles)
    TextView txtDetalles;


    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
    private DatabaseReference rutaITEM;
    private Item selecITEM;
    private String idITEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_item);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        txtDetalles.setMovementMethod(new ScrollingMovementMethod());

        // SETEAMOS LOS VALORES DEL ITEM SELECCIONADO
        Bundle intentSelecc = getIntent().getExtras();
        if (intentSelecc != null) {
            selecITEM = (Item) intentSelecc.getSerializable("Objeto");
            if (selecITEM != null) {
                txtTipo.setText(selecITEM.getTipo());
                txtMarca.setText(selecITEM.getMarca());
                txtModelo.setText(selecITEM.getModelo());
                txtCantidad.setText(selecITEM.getCantidad());
                txtDetalles.setText(selecITEM.getDetalles());

                //Definimos la Imagen a mostrar dependiendo del tipo y la marca del item
                switch (selecITEM.getTipo()) {
                    case "Bomba de Agua":
                        imagenITEM.setImageResource(R.drawable.bombaagua);
                        break;
                    case "Bombona Gas - Pequeña":
                        switch (selecITEM.getMarca()) {
                            case "Campingaz":
                                imagenITEM.setImageResource(R.drawable.gas01);
                                break;
                            case "Primus":
                                imagenITEM.setImageResource(R.drawable.gasprimus);
                                break;
                            default:
                                imagenITEM.setImageResource(R.drawable.campingaz);
                                break;
                        }
                        break;
                    case "Bombona Gas - Grande":
                        if (selecITEM.getMarca().equals("Campingaz")) {
                            imagenITEM.setImageResource(R.drawable.gas02);
                        } else {
                            imagenITEM.setImageResource(R.drawable.gas02);
                        }
                        break;
                    case "Cabezal/Hornillo":
                        if (selecITEM.getMarca().equals("Campingaz")) {
                            imagenITEM.setImageResource(R.drawable.hornillo01);
                        } else {
                            imagenITEM.setImageResource(R.drawable.hornillo02);
                        }
                        break;
                    case "Cacerola":
                        imagenITEM.setImageResource(R.drawable.cacerola);
                        break;
                    case "Grupo Electrógeno":
                        imagenITEM.setImageResource(R.drawable.grupo);
                        break;
                    case "Lumogas":
                        imagenITEM.setImageResource(R.drawable.lumogaz);
                        break;
                    case "Sarten":
                        imagenITEM.setImageResource(R.drawable.sarten);
                        break;
                    case "Olla":
                        imagenITEM.setImageResource(R.drawable.cacerola);
                        break;
                }

                // OBTENEMOS SU REFERENCIA
                idITEM = intentSelecc.getString("Referencia");
                assert idITEM != null;
                rutaITEM = ref.child(idITEM);
            }
        }
    }

    // EDITAMOS EL ITEM
    @OnClick(R.id.btnInfoItemEditar)
    public void EDITAR_ITEM() {
        Intent intent = new Intent(this, Ventana_NewItem.class);
        intent.putExtra("Objeto", selecITEM);
        intent.putExtra("Referencia", idITEM);
        startActivity(intent);
        finish();
    }

    // ELIMINAMOS EL ITEM
    @OnClick(R.id.btnInfoItemEliminar)
    public void BORRAR_ITEM() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Ventana_InfoItem.this);
        builder.setMessage("¿Borrar: " + selecITEM.getTipo() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                rutaITEM.removeValue();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }
}
