package com.example.pm2examencuentas1092.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examencuentas1092.R;
import com.example.pm2examencuentas1092.config.SQLiteConexion;
import com.example.pm2examencuentas1092.model.Contacto;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {
    private SQLiteConexion conexion;
    private List<Contacto> lista;
    private List<Contacto> listaFiltrada;
    private ContactoAdapter adapter;
    private Contacto contactoSeleccionado;
    
    private Button btnAtras, btnCompartirContacto, btnVerImagen, btnEliminarContacto, btnActualizarContacto;
    private EditText etBuscar;
    private ListView lvContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        inicializarVistas();
        configurarEventos();
        
        conexion = new SQLiteConexion(this);
        cargarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
        contactoSeleccionado = null;
    }
    
    private void inicializarVistas() {
        btnAtras = findViewById(R.id.btnAtras);
        btnCompartirContacto = findViewById(R.id.btnCompartirContacto);
        btnVerImagen = findViewById(R.id.btnVerImagen);
        btnEliminarContacto = findViewById(R.id.btnEliminarContacto);
        btnActualizarContacto = findViewById(R.id.btnActualizarContacto);
        etBuscar = findViewById(R.id.etBuscar);
        lvContactos = findViewById(R.id.lvContactos);
    }
    
    private void configurarEventos() {
        btnAtras.setOnClickListener(v -> finish());
        
        btnCompartirContacto.setOnClickListener(v -> {
            if (contactoSeleccionado != null) {
                compartir(contactoSeleccionado);
            } else {
                Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnVerImagen.setOnClickListener(v -> {
            if (contactoSeleccionado != null) {
                verImagen(contactoSeleccionado);
            } else {
                Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnEliminarContacto.setOnClickListener(v -> {
            if (contactoSeleccionado != null) {
                confirmarEliminar(contactoSeleccionado);
            } else {
                Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnActualizarContacto.setOnClickListener(v -> {
            if (contactoSeleccionado != null) {
                abrirPantallaEdicion(contactoSeleccionado);
            } else {
                Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
        
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarContactos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        lvContactos.setOnItemClickListener((parent, view, position, id) -> {
            if (listaFiltrada != null && position < listaFiltrada.size()) {
                contactoSeleccionado = listaFiltrada.get(position);
                Toast.makeText(this, "Seleccionado: " + contactoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarLista() {
        lista = conexion.getAllContactos();
        listaFiltrada = new ArrayList<>(lista);
        adapter = new ContactoAdapter(this, listaFiltrada);
        lvContactos.setAdapter(adapter);
    }
    
    private void filtrarContactos(String filtro) {
        if (lista == null) return;
        
        listaFiltrada.clear();
        if (filtro.isEmpty()) {
            listaFiltrada.addAll(lista);
        } else {
            for (Contacto contacto : lista) {
                if (contacto.getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                    contacto.getTelefono().contains(filtro) ||
                    contacto.getPais().toLowerCase().contains(filtro.toLowerCase())) {
                    listaFiltrada.add(contacto);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    private void verImagen(Contacto contacto) {
        if (contacto.getFotoUri() != null && !contacto.getFotoUri().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(contacto.getFotoUri()), "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No se puede abrir la imagen", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Este contacto no tiene imagen", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void confirmarEliminar(Contacto contacto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Contacto")
                .setMessage("¿Estás seguro de que deseas eliminar a " + contacto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    conexion.deleteContacto(contacto.getId());
                    cargarLista();
                    contactoSeleccionado = null;
                    Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void showOpciones(Contacto c) {
        contactoSeleccionado = c;
        String[] items = {"Llamar", "Actualizar", "Eliminar", "Compartir", "Ver Imagen"};
        new AlertDialog.Builder(this)
                .setTitle(c.getNombre())
                .setItems(items, (dlg, which) -> {
                    switch (which) {
                        case 0:
                            confirmarLlamada(c);
                            break;
                        case 1:
                            abrirPantallaEdicion(c);
                            break;
                        case 2:
                            confirmarEliminar(c);
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
                .setTitle("Acción")
                .setMessage("Deseo llamar a " + contacto.getNombre())
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contacto.getTelefono()));
                    try {
                        startActivity(intent);
                    } catch (SecurityException e) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + contacto.getTelefono()));
                        startActivity(dialIntent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void abrirPantallaEdicion(Contacto contacto) {
        Intent intent = new Intent(this, AgregarContactoActivity.class);
        
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_ID, contacto.getId());
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_PAIS, contacto.getPais());
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_NOMBRE, contacto.getNombre());
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_TELEFONO, contacto.getTelefono());
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_NOTA, contacto.getNota());
        intent.putExtra(AgregarContactoActivity.EXTRA_CONTACTO_FOTO, contacto.getFotoUri());
        
        startActivity(intent);
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

