package stand.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import stand.MainApp;
import stand.model.StandData;
import stand.model.User;

public class UserEditDialogController {

	public static boolean okClicked = false;
	public static boolean newUser = false;
	
	private Stage dialogStage;
	private User user;
	private StandData standData;
//	private MainApp mainApp;
	
	
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	@FXML
	private CheckBox admin;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	public void initialize() {
		standData = StandData.getInstance();
		standData.loadUsers();
		
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
//	public void setMainApp(MainApp mainApp) {
//		this.mainApp = mainApp;
//	}
	
	public void setUser(User user) {
		this.user = user;
		
		userName.setText(user.getName());
		password.setText(user.getPassword());
		admin.setSelected(user.isAdmin());
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	
	
	@FXML
	private void handleOk() {
		if(isInputValid()) {
			if(standData.isUserNameAvailable(userName.getText())) {
				user.setId(getNextUserId());
				user.setName(userName.getText());
				user.setPassword(password.getText());
				user.setAdmin(admin.isSelected());
				user.setType(user.isAdmin());
				
				okClicked = true;
				dialogStage.close();
			} else {
				if(newUser) {
					Alert invalidUserNameAlert = new Alert(AlertType.ERROR);
					invalidUserNameAlert.setTitle("Foglalt felhasználónév!");
					invalidUserNameAlert.setHeaderText("A megadott név már foglalt!");
					invalidUserNameAlert.setContentText("Kérlek válassz másikat!");
					invalidUserNameAlert.initModality(Modality.WINDOW_MODAL);
					invalidUserNameAlert.initOwner(dialogStage);
					
					invalidUserNameAlert.showAndWait();
				} else {
					user.setName(userName.getText());
					user.setPassword(password.getText());
					user.setAdmin(admin.isSelected());
					user.setType(user.isAdmin());
					
					okClicked = true;
					dialogStage.close();
				}
			}
		}
		
	}
	
	@FXML
	public void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String errorMessage = "";
		
		if(userName.getText() == null || userName.getText().length() == 0) {
			errorMessage += "Nem adtál meg felhasználó nevet!\n";
		}
		if(password.getText() == null || password.getText().length() == 0) {
			errorMessage += "Nem adtál meg jelszót!\n";
		}
		if(errorMessage.length() != 0) {
			errorMessage += "Kérlek ellenőrizd!";
		}
		if(errorMessage.length() == 0) {
			return true;
		} else {
			// show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Üres mező(k)!");
			alert.setHeaderText("Kérlek ellenőrizd az adatokat!!");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
	}
	
	
	private Integer getNextUserId() {
		return standData.getUsers().size()+1;
	}

}
