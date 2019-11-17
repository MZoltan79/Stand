package stand.model.serialization;

import java.io.Serializable;

public class SimpleProduct implements Serializable, Comparable<SimpleProduct>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer id;
	private String megnevezes;
	private String nyito;
	private String vetel;
	private String ossz;
	private String zaro;
	private String fogyas;
	private String ar;
	private String akciosAr;
	private String osszeg;
	private String elozoZaro;
	private String nyito_elozoZaro;
	private String elteres;
	private String gep;
	private boolean akcios;
	
	public SimpleProduct(Integer id, String megnevezes, String nyito, String vetel, String ossz, String zaro, String fogyas,
			 String ar, String akciosAr, String osszeg, String elozoZaro, String nyito_elozoZaro, String elteres, String gep, boolean akcios) {
		super();
		this.id = id;
		this.megnevezes = megnevezes;
		this.nyito = nyito;
		this.vetel = vetel;
		this.ossz = ossz;
		this.zaro = zaro;
		this.fogyas = fogyas;
		this.ar = ar;
		this.akciosAr = akciosAr;
		this.osszeg = osszeg;
		this.elozoZaro = elozoZaro;
		this.nyito_elozoZaro = nyito_elozoZaro;
		this.elteres = elteres;
		this.gep = gep;
		this.akcios = akcios;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMegnevezes() {
		return megnevezes;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
	}

	public String getNyito() {
		return nyito;
	}

	public void setNyito(String nyito) {
		this.nyito = nyito;
	}

	public String getVetel() {
		return vetel;
	}

	public void setVetel(String vetel) {
		this.vetel = vetel;
	}

	public String getOssz() {
		return ossz;
	}

	public void setOssz(String ossz) {
		this.ossz = ossz;
	}

	public String getZaro() {
		return zaro;
	}

	public void setZaro(String zaro) {
		this.zaro = zaro;
	}

	public String getFogyas() {
		return fogyas;
	}

	public void setFogyas(String fogyas) {
		this.fogyas = fogyas;
	}

	public String getAr() {
		return ar;
	}

	public void setAr(String ar) {
		this.ar = ar;
	}

	public String getOsszeg() {
		return osszeg;
	}

	public void setOsszeg(String osszeg) {
		this.osszeg = osszeg;
	}

	public String getElozoZaro() {
		return elozoZaro;
	}

	public void setElozoZaro(String elozoZaro) {
		this.elozoZaro = elozoZaro;
	}

	public String getNyito_elozoZaro() {
		return nyito_elozoZaro;
	}

	public void setNyito_elozoZaro(String nyito_elozoZaro) {
		this.nyito_elozoZaro = nyito_elozoZaro;
	}

	public String getElteres() {
		return elteres;
	}

	public void setElteres(String elteres) {
		this.elteres = elteres;
	}

	public String getGep() {
		return gep;
	}

	public void setGep(String gep) {
		this.gep = gep;
	}

	@Override
	public String toString() {
		return "SimpleProduct [id=" + id + ", megnevezes=" + megnevezes + "]";
	}

	


	@Override
	public int compareTo(SimpleProduct o) {
		if(this.id < o.id) {
			return -1;
		} else if (this.id == o.id){
			return 0;
		}
		return 1;
	}

	public String getAkciosAr() {
		return akciosAr;
	}

	public void setAkciosAr(String akciosAr) {
		this.akciosAr = akciosAr;
	}

	public boolean isAkcios() {
		return akcios;
	}

	public void setAkcios(boolean akcios) {
		this.akcios = akcios;
	}
	
	
	
	
	
	
	
	
}
