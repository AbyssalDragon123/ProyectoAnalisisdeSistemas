package Modelos;

import com.google.gson.annotations.SerializedName;

public class ModeloCategoria {
    @SerializedName("idCategoria")
    private int IdCategoria;

    @SerializedName("nombreCat")
    private String NombreCat;

    @SerializedName("descripcion")
    private String Descripcion;

    public int getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(int IdCategoria) {
        this.IdCategoria = IdCategoria;
    }

    public String getNombreCat() {
        return NombreCat;
    }

    public void setNombreCat(String NombreCat) {
        this.NombreCat = NombreCat;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    @Override
    public String toString() {
        return IdCategoria + " - " + NombreCat;
    }

    public String getNombre() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}