package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class MembriEchipaTable extends Entity {
	private SimpleStringProperty      numeangajat;
	private SimpleStringProperty      prenumeangajat;
	private SimpleStringProperty      datainceput;
	private SimpleStringProperty      datasfarsit;
	
	MembriEchipaTable(String numeangajat, String prenumeangajat, String datainceput, String datasfarsit)
	{
		this.numeangajat 	= new SimpleStringProperty(numeangajat);
		this.prenumeangajat = new SimpleStringProperty(prenumeangajat);
		this.datainceput 	= new SimpleStringProperty(datainceput);
		this.datasfarsit 	= new SimpleStringProperty(datasfarsit);
	}
	
	public MembriEchipaTable(ArrayList<Object> hrTable) {
        this.numeangajat  	= new SimpleStringProperty(hrTable.get(0).toString());
        this.prenumeangajat	= new SimpleStringProperty(hrTable.get(1).toString());
        this.datainceput	= new SimpleStringProperty(hrTable.get(2).toString());
        this.datasfarsit  	= new SimpleStringProperty(hrTable.get(3).toString());
    }

	public String getNumeangajat() {
		return numeangajat.get();
	}

	public void setNumeangajat(String numeangajat) {
		this.numeangajat.set(numeangajat);
	}

	public String getPrenumeangajat() {
		return prenumeangajat.get();
	}

	public void setPrenumeangajat(String prenumeangajat) {
		this.prenumeangajat.set(prenumeangajat);
	}

	public String getDatainceput() {
		return datainceput.get();
	}

	public void setDatainceput(String datainceput) {
		this.datainceput.set(datainceput);
	}

	public String getDatasfarsit() {
		return datasfarsit.get();
	}

	public void setDatasfarsit(String datasfarsit) {
		this.datasfarsit.set(datasfarsit);
	}
	
	@Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(numeangajat.get());
        values.add(prenumeangajat.get());
        values.add(datainceput.get());
        values.add(datasfarsit.get());
        return values;
    }
	
}
