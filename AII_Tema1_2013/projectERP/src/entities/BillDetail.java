package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class BillDetail extends Entity {
    final private SimpleStringProperty      iddetaliufactura;
    final private SimpleStringProperty      idfactura;
    final private SimpleStringProperty      idcarte;
    final private SimpleStringProperty      cantitate;
    
    public BillDetail(String iddetaliufactura, String idfactura, String idcarte, String cantitate) {
        this.iddetaliufactura  = new SimpleStringProperty(iddetaliufactura);
        this.idfactura         = new SimpleStringProperty(idfactura);
        this.idcarte           = new SimpleStringProperty(idcarte);
        this.cantitate         = new SimpleStringProperty(cantitate);
    }
    
    public BillDetail(ArrayList<Object> supplyorderdetail) {
        this.iddetaliufactura  = new SimpleStringProperty(supplyorderdetail.get(0).toString());
        this.idfactura         = new SimpleStringProperty(supplyorderdetail.get(1).toString());
        this.idcarte           = new SimpleStringProperty(supplyorderdetail.get(2).toString());
        this.cantitate         = new SimpleStringProperty(supplyorderdetail.get(3).toString());   
    }
    
    public String getIddetaliufactura() {
        return iddetaliufactura.get();
    }  
    
    public void setIddetaliufactura(String iddetaliufactura) {
        this.iddetaliufactura.set(iddetaliufactura);
    }  
    
    public String getIdfactura() {
        return idfactura.get();
    }
    
    public void setIdfactura(String idfactura) {
        this.idfactura.set(idfactura);
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
        values.add(iddetaliufactura.get());
        values.add(idfactura.get());
        values.add(idcarte.get());
        values.add(cantitate.get());     
        return values;
    }
}
