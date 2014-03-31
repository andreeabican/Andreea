package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Writer extends Entity {
    final private SimpleStringProperty      idscriitor;
    final private SimpleStringProperty      nume;
    final private SimpleStringProperty      prenume;
    final private SimpleStringProperty      biografie;
    
    public Writer(String idscriitor, String nume, String prenume, String biografie) {
        this.idscriitor  = new SimpleStringProperty(idscriitor);
        this.nume        = new SimpleStringProperty(nume);
        this.prenume     = new SimpleStringProperty(prenume);
        this.biografie   = new SimpleStringProperty(biografie);
    }
    
    public Writer(ArrayList<Object> writer) {
        this.idscriitor  = new SimpleStringProperty(writer.get(0).toString());
        this.nume        = new SimpleStringProperty(writer.get(1).toString());
        this.prenume     = new SimpleStringProperty(writer.get(2).toString());
        this.biografie   = new SimpleStringProperty(writer.get(3).toString());   
    }
    
    public String getIdscriitor() {
        return idscriitor.get();
    }  
    
    public void setIdscriitor(String idscriitor) {
        this.idscriitor.set(idscriitor);
    }  
    
    public String getNume() {
        return nume.get();
    }
    
    public void setNume(String nume) {
        this.nume.set(nume);
    }
    
    public String getPrenume() {
        return prenume.get();
    }
    
    public void sePrenume(String prenume) {
        this.prenume.set(prenume);
    }
    
    public String getBiografie() {
        return biografie.get();
    }
    
    public void setBiografie(String biografie) {
        this.biografie.set(biografie);
    }   

    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idscriitor.get());
        values.add(nume.get());
        values.add(prenume.get());
        values.add(biografie.get());     
        return values;
    }
}
