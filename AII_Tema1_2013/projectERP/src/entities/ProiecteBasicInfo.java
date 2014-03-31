package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class ProiecteBasicInfo extends Entity {
	private SimpleStringProperty	idproiect;
	private SimpleStringProperty	numeproiect;
	private SimpleStringProperty    descriereproiect;
	
	public ProiecteBasicInfo(String idproiect, String numeproiect, String descriereproiect) {
		this.numeproiect 		= new SimpleStringProperty(numeproiect);
		this.descriereproiect 	= new SimpleStringProperty(descriereproiect);
		this.descriereproiect 	= new SimpleStringProperty(descriereproiect);
	}
	
	public ProiecteBasicInfo(ArrayList<Object> table) {
        this.idproiect			= new SimpleStringProperty(table.get(0).toString());
		this.numeproiect		= new SimpleStringProperty(table.get(1).toString());
        this.descriereproiect	= new SimpleStringProperty(table.get(2).toString());
    }
	
	public String getIdproiect() {
		return idproiect.get();
	}
	
	public void setIdproiect(String idproiect) {
		this.idproiect.set(idproiect);
	}

	public String getNumeproiect() {
		return numeproiect.get();
	}

	public void setNumeproiect(String numeproiect) {
		this.numeproiect.set(numeproiect);
	}

	public String getDescriereproiect() {
		return descriereproiect.get();
	}

	public void setDescriereproiect(String descriereproiect) {
		this.descriereproiect.set(descriereproiect);
	}
	
	@Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(idproiect.get());
        values.add(numeproiect.get());
        values.add(descriereproiect.get());
        return values;
    }
}
