package com.example.pm2examencuentas1092.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.pm2examencuentas1092.R;
import com.example.pm2examencuentas1092.model.Contacto;

import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contacto> {
    private Context ctx;
    private List<Contacto> lista;

    public ContactoAdapter(Context context, List<Contacto> lista) {
        super(context, 0, lista);
        this.ctx = context;
        this.lista = lista;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.contacto_item, parent, false);

        Contacto c = lista.get(pos);
        ImageView ivFoto      = convertView.findViewById(R.id.ivFotoItem);
        TextView tvNombre     = convertView.findViewById(R.id.tvNombreItem);
        TextView tvPaisTel    = convertView.findViewById(R.id.tvPaisTelItem);
        ImageButton btnOpc    = convertView.findViewById(R.id.btnOpciones);

        if (c.getFotoUri() != null && !c.getFotoUri().isEmpty()) {
            ivFoto.setImageURI(Uri.parse(c.getFotoUri()));
        } else {
            ivFoto.setImageResource(R.drawable.ic_person);
        }

        tvNombre.setText(c.getNombre());
        tvPaisTel.setText(c.getPais() + " • " + c.getTelefono());

        btnOpc.setOnClickListener(v -> {
            if (ctx instanceof ListaContactosActivity) {
                ((ListaContactosActivity) ctx).showOpciones(c);
            }
        });

        return convertView;
    }
}
