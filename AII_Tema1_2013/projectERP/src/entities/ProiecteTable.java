package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class ProiecteTable extends Entity {
	private SimpleStringProperty      numeproiect;
	private SimpleStringProperty      datastart;
	private SimpleStringProperty      datafinal;
	private SimpleStringProperty      versiuni;
	
	public ProiecteTable(String numeproiect, String datastart, String datafinal, String versiuni) {
		this.numeproiect 	= new SimpleStringProperty(numeproiect);
		this.datastart 		= new SimpleStringProperty(datastart);
		this.datafinal 		= new SimpleStringProperty(datafinal);
		this.versiuni 		= new SimpleStringProperty(versiuni);
	}
	
	public ProiecteTable(ArrayList<Object> hrTable) {
        this.numeproiect	= new SimpleStringProperty(hrTable.get(0).toString());
        this.datastart	  	= new SimpleStringProperty(hrTable.get(1).toString());
        this.datafinal		= new SimpleStringProperty(hrTable.get(2).toString());
        this.versiuni		= new SimpleStringProperty(hrTable.get(3).toString());
    }

	public String getNumeproiect() {
		return numeproiect.get();
	}

	public void setNumeproiect(String numeproiect) {
		this.numeproiect.set(numeproiect);
	}

	public String getDatastart() {
		return datastart.get();
	}

	public void setDatastart(String datastart) {
		this.datastart.set(datastart);
	}

	public String getDatafinal() {
		return datafinal.get();
	}

	public void setDatafinal(String datafinal) {
		this.datafinal.set(datafinal);
	}

	public String getVersiuni() {
		return versiuni.get();
	}

	public void setVersiuni(String versiuni) {
		this.versiuni.set(versiuni);
	}
	
}
