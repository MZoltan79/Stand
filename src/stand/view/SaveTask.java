package stand.view;


import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import stand.model.Product;
import stand.model.StandData;
import stand.model.connection.Communication;

public class SaveTask extends Task<ObservableList<Product>> {
	
	Communication comm;
	StandData standData;
	
	

	public SaveTask() {
		super();
		this.comm = Communication.getInstance();
		this.standData = StandData.getInstance();
	}



	@Override
	protected ObservableList<Product> call() throws Exception {
		comm.upDateStand(standData.getProductsData());
		comm.upDateNotes(standData.getNotes());
		return null;
	}

}
