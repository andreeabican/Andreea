package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class SupplyOrderDetail extends Entity {
    final private SimpleStringProperty      iddetaliucomandaparovizionare;
    final private SimpleStringProperty      idcomandaaprovizionare;
    final private SimpleStringProperty      idcarte;
    final private SimpleStringProperty      cantitate;
    
    public SupplyOrderDetail(String iddetaliucomandaparovizionare, String idcomandaaprovizionare, String idcarte, String cantitate) {
        this.iddetaliucomandaparovizionare  = new SimpleStringProperty(iddetaliucomandaparovizionare);
        this.idcomandaaprovizionare         = new SimpleStringProperty(idcomandaaprovizionare);
        this.idcarte                        = new SimpleStringProperty(idcarte);
        this.cantitate                      = new SimpleStringProperty(cantitate);
    }
    
    public SupplyOrderDetail(ArrayList<Object> supplyorderdetail) {
        this.iddetaliucomandaparovizionare  = new SimpleStringProperty(supplyorderdetail.get(0).toString());
        this.idcomandaaprovizionare         = new SimpleStringProperty(supplyorderdetail.get(1).toString());
        this.idcarte                        = new SimpleStringProperty(supplyorderdetail.get(2).toString());
        this.cantitate                      = new SimpleStringProperty(supplyorderdetail.get(3).toString());   
    }
    
    public String getIddetaliucomandaaprovizionare() {
        return iddetaliucomandaparovizionare.get();
    }  
    
    public void setIddetaliucomandaaprovizionare(String iddetaliucomandaaprovizionare) {
        this.iddetaliucomandaparovizionare.set(iddetaliucomandaaprovizionare);
    }  
    
    public String getIdcomandaaprovizionare() {
        return idcomandaaprovizionare.get();
    }
    
    public void setIdcomandaaprovizionare(String serienumar) {
        this.idcomandaaprovizionare.set(serienumar);
    }
    
    public String getIdcarte() {
        return idcarte.get();
    }
    
    public void seIdcarte(String idcarte) {
        this.idcarte.set(idcarte);
    }
    
    public String getCantitate() {
        return cantitate.get();
    }
    
    public void setCantitate(String cantitate) {
        this.cantitate.set(cantitate);
    }   

    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(iddetaliucomandaparovizionare.get());
        values.add(idcomandaaprovizionare.get());
        values.add(idcarte.get());
        values.add(cantitate.get());     
        return values;
    }
}
