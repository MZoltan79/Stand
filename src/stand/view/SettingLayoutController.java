package stand.view;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stand.MainApp;
import stand.model.StandData;
import stand.model.User;
import stand.model.connection.Communication;
import stand.model.serialization.IOFunctions;

public class SettingLayoutController {
	
	private StandData standData;
	private ObservableList<User> usersData = FXCollections.observableArrayList();
	private ObservableList<String> saveLocations = FXCollections.observableArrayList("Helyi", "Felhő");
	private MainApp mainApp;
	private Communication comm;
	private Stage settingsStage;
	private IOFunctions io;
	private Object[] configFile;
	private boolean initSendDataInEmail;
	private String initEmailRecipient;
	
	
	@FXML
	public void initialize() {
		standData = StandData.getInstance();
		comm = Communication.getInstance();
		usersData.addAll(standData.getUsers());
		io = IOFunctions.getInstance();
		usersTable.setItems(usersData);
		userNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		userTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
		sendDataInEmail.setSelected(standData.isSendDataInEmail());
		emailRecipientField.setText(standData.getRecipient());
		saveLocationBox.setItems(saveLocations);
		saveLocationBox.setValue(standData.getConfigSaveLocation());
		saveLocationBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			standData.setConfigSaveLocation(newValue);
		});
		
		//		configFile = io.loadConfig();
		
//		sendDataInEmail.setSelected((boolean) configFile[0]);
//		emailRecipientField.setText((String) configFile[1]);
//		standData.setRecipient((String) configFile[1]);
		
	}
	
	@FXML
	private CheckBox sendDataInEmail;
	
	@FXML
	private TextField emailRecipientField;
	
	@FXML
	private ComboBox<String> saveLocationBox;
	
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button backButton;
	@FXML
	private Button newUserButton;
	@FXML
	private Button editUserButton;
	@FXML
	private Button delUserButton;
	
	@FXML
	private TableView<User> usersTable;
	
	@FXML
	private TableColumn<User, String> userNameColumn;
	@FXML
	private TableColumn<User, String> userTypeColumn;

	@FXML
	private void handleOkButton() {
		configFile = new Object[3];
		if(sendDataInEmail.isSelected()) {
			initSendDataInEmail = true;
			standData.setSendDataInEmail(true);
		} else {
			initSendDataInEmail = false;
			standData.setSendDataInEmail(false);
		}
		initEmailRecipient = emailRecipientField.getText();
		standData.setRecipient(emailRecipientField.getText());
		configFile[0] = initSendDataInEmail;
		configFile[1] = initEmailRecipient;
		configFile[2] = standData.getConfigSaveLocation();
		io.saveConfig(configFile);
		settingsStage.close();
	}
	
	@FXML
	private void handleCancelButton() {
		settingsStage.close();
	}

	@FXML
	private void handleBackButton() {
		settingsStage.close();
	}

	@FXML
	private void handleNewUserButton() {
		UserEditDialogController.newUser = true;
		User newUser = new User();
		boolean okClicked = mainApp.showUserEditDialog(newUser);
		System.out.println(okClicked);
		if(okClicked) {
			usersData.add(newUser);
			comm.insertToUsers(newUser.getId(), newUser.getName(), newUser.getPassword(), newUser.isAdmin());
		}
	}

	@FXML
	private void handleEditUserButton() {
		UserEditDialogController.newUser = false;
		User selectedUser = usersTable.getSelectionModel().getSelectedItem();
		if(selectedUser != null) {
			boolean okClicked = mainApp.showUserEditDialog(selectedUser);
			if(okClicked) {
				usersData.forEach(user -> {
					
					if(user.getId() == selectedUser.getId()) {
						user.setId(selectedUser.getId());
					}
				});
				comm.upDateUsers(selectedUser);
			}
		} else {
			Alert noUserSelectedAlert = new Alert(AlertType.WARNING);
			noUserSelectedAlert.setTitle("Nincs kiválsztás!");
			noUserSelectedAlert.setHeaderText("Nincs kiválasztva felhasználó a listából!");
			noUserSelectedAlert.setContentText("Kérlek, válassz felhasználót!");
			noUserSelectedAlert.initOwner(mainApp.getPrimaryStage());
			
			noUserSelectedAlert.showAndWait();
		}
	}

	
	@FXML
	private void handleDelUserButton() {
		User delUser = usersTable.getSelectionModel().getSelectedItem();
		if(delUser != null ) {
			Alert deletionWarningAlert = new Alert(AlertType.CONFIRMATION);
			deletionWarningAlert.setTitle("Felhasználó törlése!");
			deletionWarningAlert.setHeaderText("A felhasználó: " + delUser.getName() + " törölve lesz!");
			deletionWarningAlert.setContentText("Bizos vagy benne?");
			deletionWarningAlert.initOwner(mainApp.getPrimaryStage());
			
			Optional<ButtonType> result = deletionWarningAlert.showAndWait();
			if(result.get() == ButtonType.OK) {
				usersData.remove(delUser);
				comm.deleteFromUsers(delUser);
			}
			
		} else {
			Alert noUserSelectedAlert = new Alert(AlertType.WARNING);
			noUserSelectedAlert.setTitle("Nincs kiválsztás!");
			noUserSelectedAlert.setHeaderText("Nincs kiválasztva felhasználó a listából!");
			noUserSelectedAlert.setContentText("Kérlek, válassz felhasználót!");
			noUserSelectedAlert.initOwner(mainApp.getPrimaryStage());
			
			noUserSelectedAlert.showAndWait();
		}
	}
	
	public void setSettingsStage(Stage settingsStage) {
		this.settingsStage = settingsStage;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
}
