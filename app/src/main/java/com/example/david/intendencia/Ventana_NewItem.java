package com.example.david.intendencia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.intendencia.Objetos.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_NewItem extends AppCompatActivity {
    @Bind(R.id.txtItemModelo)
    EditText ItemModelo;
    @Bind(R.id.txtItemCantidad)
    EditText ItemCantidad;
    @Bind(R.id.txtItemDetalles)
    EditText ItemDetalles;
    @Bind(R.id.btnNuevoItem)
    Button btnAddItem;
    @Bind(R.id.txtItemMarca)
    AutoCompleteTextView ItemMarca;
    @Bind(R.id.txtItemTipo)
    AutoCompleteTextView ItemTipo;

    private boolean modificoItem = false;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
    private DatabaseReference refUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newitem);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Cargamos los string al textView de autocompletar para las marcas y los tipos
        String[] tipos = getResources().getStringArray(R.array.tipo_item);
        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tipos);
        ItemTipo.setAdapter(adapter01);

        String[] marcas = getResources().getStringArray(R.array.marca_item);
        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, marcas);
        ItemMarca.setAdapter(adapter02);

        //Cuando editamos un seteamos los valores en los campos
        Bundle intentUpdate = getIntent().getExtras();
        if (intentUpdate != null) {
            modificoItem = true;
            btnAddItem.setText(R.string.btnTiendaModify);
            getSupportActionBar().setTitle("Editar Item");

            Item editoItem = (Item) intentUpdate.getSerializable("Objeto");
            if (editoItem != null) {
                ItemTipo.setText(editoItem.getTipo());
                ItemMarca.setText(editoItem.getMarca());
                ItemModelo.setText(editoItem.getModelo());
                ItemCantidad.setText(editoItem.getCantidad());
                ItemDetalles.setText(editoItem.getDetalles());

                String path = intentUpdate.getString("Referencia");
                assert path != null;
                refUpdate = ref.child(path);
            }
        }

    }

    // CANCELAMOS
    @OnClick(R.id.btnItemCancelar)
    public void Cancel() {
        finish();
    }

    // AÑADIMOS UN NUEVO ITEM
    @OnClick(R.id.btnNuevoItem)
    public void Add_Item() {

        String Tipo = ItemTipo.getText().toString();
        String Marca = ItemMarca.getText().toString();
        String Modelo = ItemModelo.getText().toString();
        String Cantidad = ItemCantidad.getText().toString();
        String Detalles = ItemDetalles.getText().toString();

        if (VALIDATOR(Modelo, Cantidad, Detalles)) {
            Item newItem;
            if (!modificoItem) {
                newItem = new Item(Marca, Modelo, Tipo, Cantidad, Detalles);
                ref.push().setValue(newItem);
                Toast.makeText(Ventana_NewItem.this, "Añadido Correctamente", Toast.LENGTH_SHORT).show();
            } else {
                newItem = new Item(Marca, Modelo, Tipo, Cantidad, Detalles);
                refUpdate.setValue(newItem);
            }
            finish();
        }
    }


    public boolean VALIDATOR(String modelo, String cantidad, String detalles) {
        int todoOK = 0;

        if (modelo.isEmpty()) {
            ItemModelo.setError("Rellene el campo");
            todoOK += 0;
        } else {
            ItemModelo.setError(null);
            todoOK += 1;
        }

        if (cantidad.isEmpty()) {
            ItemCantidad.setError("Rellene el campo");
            todoOK += 0;
        } else {
            ItemCantidad.setError(null);
            todoOK += 1;
        }

        if (detalles.isEmpty()) {
            ItemDetalles.setError("Rellene el campo");
            todoOK += 0;
        } else {
            ItemDetalles.setError(null);
            todoOK += 1;
        }

        return todoOK == 3;
    }
}
