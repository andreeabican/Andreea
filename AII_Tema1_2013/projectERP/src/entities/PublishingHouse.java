package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class PublishingHouse extends Entity {
    final private SimpleStringProperty      ideditura;
    final private SimpleStringProperty      denumire;
    final private SimpleStringProperty      CIF;
    final private SimpleStringProperty      descriere;
    final private SimpleStringProperty      localitate;
    final private SimpleStringProperty      regiune;
    final private SimpleStringProperty      tara;
    
    public PublishingHouse(String ideditura, String denumire, String CIF, String descriere, String localitate, String regiune, String tara) {
        this.ideditura  = new SimpleStringProperty(ideditura);
        this.denumire   = new SimpleStringProperty(denumire);
        this.CIF        = new SimpleStringProperty(CIF);
        this.descriere  = new SimpleStringProperty(descriere);
        this.localitate = new SimpleStringProperty(localitate);
        this.regiune    = new SimpleStringProperty(regiune);
        this.tara       = new SimpleStringProperty(tara);
    }
    
    public PublishingHouse(ArrayList<Object> publishinghouse) {
        this.ideditura  = new SimpleStringProperty(publishinghouse.get(0).toString());
        this.denumire   = new SimpleStringProperty(publishinghouse.get(1).toString());
        this.CIF        = new SimpleStringProperty(publishinghouse.get(2).toString());
        this.descriere  = new SimpleStringProperty(publishinghouse.get(3).toString());
        this.localitate = new SimpleStringProperty(publishinghouse.get(4).toString());
        this.regiune    = new SimpleStringProperty(publishinghouse.get(5).toString());
        this.tara       = new SimpleStringProperty(publishinghouse.get(6).toString());     
    }
    
    public String getIdeditura() {
        return ideditura.get();
    }  
    
    public void setIdeditura(String ideditura) {
        this.ideditura.set(ideditura);
    }  
    
    public String getDenumire() {
        return denumire.get();
    }
    
    public void setDenumire(String denumire) {
        this.denumire.set(denumire);
    }
    
    public String getCIF() {
        return CIF.get();
    }
    
    public void setCIF(String CIF) {
        this.CIF.set(CIF);
    }
    
    public String getDescriere() {
        return descriere.get();
    }
    
    public void setDescriere(String descriere) {
        this.descriere.set(descriere);
    }
    
    public String getLocalitate() {
        return localitate.get();
    }
    
    public void setLocalitate(String localitate) {
        this.localitate.set(localitate);
    }    
    
    public String getRegiune() {
        return regiune.get();
    }
    
    public void setRegiune(String regiune) {
        this.regiune.set(regiune);
    }
    
    public String getTara() {
        return tara.get();
    }
    
    public void setTara(String tara) {
        this.tara.set(tara);
    } 
    
    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(ideditura.get());
        values.add(denumire.get());
        values.add(CIF.get());
        values.add(descriere.get());
        values.add(localitate.get());
        values.add(regiune.get());
        values.add(tara.get());       
        return values;
    }
}
