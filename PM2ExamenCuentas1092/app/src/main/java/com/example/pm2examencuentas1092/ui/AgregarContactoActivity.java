package com.example.pm2examencuentas1092.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examencuentas1092.R;
import com.example.pm2examencuentas1092.config.SQLiteConexion;
import com.example.pm2examencuentas1092.model.Contacto;

public class AgregarContactoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;

    private Spinner spPais;
    private EditText etNombre, etTelefono, etNota;
    private ImageView ivFoto;
    private ImageButton btnSeleccionarImagen;
    private Button btnGuardar, btnContactos;

    private Uri imageUri;
    private SQLiteConexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        // Inicializar vistas
        spPais               = findViewById(R.id.spPais);
        etNombre             = findViewById(R.id.etNombre);
        etTelefono           = findViewById(R.id.etTelefono);
        etNota               = findViewById(R.id.etNota);
        ivFoto               = findViewById(R.id.ivFoto);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardar           = findViewById(R.id.btnGuardar);
        btnContactos         = findViewById(R.id.btnContactos);

        conexion = new SQLiteConexion(this);

        aplicarFiltrosEntrada();

        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        btnGuardar.setOnClickListener(v -> guardarContacto());

        btnContactos.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarContactoActivity.this, ListaContactosActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivFoto.setImageURI(imageUri);
        }
    }

    private void aplicarFiltrosEntrada() {
        InputFilter filtroNombre = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.toString(source.charAt(i)).matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]")) {
                        return "";
                    }
                }
                return null;
            }
        };
        etNombre.setFilters(new InputFilter[]{filtroNombre, new InputFilter.LengthFilter(50)});

        InputFilter filtroTelefono = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        etTelefono.setFilters(new InputFilter[]{filtroTelefono, new InputFilter.LengthFilter(10)});
    }

    private void guardarContacto() {
        String pais     = spPais.getSelectedItem().toString().trim();
        String nombre   = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String nota     = etNota.getText().toString().trim();
        String fotoUri  = imageUri != null ? imageUri.toString() : "";

        if (TextUtils.isEmpty(pais) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono)) {
            new AlertDialog.Builder(this)
                    .setTitle("Campos Obligatorios")
                    .setMessage("País, Nombre y Teléfono son obligatorios.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        if (telefono.length() < 7) {
            new AlertDialog.Builder(this)
                    .setTitle("Teléfono inválido")
                    .setMessage("El teléfono debe tener al menos 7 dígitos.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        Contacto c = new Contacto(pais, nombre, telefono, nota, fotoUri);
        long id = conexion.insertContacto(c);
        if (id > 0) {
            Toast.makeText(this, "Contacto guardado exitosamente", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar contacto", Toast.LENGTH_LONG).show();
        }
    }
}
