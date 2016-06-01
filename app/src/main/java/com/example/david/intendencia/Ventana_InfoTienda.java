package com.example.david.intendencia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Tienda;

import butterknife.Bind;
import butterknife.ButterKnife;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_info_tienda);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // SETEAMOS LOS VALORES DE LA TIENDA SELECCIONADA
        Bundle intentSelecc = getIntent().getExtras();
        if (intentSelecc != null) {
            Tienda selecTienda = (Tienda) intentSelecc.getSerializable("Objeto");
            if (selecTienda != null) {
                txtNombre.setText(selecTienda.getNombre());
                txtModelo.setText(selecTienda.getModelo());
                txtTipo.setText(selecTienda.getTipo());
                txtPiquetas.append(selecTienda.getNpiquetas() + " personas ±");
                txtCapacidad.append(selecTienda.getCapacidad() + " ±");
                txtLastRevision.setText(selecTienda.getUltimaRevision());
                txtEstado.setText(selecTienda.getEstado());

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
            }
        }
    }
}
