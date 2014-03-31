package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Collection extends Entity {
    final private SimpleStringProperty      idcolectie;
    final private SimpleStringProperty      denumire;
    final private SimpleStringProperty      descriere;

    
    public Collection(String idcolectie, String denumire, String descriere) {
        this.idcolectie    = new SimpleStringProperty(idcolectie);
        this.denumire      = new SimpleStringProperty(denumire);
        this.descriere     = new SimpleStringProperty(descriere);
    }
    
    public Collection(ArrayList<Object> collection) {
        this.idcolectie    = new SimpleStringProperty(collection.get(0).toString());
        this.denumire      = new SimpleStringProperty(collection.get(1).toString());
        this.descriere     = new SimpleStringProperty(collection.get(2).toString());        
    }
    
    public String getIdcolectie() {
        return idcolectie.get();
    }  
    
    public void setIdcolectie(String idcolectie) {
        this.idcolectie.set(idcolectie);
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
        values.add(idcolectie.get());
        values.add(denumire.get());
        values.add(descriere.get());      
        return values;
    }
}