package stand.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product implements Comparable<Product>{
	
	private static Integer globalID = 1;
	private final IntegerProperty id;
	private final StringProperty megnevezes;
	private final StringProperty nyito;
	private final StringProperty vetel;
	private final StringProperty ossz;
	private final StringProperty zaro;
	private final StringProperty fogyas;
	private final StringProperty ar;
	private final StringProperty akciosAr;
	private final StringProperty osszeg;
	private final StringProperty elozoZaro;
	private final StringProperty nyito_elozoZaro;
	private final StringProperty elteres;
	private final StringProperty gep;
	private final BooleanProperty akcios;
	
	public Product(int firstFree) {
		this.id = new SimpleIntegerProperty(firstFree);
		this.megnevezes = new SimpleStringProperty(null);
		this.nyito = new SimpleStringProperty(null);
		this.vetel = new SimpleStringProperty(null);
		this.ossz = new SimpleStringProperty("0");
		this.zaro = new SimpleStringProperty("0");
		this.fogyas = new SimpleStringProperty("0");
		this.ar = new SimpleStringProperty("0");
		this.akciosAr = new SimpleStringProperty(null);
		this.osszeg = new SimpleStringProperty("0");
		this.elozoZaro = new SimpleStringProperty("0");
		this.nyito_elozoZaro = new SimpleStringProperty("0");
		this.elteres = new SimpleStringProperty("0");
		this.gep = new SimpleStringProperty("0");
		this.akcios = new SimpleBooleanProperty(false);
		globalID++;
	}
	
	public Product(String megnevezes) {
		this.id = new SimpleIntegerProperty();
		this.megnevezes = new SimpleStringProperty(megnevezes);
		this.nyito = new SimpleStringProperty("0");
		this.vetel = new SimpleStringProperty("0");
		this.ossz = new SimpleStringProperty("0");
		this.zaro = new SimpleStringProperty("0");
		this.fogyas = new SimpleStringProperty("0");
		this.ar = new SimpleStringProperty("0");
		this.akciosAr = new SimpleStringProperty(null);
		this.osszeg = new SimpleStringProperty("0");
		this.elozoZaro = new SimpleStringProperty("0");
		this.nyito_elozoZaro = new SimpleStringProperty("0");
		this.elteres = new SimpleStringProperty("0");
		this.gep = new SimpleStringProperty(null);
		this.akcios = new SimpleBooleanProperty(false);
		globalID++;
	}

	public IntegerProperty getIdProperty() {
		return id;
	}
	
	public Integer getId() {
		return id.get();
	}

	public void setId(Integer id) {
		this.id.set(id);
	}

	public StringProperty getMegnevezesProperty() {
		return megnevezes;
	}
	
	
	public String getMegnevezes() {
		return megnevezes.get();
	}
	
	public void setMegnevezes(String megnevezes) {
		this.megnevezes.set(megnevezes);
	}

	public StringProperty getNyitoProperty() {
		return nyito;
	}
	
	public String getNyito() {
		return nyito.get();
	}
	
	public void setNyito(String nyito) {
		this.nyito.set(nyito);
	}

	public StringProperty getVetelProperty() {
		return vetel;
	}
	
	public String getVetel() {
		return vetel.get();
	}

	public void setVetel(String vetel) {
		this.vetel.set(vetel);
	}
	
	public StringProperty getOsszProperty() {
		return ossz;
	}
	
	public String getOssz() {
		return ossz.get();
	}
	
	public void setOssz(String ossz) {
		this.ossz.set(ossz);
	}

	public StringProperty getZaroProperty() {
		return zaro;
	}
	
	public String getZaro() {
		return zaro.get();
	}
	
	public void setZaro(String zaro) {
		this.zaro.set(zaro);
	}

	public StringProperty getFogyasProperty() {
		return fogyas;
	}

	public String getFogyas() {
		return fogyas.get();
	}
	
	public void setFogyas(String fogyas) {
		this.fogyas.set(fogyas);
	}

	public StringProperty getArProperty() {
		return ar;
	}

	public String getAr() {
		return ar.get();
	}

	public void setAr(String ar) {
		this.ar.set(ar);
	}
	
	public StringProperty getOsszegProperty() {
		return osszeg;
	}

	public String getOsszeg() {
		return osszeg.get();
	}
	
	public void setOsszeg(String osszeg) {
		this.osszeg.set(osszeg);
	}

	public StringProperty getElozoZaroProperty() {
		return elozoZaro;
	}

	public String getElozoZaro() {
		return elozoZaro.get();
	}
	
	public void setElozoZaro(String elozoZaro) {
		this.elozoZaro.set(elozoZaro);
	}

	public StringProperty getNyito_elozoZaroProperty() {
		return nyito_elozoZaro;
	}
	
	public String getNyito_elozoZaro() {
		return nyito_elozoZaro.get();
	}
	
	public void setNyito_elozoZaro(String nyito_elozoZaro) {
		this.nyito_elozoZaro.set(nyito_elozoZaro);
	}

	public StringProperty getElteresProperty() {
		return elteres;
	}
	
	public String getElteres() {
		return elteres.get();
	}
	
	public void setElteres(String elteres) {
		this.elteres.set(elteres);
	}
	
	public BooleanProperty getAkciosProperty() {
		return akcios;
	}
	
	public Boolean isAkcios() {
		return akcios.get();
	}
	
	public void setAkcios(Boolean akcios) {
		this.akcios.set(akcios);
	}
	
	public StringProperty getGepProperty() {
		return gep;
	}

	public String getGep() {
		return gep.get();
	}
	
	public void setGep(String gep) {
		this.gep.set(gep);
	}
	
	public StringProperty getAkciosArProperty() {
		return akciosAr;
	}
	
	public String getAkciosAr() {
		return akciosAr.get();
	}
	
	public void setAkciosAr(String akciosAr) {
		this.akciosAr.set(akciosAr);
	}
	
	
	public static void setGlobalId(int globalID) {
		Product.globalID = globalID;
	}


	
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", megnevezes=" + megnevezes + ", nyito=" + nyito + ", vetel=" + vetel + ", ossz="
				+ ossz + ", zaro=" + zaro + ", fogyas=" + fogyas + ", ar=" + ar + ", osszeg=" + osszeg + ", elozoZaro="
				+ elozoZaro + ", nyito_elozoZaro=" + nyito_elozoZaro + ", elteres=" + elteres + "]";
	}
	
	public void refreshData() {
		Double nyito = 0d;
		Double vetel = 0d;
		Double zaro = 0d;
		if(getNyito().toString() != null || !getNyito().toString().equals("")) {
			nyito = Double.parseDouble(getNyito().toString());
		}
		if(this.vetel.get().toString() != null || !this.vetel.get().toString().equals("")) {
			vetel = Double.parseDouble(getVetel().toString());
		}
		if(getZaro().toString() != null || !getZaro().toString().equals("")) {
			zaro = Double.parseDouble(getZaro().toString());
		}
		Double fogyas = Double.parseDouble(getFogyas());
		Integer ar = Integer.parseInt(getAr());
		Double elozozaro = Double.parseDouble(getElozoZaro());
		
		Double ujOssz = nyito + vetel;
		Double ujFogyas = nyito-zaro;
		Double ujOsszeg = fogyas*ar;
		Double ujNyito_elozo = nyito-elozozaro;
		Double ujElteres = ujNyito_elozo*ar;
		
		setOssz(ujOssz.toString());
		setFogyas(ujFogyas.toString());
		setOsszeg(ujOsszeg.toString());
		setNyito_elozoZaro(ujNyito_elozo.toString());
		setElteres(ujElteres.toString());
	}

	@Override
	public int compareTo(Product toCompare) {
		if(this.id.get() < toCompare.id.get()) {
			return -1;
		} else if(this.id.get() == toCompare.id.get()) {
			return 0;
		}
		return 1;
	}
	
	

}
