package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class Book extends Entity {
    final private SimpleStringProperty      idcarte;
    final private SimpleStringProperty      titlu;
    final private SimpleStringProperty      descriere;
    final private SimpleStringProperty      ideditura;
    final private SimpleStringProperty      anaparitie;
    final private SimpleStringProperty      editie;
    final private SimpleStringProperty      idcolectie;
    final private SimpleStringProperty      iddomeniu;
    final private SimpleStringProperty      stoc;
    final private SimpleStringProperty      pret;
    
    public Book(String idcarte, String titlu, String descriere, String ideditura, String anaparitie, String editie, String idcolectie, String iddomeniu, String stoc, String pret) {
        this.idcarte        = new SimpleStringProperty(idcarte);
        this.titlu          = new SimpleStringProperty(titlu);
        this.descriere      = new SimpleStringProperty(descriere);
        this.ideditura      = new SimpleStringProperty(ideditura);
        this.anaparitie     = new SimpleStringProperty(anaparitie);
        this.editie         = new SimpleStringProperty(editie);
        this.idcolectie     = new SimpleStringProperty(idcolectie);
        this.iddomeniu      = new SimpleStringProperty(iddomeniu);
        this.stoc           = new SimpleStringProperty(stoc);
        this.pret           = new SimpleStringProperty(pret);
    }
    
    public Book(ArrayList<Object> book) {
        this.idcarte        = new SimpleStringProperty(book.get(0).toString());
        this.titlu          = new SimpleStringProperty(book.get(1).toString());
        this.descriere      = new SimpleStringProperty(book.get(2).toString());
        this.ideditura      = new SimpleStringProperty(book.get(3).toString());
        this.anaparitie     = new SimpleStringProperty(book.get(4).toString());
        this.editie         = new SimpleStringProperty(book.get(5).toString());
        this.idcolectie     = new SimpleStringProperty(book.get(6).toString());
        this.iddomeniu      = new SimpleStringProperty(book.get(7).toString());
        this.stoc           = new SimpleStringProperty(book.get(8).toString());
        this.pret           = new SimpleStringProperty(book.get(9).toString());
    }
    
    public String getIdcarte() {
        return idcarte.get();
    }  
    
    public void setIdcarte(String idcarte) {
        this.idcarte.set(idcarte);
    }  
    
    public String getTitlu() {
        return titlu.get();
    }
    
    public void setTitlu(String titlu) {
        this.titlu.set(titlu);
    }
    
    public String getDescriere() {
        return descriere.get();
    }
    
    public void setDescriere(String descriere) {
        this.descriere.set(descriere);
    }
    
    public String getIdeditura() {
        return ideditura.get();
    }
    
    public void setIdeditura(String ideditura) {
        this.ideditura.set(ideditura);
    }
    
    public String getAnaparitie() {
        return anaparitie.get();
    }
    
    public void setAnaparitie(String anaparitie) {
        this.anaparitie.set(anaparitie);
    }    
    
    public String getEditie() {
        return editie.get();
    }
    
    public void setEditie(String editie) {
        this.editie.set(editie);
    }
    
    public String getIdcolectie() {
        return idcolectie.get();
    }
    
    public void setIdcolectie(String idcolectie) {
        this.idcolectie.set(idcolectie);
    }
    
    public String getIddomeniu() {
        return iddomeniu.get();
    }
    
    public void setIddomeniu(String iddomeniu) {
        this.iddomeniu.set(iddomeniu);
    }
    
    public String getStoc() {
        return stoc.get();
    }
    
    public void setStoc(String stoc) {
        this.stoc.set(stoc);
    }    
    
    public String getPret() {
        return pret.get();
    }
    
    public void setPret(String pret) {
        this.pret.set(pret);
    }    
    
    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idcarte.get());
        values.add(titlu.get());
        values.add(descriere.get());
        values.add(ideditura.get());
        values.add(anaparitie.get());
        values.add(editie.get());
        values.add(idcolectie.get());
        values.add(iddomeniu.get());
        values.add(stoc.get());
        values.add(pret.get());        
        return values;
    }
}
