package com.example.crudfirebase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ExamenNacional.R;
import com.example.ExamenNacional.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.aro.setText(model.getAro());
        holder.modelo.setText(model.getModelo());
        holder.talla.setText(model.getTalla());

        Glide.with(holder.img.getContext())
                .load(model.getImgURl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                Log.d("MainAdapter", "Editar clic en posición: " + adapterPosition);

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                            .setContentHolder(new ViewHolder(R.layout.ventana_emergente))
                            .create();

                    // Obtener la vista de contenido de DialogPlus
                    View contentView = dialogPlus.getHolderView();

                    if (contentView != null) {
                        EditText aro = contentView.findViewById(R.id.aroText);
                        EditText modelo = contentView.findViewById(R.id.modeloText);
                        EditText talla = contentView.findViewById(R.id.tallaText);
                        EditText imgURL = contentView.findViewById(R.id.img1);

                        // Asegurarse de que los EditText se limpien antes de usarlos
                        aro.setText("");
                        modelo.setText("");
                        talla.setText("");
                        imgURL.setText("");

                        Button actualizar = contentView.findViewById(R.id.btn_actualizar);

                        dialogPlus.show();

                        actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("Aro", aro.getText().toString());
                                map.put("Modelo", modelo.getText().toString());
                                map.put("Talla", talla.getText().toString());
                                map.put("imgURL", imgURL.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("Empresa xyz")
                                        .child(getRef(adapterPosition).getKey()).updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(holder.aro.getContext(), "Actualizacion Correcta", Toast.LENGTH_SHORT).show();
                                                dialogPlus.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(holder.aro.getContext(), "Error de la Actualizacion", Toast.LENGTH_SHORT).show();
                                                dialogPlus.dismiss();
                                            }
                                        });
                            }
                        });
                    }

                    Log.d("MainAdapter", "Mostrando ventana emergente para posición: " + adapterPosition);
                }
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.aro.getContext());
                builder.setTitle("¿Estás seguro de ELIMINARLO?");
                builder.setMessage("ELIMINADO");

                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Empresa xyz")
                                .child(getRef(position).getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.aro.getContext(), "Eliminación exitosa", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.aro.getContext(), "Error en la eliminación", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.aro.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView aro, modelo, talla;
        Button editar, eliminar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            aro = itemView.findViewById(R.id.aroText);
            modelo = itemView.findViewById(R.id.modeloText);
            talla = itemView.findViewById(R.id.tallaText);

            editar = itemView.findViewById(R.id.btn_editar);
            eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}
