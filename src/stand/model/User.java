package stand.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User implements Comparable<User> {
	
	private final IntegerProperty id;
	private final StringProperty name;
	private final StringProperty password;
	private final BooleanProperty admin;
	private final StringProperty type;
	private final BooleanProperty loggedIn;
	
	public User() {
		this.id = new SimpleIntegerProperty(0);
		this.name = new SimpleStringProperty("");
		this.password = new SimpleStringProperty(""); 
		this.admin = new SimpleBooleanProperty(false);
		this.type = new SimpleStringProperty("");
		this.loggedIn = new SimpleBooleanProperty(false);
	}
	
	public User(int id, String name, String password, boolean admin, boolean loggedIn) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.password = new SimpleStringProperty(password); 
		this.admin = new SimpleBooleanProperty(admin);
		this.type = new SimpleStringProperty(this.isAdmin()? "admin":"felhasználó");
		this.loggedIn = new SimpleBooleanProperty(loggedIn);
	}

	public Integer getId() {
		return id.get();
	}
	
	public IntegerProperty getIdProperty() {
		return id;
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getName() {
		return name.get();
	}
	
	public StringProperty getNameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getPassword() {
		return password.get();
	}
	
	public StringProperty getPasswordProperty() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public boolean isAdmin() {
		return admin.get();
	}

	public BooleanProperty isAdminProperty() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin.set(admin);
	}
	
	public String getType() {
		return type.get();
	}
	
	public StringProperty getTypeProperty() {
		return type;
	}
	
	public void setType(boolean admin) {
		this.type.set(admin? "admin":"felhasználó");
	}
	
	public boolean isLoggedIn() {
		return loggedIn.get();
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn.set(loggedIn);
	}
	
	public BooleanProperty getLoggedInProperty() {
		return loggedIn;
	}

	@Override
	public String toString() {
		String admin = this.admin.get()? "admin":"user";
		return "Azonosító: "+ id.get() + ", név: " + "\"" + name.get() + "\", jelszó: \"" + password.get() + "\", " + admin;
	}

	@Override
	public int compareTo(User toCompare) {
		if(this.name.get().compareTo(toCompare.name.get()) < 0) {
			return -1;
		} else if(this.name.get().compareTo(toCompare.name.get()) == 0) {
			return 0;
		}
		return 1;
	}
	
	

}
