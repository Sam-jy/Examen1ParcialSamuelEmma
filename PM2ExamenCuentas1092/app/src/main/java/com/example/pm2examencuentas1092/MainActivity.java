package com.example.pm2examencuentas1092;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2examencuentas1092.ui.AgregarContactoActivity;
import com.example.pm2examencuentas1092.ui.ListaContactosActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAgregarContacto;
    private Button btnListaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        btnListaContactos = findViewById(R.id.btnListaContactos);

        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarContactoActivity.class);
                startActivity(intent);
            }
        });

        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaContactosActivity.class);
                startActivity(intent);
            }
        });
    }
} 