package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class HRtable extends Entity {
	private SimpleStringProperty      idutilizator;
	private SimpleStringProperty      nume;
	private SimpleStringProperty      prenume;
	private SimpleStringProperty      dataangajarii;
	private SimpleStringProperty      iddepartament;
	private SimpleStringProperty      departament;
	
	HRtable(String idutilizator, String nume, String prenume,
			String dataangajarii, String iddepartament, String departament)
	{
		this.idutilizator	= new SimpleStringProperty(idutilizator);
		this.nume 			= new SimpleStringProperty(nume);
		this.prenume 		= new SimpleStringProperty(prenume);
		this.dataangajarii 	= new SimpleStringProperty(dataangajarii);
		this.iddepartament 	= new SimpleStringProperty(iddepartament);
		this.departament 	= new SimpleStringProperty(departament);
	}
	
	public HRtable(ArrayList<Object> hrTable) {
        this.idutilizator   = new SimpleStringProperty(hrTable.get(0).toString());
        this.nume	        = new SimpleStringProperty(hrTable.get(1).toString());
        this.prenume		= new SimpleStringProperty(hrTable.get(2).toString());
        this.dataangajarii  = new SimpleStringProperty(hrTable.get(3).toString());
        this.iddepartament  = new SimpleStringProperty(hrTable.get(4).toString());
        this.departament	= new SimpleStringProperty(hrTable.get(5).toString());
    }

	public String getIdutilizator() {
		return idutilizator.get();
	}

	public void setIdutilizator(String idutilizator) {
		this.idutilizator.set(idutilizator);
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

	public String getDataangajarii() {
		return dataangajarii.get();
	}

	public void setDataangajarii(String dataangajarii) {
		this.dataangajarii.set(dataangajarii);
	}

	public String getIddepartament() {
		return iddepartament.get();
	}

	public void setIddepartament(String iddepartament) {
		this.iddepartament.set(iddepartament);
	}

	public String getDepartament() {
		return departament.get();
	}

	public void setDepartament(String departament) {
		this.departament.set(departament);
	}
	
	@Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idutilizator.get());
        values.add(nume.get());
        values.add(prenume.get());
        values.add(dataangajarii.get());
        values.add(iddepartament.get());
        values.add(departament.get());
        return values;
    }
	
}
