package stand.model.serialization;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import stand.MainApp;
import stand.model.Product;
import stand.model.StandData;
import stand.model.connection.Communication;

public class IOFunctions {
	
	private static IOFunctions instance;
	
	private static final Logger log4j = LogManager.getLogger(IOFunctions.class.getName());

	
	public static String saveLocation = "";
	
	private File currDir; 
	private FileOutputStream fos;
	private FileInputStream fis;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private SerializationHelper sh = SerializationHelper.getInstance();
	private MainApp mainApp;
	private FileChooser fc;
	private String filePath;
	private StandData standData;
	private String prevStandFilePath;
	
	
	private IOFunctions() {
	}
	
	public static IOFunctions getInstance() {
		if(instance == null) {
			instance = new IOFunctions();
		}
		return instance;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.standData = StandData.getInstance();
		this.filePath = standData.getPathToProgramFiles();
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public String exceptionStackTraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
	
	
	
	public void saveTofile(boolean defaultFileName) {
		File file = null;
		if(defaultFileName) {
			File currentDir = new File(".");
			String path = currentDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length()-1) + "stand-" + standData.getToday().toString() + ".ser";
			file = new File(fileLocation);
		} else {
			currDir = new File(".");
			fc = new FileChooser();
			fc.setInitialDirectory(currDir);
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ser fájlok", "*.ser"));
			file = fc.showSaveDialog(mainApp.getPrimaryStage());
		}
		if (file != null) {
			try {
				fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				Object[] stand = new Object[2];
				ArrayList<SimpleProduct> list = sh.convertProductDataToSimpleFormat();
				stand[0] = list;
				stand[1] = standData.getNotes();
				oos.writeObject(stand);
				oos.close();
				fos.close();
			} catch (FileNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl lemezre írása nem sikerült!", "001","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (IOException e) {
				log4j.error(exceptionStackTraceToString(e));
				e.printStackTrace();
			}
			
		}
			
	}
	
	private Object[] castStand(Object object) throws ClassCastException {
		Object[] stand = (Object[]) object;
		return stand;
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public ObservableList<Product> openFromFile() {
		currDir = new File(".");
		fc = new FileChooser();
		fc.setInitialDirectory(currDir);
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ser fájlok", "*.ser"));
		File file = fc.showOpenDialog(mainApp.getPrimaryStage());
		prevStandFilePath = file.getPath();
		ArrayList<SimpleProduct> tempList = new ArrayList<SimpleProduct>();
		ObservableList<Product> list = FXCollections.observableArrayList();
		if(file != null) {
			try {
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				Object object = ois.readObject() ;
				Object[] stand = castStand(object);
				tempList.addAll((ArrayList<SimpleProduct>) stand[0]);
				standData.setPrevNotes((String) stand[1]);
				ois.close();
				fis.close();
				for(int i = 0; i < tempList.size(); i++) {
					list.add(new Product(tempList.get(i).getMegnevezes()));
					list.get(i).setId(tempList.get(i).getId());
					list.get(i).setNyito(tempList.get(i).getNyito());
					list.get(i).setVetel(tempList.get(i).getVetel());
					list.get(i).setOssz(tempList.get(i).getOssz());
					list.get(i).setZaro(tempList.get(i).getZaro());
					list.get(i).setFogyas(tempList.get(i).getFogyas());
					list.get(i).setAr(tempList.get(i).getAr());
					list.get(i).setAkciosAr(tempList.get(i).getAkciosAr());
					list.get(i).setOsszeg(tempList.get(i).getOsszeg());
					list.get(i).setElozoZaro(tempList.get(i).getElozoZaro());
					list.get(i).setNyito_elozoZaro((tempList.get(i).getNyito_elozoZaro()));
					list.get(i).setElteres(tempList.get(i).getElteres());
					list.get(i).setGep(tempList.get(i).getGep());
					list.get(i).setAkcios(tempList.get(i).isAkcios());
				}
				
			} catch (FileNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				e.printStackTrace();
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
			} catch (IOException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (ClassCastException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","");
			}
			return list;
			
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ObservableList<Product> loadLastStand() {
		File file = null;
		File currentDir = new File(".");
		String path = currentDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length()-1) + "lastStand.ser";
		file = new File(fileLocation);
		prevStandFilePath = file.getPath();
		ArrayList<SimpleProduct> tempList = new ArrayList<SimpleProduct>();
		ObservableList<Product> list = FXCollections.observableArrayList();
		if(file != null) {
			try {
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				Object object = ois.readObject() ;
				Object[] stand = castStand(object);
				tempList.addAll((ArrayList<SimpleProduct>) stand[0]);
				ois.close();
				fis.close();
				for(int i = 0; i < tempList.size(); i++) {
					list.add(new Product(tempList.get(i).getMegnevezes()));
					list.get(i).setId(tempList.get(i).getId());
					list.get(i).setNyito(tempList.get(i).getNyito());
					list.get(i).setVetel(tempList.get(i).getVetel());
					list.get(i).setOssz(tempList.get(i).getOssz());
					list.get(i).setZaro(tempList.get(i).getZaro());
					list.get(i).setFogyas(tempList.get(i).getFogyas());
					list.get(i).setAr(tempList.get(i).getAr());
					list.get(i).setAkciosAr(tempList.get(i).getAkciosAr());
					list.get(i).setOsszeg(tempList.get(i).getOsszeg());
					list.get(i).setElozoZaro(tempList.get(i).getElozoZaro());
					list.get(i).setNyito_elozoZaro((tempList.get(i).getNyito_elozoZaro()));
					list.get(i).setElteres(tempList.get(i).getElteres());
					list.get(i).setGep(tempList.get(i).getGep());
					list.get(i).setAkcios(tempList.get(i).isAkcios());
				}
				
			} catch (FileNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				e.printStackTrace();
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
			} catch (IOException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (ClassCastException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl sérült!", "002","");
			}
			return list;
			
		}
		return null;
	}
	
	public void saveLastStand() {
		File file = null;
		File currentDir = new File(".");
		String path = currentDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length()-1) + "lastStand.ser";
		file = new File(fileLocation);
			
		if (file != null) {
			try {
				fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				Object[] stand = new Object[2];
				ArrayList<SimpleProduct> list = sh.convertProductDataToSimpleFormat();
				stand[0] = list;
				stand[1] = standData.getNotes();
				oos.writeObject(stand);
				oos.close();
				fos.close();
			} catch (FileNotFoundException e) {
				log4j.error(exceptionStackTraceToString(e));
				mainApp.errorAlert("A fájl lemezre írása nem sikerült!", "001","Kérlek indítsd újra a programot!");
				e.printStackTrace();
			} catch (IOException e) {
				log4j.error(exceptionStackTraceToString(e));
				e.printStackTrace();
			}
			
		}
			
	}
	
	public void saveDateOfClosedStand(LocalDate dateOfLastClosedStand) {
		File currDir = new File(".");
		filePath = currDir.getAbsolutePath();
		String fileLocation = filePath.substring(0,filePath.length()-1)+"last_closed_stand.lcs";
		File file = new File(fileLocation);
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(dateOfLastClosedStand);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			mainApp.errorAlert("Konfigurációs fájl lemezre írása nem sikerült!", "003","Kérlek indítsd újra a programot!");
			e.printStackTrace();
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		}
	}
	
	public LocalDate loadDateOfClosedStand() {
		
		File currDir = new File(".");
		filePath = currDir.getAbsolutePath();
		String fileLocation = filePath.substring(0,filePath.length()-1)+"last_closed_stand.lcs";
		File file = new File(fileLocation);
		LocalDate dateOfLastClosedStand = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			dateOfLastClosedStand = (LocalDate) ois.readObject();
			ois.close();
			fis.close();
			return dateOfLastClosedStand;
		} catch (FileNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			mainApp.errorAlert("Konfigurációs fájl nem található!", "004","Kérlek indítsd újra a programot!");
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			mainApp.errorAlert("Konfigurációs fájl sérült!", "004","Kérlek indítsd újra a programot!");
		}
		
		return null;
	}

	public void saveConfig(Object[] configFile) {

		File currDir = new File(".");
		filePath = currDir.getAbsolutePath();
		String fileLocation = filePath.substring(0,filePath.length()-1)+"sysconfig.cfg";
		File file = new File(fileLocation);
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(configFile);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
			mainApp.errorAlert("Konfigurációs fájl lemezre írása nem sikerült!", "005","Kérlek indítsd újra a programot!");
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} 
		
	}

	public Object[] loadConfig() {
		
		
		Object[] configFile = null;

		File currDir = new File(".");
		filePath = currDir.getAbsolutePath();
		String fileLocation = filePath.substring(0,filePath.length()-1)+"sysconfig.cfg";
		File file = new File(fileLocation);

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			configFile = (Object[]) ois.readObject();
			ois.close();
			fis.close();
			saveLocation = (String) configFile[2];
			return configFile;
		} catch (FileNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			mainApp.errorAlert("Konfigurációs fájl nem található!", "006","Kérlek indítsd újra a programot!");
			e.printStackTrace();
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
			mainApp.errorAlert("Konfigurációs fájl sérült!", "006","Kérlek indítsd újra a programot!");
			e.printStackTrace();
		}
		return null;
	}

	public String getPrevStandFilePath() {
		return prevStandFilePath;
	}
}
