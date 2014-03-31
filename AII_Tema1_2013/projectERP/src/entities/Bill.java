package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Bill extends Entity {
    final private SimpleStringProperty      idfactura;
    final private SimpleStringProperty      serienumar;
    final private SimpleStringProperty      data;
    final private SimpleStringProperty      stare;
    final private SimpleStringProperty      CNP;
    
    public Bill(String idfactura, String serienumar, String data, String stare, String CNP) {
        this.idfactura  = new SimpleStringProperty(idfactura);
        this.serienumar = new SimpleStringProperty(serienumar);
        this.data       = new SimpleStringProperty(data);
        this.stare      = new SimpleStringProperty(stare);
        this.CNP        = new SimpleStringProperty(CNP);
    }
    
    public Bill(ArrayList<Object> bill) {
        this.idfactura  = new SimpleStringProperty(bill.get(0).toString());
        this.serienumar = new SimpleStringProperty(bill.get(1).toString());
        this.data       = new SimpleStringProperty(bill.get(2).toString());
        this.stare      = new SimpleStringProperty(bill.get(3).toString());
        this.CNP        = new SimpleStringProperty(bill.get(4).toString());       
    }
    
    public String getIdfactura() {
        return idfactura.get();
    }  
    
    public void setIdfactura(String idfactura) {
        this.idfactura.set(idfactura);
    }  
    
    public String getSerienumar() {
        return serienumar.get();
    }
    
    public void setSerienumar(String serienumar) {
        this.serienumar.set(serienumar);
    }
    
    public String getData() {
        return data.get();
    }
    
    public void setData(String data) {
        this.data.set(data);
    }
    
    public String getStare() {
        return stare.get();
    }
    
    public void setStare(String stare) {
        this.stare.set(stare);
    }
    
    public String getCNP() {
        return CNP.get();
    }
    
    public void setCNP(String CNP) {
        this.CNP.set(CNP);
    }    

    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idfactura.get());
        values.add(serienumar.get());
        values.add(data.get());
        values.add(stare.get());
        values.add(CNP.get());       
        return values;
    }
}
