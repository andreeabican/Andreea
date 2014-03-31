package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class SupplyOrder extends Entity {
    final private SimpleStringProperty      idcomandaaprovizionare;
    final private SimpleStringProperty      serienumar;
    final private SimpleStringProperty      data;
    final private SimpleStringProperty      stare;
    final private SimpleStringProperty      ideditura;
    
    public SupplyOrder(String idcomandaaprovizionare, String serienumar, String data, String stare, String ideditura) {
        this.idcomandaaprovizionare = new SimpleStringProperty(idcomandaaprovizionare);
        this.serienumar             = new SimpleStringProperty(serienumar);
        this.data                   = new SimpleStringProperty(data);
        this.stare                  = new SimpleStringProperty(stare);
        this.ideditura              = new SimpleStringProperty(ideditura);
    }
    
    public SupplyOrder(ArrayList<Object> supplyorder) {
        this.idcomandaaprovizionare = new SimpleStringProperty(supplyorder.get(0).toString());
        this.serienumar             = new SimpleStringProperty(supplyorder.get(1).toString());
        this.data                   = new SimpleStringProperty(supplyorder.get(2).toString());
        this.stare                  = new SimpleStringProperty(supplyorder.get(3).toString());
        this.ideditura              = new SimpleStringProperty(supplyorder.get(4).toString());       
    }
    
    public String getIdcomandaaprovizionare() {
        return idcomandaaprovizionare.get();
    }  
    
    public void setIdcomandaaprovizionare(String idcomandaaprovizionare) {
        this.idcomandaaprovizionare.set(idcomandaaprovizionare);
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
    
    public String getIdeditura() {
        return ideditura.get();
    }
    
    public void setIdeditura(String ideditura) {
        this.ideditura.set(ideditura);
    }    

    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idcomandaaprovizionare.get());
        values.add(serienumar.get());
        values.add(data.get());
        values.add(stare.get());
        values.add(ideditura.get());       
        return values;
    }
}
