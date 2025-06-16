package com.example.pm2examencuentas1092.model;

public class Contacto {
    private int    id;
    private String pais;
    private String nombre;
    private String telefono;
    private String nota;
    private String fotoUri;

    public Contacto() { }

    public Contacto(String pais, String nombre, String telefono, String nota, String fotoUri) {
        this.pais     = pais;
        this.nombre   = nombre;
        this.telefono = telefono;
        this.nota     = nota;
        this.fotoUri  = fotoUri;
    }

    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }
    public String getPais()            { return pais; }
    public void   setPais(String pais) { this.pais = pais; }
    public String getNombre()          { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono()        { return telefono; }
    public void   setTelefono(String telefono) { this.telefono = telefono; }
    public String getNota()            { return nota; }
    public void   setNota(String nota) { this.nota = nota; }
    public String getFotoUri()         { return fotoUri; }
    public void   setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }
}
