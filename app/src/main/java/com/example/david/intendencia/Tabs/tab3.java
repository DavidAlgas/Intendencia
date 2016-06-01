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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Moroso;
import com.example.david.intendencia.Objetos.Tienda;
import com.example.david.intendencia.R;
import com.example.david.intendencia.Ventana_InfoTienda;
import com.example.david.intendencia.Ventana_NewTienda;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class tab3 extends Fragment {

    private RecyclerView tiendasRV;
    private FirebaseRecyclerAdapter adaptadorTiendas;
    private final DatabaseReference refTiendas = FirebaseDatabase.getInstance().getReference("Tiendas");
    private final DatabaseReference refMorosos = FirebaseDatabase.getInstance().getReference("Morosos");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                holder.ulltimaRevision.setText(tienda.getUltimaRevision());

                // MOSTRAMOS IMAGEN SEGUN MODELO
                switch (tienda.getModelo()) {
                    case "Canadiense":
                        holder.imgTienda.setImageResource(R.drawable.canadiense);
                        break;
                    case "Batisielles":
                        holder.imgTienda.setImageResource(R.drawable.batisielles);
                        break;
                    case "Pabellón":
                        holder.imgTienda.setImageResource(R.drawable.pabellon);
                        break;
                }

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
        refTiendas.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tiendasRV.scrollToPosition(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (getView() != null) {
                    Snackbar.make(getView(), "Tienda Modificada", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (getView() != null) {
                    Snackbar.make(getView(), "Tienda Eliminada", Snackbar.LENGTH_LONG).show();
                }
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
    public class TiendasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        @Bind(R.id.cvv)
        CardView fondoTienda;
        @Bind(R.id.txtLineaTiendaNombre)
        TextView txtNombre;
        @Bind(R.id.txtLineaTiendaModelo)
        TextView txtModelo;
        @Bind(R.id.imgLineaTienda)
        ImageView imgTienda;
        @Bind(R.id.txtLineaUltimaRevision)
        TextView ulltimaRevision;

        public TiendasViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.add(groupId, itemId, order, title)
            final Tienda tiendaSelecc = (Tienda) adaptadorTiendas.getItem(getAdapterPosition());
            String ID = "";
            if (user != null) {
                ID = user.getUid();
            }

            if (tiendaSelecc.getPoseedor().equals(ID)) {
                MenuItem accion0 = menu.add(0, v.getId(), 1, "Devolver");
                accion0.setOnMenuItemClickListener(this);
            } else {
                MenuItem accion0 = menu.add(0, v.getId(), 0, "Tomar Prestada");
                accion0.setOnMenuItemClickListener(this);
            }

            MenuItem accion1 = menu.add(0, v.getId(), 2, "Editar");
            accion1.setOnMenuItemClickListener(this);

            MenuItem accion2 = menu.add(0, v.getId(), 3, "Eliminar");
            accion2.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            final Tienda tiendaSelecc = (Tienda) adaptadorTiendas.getItem(position);
            final DatabaseReference rutaTienda = adaptadorTiendas.getRef(position);

            //ALMACENAMOS EL ID DE LA PERSONA Y UN NOMBRE DE USUARIO
            String ID = "", QUIEN = "";
            if (user != null) {
                ID = user.getUid();
                if (user.getDisplayName() == null) {
                    QUIEN = user.getEmail();
                } else {
                    QUIEN = user.getDisplayName();
                }
            }

            switch (item.getOrder()) {
                // OPCION TOMAR ITEM
                case 0:
                    if (tiendaSelecc.isDisponible()) {
                        ADD_TIENDA_MOROSO(tiendaSelecc, rutaTienda, ID, QUIEN);
                    } else {
                        Snackbar.make(itemView, "Tienda NO disponible", Snackbar.LENGTH_LONG).show();
                    }
                    return true;
                // OPCION DEVOLVER ITEM
                case 1:
                    DEL_TIENDA_MOROSO(tiendaSelecc, rutaTienda, ID, QUIEN);
                    return true;
                // OPCION EDITAR ITEM
                case 2:
                    // Editar Tienda.
                    Intent intent = new Intent(getActivity(), Ventana_NewTienda.class);
                    intent.putExtra("Objeto", tiendaSelecc);
                    intent.putExtra("Referencia", rutaTienda.getKey());
                    startActivity(intent);
                    return true;
                // OPCION ELIMINAR ITEM
                case 3:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Borrar: " + tiendaSelecc.getNombre() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            rutaTienda.removeValue();
                            //Snackbar.make(itemView, "Tienda Eliminada", Snackbar.LENGTH_LONG).show();
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

        @Override
        public void onClick(View v) {
            final Tienda tiendaSelecc = (Tienda) adaptadorTiendas.getItem(getAdapterPosition());
            final DatabaseReference rutaTienda = adaptadorTiendas.getRef(getAdapterPosition());

            Intent intent = new Intent(getActivity(), Ventana_InfoTienda.class);
            intent.putExtra("Objeto", tiendaSelecc);
            intent.putExtra("Referencia", rutaTienda.getKey());
            startActivity(intent);
        }
    }

    // METODO PARA AÑADIR LA NUEVA TIENDA A LA LISTA DEL MOROSO
    public void ADD_TIENDA_MOROSO(Tienda tiendaSelecc, final DatabaseReference rutaTienda, String ID, String QUIEN) {
        // Marcamos como NO DISPONIBLE esa tienda y le asignamos poseedor
        tiendaSelecc.setDisponible(false);
        tiendaSelecc.setPoseedor(ID);
        Map<String, Object> updates = new HashMap<>();
        updates.put("poseedor", ID);
        updates.put("disponible", false);
        rutaTienda.updateChildren(updates);


        // LO EJECUTAMOS 1 VEZ
        final String finalID = ID, finalQUIEN = QUIEN;
        refMorosos.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // COMPROBAMOS SI EXITE EL MOROSO
                if (dataSnapshot.exists()) {
                    //Si exite ese usuario le añadimos la nueva tienda
                    Moroso moroso = dataSnapshot.getValue(Moroso.class);
                    List<String> listaAdd = moroso.getIDTIENDA();
                    listaAdd.add(rutaTienda.getKey());

                    Moroso morodoUpdate = new Moroso(finalQUIEN, listaAdd);
                    refMorosos.child(finalID).setValue(morodoUpdate);
                } else {
                    // Si no exite ese moroso lo creamos y le añadimos la tienda.
                    List<String> listaNew = new ArrayList<>();
                    listaNew.add(rutaTienda.getKey());
                    Moroso moroso = new Moroso(finalQUIEN, listaNew);
                    refMorosos.child(finalID).setValue(moroso);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // METODO PARA DEVOLVER LA TIENDA
    public void DEL_TIENDA_MOROSO(Tienda tiendaSelecc, final DatabaseReference rutaTienda, String ID, String QUIEN) {
        // Marcamos como DISPONIBLE esa tienda y le eliminamos poseedor
        tiendaSelecc.setDisponible(true);
        tiendaSelecc.setPoseedor("Nadie");
        Map<String, Object> updates = new HashMap<>();
        updates.put("poseedor", "Nadie");
        updates.put("disponible", true);
        rutaTienda.updateChildren(updates);

        // LO EJECUTAMOS 1 VEZ
        final String finalID = ID, finalQUIEN = QUIEN;
        refMorosos.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // RECORREMOS EL ARRAY Y ELIMINAMOS LA TIENDA
                Moroso exitente = dataSnapshot.getValue(Moroso.class);
                if (exitente != null) {
                    List<String> listaDel = exitente.getIDTIENDA();
                    for (int a = 0; a < listaDel.size(); a++) {
                        if (listaDel.contains(rutaTienda.getKey())) {
                            listaDel.remove(a);
                            break;
                        }
                    }

                    // Si la lista esta vacia eliminamos la entrada entra para ahorrar espacio de BD
                    if (listaDel.isEmpty()) {
                        refMorosos.child(finalID).removeValue();
                    } else {
                        Moroso morodoUpdate = new Moroso(finalQUIEN, listaDel);
                        refMorosos.child(finalID).setValue(morodoUpdate);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
