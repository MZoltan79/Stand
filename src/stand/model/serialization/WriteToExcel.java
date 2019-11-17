package stand.model.serialization;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import stand.model.StandData;
import stand.model.connection.Communication;

public class WriteToExcel {
	
	private static final Logger log4j = LogManager.getLogger(WriteToExcel.class.getName());

	
	private StandData standData;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private SerializationHelper sh;
	private Map<Integer, Object[]> data;
	private ArrayList<SimpleProduct> stock;
//	private String filePath;
	
	
	
	public WriteToExcel() {
		super();
		this.standData = StandData.getInstance();
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet("stand-"+standData.getToday().toString());
		this.sh = SerializationHelper.getInstance();
		this.data = new TreeMap<Integer, Object[]>();
		this.stock = sh.convertProductDataToSimpleFormat();
	}

	private void formatDecimalPoint() {
		for(SimpleProduct p: stock) {
			String nyito = replacePoints(p.getNyito());
			p.setNyito(nyito);
			if(p.getVetel() == null || p.getVetel().equalsIgnoreCase("")) {
				p.setVetel("0");
			} else {
				String vetel = replacePoints(p.getVetel());
				p.setVetel(vetel);
			}
			String ossz = replacePoints(p.getOssz());
			p.setOssz(ossz);
			String zaro = replacePoints(p.getZaro());
			p.setZaro(zaro);
			String fogyas = replacePoints(p.getFogyas());
			p.setFogyas(fogyas);
			String elozo = replacePoints(p.getElozoZaro());
			p.setElozoZaro(elozo);
			String ny_ez = replacePoints(p.getNyito_elozoZaro());
			p.setNyito_elozoZaro(ny_ez);
			
			
		}
	}
	private String replacePoints(String data) {
		if(data != null && !data.equals("")) {
			if(data.contains(".")) {
				data.replace('.', ',');
				return data;
			}
		}
		return data;
	}
	
	public boolean createSheet() {
		formatDecimalPoint();
		Collections.sort(stock);
		data.put(1, new Object[] {"Sorszám","Megnevezés","nyitó","Vétel","Össz","Záró","Fogyás","Ár","Összeg","Előző záró","Nyitó - EZ", "Eltérés"});
		for(Integer i = 0; i < stock.size(); i++) {
			Integer key = i+2;
			data.put(key,new Object[] {stock.get(i).getId(),stock.get(i).getMegnevezes(),Double.parseDouble(stock.get(i).getNyito()),
					Double.parseDouble(stock.get(i).getVetel()), Double.parseDouble(stock.get(i).getOssz()), Double.parseDouble(stock.get(i).getZaro()),
					Double.parseDouble(stock.get(i).getFogyas()),Integer.parseInt(stock.get(i).getAr()),Integer.parseInt(stock.get(i).getOsszeg()),
					Double.parseDouble(stock.get(i).getElozoZaro()),Double.parseDouble(stock.get(i).getNyito_elozoZaro()),
					Double.parseDouble(stock.get(i).getElteres())/*,novohost*/});
		}
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for(Integer key: keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj: objArr) {
				Cell cell = row.createCell(cellnum++);
				if(obj instanceof String) {
					cell.setCellValue((String)obj);
				} else if(obj instanceof Integer) {
					cell.setCellValue((Integer)obj);
				}	else if(obj instanceof Double) {
					cell.setCellValue((Double)obj);
				}
			}
		}
		try {
			File currentDir = new File(".");
			String path = currentDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length()-1) + "stand-" + standData.getToday().toString() + ".xls";
			FileOutputStream fos = new FileOutputStream(fileLocation);
			workbook.write(fos);
			fos.close();
			workbook.close();
			return true;
		} catch (FileNotFoundException e) {
			log4j.error(exceptionStackTraceToString(e));
//			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
//			e.printStackTrace();
			return false;
		}
	}
	
	public String exceptionStackTraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
	


}
