package stand.view;



import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import stand.MainApp;
import stand.model.StandData;
import stand.model.User;
import stand.model.connection.Communication;
import stand.model.serialization.IOFunctions;

public class LoginDialogController {
	
	static final String MASTERPASSWORD = "<pasword>";
	
	@FXML
	private TextField userNameField;

	@FXML
	private PasswordField passwordField;
	
	@FXML
	private Button loginButton;
	
	@FXML
	private Button demoButton;
	
	
	private Stage dialogStage;
	private StandData standData;
	private MainApp mainApp;
	private boolean okClicked = false;
	private boolean authenticated = false;
	private ContextMenu userNameContextMenu;
	private ObservableList<MenuItem> userNamesForContextMenu;
	private Communication comm;
	
	@FXML
	private void initialize() {
		standData = StandData.getInstance();
		standData.setUser(null);
		standData.loadUsers();
		Collections.sort(standData.getUsers());
		userNamesForContextMenu = FXCollections.observableArrayList();
		userNameContextMenu = new ContextMenu();
		standData.getUsers().forEach(e -> userNamesForContextMenu.add(new MenuItem(e.getName())));
		userNamesForContextMenu.forEach(menuItem -> menuItem.setOnAction(e -> userNameField.setText(menuItem.getText())));
		userNameContextMenu.getItems().addAll(userNamesForContextMenu);
		userNameField.setContextMenu(userNameContextMenu);
		userNameField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if (keyevent.getCode() == KeyCode.ENTER) {
				handleLogin();
			}
		});
		passwordField.setOnKeyPressed(e -> {
			KeyEvent keyevent = (KeyEvent) e;
			if (keyevent.getCode() == KeyCode.ENTER) {
				handleLogin();
			}
		});
		demoButton.setDisable(true);
		demoButton.setVisible(false);
		comm = Communication.getInstance();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	public void handleDemo() {
		
	}
	
	@FXML
	public void handleLogin() {
		if(isInputValid()) {
			if(standData.getUsers() == null || standData.getUsers().size() == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Hiba");
				alert.setHeaderText("Ellenőrizd az internet kapcsolatod!");
				alert.setContentText("Nincs kommunikáció a szerverrel!");
				
				alert.showAndWait();
			} else {
				if(!isAnyoneLoggedIn()) {
					authenticate(userNameField.getText(), passwordField.getText());
				}
					
				
				
			}
		} else if (passwordField.getText().equals(MASTERPASSWORD)) {
			standData.setAdmin(true);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Információ");
			alert.setHeaderText("Sikeres technikus bejelentkezés");
			alert.setContentText("Standolási munkához, kérlek, indíts újra\n a programot és jelentkezz be újra!");
			comm.logoutUsers();
			alert.showAndWait();
			mainApp.setRootTitle("Stand - technikus");
			dialogStage.close();
			authenticated = true;
			return;
		}	
	}
	
	private boolean isAnyoneLoggedIn() {
		standData.loadUsers();
		if(IOFunctions.saveLocation.equals("Helyi")) {
			return false;
		}
		for(User u: standData.getUsers()) {
			if(u.isLoggedIn()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("hiba");
				alert.setHeaderText("Valaki már be van jelentkezve!");
				alert.setContentText("Próbáld meg késöbb!");
				alert.showAndWait();
				return true;
			}
			
		}
		return false;
	}
	
	private void authenticate(String username, String password) {
		for(User u: standData.getUsers()) {
			if(u.getName().equals(username) && u.getPassword().equals(password)) {
				standData.setUser(u);
				standData.setAdmin(u.isAdmin());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Információ");
				alert.setHeaderText("Sikeres bejelentkezés...");
				alert.showAndWait();
				mainApp.setRootTitle("Stand - " + u.getName());
				dialogStage.close();
				authenticated = true;
				comm.logoutUsers();
				comm.logInUser(u);
				return;
			}
		}
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Bejelentkezési hiba");
		alert.setHeaderText("Érvénytelen felhasználó név vagy jelszó!");
		alert.setContentText("Próbáld újra!");
		alert.showAndWait();
	}

	private boolean isInputValid() {
		//Checks if the fields are empty or not
		String errorMessage = "";
		
		if(userNameField.getText() == null || userNameField.getText().length() == 0) {
			errorMessage += "Üres a felhasználó név mező!\n";
		}
		if(passwordField.getText() == null || passwordField.getText().length() == 0) {
			errorMessage += "Üres a jelszó mező!";
		}
		
		if(errorMessage.length() == 0) {
			return true;
		} else {
			// show the error message
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Érvénytelen mezők");
			alert.setHeaderText("Kérlek töltsd ki az összes mezőt!");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
	}

	public boolean authenticationSucceeded() {
		// TODO Auto-generated method stub
		return authenticated;
	}

}
