package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Category extends Entity {
    final private SimpleStringProperty      iddomeniu;
    final private SimpleStringProperty      denumire;
    final private SimpleStringProperty      descriere;

    
    public Category(String iddomeniu, String denumire, String descriere) {
        this.iddomeniu    = new SimpleStringProperty(iddomeniu);
        this.denumire     = new SimpleStringProperty(denumire);
        this.descriere    = new SimpleStringProperty(descriere);
    }
    
    public Category(ArrayList<Object> category) {
        this.iddomeniu    = new SimpleStringProperty(category.get(0).toString());
        this.denumire     = new SimpleStringProperty(category.get(1).toString());
        this.descriere    = new SimpleStringProperty(category.get(2).toString());
    }
    
    public String getIddomeniu() {
        return iddomeniu.get();
    }  
    
    public void setIddomeniu(String iddomeniu) {
        this.iddomeniu.set(iddomeniu);
    }  
    
    public String getDenumire() {
        return denumire.get();
    }
    
    public void setDenumire(String denumire) {
        this.denumire.set(denumire);
    }
    
    public String getDescriere() {
        return descriere.get();
    }
    
    public void setDescriere(String descriere) {
        this.descriere.set(descriere);
    }   
    
    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(iddomeniu.get());
        values.add(denumire.get());
        values.add(descriere.get());      
        return values;
    }
}