package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class HRinfo extends Entity {
	private SimpleStringProperty      orasosire;
	private SimpleStringProperty      oraplecare;
	private SimpleStringProperty      intarziere;
	private SimpleStringProperty      dupaprogram;
	private SimpleStringProperty      oresuplimentare;
	
	HRinfo(String orasosire, String oraplecare, String intarziere,
			String dupaprogram, String oresuplimentare)
	{
		this.orasosire			= new SimpleStringProperty(orasosire);
		this.oraplecare 		= new SimpleStringProperty(oraplecare);
		this.intarziere 		= new SimpleStringProperty(intarziere);
		this.dupaprogram 		= new SimpleStringProperty(dupaprogram);
		this.oresuplimentare 	= new SimpleStringProperty(oresuplimentare);
	}
	
	public HRinfo(ArrayList<Object> hrInfo) {
        this.orasosire			= new SimpleStringProperty(hrInfo.get(0).toString());
        this.oraplecare			= new SimpleStringProperty(hrInfo.get(1).toString());
        this.intarziere			= new SimpleStringProperty(hrInfo.get(2).toString());
        this.dupaprogram		= new SimpleStringProperty(hrInfo.get(3).toString());
        this.oresuplimentare	= new SimpleStringProperty(hrInfo.get(4).toString());
    }

	public String getOrasosire() {
		return orasosire.get();
	}

	public void setOrasosire(String orasosire) {
		this.orasosire.set(orasosire);
	}

	public String getOraplecare() {
		return oraplecare.get();
	}

	public void setOraplecare(String oraplecare) {
		this.oraplecare.set(oraplecare);
	}

	public String getIntarziere() {
		return intarziere.get();
	}

	public void setIntarziere(String intarziere) {
		this.intarziere.set(intarziere);
	}

	public String getDupaprogram() {
		return dupaprogram.get();
	}

	public void setDupaprogram(String dupaprogram) {
		this.dupaprogram.set(dupaprogram);
	}

	public String getOresuplimentare() {
		return oresuplimentare.get();
	}

	public void setOresuplimentare(String oresuplimentare) {
		this.oresuplimentare.set(oresuplimentare);
	}
}
