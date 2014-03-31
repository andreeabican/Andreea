package entities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

public class User extends Entity {
    final private SimpleStringProperty      CNP;
    final private SimpleStringProperty      nume;
    final private SimpleStringProperty      prenume;
    final private SimpleStringProperty      adresa;
    final private SimpleStringProperty      telefon;
    final private SimpleStringProperty      email;
    final private SimpleStringProperty      tip;
    final private SimpleStringProperty      rol;
    final private SimpleStringProperty      numeutilizator;
    final private SimpleStringProperty      parola;
    
    public User(String CNP, String nume, String prenume, String adresa, String telefon, String email, String tip, String rol, String numeutilizator, String parola) {
        this.CNP            = new SimpleStringProperty(CNP);
        this.nume           = new SimpleStringProperty(nume);
        this.prenume        = new SimpleStringProperty(prenume);
        this.adresa         = new SimpleStringProperty(adresa);
        this.telefon        = new SimpleStringProperty(telefon);
        this.email          = new SimpleStringProperty(email);
        this.tip            = new SimpleStringProperty(tip);
        this.rol            = new SimpleStringProperty(rol);
        this.numeutilizator = new SimpleStringProperty(numeutilizator);
        this.parola         = new SimpleStringProperty(parola);
    }
    
    public User(ArrayList<Object> user) {
        this.CNP            = new SimpleStringProperty(user.get(0).toString());
        this.nume           = new SimpleStringProperty(user.get(1).toString());
        this.prenume        = new SimpleStringProperty(user.get(2).toString());
        this.adresa         = new SimpleStringProperty(user.get(3).toString());
        this.telefon        = new SimpleStringProperty(user.get(4).toString());
        this.email          = new SimpleStringProperty(user.get(5).toString());
        this.tip            = new SimpleStringProperty(user.get(6).toString());
        this.rol            = new SimpleStringProperty(user.get(7).toString());
        this.numeutilizator = new SimpleStringProperty(user.get(8).toString());
        this.parola         = new SimpleStringProperty(user.get(9).toString());        
    }
    
    public String getCNP() {
        return CNP.get();
    }  
    
    public void setCNP(String CNP) {
        this.CNP.set(CNP);
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
    
    public void setPrenume(String prenume) {
        this.prenume.set(prenume);
    }
    
    public String getAdresa() {
        return adresa.get();
    }
    
    public void setAdresa(String adresa) {
        this.adresa.set(adresa);
    }
    
    public String getTelefon() {
        return telefon.get();
    }
    
    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }    
    
    public String getEmail() {
        return email.get();
    }
    
    public void setEmail(String email) {
        this.email.set(email);
    }
    
    public String getTip() {
        return tip.get();
    }
    
    public void setTip(String tip) {
        this.tip.set(tip);
    }
    
    public String getRol() {
        return rol.get();
    }
    
    public void setRol(String rol) {
        this.rol.set(rol);
    }
    
    public String getNumeutilizator() {
        return numeutilizator.get();
    }
    
    public void setNumeutilizator(String numeutilizator) {
        this.numeutilizator.set(numeutilizator);
    }    
    
    public String getParola() {
        return parola.get();
    }
    
    public void setParola(String parola) {
        this.parola.set(parola);
    }    
    
    @Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(CNP.get());
        values.add(nume.get());
        values.add(prenume.get());
        values.add(adresa.get());
        values.add(telefon.get());
        values.add(email.get());
        values.add(tip.get());
        values.add(rol.get());
        values.add(numeutilizator.get());
        values.add(parola.get());        
        return values;
    }
}
