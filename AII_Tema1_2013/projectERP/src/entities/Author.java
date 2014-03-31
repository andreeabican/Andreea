package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Author extends Entity {
    final private SimpleStringProperty      idautor;
    final private SimpleStringProperty      idcarte;
    final private SimpleStringProperty      idscriitor;

    
    public Author(String idautor, String idcarte, String idscriitor) {
        this.idautor        = new SimpleStringProperty(idautor);
        this.idcarte        = new SimpleStringProperty(idcarte);
        this.idscriitor     = new SimpleStringProperty(idscriitor);
    }
    
    public Author(ArrayList<Object> author) {
        this.idautor        = new SimpleStringProperty(author.get(0).toString());
        this.idcarte        = new SimpleStringProperty(author.get(1).toString());
        this.idscriitor     = new SimpleStringProperty(author.get(2).toString());        
    }
    
    public String getIdautor() {
        return idautor.get();
    }  
    
    public void setIdautor(String idautor) {
        this.idautor.set(idautor);
    }  
    
    public String getIdcarte() {
        return idcarte.get();
    }
    
    public void setIdcarte(String idcarte) {
        this.idcarte.set(idcarte);
    }
    
    public String getIdscriitor() {
        return idscriitor.get();
    }
    
    public void setIdscriitor(String idscriitor) {
        this.idscriitor.set(idscriitor);
    }   
    
    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idautor.get());
        values.add(idcarte.get());
        values.add(idscriitor.get());      
        return values;
    }
}