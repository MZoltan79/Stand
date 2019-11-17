package stand.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import stand.model.connection.Communication;
import stand.model.serialization.IOFunctions;
import stand.view.PrevStandOverviewController;

public class StandData {

	private Communication comm;
	private ObservableList<Product> productsData = FXCollections.observableArrayList();
	private ObservableList<Product> prevProductsData = FXCollections.observableArrayList();
	private ArrayList<User> users;
	private Product selectedProduct;
	private User user;
	private boolean admin;
	private LocalDate today;
	private LocalDate dateOfLastClosedStand;
	private IOFunctions io;
	private boolean connEstablished;
	private String recipient;
	private boolean sendDataInEmail;
	private String notes;
	private String prevNotes;
	private String pathToProgramFiles;
	private String configSaveLocation;
	private ArrayList<String> productsOnSale;
	private Integer sumDifference;
	
	private static StandData instance;
	
	public static synchronized StandData getInstance() {
		if(instance == null) {
			instance = new StandData();
		}
		return instance;
	}
	
	private StandData() {
		this.user = new User();
		this.admin = user.isAdmin();
		this.users = new ArrayList<User>();
		this.comm = Communication.getInstance();
		this.io = IOFunctions.getInstance();
		this.today = LocalDate.now();
		this.dateOfLastClosedStand = io.loadDateOfClosedStand();
		this.prevProductsData = io.loadLastStand();
		this.connEstablished = false;
		this.recipient = "";
		this.pathToProgramFiles = System.getenv("ProgramFiles");
		this.productsOnSale = new ArrayList<>();
		for(Product product: prevProductsData) {
			if(product.isAkcios()) {
				productsOnSale.add(product.getMegnevezes());
			}
		}
	}
	
	public void loadProducts() {
		productsData.removeAll(productsData);
		productsData.addAll(comm.selectAllFromStand());
		Collections.sort(productsData);
		this.notes = comm.selectAllFromNotes();
	}
	
	public void loadUsers() {
		users.removeAll(users);
		users.addAll(comm.selectFromUsers());
	}
	
	
	public boolean checkId(int id) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(Product p: productsData) {
			list.add(p.getId());
		}
		if(list.contains(id)) {
			return true;
		}
		return false;
		
	}
	
	public ObservableList<Product> getProductsData() {
		return productsData;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public LocalDate getToday() {
		return today;
	}

	public void setToday(LocalDate today) {
		this.today = today;
	}

	public LocalDate getDateOfLastClosedStand() {
		return dateOfLastClosedStand;
	}

	public void setDateOfLastClosedStand(LocalDate dateOfLastClosedStand) {
		this.dateOfLastClosedStand = dateOfLastClosedStand;
	}

	public boolean isConnEstablished() {
		return connEstablished;
	}

	public void setConnEstablished(boolean connEstablished) {
		this.connEstablished = connEstablished;
	}
	
	public boolean isUserNameAvailable(String userName) {
		for(User user: users) {
			if(user.getName().equals(userName)) {
				return false;
			}
		}
		return true;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setSendDataInEmail(boolean b) {
		this.sendDataInEmail = b;
	}

	public boolean isSendDataInEmail() {
		return sendDataInEmail;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPrevNotes() {
		return prevNotes;
	}

	public void setPrevNotes(String prevNotes) {
		this.prevNotes = prevNotes;
	}

	public String getPathToProgramFiles() {
		return pathToProgramFiles;
	}

	public String getConfigSaveLocation() {
		return configSaveLocation;
	}

	public void setConfigSaveLocation(String configSaveLocation) {
		this.configSaveLocation = configSaveLocation;
	}
	
	public void refreshStand() {
		productsData.forEach(product -> {
			if(product.isAkcios() && product.getAkciosAr() != null) {
				Double osszeg = Double.parseDouble(product.getAkciosAr())*Double.parseDouble(product.getFogyas());
				product.setOsszeg(new Integer(osszeg.intValue()).toString());
			} else {
				Double osszeg = Double.parseDouble(product.getAr())*Double.parseDouble(product.getFogyas());
				product.setOsszeg(new Integer(osszeg.intValue()).toString());
			}
		});
	}

	public ArrayList<String> getProductsOnSale() {
		return productsOnSale;
	}

	public Integer getSumDifference() {
		return sumDifference;
	}

	public void setSumDifference(int sumDifference) {
		this.sumDifference = sumDifference;
	}
	
}
