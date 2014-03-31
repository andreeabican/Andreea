package entities;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class DefecteTable extends Entity {
	private SimpleStringProperty 	iddefect;
	private SimpleStringProperty 	denumiredefect;
	private SimpleStringProperty 	severitatedefect;
	private SimpleStringProperty 	descrieredefect;
	private SimpleStringProperty 	proiectdefect;
	private SimpleStringProperty 	versiunedefect;
	private SimpleStringProperty 	reproductibilitatedefect;
	private SimpleStringProperty 	statutdefect;
	private SimpleStringProperty 	rezultatdefect;
	private SimpleStringProperty 	ultimamodificare;
	private SimpleStringProperty	utilizatorultimamodificare;
	
	DefecteTable(String iddefect, String denumiredefect, String severitatedefect,
			String descrieredefect, String proiectdefect, String versiunedefect,
			String reproductibilitatedefect, String statutdefect, String rezultatdefect,
			String ultimamodificare, String utilizatorultimamodificare)
	{
		this.iddefect					= new SimpleStringProperty(iddefect);
		this.denumiredefect				= new SimpleStringProperty(denumiredefect);
		this.severitatedefect 			= new SimpleStringProperty(severitatedefect);
		this.descrieredefect 			= new SimpleStringProperty(descrieredefect);
		this.proiectdefect				= new SimpleStringProperty(proiectdefect);
		this.versiunedefect 			= new SimpleStringProperty(versiunedefect);
		this.reproductibilitatedefect 	= new SimpleStringProperty(reproductibilitatedefect);
		this.statutdefect 				= new SimpleStringProperty(statutdefect);
		this.rezultatdefect				= new SimpleStringProperty(rezultatdefect);
		this.ultimamodificare 			= new SimpleStringProperty(ultimamodificare);
		this.utilizatorultimamodificare	= new SimpleStringProperty(utilizatorultimamodificare);
	}
	
	public DefecteTable(ArrayList<Object> hrTable) {
        this.iddefect					= new SimpleStringProperty(hrTable.get(0).toString());
		this.denumiredefect				= new SimpleStringProperty(hrTable.get(1).toString());
		this.severitatedefect 			= new SimpleStringProperty(hrTable.get(2).toString());
		this.descrieredefect 			= new SimpleStringProperty(hrTable.get(3).toString());
		this.proiectdefect				= new SimpleStringProperty(hrTable.get(4).toString());
		this.versiunedefect 			= new SimpleStringProperty(hrTable.get(5).toString());
		this.reproductibilitatedefect 	= new SimpleStringProperty(hrTable.get(6).toString());
		this.statutdefect 				= new SimpleStringProperty(hrTable.get(7).toString());
		this.rezultatdefect				= new SimpleStringProperty(hrTable.get(8).toString());
		this.ultimamodificare 			= new SimpleStringProperty(hrTable.get(9).toString());
		this.utilizatorultimamodificare	= new SimpleStringProperty(hrTable.get(10).toString());
    }
	
	public String getIddefect() {
		return iddefect.get();
	}

	public void setIddefect(String iddefect) {
		this.iddefect.set(iddefect);
	}

	public String getDenumiredefect() {
		return denumiredefect.get();
	}

	public void setDenumiredefect(String denumiredefect) {
		this.denumiredefect.set(denumiredefect);
	}

	public String getSeveritatedefect() {
		return severitatedefect.get();
	}

	public void setSeveritatedefect(String severitatedefect) {
		this.severitatedefect.set(severitatedefect);
	}

	public String getDescrieredefect() {
		return descrieredefect.get();
	}

	public void setDescrieredefect(String descrieredefect) {
		this.descrieredefect.set(descrieredefect);
	}

	public String getProiectdefect() {
		return proiectdefect.get();
	}

	public void setProiectdefect(String proiectdefect) {
		this.proiectdefect.set(proiectdefect);
	}

	public String getVersiunedefect() {
		return versiunedefect.get();
	}

	public void setVersiunedefect(String versiunedefect) {
		this.versiunedefect.set(versiunedefect);
	}

	public String getReproductibilitatedefect() {
		return reproductibilitatedefect.get();
	}

	public void setReproductibilitatedefect(
			String reproductibilitatedefect) {
		this.reproductibilitatedefect.set(reproductibilitatedefect);
	}

	public String getStatutdefect() {
		return statutdefect.get();
	}

	public void setStatutdefect(String statutdefect) {
		this.statutdefect.set(statutdefect);
	}

	public String getRezultatdefect() {
		return rezultatdefect.get();
	}

	public void setRezultatdefect(String rezultatdefect) {
		this.rezultatdefect.set(rezultatdefect);
	}

	public String getUltimamodificare() {
		return ultimamodificare.get();
	}

	public void setUltimamodificare(String ultimamodificare) {
		this.ultimamodificare.set(ultimamodificare);
	}

	public String getUtilizatorultimamodificare() {
		return utilizatorultimamodificare.get();
	}

	public void setUtilizatorultimamodificare(
			String utilizatorultimamodificare) {
		this.utilizatorultimamodificare.set(utilizatorultimamodificare);
	}

	@Override
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(iddefect.get());
        values.add(denumiredefect.get());
        values.add(severitatedefect.get());
        values.add(descrieredefect.get());
        values.add(proiectdefect.get());
        values.add(versiunedefect.get());
        values.add(reproductibilitatedefect.get());
        values.add(statutdefect.get());
        values.add(rezultatdefect.get());
        values.add(ultimamodificare.get());
        values.add(utilizatorultimamodificare.get());
        return values;
    }
}
