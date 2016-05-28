package com.example.david.intendencia.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Tienda;
import com.example.david.intendencia.R;
import com.example.david.intendencia.Ventana_NewTienda;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class tab3 extends Fragment {

    private RecyclerView tiendasRV;
    private FirebaseRecyclerAdapter adaptadorTiendas;
    private final DatabaseReference refTiendas = FirebaseDatabase.getInstance().getReference("Tiendas");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View tab3 = inflater.inflate(R.layout.tab03, container, false);

        tiendasRV = (RecyclerView) tab3.findViewById(R.id.ListadoTiendas);

        tiendasRV.setHasFixedSize(true);
        tiendasRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        adaptadorTiendas = new FirebaseRecyclerAdapter<Tienda, TiendasViewHolder>(Tienda.class, R.layout.linea_tiendas, TiendasViewHolder.class, refTiendas) {
            @Override
            protected void populateViewHolder(TiendasViewHolder holder, Tienda tienda, int i) {
                holder.txtNombre.setText(tienda.getNombre());
                holder.txtModelo.setText(tienda.getModelo());

                if (tienda.isDisponible()) {
                    holder.fondoTienda.setBackgroundColor(0xffffffff);
                } else {
                    holder.fondoTienda.setBackgroundColor(0xffff0000);
                }
            }

            @Override
            public TiendasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new TiendasViewHolder(view);
            }
        };

        // Creamos el listener para ver los eventos
        ChildEventListener tiendasListener = refTiendas.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tiendasRV.scrollToPosition(adaptadorTiendas.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                tiendasRV.scrollToPosition(adaptadorTiendas.getItemCount() - 1);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                tiendasRV.scrollToPosition(adaptadorTiendas.getItemCount() - 1);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tiendasRV.setAdapter(adaptadorTiendas);

        return tab3;
    }

    // Create a custom ViewHolder
    public class TiendasViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        @Bind(R.id.cvv)
        CardView fondoTienda;
        @Bind(R.id.txtLineaTiendaNombre)
        TextView txtNombre;
        @Bind(R.id.txtLineaTiendaModelo)
        TextView txtModelo;

        public TiendasViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.add(groupId, itemId, order, title)
            MenuItem accion0 = menu.add(0, v.getId(), 0, "Tomar Prestada");
            accion0.setOnMenuItemClickListener(this);

            MenuItem accion1 = menu.add(0, v.getId(), 1, "Editar");
            accion1.setOnMenuItemClickListener(this);

            MenuItem accion2 = menu.add(0, v.getId(), 2, "Eliminar");
            accion2.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            final Tienda tiendaSelecc = (Tienda) adaptadorTiendas.getItem(position);
            final DatabaseReference rutaTienda = adaptadorTiendas.getRef(position);

            switch (item.getOrder()) {
                case 0:
                    if (tiendaSelecc.isDisponible()) {
                        tiendaSelecc.setDisponible(false);

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("disponible", false);
                        rutaTienda.updateChildren(updates);
                    } else {
                        Snackbar.make(itemView, "Tienda NO disponible", Snackbar.LENGTH_LONG).show();
                    }
                    return true;
                case 1:
                    // Editar Tienda.
                    Intent intent = new Intent(getActivity(), Ventana_NewTienda.class);
                    intent.putExtra("TNombre", tiendaSelecc.getNombre());
                    intent.putExtra("TModelo", tiendaSelecc.getModelo());
                    intent.putExtra("TTipo", tiendaSelecc.getTipo());
                    intent.putExtra("TCapacidad", tiendaSelecc.getCapacidad());
                    intent.putExtra("TPiquetas", tiendaSelecc.getNpiquetas());
                    intent.putExtra("TEstado", tiendaSelecc.getEstado());
                    intent.putExtra("TFecha", tiendaSelecc.getUltimaRevision());
                    intent.putExtra("Referencia", rutaTienda.getKey());
                    startActivity(intent);
                    return true;
                case 2:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Borrar: " + tiendaSelecc.getNombre() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            rutaTienda.removeValue();
                            Snackbar.make(itemView, "Tienda Eliminada", Snackbar.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
                    return true;
            }
            return false;
        }


    }
}
