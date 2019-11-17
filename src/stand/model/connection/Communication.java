package stand.model.connection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import stand.model.Product;
import stand.model.StandData;
import stand.model.User;
import stand.model.serialization.IOFunctions;

public class Communication {
	
	private StandData standData;
	
	
	private static final Logger log4j = LogManager.getLogger(Communication.class.getName());

	
	static final String DB_URL_CLOUD = 
			"<some url or host>";
	static final String DB_URL_LOCAL = 
    		"<some url or host>";
	
	static final String USERNAME_CLOUD = 
			"<username>";
	static final String USERNAME_LOCAL = 
			"<username>";
	
	static final String PASS_CLOUD = 
			"password";
	static final String PASS_LOCAL = 
			"<password>";
	
	public static Communication instance;
	
	public static Communication getInstance() {
		if(instance == null) {
			instance = new Communication();
		}
		return instance;
	}
	
	private String db_Url = null;
	private String userName = null;
	private String pass = null;
	
	
	private Communication() {
	}
	
	public void setLocationOfSave() {
		db_Url = IOFunctions.saveLocation.equals("Helyi")? DB_URL_LOCAL:DB_URL_CLOUD;
		userName = IOFunctions.saveLocation.equals("Helyi")? USERNAME_LOCAL:USERNAME_CLOUD;
		pass = IOFunctions.saveLocation.equals("Helyi")? PASS_LOCAL:PASS_CLOUD;
	}
	
	public ArrayList<User> selectFromUsers() {
		ArrayList<User> users = new ArrayList<User>();
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String query = "SELECT * FROM public.users;";
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					boolean admin = rs.getString("admin").equals("t")? true:false;
					users.add(new User(Integer.parseInt(rs.getString("id")), rs.getString("name"), rs.getString("password"), admin, rs.getBoolean("loggedIn")));
				}
			}
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Hiba!");
			alert.setHeaderText("Nincs kapcsolat a szerverrel!");
			alert.setContentText("Kérlek ellenőrizd a net kapcsolatot!\n"
					+ "(Ha a hiba továbbra is fennáll,\n"+" szerver hiba a gond.)");
			alert.showAndWait();
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
		return users;
	}
	
	public void insertToUsers(int id, String name, String password, boolean admin) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String insert = "INSERT INTO public.users(" +
						"id, name, password, admin)" + " VALUES (" + id + ", '" + name + "', '" + 
						password + "', " + admin + ");";
				stmt.execute(insert);
			}
			
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteFromUsers(User delUser) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String delete = "DELETE FROM public.users WHERE id=" + delUser.getId() + ";";
//				System.out.println(delete);
				stmt.execute(delete);
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}

	
	public void upDateUsers(User user) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String update = "UPDATE public.users " +
						"SET name='" + user.getName() + "', password='" + user.getPassword() + "', admin=" + user.isAdmin() +
						", \"loggedIn\"=" + user.isLoggedIn() + " WHERE id= " + user.getId().toString() + ";";
				stmt.execute(update);
				
			}
			
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public void logoutUsers() {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String update = "UPDATE public.users " +
						"SET \"loggedIn\"=false WHERE 1 = 1;";
				stmt.execute(update);
				
			}
			
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public void logInUser(User user) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String update = "UPDATE public.users " +
						"SET \"loggedIn\"=true WHERE id= " + user.getId().toString() + ";";
				stmt.execute(update);
				
			}
			
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<Product> selectAllFromStand() {
		ArrayList<Product> products = new ArrayList<Product>();
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String query = "SELECT * FROM public.stand;";
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					Product product = new Product(rs.getString("megnevezes"));
					product.setId(Integer.parseInt(rs.getString("id")));
					product.setNyito(round(rs.getString("nyito")));
					product.setVetel(round(rs.getString("vetel")));
					product.setOssz(round(rs.getString("ossz")));
					product.setZaro(round(rs.getString("zaro")));
					product.setFogyas(round(rs.getString("fogyas")));
					product.setAr(rs.getString("ar"));
					product.setAkciosAr(rs.getString("akciosar"));
					product.setOsszeg(rs.getString("osszeg"));
					product.setElozoZaro(round(rs.getString("elozozaro")));
					product.setNyito_elozoZaro(round(rs.getString("nyito-elozozaro")));
					product.setElteres(rs.getString("elteresosszeg"));
					product.setGep(round(rs.getString("gep")));
					product.setAkcios(rs.getBoolean("akcios"));
					products.add(product);
				}
			}
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
		return products;
	}
	
	public void insertToStand(Product product) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String insert = "INSERT INTO public.stand (" +
						"id, megnevezes, nyito, vetel, ossz, zaro, fogyas, ar, akciosar, osszeg, elozozaro, \"nyito-elozozaro\", elteresosszeg, gep, akcios)" +
						"VALUES(" + product.getId().toString() + ", '" + parseApostroph(product.getMegnevezes()) + "', " + product.getNyito() + ", " + product.getVetel() + ", " + 
						product.getOssz() + ", " + product.getZaro() + ", " + product.getFogyas() + ", " + product.getAr() + ", " + product.getAkciosAr() + ", " +
						product.getOsszeg() + ", " + product.getElozoZaro() + ", " + product.getNyito_elozoZaro() + ", " + 
						product.getElteres() + ", " + product.getGep() + ", " +product.isAkcios() + ");";
				stmt.execute(insert);
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public void deleteFromStand(Product product) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String delete = "DELETE FROM public.stand WHERE id=" + product.getId() + ";";
				stmt.execute(delete);
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public void upDateNotes(String notes) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				
					String insert = "UPDATE public.notes " +
						"SET notes='" + notes + "' WHERE id=1;";
