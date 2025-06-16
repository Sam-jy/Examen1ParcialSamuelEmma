package com.example.pm2examencuentas1092.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examencuentas1092.R;
import com.example.pm2examencuentas1092.config.SQLiteConexion;
import com.example.pm2examencuentas1092.model.Contacto;

import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {
    private SQLiteConexion conexion;
    private List<Contacto> lista;
    private ContactoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        conexion = new SQLiteConexion(this);
        ListView lv = findViewById(R.id.lvContactos);

        cargarLista(lv);
    }

    private void cargarLista(ListView lv) {
        lista = conexion.getAllContactos();
        adapter = new ContactoAdapter(this, lista);
        lv.setAdapter(adapter);
    }

    public void showOpciones(Contacto c) {
        String[] items = {"Llamar", "Eliminar", "Actualizar", "Compartir", "Ver Imagen"};
        new AlertDialog.Builder(this)
                .setTitle(c.getNombre())
                .setItems(items, (dlg, which) -> {
                    switch (which) {
                        case 0:
                            confirmarLlamada(c);
                            break;
                        case 1:
                            eliminarContacto(c);
                            break;
                        case 2:
                            actualizarContacto(c);
                            break;
                        case 3:
                            compartir(c);
                            break;
                        case 4:
                            verImagen(c);
                            break;
                    }
                })
                .show();
    }
    
    private void confirmarLlamada(Contacto contacto) {
        new AlertDialog.Builder(this)
                .setTitle("Llamar")
                .setMessage("¿Deseas llamar a " + contacto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contacto.getTelefono()));
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    private void eliminarContacto(Contacto contacto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Eliminar contacto " + contacto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    conexion.deleteContacto(contacto.getId());
                    cargarLista(findViewById(R.id.lvContactos));
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    private void actualizarContacto(Contacto contacto) {
        new AlertDialog.Builder(this)
                .setTitle("Actualizar")
                .setMessage("Función de actualización para " + contacto.getNombre())
                .setPositiveButton("OK", null)
                .show();
    }
    
    private void verImagen(Contacto contacto) {
        if (contacto.getFotoUri() != null && !contacto.getFotoUri().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(contacto.getFotoUri()), "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Imagen")
                    .setMessage("Este contacto no tiene imagen")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void compartir(Contacto c) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String texto = "Nombre: " + c.getNombre() +
                "\nTeléfono: " + c.getTelefono() +
                "\nPaís: " + c.getPais() +
                "\nNota: " + c.getNota();
        i.putExtra(Intent.EXTRA_TEXT, texto);
        if (c.getFotoUri() != null && !c.getFotoUri().isEmpty()) {
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(c.getFotoUri()));
            i.setType("image/*");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(Intent.createChooser(i, "Compartir contacto"));
    }
}

