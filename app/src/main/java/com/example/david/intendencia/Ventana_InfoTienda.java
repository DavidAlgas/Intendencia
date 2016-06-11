package com.example.david.intendencia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Tienda;
import com.example.david.intendencia.Tabs.tab3;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Ventana_InfoTienda extends AppCompatActivity {

    @Bind(R.id.imgInfoTienda)
    ImageView imagenTienda;
    @Bind(R.id.txtInfoTiendaNombre)
    TextView txtNombre;
    @Bind(R.id.txtInfoTiendaModelo)
    TextView txtModelo;
    @Bind(R.id.txtInfoTiendaTipo)
    TextView txtTipo;
    @Bind(R.id.txtInfoTiendaNpiquetas)
    TextView txtPiquetas;
    @Bind(R.id.txtInfoTiendaCapacidad)
    TextView txtCapacidad;
    @Bind(R.id.txtInfoTiendaRevision)
    TextView txtLastRevision;
    @Bind(R.id.txtInfoTiendaEstado)
    TextView txtEstado;
    @Bind(R.id.txtInfoTiendaPoseedor)
    TextView txtPoseedor;
    @Bind(R.id.btnInfoTiendaCoger)
    Button btnCoger_Dejar;
    @Bind(R.id.btnInfoTiendaEditar)
    Button btnEditar;
    @Bind(R.id.btnInfoTiendaEliminar)
    Button btnEliminar;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tiendas");
    private DatabaseReference rutaTienda;
    private Tienda selecTienda;
    private String idTienda, usuarioID, usuarioTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tienda);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (user != null) {
            usuarioID = user.getUid();
            if (user.getDisplayName() == null) {
                usuarioTag = user.getEmail();
            } else {
                usuarioTag = user.getDisplayName();
            }
        }

        // SETEAMOS LOS VALORES DE LA TIENDA SELECCIONADA
        Bundle intentSelecc = getIntent().getExtras();
        if (intentSelecc != null) {
            selecTienda = (Tienda) intentSelecc.getSerializable("Objeto");
            if (selecTienda != null) {
                txtNombre.setText(selecTienda.getNombre());
                txtModelo.setText(selecTienda.getModelo());
                txtTipo.setText(selecTienda.getTipo());
                txtPiquetas.append(selecTienda.getNpiquetas() + " personas ±");
                txtCapacidad.append(selecTienda.getCapacidad() + " ±");
                txtLastRevision.setText(selecTienda.getUltimaRevision());
                txtEstado.setText(selecTienda.getEstado());


                //si la tienda no esta disponible no podremos editar
                if (selecTienda.isDisponible()) {
                    btnEditar.setEnabled(true);
                } else {
                    btnEditar.setEnabled(false);
                }

                // Comprobamos si el usuario logeado actual es el poseedor de la tienda
                if (selecTienda.getPoseedorID().equals(user.getUid())) {
                    btnCoger_Dejar.setText("Dejar");
                    btnEditar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                } else {
                    if (selecTienda.isDisponible()) {
                        btnCoger_Dejar.setEnabled(true);
                        btnCoger_Dejar.setText("Coger");
                        btnEditar.setEnabled(true);
                    } else {
                        btnCoger_Dejar.setEnabled(false);
                        btnEliminar.setEnabled(false);
                    }
                }

                // Seteamos el poseedor de la tienda actual
                if (selecTienda.getPoseedorNombre().equalsIgnoreCase("Nadie")) {
                    txtPoseedor.setText("Nadie");
                } else {
                    txtPoseedor.setText(selecTienda.getPoseedorNombre());
                }

                // MOSTRAMOS AL IMAGEN DE LA TIENDA SELECCIONADA
                switch (selecTienda.getModelo()) {
                    case "Canadiense":
                        imagenTienda.setImageResource(R.drawable.canadiense);
                        break;
                    case "Batisielles":
                        imagenTienda.setImageResource(R.drawable.batisielles);
                        break;
                    case "Pabellón":
                        imagenTienda.setImageResource(R.drawable.pabellon);
                        break;
                }

                // OBTENEMOS SU REFERENCIA
                idTienda = intentSelecc.getString("Referencia");
                assert idTienda != null;
                rutaTienda = ref.child(idTienda);
            }
        }
    }

    // TOMAMOS PRESTADA LA TIENDA
    @OnClick(R.id.btnInfoTiendaCoger)
    public void TOMAR_PRESTADA() {

        switch (btnCoger_Dejar.getText().toString()) {
            case "Coger":
                tab3.ADD_TIENDA_MOROSO(selecTienda, rutaTienda, usuarioID, usuarioTag);
                finish();
                break;
            case "Dejar":
                tab3.DEL_TIENDA_MOROSO(selecTienda, rutaTienda, usuarioID, usuarioTag);
                finish();
                break;
        }
    }

    // EDITAMOS LA TIENDA
    @OnClick(R.id.btnInfoTiendaEditar)
    public void EDITAR_TIENDA() {
        Intent intent = new Intent(this, Ventana_NewTienda.class);
        intent.putExtra("Objeto", selecTienda);
        intent.putExtra("Referencia", idTienda);
        startActivity(intent);
        finish();
    }

    // ELIMINAMOS LA TIENDA
    @OnClick(R.id.btnInfoTiendaEliminar)
    public void BORRAR_TIENDA() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Ventana_InfoTienda.this);
        builder.setMessage("¿Borrar: " + selecTienda.getNombre() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                rutaTienda.removeValue();
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
