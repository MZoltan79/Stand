package stand.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import stand.model.Product;

public class ProductEditDialogController {
	
	@FXML
	TextField idField;

	@FXML
	TextField nameField;
	
	@FXML
	TextField priceField;
	
	@FXML
	Button cancelButton;
	
	@FXML
	Button okButton;

	
	private Stage dialogStage;
	private Product product;
	private boolean okClicked;
	
	@FXML
	private void initialize() {
		
	}
	@FXML
	public void handleOk() {
		if(isInputValid()) {
			product.setId(Integer.parseInt(idField.getText()));
			product.setMegnevezes(nameField.getText());
			product.setNyito("0");
			product.setAr(priceField.getText());
			okClicked = true;
			dialogStage.close();
		}
	}
	
	private boolean isInputValid() {
		String errorMessage = "";
		
		
		if(idField.getText() == null || idField.getText().length() == 0) {
			errorMessage += "Üres a sorszám mező!\n";
		}
		if(nameField.getText() == null || nameField.getText().length() == 0) {
			errorMessage += "Üres a megnevezés mező!\n";
		}
		if(priceField.getText() == null || priceField.getText().length() == 0) {
			errorMessage += "Üres az Ár mező!\n";
		}
		
		
		if(errorMessage.length() == 0) {
			return true;
		} else {
			// show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Üres mezők");
			alert.setHeaderText("Kérlek töltsd ki az összes mezőt!");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
	}
	
	public void handleCancel() {
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setProduct(Product product) {
		this.product = product;
		idField.setText(product.getId().toString());
	}

	public boolean isOkClicked() {
		return okClicked;
	}
}
