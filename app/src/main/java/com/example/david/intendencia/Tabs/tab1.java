package com.example.david.intendencia.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.intendencia.Objetos.Item;
import com.example.david.intendencia.R;
import com.example.david.intendencia.Ventana_InfoItem;
import com.example.david.intendencia.Ventana_NewItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class tab1 extends Fragment {

    private RecyclerView itemsRV;
    private FirebaseRecyclerAdapter adaptadorItems;
    private final DatabaseReference refItems = FirebaseDatabase.getInstance().getReference("Items");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tab1 = inflater.inflate(R.layout.tab01, container, false);


        itemsRV = (RecyclerView) tab1.findViewById(R.id.ListadoItems);

        itemsRV.setHasFixedSize(true);
        itemsRV.setLayoutManager(new LinearLayoutManager(getActivity()));


        adaptadorItems = new FirebaseRecyclerAdapter<Item, ItemsViewHolder>(Item.class, R.layout.linea_items, ItemsViewHolder.class, refItems) {
            @Override
            protected void populateViewHolder(ItemsViewHolder holder, Item item, int i) {
                holder.txtItemTipo.setText(item.getTipo());
                holder.txtItemMarca.setText(item.getMarca());
                holder.txtItemModelo.setText(item.getModelo());
                holder.txtItemCantidad.setText(item.getCantidad());


                //Definimos la Imagen a mostrar dependiendo del tipo y la marca del item
                switch (item.getTipo()) {
                    case "Bomba de Agua":
                        holder.imagenItem.setImageResource(R.drawable.bombaagua);
                        break;
                    case "Bombona Gas - Pequeña":
                        switch (item.getMarca()) {
                            case "Campingaz":
                                holder.imagenItem.setImageResource(R.drawable.gas01);
                                break;
                            case "Primus":
                                holder.imagenItem.setImageResource(R.drawable.gasprimus);
                                break;
                            default:
                                holder.imagenItem.setImageResource(R.drawable.campingaz);
                                break;
                        }
                        break;
                    case "Bombona Gas - Grande":
                        if (item.getMarca().equals("Campingaz")) {
                            holder.imagenItem.setImageResource(R.drawable.gas02);
                        } else {
                            holder.imagenItem.setImageResource(R.drawable.gas02);
                        }
                        break;
                    case "Cabezal/Hornillo":
                        if (item.getMarca().equals("Campingaz")) {
                            holder.imagenItem.setImageResource(R.drawable.hornillo01);
                        } else {
                            holder.imagenItem.setImageResource(R.drawable.hornillo02);
                        }
                        break;
                    case "Cacerola":
                        holder.imagenItem.setImageResource(R.drawable.cacerola);
                        break;
                    case "Grupo Electrogeno":
                        holder.imagenItem.setImageResource(R.drawable.grupo);
                        break;
                    case "Lumogas":
                        holder.imagenItem.setImageResource(R.drawable.lumogaz);
                        break;
                    case "Sarten":
                        holder.imagenItem.setImageResource(R.drawable.sarten);
                        break;
                    case "Olla":
                        holder.imagenItem.setImageResource(R.drawable.cacerola);
                        break;
                }
            }

            @Override
            public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new ItemsViewHolder(view);
            }
        };

        // Creamos el listener para ver los eventos
        refItems.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                itemsRV.scrollToPosition(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (getView() != null) {
                    Snackbar.make(getView(), "Item Modificado", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (getView() != null) {
                    Snackbar.make(getView(), "Item Eliminado", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        itemsRV.setAdapter(adaptadorItems);
        return tab1;
    }

    // Create a custom ViewHolder
    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        @Bind(R.id.imgLineaItem)
        ImageView imagenItem;
        @Bind(R.id.txtLineaItemTipo)
        TextView txtItemTipo;
        @Bind(R.id.txtLineaItemMarca)
        TextView txtItemMarca;
        @Bind(R.id.txtLineaItemModelo)
        TextView txtItemModelo;
        @Bind(R.id.txtLineaItemCantidad)
        TextView txtItemCantidad;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            final Item itemSelecc = (Item) adaptadorItems.getItem(getAdapterPosition());
            final DatabaseReference rutaItem = adaptadorItems.getRef(getAdapterPosition());

            Intent intent = new Intent(getActivity(), Ventana_InfoItem.class);
            intent.putExtra("Objeto", itemSelecc);
            intent.putExtra("Referencia", rutaItem.getKey());
            startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.add(groupId, itemId, order, title)
            MenuItem accion1 = menu.add(0, v.getId(), 0, "Editar");
            accion1.setOnMenuItemClickListener(this);

            MenuItem accion2 = menu.add(0, v.getId(), 1, "Eliminar");
            accion2.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final Item itemSelecc = (Item) adaptadorItems.getItem(getAdapterPosition());
            final DatabaseReference rutaItem = adaptadorItems.getRef(getAdapterPosition());

            switch (item.getOrder()) {
                // OPCION EDITAR ITEM
                case 0:
                    // Editar Elemento.
                    Intent intent = new Intent(getActivity(), Ventana_NewItem.class);
                    intent.putExtra("Objeto", itemSelecc);
                    intent.putExtra("Referencia", rutaItem.getKey());
                    startActivity(intent);
                    return true;
                // OPCION ELIMINAR ITEM
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Borrar: " + itemSelecc.getTipo() + "?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            rutaItem.removeValue();
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
