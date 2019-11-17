package stand.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.transform.Scale;
import stand.MainApp;
import stand.model.Product;
import stand.model.StandData;

public class PrevStandOverviewController {
	
	private ObservableList<Product> list;
	
	@FXML
	private TableView<Product> productTable;

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
	private Label productIDLabel;
	
	@FXML
	private Label productNameLabel;
	
	@FXML
	private Label productOpeningLabel;
	
	@FXML
	private Label productBuyLabel;
	
	@FXML
	private Label productSumLabel;
	
	@FXML 
	private Label productClosingLabel;
	
	@FXML
	private Label productConsumptionLabel;
	
	@FXML
	private Label productPriceLabel;
	
	@FXML
	private Label productSumPriceLabel;
	
	@FXML
	private Label productPrevClosingLabel;
	
	@FXML
	private Label productOpenMinPrevCloseLabel;
	
	@FXML
	private Label productDifferenceLabel;
	
	@FXML
	private TextArea notes;
	
	//Reference to the MainApp
	@SuppressWarnings("unused")
	private MainApp mainApp;
	
	private StandData standData;
	
	@FXML
	private Button print;
	
	/*
	 * The Constructor.
	 * The constructor is called before initialize() method.
	 */
	public PrevStandOverviewController() {
	}
	
	@FXML
	private void initialize() {
		
		
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
		
		//initialize the product table with all the columns.
		
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
		
		//clear product Details
		showProductDetails(null);
		standData = StandData.getInstance();
		
//		ps = PrintingService.getInstance();
//	
		//Listen for selection changes and show the product details when changed.
		productTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showProductDetails(newValue));
	}
	
	
	
	
	/*
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		
	}
	
	/*
	 * Fills all text fields to show details about the product.
	 * if the specified product is null, all text fields are cleared.
	 * 
	 * @param product the product or null
	 */
	
	private void showProductDetails(Product product) {
		if(product != null) {
			//Fill labels with info from the product object.
			productIDLabel.setText(product.getId().toString());
			productNameLabel.setText(product.getMegnevezes());
			productOpeningLabel.setText(product.getNyito());
			productBuyLabel.setText(product.getVetel());
			productSumLabel.setText(product.getOssz());
			productClosingLabel.setText(product.getZaro());
			productConsumptionLabel.setText(product.getFogyas());
			productPriceLabel.setText(product.isAkcios()? product.getAkciosAr() + " Ft.":product.getAr() + " Ft.");
			productSumPriceLabel.setText(product.getOsszeg());
			productPrevClosingLabel.setText(product.getElozoZaro());
			productOpenMinPrevCloseLabel.setText(product.getNyito_elozoZaro());
			productDifferenceLabel.setText(product.getElteres());
		} else {
			//if product is null remove all the text
			productIDLabel.setText("");
			productNameLabel.setText("");
			productOpeningLabel.setText("");
			productBuyLabel.setText("");
			productSumLabel.setText("");
			productClosingLabel.setText("");
			productConsumptionLabel.setText("");
			productPriceLabel.setText("");
			productSumPriceLabel.setText("");
			productPrevClosingLabel.setText("");
			productOpenMinPrevCloseLabel.setText("");
			productDifferenceLabel.setText("");
		}
	}
	
	public Number plus(Number num1, Number num2) {
		Double result = num1.doubleValue() + num2.doubleValue();
		result = Math.round(result*100)/100d;
		if(result %1 == 0) {
			Integer integerResult = result.intValue();
			return integerResult;
		} else {
			Double doubleResult = result;
			return doubleResult;
		}
	}

	public Number multiply(Number num1, Number num2) {
		Double result = num1.doubleValue() * num2.doubleValue();
		result = Math.round(result*100)/100d;
		if(result %1 == 0) {
			Integer integerResult = result.intValue();
			return integerResult;
		} else {
			Double doubleResult = result;
			return doubleResult;
		}
	}

	public void setProductsData(ObservableList<Product> prevStand) {
		productTable.setItems(prevStand);		
		list = prevStand;
		notes.setText("Megjegyzések:\n\n" + standData.getPrevNotes());
		
	}
	
	public void changeFontSizeForColumns(int fontSize) {
		productIdColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productNameColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productOpeningColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productBuyColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productQuantityColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productClosingColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productConsumptionColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productPriceColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productSumPriceColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productPrevClosingColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productOpenMinPrevCloseColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productDiffColumn.setStyle("-fx-font-size: " + fontSize + ";");
		productPrevClosingColumn.setVisible(false);
	}
	
	@FXML
	public void handlePrintButton() {
		ObservableList<Product> tempList = list;
		int numOfVisibleRows = 17;
		Integer numOfListsToCreate = list.size()%numOfVisibleRows == 0? list.size()/numOfVisibleRows:(list.size()/numOfVisibleRows)+1; 
		for(int i = 0; i < numOfListsToCreate; i++) {
			if(i != 0) {
				for(int j = 0; j < numOfVisibleRows; j++) {
					list.remove(0);
				}
			}
			changeFontSizeForColumns(9);
			Printer printer = Printer.getDefaultPrinter();
			PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
			
			double scaleWidth = pageLayout.getPrintableWidth() / productTable.getBoundsInParent().getWidth();
			double scaleHeight = pageLayout.getPrintableHeight() / productTable.getBoundsInParent().getHeight();
			productTable.getTransforms().add(new Scale(scaleWidth,scaleHeight));
			PrinterJob job = PrinterJob.createPrinterJob();
			if(job != null) {
				boolean success = job.printPage(pageLayout,productTable);
				
				if(success) {
					job.endJob();
				}
			}
			
		}
		changeFontSizeForColumns(13);
		list.removeAll(list);
		list.addAll(tempList);
		initialize();

	}
	
	
}