//					System.out.println("notes: " + insert);
				stmt.execute(insert);
				}
			
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public String selectAllFromNotes() {
		String notes = "";
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String query = "SELECT * FROM public.notes;";
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					notes = rs.getString("notes");
				}
			}
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
		return notes;
	}
	
	public void decreaseId(Integer id, int productdDataSize) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				for(Integer i = id+1; i < productdDataSize+1; i++) {
					String insert = "UPDATE public.stand" + 
							"	SET id= (select id from public.stand where id =" +i.toString()+")-1" + 
							"	WHERE id ="+ i.toString()+";";
//				System.out.println(insert);
					stmt.execute(insert);
				}
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}

	public void increaseId(Integer id) {
		standData = StandData.getInstance();
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				System.out.println();
				for(Integer i = standData.getProductsData().size(); i >= id; i--) {
					String insert = "UPDATE public.stand" + 
							"	SET id= (select id from public.stand where id =" +i.toString()+")+1" + 
							"	WHERE id ="+ i.toString()+";";
				stmt.execute(insert);
				}
			}
			standData.loadProducts();
			for(Product p: standData.getProductsData()) {
				Integer insert = p.getId();
				if(insert > id) {
					p.setId(insert+1);
				}
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void upDateStand(ObservableList<Product> productsData) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				for(int i = 0; i < productsData.size(); i++) {
					String nyito = null;
					if(productsData.get(i).getNyito() != null) {
						nyito = productsData.get(i).getNyito().equals("")? null:productsData.get(i).getNyito();
					} 
					String insert = "UPDATE public.stand " +
						"SET megnevezes='" + parseApostroph(productsData.get(i).getMegnevezes()) + "', nyito=" + nyito + 
						", vetel=" + productsData.get(i).getVetel() + ", ossz=" + productsData.get(i).getOssz() + 
						", zaro=" + productsData.get(i).getZaro() + ", fogyas=" + productsData.get(i).getFogyas() +
						", ar=" + productsData.get(i).getAr() + ", akciosar=" + productsData.get(i).getAkciosAr() + 
						", osszeg=" + productsData.get(i).getOsszeg() + ", elozozaro=" + productsData.get(i).getElozoZaro() + 
						", \"nyito-elozozaro\"=" + productsData.get(i).getNyito_elozoZaro() + ", elteresosszeg=" + productsData.get(i).getElteres() +
						", gep=" + productsData.get(i).getGep() + ", akcios=" + productsData.get(i).isAkcios() + 
						" WHERE id=" + productsData.get(i).getId() + ";";
				stmt.execute(insert);
				}
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	public void closeStand(ObservableList<Product> productsData) {
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				for(int i = 0; i < productsData.size(); i++) {
					String update = "UPDATE public.stand " +
						"SET megnevezes='" + parseApostroph(productsData.get(i).getMegnevezes()) + "', nyito=" + productsData.get(i).getZaro() + 
						", vetel=" + null + ", ossz=" + productsData.get(i).getZaro() + ", zaro=" + null + ", fogyas=0" +
						", ar=" + productsData.get(i).getAr() + ", osszeg=0" +
						", elozozaro=" + productsData.get(i).getZaro() + ", \"nyito-elozozaro\"=0" +
						", elteresosszeg=0, gep=null WHERE id=" + productsData.get(i).getId() + ";";
//				System.out.println("close: "+update);
				stmt.execute(update);
				}
			}
			System.out.println("Adatok sikeresen mentve az adatbázisba.");
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			System.out.println("Hiba az adatok adatbázisba mentése közben!!!");
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public String round(String num) {
		double d;
		if(num == null || num.equals("")) {
			return num;
		} else {
			d = Double.parseDouble(num);
			d = Math.round(d*100)/100d;
			if(d % 1 == 0) {
				Integer i = (int) d;
				return i.toString();
			} else {
				Double res = d;
				return res.toString();
			}
		}
	}
	
	public Integer getFirstfreeProductId() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Statement stmt = null;
		try {
			Connection con = DriverManager.getConnection(db_Url, userName, pass);
			if(con != null) {
				stmt = con.createStatement();
				String query = "SELECT id FROM public.stand;";
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					list.add(rs.getInt("id"));
				}
			}
			
		} catch (SQLException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log4j.error(exceptionStackTraceToString(e));
					e.printStackTrace();
				}
			}
		}
		for(int i = 0; i < list.size()+1; i++) {
			if(!list.contains(i+1)) {
				return i+1;
			}
		}
		return null;
	}
	
	private String parseApostroph(String str) {
		String res = "";
		if(str.contains("'")) {
			String[] tmp = str.split("'");
			for(int i = 0; i < tmp.length-1; i++) {
				res += tmp[i] + "''";
			}
			res += tmp[tmp.length-1];
			return res;
		}
		return str;
	}
	
	public String exceptionStackTraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}

}
