package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class UserDataTable extends Entity {
	
	private SimpleStringProperty	idutilizator;
	private SimpleStringProperty	cnputilizator;
	private SimpleStringProperty	numeutilizator;
	private SimpleStringProperty	prenumeutilizator;
	private SimpleStringProperty	adresautilizator;
	private SimpleStringProperty	telefonutilizator;
	private SimpleStringProperty	emailutilizator;
	private SimpleStringProperty	ibanutilizator;
	private SimpleStringProperty	nrcontractutilizator;
	private SimpleStringProperty	dataangajariiutilizator;
	private SimpleStringProperty	tiputilizator;
	private SimpleStringProperty	zileconcediuutilizator;
	private SimpleStringProperty	orecontractutilizator;
	private SimpleStringProperty	salariuutilizator;
	private SimpleStringProperty	usernameutilizator;
	private SimpleStringProperty	parolautilizator;
	
	public UserDataTable(String idutilizator, String cnputilizator, String numeutilizator, String prenumeutilizator,
						String adresautilizator, String telefonutilizator, String emailutilizator,
						String ibanutilizator, String nrcontractutilizator, String dataangajariiutilizator,
						String tiputilizator, String zileconcediuutilizator, String orecontractutilizator,
						String salariuutilizator, String usernameutilizator, String parolautilizator) 
	{
		this.idutilizator = new SimpleStringProperty(idutilizator);
		this.cnputilizator = new SimpleStringProperty(cnputilizator);
		this.numeutilizator = new SimpleStringProperty(numeutilizator);
		this.prenumeutilizator = new SimpleStringProperty(prenumeutilizator);
		this.adresautilizator = new SimpleStringProperty(adresautilizator);
		this.telefonutilizator = new SimpleStringProperty(telefonutilizator);
		this.emailutilizator = new SimpleStringProperty(emailutilizator);
		this.ibanutilizator = new SimpleStringProperty(ibanutilizator);
		this.nrcontractutilizator = new SimpleStringProperty(nrcontractutilizator);
		this.dataangajariiutilizator = new SimpleStringProperty(dataangajariiutilizator);
		this.tiputilizator = new SimpleStringProperty(tiputilizator);
		this.zileconcediuutilizator = new SimpleStringProperty(zileconcediuutilizator);
		this.orecontractutilizator = new SimpleStringProperty(orecontractutilizator);
		this.salariuutilizator = new SimpleStringProperty(salariuutilizator);
		this.usernameutilizator = new SimpleStringProperty(usernameutilizator);
		this.parolautilizator = new SimpleStringProperty(parolautilizator);
	}
	
	public UserDataTable(ArrayList<Object> user) {
		this.idutilizator = new SimpleStringProperty(user.get(0).toString());
		this.cnputilizator = new SimpleStringProperty(user.get(1).toString());
		this.numeutilizator = new SimpleStringProperty(user.get(2).toString());
		this.prenumeutilizator = new SimpleStringProperty(user.get(3).toString());
		this.adresautilizator = new SimpleStringProperty(user.get(4).toString());
		this.telefonutilizator = new SimpleStringProperty(user.get(5).toString());
		this.emailutilizator = new SimpleStringProperty(user.get(6).toString());
		this.ibanutilizator = new SimpleStringProperty(user.get(7).toString());
		this.nrcontractutilizator = new SimpleStringProperty(user.get(8).toString());
		this.dataangajariiutilizator = new SimpleStringProperty(user.get(9).toString());
		this.tiputilizator = new SimpleStringProperty(user.get(10).toString());
		this.zileconcediuutilizator = new SimpleStringProperty(user.get(11).toString());
		this.orecontractutilizator = new SimpleStringProperty(user.get(12).toString());
		this.salariuutilizator = new SimpleStringProperty(user.get(13).toString());
		this.usernameutilizator = new SimpleStringProperty(user.get(14).toString());
		this.parolautilizator = new SimpleStringProperty(user.get(15).toString());     
    }

	public String getIdutilizator() {
		return idutilizator.get();
	}

	public void setIdutilizator(String idutilizator) {
		this.idutilizator.set(idutilizator);
	}
	
	public String getCnputilizator() {
		return cnputilizator.get();
	}

	public void setCnputilizator(String cnputilizator) {
		this.cnputilizator.set(cnputilizator);
	}

	public String getNumeutilizator() {
		return numeutilizator.get();
	}

	public void setNumeutilizator(String numeutilizator) {
		this.numeutilizator.set(numeutilizator);
	}

	public String getPrenumeutilizator() {
		return prenumeutilizator.get();
	}

	public void setPrenumeutilizator(String prenumeutilizator) {
		this.prenumeutilizator.set(prenumeutilizator);
	}

	public String getAdresautilizator() {
		return adresautilizator.get();
	}

	public void setAdresautilizator(String adresautilizator) {
		this.adresautilizator.set(adresautilizator);
	}

	public String getTelefonutilizator() {
		return telefonutilizator.get();
	}

	public void setTelefonutilizator(String telefonutilizator) {
		this.telefonutilizator.set(telefonutilizator);
	}

	public String getEmailutilizator() {
		return emailutilizator.get();
	}

	public void setEmailutilizator(String emailutilizator) {
		this.emailutilizator.set(emailutilizator);
	}

	public String getIbanutilizator() {
		return ibanutilizator.get();
	}

	public void setIbanutilizator(String ibanutilizator) {
		this.ibanutilizator.set(ibanutilizator);
	}

	public String getNrcontractutilizator() {
		return nrcontractutilizator.get();
	}

	public void setNrcontractutilizator(String nrcontractutilizator) {
		this.nrcontractutilizator.set(nrcontractutilizator);
	}

	public String getDataangajariiutilizator() {
		return dataangajariiutilizator.get();
	}

	public void setDataangajariiutilizator(
			String dataangajariiutilizator) {
		this.dataangajariiutilizator.set(dataangajariiutilizator);
	}

	public String getTiputilizator() {
		return tiputilizator.get();
	}

	public void setTiputilizator(String tiputilizator) {
		this.tiputilizator.set(tiputilizator);
	}

	public String getZileconcediuutilizator() {
		return zileconcediuutilizator.get();
	}

	public void setZileconcediuutilizator(
			String zileconcediuutilizator) {
		this.zileconcediuutilizator.set(zileconcediuutilizator);
	}

	public String getOrecontractutilizator() {
		return orecontractutilizator.get();
	}

	public void setOrecontractutilizator(String orecontractutilizator) {
		this.orecontractutilizator.set(orecontractutilizator);
	}

	public String getSalariuutilizator() {
		return salariuutilizator.get();
	}

	public void setSalariuutilizator(String salariuutilizator) {
		this.salariuutilizator.set(salariuutilizator);
	}

	public String getUsernameutilizator() {
		return usernameutilizator.get();
	}

	public void setUsernameutilizator(String usernameutilizator) {
		this.usernameutilizator.set(usernameutilizator);
	}

	public String getParolautilizator() {
		return parolautilizator.get();
	}

	public void setParolautilizator(String parolautilizator) {
		this.parolautilizator.set(parolautilizator);
	}
	
	@Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();  
		values.add(idutilizator.get());
		values.add(cnputilizator.get());
        values.add(numeutilizator.get());
        values.add(prenumeutilizator.get());
        values.add(adresautilizator.get());
        values.add(telefonutilizator.get());
        values.add(emailutilizator.get());
        values.add(ibanutilizator.get());
        values.add(nrcontractutilizator.get());
        values.add(dataangajariiutilizator.get());
        values.add(tiputilizator.get());
        values.add(zileconcediuutilizator.get());
        values.add(orecontractutilizator.get());
        values.add(salariuutilizator.get());
        values.add(usernameutilizator.get());
        values.add(parolautilizator.get());
        return values;
    }
}
