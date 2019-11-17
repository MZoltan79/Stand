package stand.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stand.MainApp;
import stand.model.Product;
import stand.model.StandData;
import stand.model.connection.Communication;
import stand.model.connection.MailSender;
import stand.model.serialization.IOFunctions;
import stand.model.serialization.WriteToExcel;

public class StandclosingDialogcontroller {
	
	private Stage closeDialogStage;
	private MainApp mainApp;
	private StandData standData;
	private Integer stand;
	private Integer standMinHH;
	private Integer fizu;
	private Integer keszpenz;
	private Integer valto;
	private Integer borravalo;
	private Integer leado;
	private Integer leadoPluszJatt;
	private Integer happyHours;
	private Integer tartozasok;
	private Communication comm;
	private IOFunctions io;
//	private String filePath;
	
	@FXML
	private TextArea notesArea;
	
	@FXML
	private TextField fizuField;
	@FXML
	private TextField kpField;
	@FXML
	private TextField valtoField;
	@FXML
	private TextField happyHoursField;
	@FXML
	private TextField tartozasokField;
	
	
	@FXML
	private Button backButton;
	@FXML
	private Button closeStandButton;
	@FXML
	private Button countButton;

	@FXML
	private Label standLabel;
	@FXML
	private Label leadoLabel;
	@FXML 
	private Label borravaloLabel;
	@FXML
	private Label leadoPluszBorravaloLabel;
	

