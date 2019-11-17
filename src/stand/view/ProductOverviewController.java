package stand.view;


import java.time.DayOfWeek;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import stand.MainApp;
import stand.model.Product;
import stand.model.StandData;
//import stand.model.connection.Communication;
import stand.model.serialization.IOFunctions;

public class ProductOverviewController {
	
	@FXML
	public TableView<Product> productTable;

	@FXML
	private TableColumn<Product, Integer> productIdColumn;
	@FXML
	private TableColumn<Product, String> productNameColumn;
	@FXML
	private TableColumn<Product, String> productOpeningColumn;
	@FXML
	private TableColumn<Product, String> productBuyColumn;
	@FXML
	private TableColumn<Product, String> productQuantityColumn;
	@FXML
	private TableColumn<Product, String> productClosingColumn;
	@FXML
	private TableColumn<Product, String> productConsumptionColumn;
	@FXML
	private TableColumn<Product, String> productPriceColumn;
	@FXML
	private TableColumn<Product, String> productSumPriceColumn;
	@FXML
	private TableColumn<Product, String> productPrevClosingColumn;
	@FXML
	private TableColumn<Product, String> productOpenMinPrevCloseColumn;
	@FXML
	private TableColumn<Product, String> productDiffColumn;
	@FXML
	private TableColumn<Product, String> fromNovohost;
	
	@FXML
	private Label sumStandLabel;
	@FXML
	private Label sumDiffLabel;
	@FXML
	private Label salesIndicatorLabel;
	@FXML
	private Label salesLabel;
	
	@FXML
	private TextArea notes;

	@FXML
	private Button closeStandButton;
	@FXML
	private Button openButton;
	@FXML
	private Button saveAsButton;
	@FXML
	private Button saveButton;
	
	private MainApp mainApp;
//	private Communication comm;
	private IOFunctions io;
	private StandData standData;
	private IntegerProperty sumStand;
	private IntegerProperty sumDiff;
	
	/*
	 * The Constructor.
	 * The constructor is called before initialize() method.
	 */
	public ProductOverviewController() {
//		comm = Communication.getInstance();
		io = IOFunctions.getInstance();
		standData = StandData.getInstance();
	}
	
