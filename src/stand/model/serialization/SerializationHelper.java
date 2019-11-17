package stand.model.serialization;

import java.util.ArrayList;
import java.util.Collections;
import stand.model.Product;
import stand.model.StandData;

public class SerializationHelper {
	
	private static SerializationHelper instance;
	
	public static SerializationHelper getInstance() {
		if(instance == null) {
			instance = new SerializationHelper();
		}
		return instance;
	}

	private StandData standData;
	private ArrayList<SimpleProduct> simpleformat;
	
	public SerializationHelper() {
		super();
		this.simpleformat = new ArrayList<SimpleProduct>();
	}
	
	public ArrayList<SimpleProduct> convertProductDataToSimpleFormat() {
		standData = StandData.getInstance();
		simpleformat.removeAll(simpleformat);
		for(Product p: standData.getProductsData()) {
			simpleformat.add(new SimpleProduct(p.getId(), p.getMegnevezes(), p.getNyito(), p.getVetel(),
					p.getOssz(), p.getZaro(), p.getFogyas(), p.getAr(), p.getAkciosAr(), p.getOsszeg(), p.getElozoZaro(),
					p.getNyito_elozoZaro(), p.getElteres(),p.getGep(), p.isAkcios()));
		}
		Collections.sort(simpleformat);
		return simpleformat;
	}
}
