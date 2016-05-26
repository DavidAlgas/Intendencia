package com.example.david.intendencia;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Tarea;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ventana_Tareas extends AppCompatActivity {

    @Bind(R.id.ListadoTareas)
    RecyclerView listadoRV;

    @Bind(R.id.txtNuevoTarea)
    EditText NuevaTarea;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tareas");
    private FirebaseRecyclerAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        ButterKnife.bind(this);


        listadoRV.setHasFixedSize(true);
        listadoRV.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new FirebaseRecyclerAdapter<Tarea, TareasViewHolder>(Tarea.class, R.layout.linea_tareas, TareasViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(TareasViewHolder holder, Tarea tarea, int i) {
                holder.txtItem.setText(tarea.getTarea());
                holder.txtUser.setText(tarea.getCreadoPOR());

                if (tarea.isCompleto()) {
                    holder.imgDone.setVisibility(View.VISIBLE);
                } else {
                    holder.imgDone.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public TareasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new TareasViewHolder(view);
            }
        };

        listadoRV.setAdapter(adaptador);


        // Creamos el listener para ver los eventos
        ChildEventListener tareasListener = ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listadoRV.scrollToPosition(adaptador.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listadoRV.scrollToPosition(adaptador.getItemCount() - 1);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adaptador.cleanup();
    }


    // Create a custom ViewHolder
    public class TareasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @Bind(R.id.txtLineaTarea)
        TextView txtItem;
        @Bind(R.id.txxLineaUsuario)
        TextView txtUser;
        @Bind(R.id.imgLineaDone)
        ImageView imgDone;

        public TareasViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Tarea itemSelec = (Tarea) adaptador.getItem(position);
            DatabaseReference modificaA = adaptador.getRef(position);
            boolean completed = !itemSelec.isCompleto();

            itemSelec.setCompleto(completed);
            Map<String, Object> updates = new HashMap<>();
            updates.put("completo", completed);
            modificaA.updateChildren(updates);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            Tarea itemSelec = (Tarea) adaptador.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(Ventana_Tareas.this);
            builder.setMessage("¿Borrar: " + itemSelec.getTarea() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    int position = getAdapterPosition();
                    DatabaseReference borrarA = adaptador.getRef(position);
                    borrarA.removeValue();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();

            return false;
        }
    }


    // Pulsamos para add una nueva tarea
    @OnClick(R.id.btnAddTarea)
    public void añadirTarea() {
        String tareaNueva = NuevaTarea.getText().toString();
        String usuario = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() == null) {
                usuario = user.getEmail();
            } else {
                usuario = user.getDisplayName();
            }
        }
        NuevaTarea.setText("");

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        if (!tareaNueva.isEmpty()) {
            Tarea newTarea = new Tarea(usuario, tareaNueva.trim());
            ref.push().setValue(newTarea);
        }
    }
}