	@FXML
	private void initialize() {
		
		sumStand = new SimpleIntegerProperty();
		sumDiff = new SimpleIntegerProperty();
		
		//setting alignments to columns 
		productIdColumn.setStyle("-fx-alignment: center;");
		productNameColumn.setStyle("-fx-alignment: center-left;");
		productOpeningColumn.setStyle("-fx-alignment: center;");
		productBuyColumn.setStyle("-fx-alignment: center;");
		productQuantityColumn.setStyle("-fx-alignment: center;");
		productClosingColumn.setStyle("-fx-alignment: center;");
		productConsumptionColumn.setStyle("-fx-alignment: center;");
		productPriceColumn.setStyle("-fx-alignment: center-right;");
		productSumPriceColumn.setStyle("-fx-alignment: center-right;");
		productPrevClosingColumn.setStyle("-fx-alignment: center;");
		productOpenMinPrevCloseColumn.setStyle("-fx-alignment: center;");
		productDiffColumn.setStyle("-fx-alignment: center;");
		fromNovohost.setStyle("-fx-alignment: center;");
		productPrevClosingColumn.setVisible(false);
		
		if(standData.getToday().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			salesIndicatorLabel.setVisible(true);
			salesIndicatorLabel.setText("Péntek van!");
			salesLabel.setVisible(true);
			salesLabel.setText("Akciókat állítsd be!");
			salesLabel.setStyle("-fx-text-fill: red;");
			salesIndicatorLabel.setStyle("-fx-text-fill: red;");
		} else if(standData.getToday().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			salesIndicatorLabel.setVisible(true);
			salesIndicatorLabel.setText("Szombat van!");
			salesLabel.setVisible(true);
			salesLabel.setText("Akciókat töröld, ha kell!");
			salesLabel.setStyle("-fx-text-fill: red;");
			salesIndicatorLabel.setStyle("-fx-text-fill: red;");
		} else {
			salesIndicatorLabel.setVisible(false);
			salesLabel.setVisible(false);
		}
		
		
		//initialize the product table with all the columns.
		initTableView();
		
		//clear product Details
//		showProductDetails(null);
		
		//Listen for selection changes and show the product details when changed.
		productTable.setEditable(true);
		productTable.getSelectionModel().setCellSelectionEnabled(true);
//		productTable.getSelectionModel().selectedItemProperty().addListener(
//				(observable, oldValue, newValue) -> showProductDetails(newValue));
		productTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> standData.setSelectedProduct(newValue));
		sumStand.addListener((observable, oldValue, newValue) -> showStandStats());
		sumDiff.addListener((observable, oldValue, newValue) -> showStandStats());
	}
	
	public void initEditableColumns() {
		
		productOpeningColumn.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
		productOpeningColumn.setOnEditCommit((CellEditEvent<Product, String> event) -> {
			TablePosition<Product, String> pos = event.getTablePosition();
			String newOpening = event.getNewValue();
			int row = pos.getRow();
			Product product = event.getTableView().getItems().get(row);
			if(isNumberInputValid(newOpening)) {
				if(product.getZaro() == null) {
					product.setNyito(decimalConverter(newOpening));
					if(getSelectedItemFromProductsTable().getMegnevezes().equals("Kávé")) {
						StringBuilder sb = new StringBuilder(plusResultFormatter(newOpening, product.getVetel()));
						sb.deleteCharAt(0);
						product.setOssz(sb.toString());
					} else {
						product.setOssz(plusResultFormatter(newOpening, product.getVetel()));
					}
					product.setNyito_elozoZaro(plusResultFormatter(newOpening, "-".concat(product.getElozoZaro())));
					product.setElteres(standData.getProductsOnSale().contains(product.getMegnevezes())? 
							multiplyResultFormatter(product.getNyito_elozoZaro(), product.getAkciosAr()):
								multiplyResultFormatter(product.getNyito_elozoZaro(), product.getAr()));
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Hiba!");
					alert.setHeaderText("Záró oszlop már ki van töltve!");
					alert.setContentText("Előbb töröld a záró értéket!");
					alert.initOwner(mainApp.getPrimaryStage());
					alert.initModality(Modality.WINDOW_MODAL);
					alert.showAndWait();
				}
			} else {
				productTable.getSelectionModel().getSelectedItem().setNyito("");
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nem megfelelő formátum");
				alert.setHeaderText("Nem számot írtál be!");
				alert.initOwner(mainApp.getPrimaryStage());
				product.setNyito(null);
				alert.initModality(Modality.WINDOW_MODAL);
				alert.showAndWait();
			}
			countSumStand();
			countSumDiff();
		}) ;

		productBuyColumn.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
		productBuyColumn.setOnEditCommit((CellEditEvent<Product, String> event) -> {
			if(getSelectedItemFromProductsTable() != null/* && !getSelectedItemFromProductsTable().getMegnevezes().equals("Kávé")*/) {
				TablePosition<Product, String> pos = event.getTablePosition();
				String newBuy = event.getNewValue() == null? "0": event.getNewValue();
				int row = pos.getRow();
				Product product = event.getTableView().getItems().get(row);
				if(isNumberInputValid(newBuy)) {
					if(product.getZaro() == null) {
						product.setVetel(decimalConverter(newBuy));
						if(getSelectedItemFromProductsTable().getMegnevezes().equals("Kávé")) {
							Integer temp = -1* Integer.parseInt(newBuy);
							Integer ossz =(int) (Double.parseDouble(plusResultFormatter(temp.toString(), product.getNyito())) + 
									(Double.parseDouble(getSelectedItemFromProductsTable().getNyito())*2)); 
							product.setOssz(ossz.toString());
						} else {
							product.setOssz(plusResultFormatter(newBuy, product.getNyito()));
						}
						if(product.getZaro() != null) {
							product.setFogyas(plusResultFormatter(product.getOssz(), ("-".concat(product.getZaro()))));
						} 
						product.setOsszeg(product.isAkcios()? multiplyResultFormatter(product.getFogyas(), product.getAkciosAr()):
							multiplyResultFormatter(product.getFogyas(), product.getAr()));
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Hiba!");
						alert.setHeaderText("Záró oszlop már ki van töltve!");
						alert.setContentText("Előbb töröld a záró értéket!");
						alert.initOwner(mainApp.getPrimaryStage());
						alert.initModality(Modality.WINDOW_MODAL);
						alert.showAndWait();
					}
				} else {
					productTable.getSelectionModel().getSelectedItem().setVetel("");
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Nem megfelelő formátum");
					alert.setHeaderText("Nem számot írtál be!");
					alert.initOwner(mainApp.getPrimaryStage());
					product.setVetel(null);
					alert.initModality(Modality.WINDOW_MODAL);
					alert.showAndWait();
				}
			}
			countSumStand();
			countSumDiff();
		}) ;
		
		productClosingColumn.setOnEditCommit((CellEditEvent<Product, String> event) -> {
			if(getSelectedItemFromProductsTable() != null) {
				if(getSelectedItemFromProductsTable().getNyito() != null) {
					TablePosition<Product, String> pos = event.getTablePosition();
					String newClosing = event.getNewValue();
					int row = pos.getRow();
					Product product = event.getTableView().getItems().get(row);
					if(isNumberInputValid(newClosing)) {
						product.setZaro(decimalConverter(newClosing));
						product.setFogyas(plusResultFormatter(product.getOssz(), ("-".concat(product.getZaro()))));
						product.setOsszeg(product.isAkcios()? multiplyResultFormatter(product.getFogyas(), product.getAkciosAr()):
															multiplyResultFormatter(product.getFogyas(), product.getAr()));
					} else {
						productTable.getSelectionModel().getSelectedItem().setZaro("");
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Nem megfelelő formátum");
						alert.setHeaderText("Nem számot írtál be!");
						alert.initOwner(mainApp.getPrimaryStage());
						product.setZaro(null);
						alert.initModality(Modality.WINDOW_MODAL);
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Nyitó mező üres");
					alert.setHeaderText("Előbb írd be a termék nyitó értékét!");
					alert.initOwner(mainApp.getPrimaryStage());
					alert.initModality(Modality.WINDOW_MODAL);
					alert.showAndWait();
					productTable.getSelectionModel().getSelectedItem().setZaro("");
				}
				
			}
			countSumStand();
			countSumDiff();
		}) ;
		
		
		if(standData.isAdmin()) {
			productPrevClosingColumn.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
			productPrevClosingColumn.setOnEditCommit((CellEditEvent<Product, String> event) -> {
				if(getSelectedItemFromProductsTable() != null) {
					TablePosition<Product, String> pos = event.getTablePosition();
					String newPrevClosing = event.getNewValue();
					int row = pos.getRow();
					Product product = event.getTableView().getItems().get(row);
					if(isNumberInputValid(newPrevClosing)) {
						product.setElozoZaro(decimalConverter(newPrevClosing));
						product.setNyito_elozoZaro(plusResultFormatter(product.getNyito(), "-".concat(product.getElozoZaro())));
					} else {
						productTable.getSelectionModel().getSelectedItem().setZaro("");
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Nem megfelelő formátum");
						alert.setHeaderText("Nem számot írtál be!");
						alert.initOwner(mainApp.getPrimaryStage());
						product.setElozoZaro(null);
						alert.initModality(Modality.WINDOW_MODAL);
						alert.showAndWait();
					}
					
				}
			}) ;
		}
		
		if(standData.isAdmin()) {
			fromNovohost.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
			fromNovohost.setOnEditCommit((CellEditEvent<Product, String> event) -> {
				if(getSelectedItemFromProductsTable() != null) {
					TablePosition<Product, String> pos = event.getTablePosition();
					String newGep = event.getNewValue();
					int row = pos.getRow();
					Product product = event.getTableView().getItems().get(row);
					if(isNumberInputValid(newGep)) {
						product.setGep(decimalConverter(newGep));
//						showProductDetails(product);
					} else {
						productTable.getSelectionModel().getSelectedItem().setGep("");
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Nem megfelelő formátum");
						alert.setHeaderText("Nem számot írtál be!");
						alert.initOwner(mainApp.getPrimaryStage());
						product.setGep(null);;
						alert.initModality(Modality.WINDOW_MODAL);
						alert.showAndWait();
					}
					
				}
			}) ;
		}
		Platform.runLater(() -> {
			productTable.requestFocus();
			productTable.getSelectionModel().select(0);
			productTable.scrollTo(0);
			int moveCells7Times = 7;
			for(int i = 0; i < moveCells7Times; i++) {
				productTable.getSelectionModel().selectLeftCell();
			}
			initClosingColumn();
			countSumStand();
			countSumDiff();
//			showStandStats();
		});
		notes.setText(mainApp.getNotes());
		countSumStand();
	}
	
	public void initClosingColumn()  {
		productClosingColumn.setCellFactory(column -> {
			TextFieldTableCell<Product, String> cell =  new TextFieldTableCell<Product, String>(new StringConverter<String>() {
				@Override
				public String toString(String object) {
					if(object == null || object.equals("")) {
						return null;
					}
					return object;
				}
				@Override
				public String fromString(String string) {
					if(string == null || string.equals("")) {
						return null;
					}
					return string;
				}
			}) {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					double fogyas;
					
					if(standData.getSelectedProduct() != null && standData.getSelectedProduct().getFogyas() != null) {
						fogyas = Double.parseDouble(standData.getSelectedProduct().getFogyas());
					} else {
						fogyas = 0;
					}
					Platform.runLater(() -> {
						if(item == null || empty) {
							setStyle("-fx-border-color: pink");
						} else if(item != null && fogyas < 0) {
							setStyle("-fx-alignment: center; -fx-border-color: transparent -fx-table-cell-border-color transparent transparent; -fx-text-fill: red;");
						} else if(item != null && fogyas >= 0) {
							setStyle("-fx-alignment: center; -fx-border-color: transparent -fx-table-cell-border-color transparent transparent; -fx-text-fill: -fx-text-background-color;");
						}
					});
				}
			};
			return cell;
		});
	}
	
	/*
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		// Add observable list data to the table
		productTable.setItems(standData.getProductsData());
		fromNovohost.setVisible(false);
	}
	
	
	public String plusResultFormatter(String num1, String num2) {
		Double d1;
		Double d2;
		if(num1 == null || num1.equals("")) {
			d1 = 0d;
		} else {
			d1 = Double.parseDouble(decimalConverter(num1));
		}
		if(num2 == null || num2.equals("") || num2.equals("-")) {
			d2 = 0d;
		} else {
			d2 = Double.parseDouble(decimalConverter(num2));
		}
		double d3 = 0;
		if(getSelectedItemFromProductsTable().getMegnevezes().equals("Kávé")) {
			d3 =-1*(d2 + d1);
		} else {
			d3 = d1 + d2;
		}
		d3 = Math.round(d3*100)/100.0;
		if(d3%1 == 0) {
			Integer i = (int) d3;
			return i.toString();
		} else {
			Double res = d3;
			return res.toString();
		}
	}

	public String multiplyResultFormatter(String num1, String num2) {
		Double d1;
		Double d2;
		if(num1 == null || num1.equals("")) {
			d1 = 0d;
		} else {
			d1 = Double.parseDouble(decimalConverter(num1));
		}
		if(num2 == null || num2.equals("")) {
			d2 = 0d;
		} else {
			d2 = Double.parseDouble(decimalConverter(num2));
		}
		double d3 = d1 * d2;
		d3 = Math.round(d3*100)/100d;
		Integer i = (int) d3;
		return i.toString();
	}
	
	public void initTableView() {
		productIdColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
		productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMegnevezesProperty());
		productOpeningColumn.setCellValueFactory(cellData -> cellData.getValue().getNyitoProperty());
		productBuyColumn.setCellValueFactory(cellData -> cellData.getValue().getVetelProperty());
		productQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getOsszProperty());
		productClosingColumn.setCellValueFactory(cellData -> cellData.getValue().getZaroProperty());
		productConsumptionColumn.setCellValueFactory(cellData -> cellData.getValue().getFogyasProperty());
		productPriceColumn.setCellValueFactory(cellData -> cellData.getValue().isAkcios()? cellData.getValue().getAkciosArProperty():cellData.getValue().getArProperty());
		productSumPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getOsszegProperty());
		productQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getOsszProperty());
		productPrevClosingColumn.setCellValueFactory(cellData -> cellData.getValue().getElozoZaroProperty());
		productOpenMinPrevCloseColumn.setCellValueFactory(cellData -> cellData.getValue().getNyito_elozoZaroProperty());
		productDiffColumn.setCellValueFactory(cellData -> cellData.getValue().getElteresProperty());
		fromNovohost.setCellValueFactory(cellData -> cellData.getValue().getGepProperty());
	}
	
	public String decimalConverter(String num) {
		if(num.contains(",")) num = num.replace(',', '.');
		return num;
	}
	
	public String round(String num) {
		Double d = Math.round((Double.parseDouble(num))*100)/100d;
		return d.toString();
	}
	
	public Product getSelectedItemFromProductsTable() {
		return productTable.getSelectionModel().getSelectedItem();
	}
	
	private boolean isNumberInputValid(String num) {
		if(num != null && num.equals("")) return true;
		if(num != null && (num.matches("-?[0-9]+\\.?[0-9]*"))) return true;
		if(num != null && (num.matches("-?[0-9]+,?[0-9]*"))) return true;
		return false;
	}

	
	public void setStandEditable(boolean editable) {
		productTable.setEditable(editable);
	}
	
	
	@FXML
	public void handleSaveButton() {
		mainApp.showSaveProgress();
	}

	@FXML
	public void handleSaveAsButton() {
		standData.setNotes(notes.getText());
		io.saveTofile(false);
	}
	
	@FXML
	public void handleOpenButton() {
		mainApp.showPrevStandOverview();
	}
	
	@FXML private void handleCloseStandButton() {
		if(mainApp.checkOpeningAndClosingColumns()) {
			mainApp.showStandClosingDialog();
		}
		
	}
	
	public void setNotesToStandData() {
		standData.setNotes(notes.getText());
	}
	
	public IntegerProperty getSumStandProperty() {
		return sumStand;
	}
	
	public Integer getSumStand() {
		return sumStand.get();
	}
	
	public void setSumStand(int sumStand) {
		this.sumStand.set(sumStand);
	}
	public IntegerProperty getSumDiffProperty() {
		return sumDiff;
	}
	
	public Integer getSumDiff() {
		return sumDiff.get();
	}
	
	public void setSumDiff(int sumDiff) {
		this.sumDiff.set(sumDiff);
	}
	
	public Integer countSumStand() {
		int sum = 0;
		for(Product product: standData.getProductsData()) {
			sum += Integer.parseInt(product.getOsszeg());
		}
		setSumStand(sum);
		return sum;
	}
	public Integer countSumDiff() {
		int sum = 0;
		for(Product product: standData.getProductsData()) {
			sum += Integer.parseInt(product.getElteres());
		}
		setSumDiff(sum);
		standData.setSumDifference(sum);
		return sum;
	}
	
	public void showStandStats() {
		sumStandLabel.setText(getSumStand().toString() + " Ft.");
		sumDiffLabel.setText(getSumDiff().toString() + " Ft.");
	}
}
