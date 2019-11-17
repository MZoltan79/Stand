package stand;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stand.model.Product;
import stand.model.StandData;
import stand.model.User;
import stand.model.connection.Communication;
import stand.model.serialization.IOFunctions;
import stand.view.LoginDialogController;
import stand.view.ManageSalesLayoutController;
import stand.view.PrevStandOverviewController;
import stand.view.ProductEditDialogController;
import stand.view.ProductOverviewController;
import stand.view.SettingLayoutController;
import stand.view.StandclosingDialogcontroller;
import stand.view.UserEditDialogController;
import stand.view.SaveTask;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private Stage saveProgressStage;
	private BorderPane rootLayout;
	private Communication comm;
	private IOFunctions io;
	private StandData standData;
	
	
	private MenuItem save;
	private MenuItem saveTofile;
	private MenuItem loadFromfile;
	private MenuItem exit;
	private MenuItem settings;
	private MenuItem switchUser;
	private MenuItem help;
	private MenuItem newProduct;
	private MenuItem editProduct;
	private MenuItem delProduct;
	private MenuItem closeStandItem;
	private MenuItem sales;
	
	
	
	/*
	 * The data as an observable list of products.
	 */

//	private ObservableList<Product> productsData = FXCollections.observableArrayList();
	
	
	public MainApp() {
		comm = Communication.getInstance();
		io = IOFunctions.getInstance();
		io.setMainApp(this);
		
//		dateOfLastClosedStand = LocalDate.of(2019, 9, 18);
//		io.saveDateOfClosedStand(today);
//		dateOfLastClosedStand = io.loadDateOfClosedStand();
		
	}
	
	public void setRootTitle(String title) {
		primaryStage.setTitle(title);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stand");
		initStandData();
		setConfiguration();
		comm.setLocationOfSave();
		initRootLayout();
		showLoginDialog();
	}
	
	public void setConfiguration() {
		Object[] sysconfig = io.loadConfig();
		standData.setSendDataInEmail((boolean)sysconfig[0]);
		standData.setRecipient((String)sysconfig[1]);
		standData.setConfigSaveLocation((String)sysconfig[2]);
//		comm.setLocationOfSave();
	}
	
	
	public void initStandData() {
		standData = StandData.getInstance();
	}


	/*
	 * initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			primaryStage.setResizable(true);
			primaryStage.setMinHeight(400);
			primaryStage.setMinWidth(600);
			
			save = (MenuItem) loader.getNamespace().get("save");
			saveTofile = (MenuItem) loader.getNamespace().get("saveToFile");
			sales = (MenuItem)loader.getNamespace().get("sales");
			loadFromfile = (MenuItem) loader.getNamespace().get("loadFromFile");
			exit = (MenuItem) loader.getNamespace().get("exit");
			settings = (MenuItem) loader.getNamespace().get("settings");
			switchUser = (MenuItem) loader.getNamespace().get("switchUser");
			help = (MenuItem) loader.getNamespace().get("help");
			newProduct = (MenuItem) loader.getNamespace().get("newProduct");
			editProduct = (MenuItem) loader.getNamespace().get("editProduct");
			delProduct = (MenuItem) loader.getNamespace().get("delProduct");
			closeStandItem = (MenuItem) loader.getNamespace().get("closeStandItem");
			
			loadFromfile.setOnAction(event -> {
				showPrevStandOverview();
			});
			settings.setOnAction(event -> showSettingsLayout());
			switchUser.setOnAction(e -> {
				comm.logoutUsers();
				showLoginDialog();
				showProductOverview();
			});
			help.setOnAction(event -> {});
			newProduct.setOnAction(event -> {
				Product tempProduct = new Product(comm.getFirstfreeProductId());
				boolean okClicked = showProductEditDialog(tempProduct, true);
				tempProduct.setVetel(null);
				tempProduct.setZaro(null);
				if(okClicked) {
					if(tempProduct.getId() != comm.getFirstfreeProductId()) {
						comm.increaseId(tempProduct.getId());
					}
					comm.insertToStand(tempProduct);
				}
				standData.loadProducts();
			});
			editProduct.setOnAction(event -> {
				if(standData.getSelectedProduct() != null) {
					boolean okClicked = showProductEditDialog(standData.getSelectedProduct(), false);
					if(okClicked) {
						comm.upDateStand(standData.getProductsData());
					}
				} else {
					//nothing selected
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(primaryStage);
					alert.setTitle("Nincs kiválasztva termék");
					alert.setHeaderText("Nem választottad ki melyik terméket szeretnéd szerkeszteni!");
					alert.setContentText("Kérlek, válassz terméket!");
					
					alert.showAndWait();
				}
			});
			delProduct.setOnAction(event -> {
				if(standData.getSelectedProduct() != null) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.initOwner(primaryStage);
					alert.setTitle("Megerősítés");
					alert.setHeaderText("A termék: " + standData.getSelectedProduct().getMegnevezes() + " törölve lesz!");
					alert.setContentText("Biztos vagy benne?");
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK) {
						comm.deleteFromStand(standData.getSelectedProduct());
						comm.decreaseId(standData.getSelectedProduct().getId(), standData.getProductsData().size());
						standData.getProductsData().remove(standData.getSelectedProduct());
						standData.loadProducts();
						showProductOverview();
					} else {
						alert.close();
					}
				} else {
					//nothing selected
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(primaryStage);
					alert.setTitle("Nincs kiválasztva termék");
					alert.setHeaderText("Nem választottad ki melyik terméket szeretnéd törölni!");
					alert.setContentText("Kérlek, válassz terméket!");
					
					alert.showAndWait();
				}
			});
			closeStandItem.setOnAction(e -> {
				if(checkOpeningAndClosingColumns()) {
					showStandClosingDialog();
				}
			});
			sales.setOnAction(e -> showSalesLayout());

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initMenus(){
		if(standData.isAdmin()) {
			settings.setDisable(false);
			newProduct.setDisable(false);
			editProduct.setDisable(false);
			delProduct.setDisable(false);
		} else {
			settings.setDisable(true);
			newProduct.setDisable(true);
			editProduct.setDisable(true);
			delProduct.setDisable(true);
		}
	}
	
	/*
	 * Show the login screen
	 */
	public void showLoginDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LoginDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Bejelentkezés");
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			LoginDialogController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			dialogStage.setOnCloseRequest(e -> Platform.exit());
			dialogStage.showAndWait();
			if(controller.authenticationSucceeded()) {
				showProductOverview();
			}
			initMenus();
			standData.loadProducts();
			standData.loadUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void showSalesLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ManageSalesLayout.fxml"));
			AnchorPane manageSales = (AnchorPane) loader.load();
			Stage salesStage = new Stage();
			salesStage.setTitle("Akciók");
			salesStage.initOwner(primaryStage);
			Scene scene = new Scene(manageSales);
			salesStage.setScene(scene);
			ManageSalesLayoutController controller = loader.getController();
			controller.setMainApp(this);
			controller.setSalesStage(salesStage);
			salesStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Show the product overview inside the root layout
	 */
	
	public void showProductOverview() {
		try {
			initMenus();
			// load product overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ProductOverview.fxml"));
			AnchorPane productOverview = (AnchorPane) loader.load();
			
			
			//set product overview to the center of root layout.
			rootLayout.setCenter(productOverview);
			
			//Give the controller access to mainApp.
			ProductOverviewController controller = loader.getController();
			controller.setMainApp(this);
			save.setOnAction(event -> {
				
				controller.handleSaveButton();
			});
			saveTofile.setOnAction(event -> {
				controller.setNotesToStandData();
				io.saveTofile(false);
			});
			exit.setOnAction(event -> {
				comm.logoutUsers();
				Alert exitAlert = new Alert(AlertType.CONFIRMATION);
				exitAlert.setTitle("Megerősítés");
				exitAlert.setHeaderText("Akarod elmenteni a standot mielőtt kilépsz?");
				exitAlert.initOwner(primaryStage);
				Optional<ButtonType> result = exitAlert.showAndWait();
				if(result.get() == ButtonType.OK) {
					controller.handleSaveButton();
					Platform.exit();
				} else {
					Platform.exit();
				}
			});
			primaryStage.setOnCloseRequest(closeRequest -> {
				Alert exitAlert = new Alert(AlertType.CONFIRMATION);
				exitAlert.setTitle("Megerősítés");
				exitAlert.setHeaderText("Akarod elmenteni a standot mielőtt kilépsz?");
				exitAlert.initOwner(primaryStage);
				Optional<ButtonType> result = exitAlert.showAndWait();
				if(result.get() == ButtonType.OK) {
					controller.handleSaveButton();
				}
				comm.logoutUsers();
			});
			controller.initEditableColumns();
			controller.showStandStats();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPrevStandOverview() {
		try {
			// load product overview.
			Stage prevStandStage = new Stage();
			prevStandStage.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PrevStandOverview.fxml"));
			AnchorPane prevStandOverview = (AnchorPane) loader.load();
			
			Scene scene = new Scene(prevStandOverview);
			prevStandStage.setScene(scene);
			ObservableList<Product> prevStand = io.openFromFile();
			prevStandStage.setTitle(io.getPrevStandFilePath());
			prevStandStage.show();
			//Give the controller acces to main app.
			PrevStandOverviewController controller = loader.getController();
			controller.setProductsData(prevStand);
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean showProductEditDialog(Product product, boolean newProduct) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ProductEditdialog.fxml"));
			AnchorPane dialogPane = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Termék szerkesztése");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(dialogPane);
			dialogStage.setScene(scene);
			TextField idField = (TextField) loader.getNamespace().get("idField");
			if(newProduct) {
				idField.setEditable(true);
			} else {
				idField.setEditable(false);
			}
			TextField nameField = (TextField) loader.getNamespace().get("nameField");
			TextField priceField = (TextField) loader.getNamespace().get("priceField");
			if(product.getMegnevezes() != null) {
				nameField.setText(product.getMegnevezes());
				priceField.setText(product.getAr());
			}
			
			ProductEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setProduct(product);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void showStandClosingDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/StandclosingDialog.fxml"));
			AnchorPane dialogPane;
			dialogPane = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Stand lezárása");
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			Scene scene = new Scene(dialogPane);
			dialogStage.setScene(scene);
			
			StandclosingDialogcontroller controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showSettingsLayout() {
		try {
			// load product overview.
			Stage settingsStage = new Stage();
			settingsStage.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SettingsLayout.fxml"));
			AnchorPane settingsLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(settingsLayout);
			settingsStage.setScene(scene);
			settingsStage.setTitle("Beállítások");
			settingsStage.setResizable(false);
			settingsStage.show();
			//Give the controller acces to main app.
			SettingLayoutController controller = loader.getController();
			controller.setSettingsStage(settingsStage);
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean showUserEditDialog(User user) {
		try {
			Stage userEditDialogStage = new Stage();
			userEditDialogStage.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/UserEditDialog.fxml"));
			AnchorPane editLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(editLayout);
			
			userEditDialogStage.setTitle("Felhasználó szerkesztése");
			userEditDialogStage.setScene(scene);
			
			UserEditDialogController controller = loader.getController();
			controller.setDialogStage(userEditDialogStage);
			controller.setUser(user);
			userEditDialogStage.setResizable(false);
			userEditDialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void showSaveProgress() {
		try {
			saveProgressStage = new Stage();
			saveProgressStage.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SaveProgressStage.fxml"));
			AnchorPane saveProgressPane = (AnchorPane)loader.load();
			Scene scene = new Scene(saveProgressPane);
			saveProgressStage.setTitle("Mentés...");
			saveProgressStage.setScene(scene);
			saveProgressStage.setResizable(false);
			ProgressBar pBar = (ProgressBar) loader.getNamespace().get("pBar");
			pBar.setProgress(0);
			SaveTask sTask = new SaveTask();
			pBar.progressProperty().unbind();
			pBar.progressProperty().bind(sTask.progressProperty());
			sTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					saveProgressStage.close();
				}
			});
			Thread th = new Thread(sTask);
			th.start();
			
			saveProgressStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * These 2 methods below will be necessary to check if the "Stand" is filled with data correctly or not.
	 */
	public boolean checkOpeningAndClosingColumns() {
		String errorMessage = "";
		int emptyCellsInOpeningcolumn = countEmptyCellsInOpeningcolumn();
		int emptyCellsInClosingcolumn = countEmptyCellsInClosingcolumn();
		int rowCounterForEmptyCells = 0;

		errorMessage += emptyCellsInOpeningcolumn == 0? "":"Hiányzó nyitó érték(ek) a(z)\n";
		for(int i = 1; i < standData.getProductsData().size()+1; i++) {
			if(standData.getProductsData().get(i-1).getNyito() == null || standData.getProductsData().get(i-1).getNyito().equals("")) {
				errorMessage += standData.getProductsData().get(i-1).getId() + ", ";
				rowCounterForEmptyCells++;
				if(rowCounterForEmptyCells != 0 && rowCounterForEmptyCells%10 == 0) {
					errorMessage += "\n";
				}
			}
		}
		errorMessage += emptyCellsInOpeningcolumn == 0? "":"\nsorszámú sor(ok)ban!\n";
		
		errorMessage += (emptyCellsInClosingcolumn !=0 && emptyCellsInOpeningcolumn != 0)? "\n\n":"";
		rowCounterForEmptyCells = 0;
		errorMessage += emptyCellsInClosingcolumn == 0? "":"Hiányzó záró érték(ek) a(z)\n";
		for(int i = 1; i < standData.getProductsData().size()+1; i++) {
			if(standData.getProductsData().get(i-1).getZaro() == null || standData.getProductsData().get(i-1).getZaro().equals("")) {
				errorMessage += standData.getProductsData().get(i-1).getId() + ", ";
				rowCounterForEmptyCells++;
				if(rowCounterForEmptyCells != 0 && rowCounterForEmptyCells%10 == 0) {
					errorMessage += "\n";
				}
			}
		}
		errorMessage += emptyCellsInClosingcolumn == 0? "":"\nsorszámú sor(ok)ban!\n";
		
		if(errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Hiányzó adatok");
			alert.setHeaderText("Nincs kitöltve a nyitó/záró oszlopok valamelyike!");
			alert.setContentText(errorMessage);
			alert.initOwner(primaryStage);
			alert.initModality(Modality.WINDOW_MODAL);
			alert.showAndWait();
			return false;
		}
	}
	
	public boolean checkNovoHostColumnIfItsFilled() {
		for(Product product: standData.getProductsData()) {
			if(product.getGep() == null || product.getGep().equals("")) {
				return false;
			}
		}
		return true;
	}
	

	public int countEmptyCellsInOpeningcolumn() {
		int result = 0;
		for(Product p: standData.getProductsData()) {
			if(p.getNyito() == null || p.getNyito().equals("")) {
				result++;
			}
		}
		return result;
	}

	public int countEmptyCellsInClosingcolumn() {
		int result = 0;
		for(Product p: standData.getProductsData()) {
			if(p.getZaro() == null || p.getZaro().equals("")) {
				result++;
			}
		}
		return result;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public String getNotes() {
		return comm.selectAllFromNotes();
	}
	
	
	public void errorAlert(String messageToUser, String errorCode, String contentText) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Hiba!");
		errorAlert.setHeaderText(messageToUser+"\nHibakód: " + errorCode);
		errorAlert.setContentText(contentText);
		errorAlert.initModality(Modality.WINDOW_MODAL);
		errorAlert.initOwner(primaryStage);
		
		errorAlert.showAndWait();
	}
	
	public void closeSaveProgressStage() {
		saveProgressStage.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