	@FXML
	private void initialize() {
		standMinHH =  0;
		standData = StandData.getInstance();
		comm = Communication.getInstance();
		io = IOFunctions.getInstance();
		fizuField.setText("10000");
		kpField.setText("0");
		valtoField.setText("30000");
		tartozasokField.setText("0");
		happyHoursField.setText("0");
		notesArea.setText(standData.getNotes());
//		filePath = standData.getPathToProgramFiles();
		valto = Integer.parseInt(valtoField.getText());
		fizu = Integer.parseInt(fizuField.getText());
		kpField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if (keyevent.getCode() == KeyCode.ENTER) {
				if(isInputValid(kpField)) {
					keszpenz = Integer.parseInt(kpField.getText());
					countAndUpdateResultLabels();
				} else {
					Alert invalidInputAlert = new Alert(AlertType.ERROR);
					invalidInputAlert.setTitle("Hiba!");
					invalidInputAlert.setHeaderText("Nem megfelelő formátum!");
					invalidInputAlert.setContentText("Kérlek, számot írj be!");
					invalidInputAlert.initModality(Modality.WINDOW_MODAL);
					invalidInputAlert.initOwner(mainApp.getPrimaryStage());
					
					invalidInputAlert.showAndWait();
				}
			}
		});
		fizuField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if (keyevent.getCode() == KeyCode.ENTER) {
				if(isInputValid(fizuField)) {
					fizu = Integer.parseInt(fizuField.getText());
					countAndUpdateResultLabels();
				} else {
					Alert invalidInputAlert = new Alert(AlertType.ERROR);
					invalidInputAlert.setTitle("Hiba!");
					invalidInputAlert.setHeaderText("Nem megfelelő formátum!");
					invalidInputAlert.setContentText("Kérlek, számot írj be!");
					invalidInputAlert.initModality(Modality.WINDOW_MODAL);
					invalidInputAlert.initOwner(mainApp.getPrimaryStage());
					
					invalidInputAlert.showAndWait();
				}
			}
		});
		valtoField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if (keyevent.getCode() == KeyCode.ENTER) {
				if(isInputValid(valtoField)) {
					valto = Integer.parseInt(valtoField.getText());
					countAndUpdateResultLabels();
				} else {
					Alert invalidInputAlert = new Alert(AlertType.ERROR);
					invalidInputAlert.setTitle("Hiba!");
					invalidInputAlert.setHeaderText("Nem megfelelő formátum!");
					invalidInputAlert.setContentText("Kérlek, számot írj be!");
					invalidInputAlert.initModality(Modality.WINDOW_MODAL);
					invalidInputAlert.initOwner(mainApp.getPrimaryStage());
					
					invalidInputAlert.showAndWait();
				}
			}
		});
		tartozasokField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if(keyevent.getCode() == KeyCode.ENTER) {
				if(isInputValid(tartozasokField)) {
					tartozasok = Integer.parseInt(tartozasokField.getText());
					countAndUpdateResultLabels();
				} else {
					Alert invalidInputAlert = new Alert(AlertType.ERROR);
					invalidInputAlert.setTitle("Hiba!");
					invalidInputAlert.setHeaderText("Nem megfelelő formátum!");
					invalidInputAlert.setContentText("Kérlek, számot írj be!");
					invalidInputAlert.initModality(Modality.WINDOW_MODAL);
					invalidInputAlert.initOwner(mainApp.getPrimaryStage());
					
					invalidInputAlert.showAndWait();
				}
			}
		});
		happyHoursField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if(keyevent.getCode() == KeyCode.ENTER) {
				if(isInputValid(happyHoursField)) {
					happyHours = Integer.parseInt(happyHoursField.getText());
					countAndUpdateResultLabels();
				} else {
					Alert invalidInputAlert = new Alert(AlertType.ERROR);
					invalidInputAlert.setTitle("Hiba!");
					invalidInputAlert.setHeaderText("Nem megfelelő formátum!");
					invalidInputAlert.setContentText("Kérlek, számot írj be!");
					invalidInputAlert.initModality(Modality.WINDOW_MODAL);
					invalidInputAlert.initOwner(mainApp.getPrimaryStage());
					
					invalidInputAlert.showAndWait();
				}
			}
		});
	}
	
	
	@FXML
	private void handleBackButton() {
		closeDialogStage.close();
		
	}
	
	@FXML
	private void handleCloseStandButton() {
//		countSum();
		countAndUpdateResultLabels();
		if(mainApp.checkOpeningAndClosingColumns()) {
			WriteToExcel we = new WriteToExcel();
			String tmp = standData.getNotes();
			Integer hHPer3 = happyHours/3;
			String onSale = "Akciók:\n";
			for(Product p: standData.getProductsData()) {
				if(p.isAkcios()) {
					onSale += p.getMegnevezes() + "\n";
				}
			}
			String newNotes = "A standot készítette: " + standData.getUser().getName() +
					"\nA stand végösszege: " + stand.toString() +
					" Ft.\nHappy Hours összege: " + hHPer3.toString() +
					" Ft.\nStand - Happy Hours: " + standMinHH.toString() +
					" Ft.\nTartozások: " + tartozasok.toString() +
					" Ft.\nFizu: " + fizu.toString() + " Ft.\nJatt: " + borravalo.toString() +
					" Ft.\nEltérés előző napról: " + standData.getSumDifference().toString() +
					" Ft.\nVáltó: " + valtoField.getText() + " Ft.\nLeadó jatt nélkül: " + leado.toString() +
					" Ft.\nLeadó: " + leadoPluszJatt.toString() +
					" Ft.\nMegjegyzések:\n" + notesArea.getText() + "\n" + 
					onSale;
			standData.setNotes(newNotes);
			io.saveTofile(true);
			io.saveLastStand();
			if(!we.createSheet()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Hiba!");
				alert.setHeaderText("Valami gond volt az adatok lemezre írásakkor!");
				alert.setContentText("Hiba kód: 001");
				
				alert.showAndWait();
			}
			standData.setNotes(tmp);
		}
		File[] files = new File[2];
		String fileLocation;
		File currentDir = new File(".");
		String path = currentDir.getAbsolutePath();
		fileLocation = path.substring(0, path.length()-1);
		files[0] = new File(fileLocation + "stand-" + standData.getToday().toString() + ".ser");
		files[1] = new File(fileLocation + "stand-" + standData.getToday().toString() + ".xls");
		
		if(standData.isSendDataInEmail()) {
			String onSale = "Akciók:\n";
			for(Product p: standData.getProductsData()) {
				if(p.isAkcios()) {
					onSale += p.getMegnevezes() + "\n";
				}
			}
			Integer hHPer3 = happyHours/3;
			MailSender ms = new MailSender();
			String messageBody = "";
				messageBody += "A standot készítette: " + standData.getUser().getName() +
						"\nA stand végösszege: " + stand.toString() +
						" Ft.\nHappy Hours összege: " + hHPer3.toString() +
						" Ft.\nStand - Happy Hours: " + standMinHH.toString() +
						" Ft.\nTartozások: " + tartozasok.toString() +
						" Ft.\nFizu: " + fizu.toString() + " Ft.\nJatt: " + borravalo.toString() +
						" Ft.\nEltérés előző napról: " + standData.getSumDifference().toString() +
						" Ft.\nVáltó: " + valtoField.getText() + " Ft.\nLeadó jatt nélkül: " + leado.toString() +
						" Ft.\nLeadó: " + leadoPluszJatt.toString() +
						" Ft.\nMegjegyzések:\n" + notesArea.getText() + "\n" + 
						onSale;
						
					ms.sendEmail(files, messageBody, standData.getRecipient());
		}
		standData.setDateOfLastClosedStand(standData.getToday());
		io.saveDateOfClosedStand(standData.getDateOfLastClosedStand());
		comm.closeStand(standData.getProductsData());
		comm.upDateNotes("");
		standData.loadProducts();
		mainApp.showProductOverview();
		
		closeDialogStage.close();
	}
	
	public void setDialogStage(Stage closeDialogStage) {
		this.closeDialogStage = closeDialogStage;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
			closeDialogStage.setWidth(600);
			countSum();
	}
	
	public void countSum() {
		borravalo = 0;
		leado = 0;
		leadoPluszJatt = 0;
		stand = 0;
		fizu = Integer.parseInt(fizuField.getText());
		keszpenz = Integer.parseInt(kpField.getText());
		valto = Integer.parseInt(valtoField.getText());
		tartozasok = Integer.parseInt(tartozasokField.getText());
		happyHours = Integer.parseInt(happyHoursField.getText());
		
//		for(Product p: standData.getProductsData()) {
//			stand += p.isAkcios()? Integer.parseInt(p.getAkciosAr()):Integer.parseInt(p.getOsszeg());
//		}
		for(Product p: standData.getProductsData()) {
			stand += Integer.parseInt(p.getOsszeg());
		}
		leado = stand - fizu - borravalo;
		leadoPluszJatt = leado + borravalo;
		borravaloLabel.setText( " Ft.");
		leadoLabel.setText(leado.toString() + " Ft.");
		standLabel.setText(stand.toString() + " Ft.");
		leadoPluszBorravaloLabel.setText(leadoPluszJatt.toString() + " Ft.");
		
	}
	
	public String round(Double num) {
		double d;
		if(num == null ) {
			return "0";
		} else {
			d = num;
			d = Math.round(d*100)/100d;
			if(d % 1 == 0) {
				Integer i = (int) d;
				return i.toString();
			} else {
				Double res = d;
				return res.toString();
			}
		}
	}
	@FXML
	private void countAndUpdateResultLabels() {
		countSum();
		standMinHH = stand - (happyHours/3);
		borravalo = keszpenz - standMinHH - valto + tartozasok;
		leado = standMinHH - fizu - tartozasok;
		leadoPluszJatt = leado + borravalo;
		standLabel.setText(standMinHH.toString() + " Ft.");
		borravaloLabel.setText(borravalo.toString() + " Ft.");
		leadoLabel.setText(leado.toString() + " Ft.");
		leadoPluszBorravaloLabel.setText(leadoPluszJatt.toString() + " Ft.");
	}
	
	private boolean isInputValid(TextField textField) {
		if(textField.getText().matches("[0-9]+")) return true;
		return false;
	}
}
