package com.example.david.intendencia;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.intendencia.Objetos.Tienda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_NewTienda extends AppCompatActivity {

    @Bind(R.id.txtTiendaNombre)
    EditText TiendaNombre;
    @Bind(R.id.spnTiendaModelo)
    Spinner TiendaModelo;
    @Bind(R.id.spnTiendaTipo)
    Spinner TiendaTipo;
    @Bind(R.id.txtTiendaCapacidad)
    EditText TiendaCapacidad;
    @Bind(R.id.txtTiendaPiquetas)
    EditText TiendaPiquetas;
    @Bind(R.id.txtTiendaEstado)
    EditText TiendaEstado;
    @Bind(R.id.txtTiendaUltima)
    EditText TiendaUltima;
    @Bind(R.id.btnNuevaTienda)
    Button botonAddTienda;

    private boolean modificotienda = false;
    private boolean Disponible = true;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tiendas");
    private DatabaseReference refUpdate;
    private Locale locale = new Locale("es", "ES");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss", locale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtienda);
        ButterKnife.bind(this);
        Ventana_Main demo = new Ventana_Main();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Cuando editamos un seteamos los valores en los campos
        Bundle tiendaUpdate = getIntent().getExtras();
        if (tiendaUpdate != null) {
            modificotienda = true;
            botonAddTienda.setText(R.string.btnTiendaModify);
            getSupportActionBar().setTitle("Editar Tienda");

            TiendaNombre.setText(tiendaUpdate.getString("TNombre"));
            ArrayAdapter spn1 = (ArrayAdapter) TiendaModelo.getAdapter();
            TiendaModelo.setSelection(spn1.getPosition(tiendaUpdate.getString("TModelo")));
            ArrayAdapter spn2 = (ArrayAdapter) TiendaTipo.getAdapter();
            TiendaTipo.setSelection(spn2.getPosition(tiendaUpdate.getString("TTipo")));
            TiendaCapacidad.setText(tiendaUpdate.getString("TCapacidad"));
            TiendaPiquetas.setText(tiendaUpdate.getString("TPiquetas"));
            TiendaEstado.setText(tiendaUpdate.getString("TEstado"));
            TiendaUltima.setText(tiendaUpdate.getString("TFecha"));
            Disponible = tiendaUpdate.getBoolean("IDisponibilidad");

            String path = tiendaUpdate.getString("Referencia");
            assert path != null;
            refUpdate = ref.child(path);
        }
    }


    // AÑADIMOS UNA NUEVA TIENDA
    @OnClick(R.id.btnNuevaTienda)
    public void Add_Tienda() {
        final Calendar calend = Calendar.getInstance();

        String Nombre = TiendaNombre.getText().toString();
        String Modelo = TiendaModelo.getSelectedItem().toString();
        String Tipo = TiendaTipo.getSelectedItem().toString();
        String Capacidad = TiendaCapacidad.getText().toString();
        String Piquetas = TiendaPiquetas.getText().toString();
        String Estado = TiendaEstado.getText().toString();
        String Revision = TiendaUltima.getText().toString();

        // Log con el usuario que lo edito y la fecha
        String LUpdate = sdf.format(new Date());
        if (user != null) {
            if (user.getDisplayName() == null) {
                LUpdate += " por " + user.getEmail();
            } else {
                LUpdate += " por " + user.getDisplayName();
            }
        }


        if (VALIDATOR(Nombre, Modelo, Tipo, Capacidad, Piquetas, Estado, Revision)) {
            Tienda nuevaTienda;
            if (!modificotienda) {
                nuevaTienda = new Tienda(Nombre, Modelo, Tipo, Capacidad, Piquetas, Estado, Revision, LUpdate, Disponible);
                ref.push().setValue(nuevaTienda);
                Toast.makeText(Ventana_NewTienda.this, "Añadido Correctamente", Toast.LENGTH_SHORT).show();
            } else {
                nuevaTienda = new Tienda(Nombre, Modelo, Tipo, Capacidad, Piquetas, Estado, Revision, LUpdate, Disponible);
                refUpdate.setValue(nuevaTienda);
            }
            finish();
        }
    }

    public boolean VALIDATOR(String nombre, String modelo, String tipo, String capacidad, String piquetas, String estado, String ultima) {
        int todoOK = 0;

        if (nombre.isEmpty()) {
            TiendaNombre.setError("Rellene el campo");
            todoOK += 0;
        } else {
            TiendaNombre.setError(null);
            todoOK += 1;
        }

        if (modelo.equalsIgnoreCase("- Seleccione Modelo -")) {
            TextView errorText = (TextView) TiendaModelo.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("- Seleccione Modelo -");
            todoOK += 0;
        } else {
            todoOK += 1;
        }

        if (tipo.equalsIgnoreCase("- Seleccione Tipo -")) {
            TextView errorText = (TextView) TiendaTipo.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("- Seleccione Tipo -");
            todoOK += 0;
        } else {
            todoOK += 1;
        }

        if (capacidad.isEmpty()) {
            TiendaCapacidad.setError("Rellene el campo");
            todoOK += 0;
        } else {
            TiendaCapacidad.setError(null);
            todoOK += 1;
        }

        if (piquetas.isEmpty()) {
            TiendaPiquetas.setError("Rellene el campo");
            todoOK += 0;
        } else {
            TiendaPiquetas.setError(null);
            todoOK += 1;
        }

        if (estado.isEmpty()) {
            TiendaEstado.setError("Rellene el campo");
            todoOK += 0;
        } else {
            TiendaEstado.setError(null);
            todoOK += 1;
        }

        if (ultima.isEmpty()) {
            TiendaUltima.setError("Rellene el campo");
            todoOK += 0;
        } else {
            TiendaUltima.setError(null);
            todoOK += 1;
        }


        return todoOK == 7;
    }

    @OnClick(R.id.btnFecha)
    public void Fecha() {
        DialogFragment newFragment = new DatePickerFragment(TiendaUltima);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // CLASE PARA EL DATAPICKER
    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        String fecha;
        EditText UltimaFecha;

        public DatePickerFragment(EditText UltimaFecha) {
            this.UltimaFecha = UltimaFecha;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            fecha = day + "/" + (month + 1) + "/" + year;
            UltimaFecha.setText(fecha);
        }
    }
}