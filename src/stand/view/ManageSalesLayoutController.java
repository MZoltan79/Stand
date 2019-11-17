package stand.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import stand.MainApp;
import stand.model.Product;
import stand.model.StandData;

public class ManageSalesLayoutController {
	
	private StandData standData;
	private MainApp mainApp;
	private Stage salesStage;
	
	@FXML
	private TableView<Product> salesTable;
	
	@FXML
	private TableColumn<Product, String> nameColumn;
	@FXML
	private TableColumn<Product, String> priceColumn;
	@FXML
	private TableColumn<Product, String> onsalePriceColumn;
	@FXML
	private TableColumn<Product, Boolean> isOnsaleColumn;
	
	@FXML
	private Button closeButton;
	
	public void initialize() {
		standData = StandData.getInstance();
		
		salesTable.setItems(standData.getProductsData());
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getMegnevezesProperty());
		priceColumn.setCellValueFactory(cellData -> cellData.getValue().getArProperty());
		onsalePriceColumn.setCellValueFactory(cellData -> cellData.getValue().getAkciosArProperty());
		onsalePriceColumn.setCellValueFactory(cellData -> cellData.getValue().getAkciosArProperty());
		isOnsaleColumn.setCellValueFactory(cellData -> cellData.getValue().getAkciosProperty());
		
		salesTable.setEditable(true);
		salesTable.getSelectionModel().setCellSelectionEnabled(true);
		salesTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> standData.setSelectedProduct(newValue));
		
		onsalePriceColumn.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
		onsalePriceColumn.setOnEditCommit((CellEditEvent<Product, String> event) -> {
			TablePosition<Product, String> pos = event.getTablePosition();
			String newValue = event.getNewValue();
			int row = pos.getRow();
			Product product = event.getTableView().getItems().get(row);
			if(isNumberInputValid(newValue)) {
				product.setAkciosAr(newValue);
			} else {
				salesTable.getSelectionModel().getSelectedItem().setAkciosAr(null);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nem megfelelő formátum");
				alert.setHeaderText("Nem számot írtál be!");
				alert.initOwner(mainApp.getPrimaryStage());
				product.setNyito(null);
				alert.initModality(Modality.WINDOW_MODAL);
				alert.showAndWait();
			}
		}) ;
		
		 isOnsaleColumn.setCellFactory(new Callback<TableColumn<Product, Boolean>, //
			        TableCell<Product, Boolean>>() {
			            @Override
			            public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> p) {
			                CheckBoxTableCell<Product, Boolean> cell = new CheckBoxTableCell<Product, Boolean>();
			                cell.setAlignment(Pos.CENTER);
			                return cell;
			            }
			        });
		
		
		
	}
	
	public Product getSelectedItemFromSalesTable() {
		return salesTable.getSelectionModel().getSelectedItem();
	}
	
	private boolean isNumberInputValid(String num) {
		if(num != null && (num.matches("[0-9]+"))) return true;
		return false;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setSalesStage(Stage salesStage) {
		this.salesStage = salesStage;
	}
	
	public void handleCloseButton() {
		standData.refreshStand();
		salesStage.close();
		mainApp.showProductOverview();
	}
	
}
