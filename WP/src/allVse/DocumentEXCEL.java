package allVse;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DocumentEXCEL {
	
	private String NameXLSX;
	private String NameEndXLSX;
	
	public DocumentEXCEL(String NameXLSX, String NameEndXLSX) {
		this.NameXLSX = NameXLSX;
		this.NameEndXLSX = NameEndXLSX;		
	}
	
	public void AccountEXCELL(int i,String name) throws IOException {
	       File file = new File(NameXLSX);
	       FileInputStream inputStream = new FileInputStream(file);
	       XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	       XSSFSheet sheet = workbook.getSheetAt(4);
	       XSSFCell cell1 = sheet.getRow(i+16).getCell(0);
	       XSSFCell cell2 = sheet.getRow(i+16).getCell(1);
	       String nameS = cell1.getStringCellValue();
	       System.out.println("ЭКСЕЛЬ - "+nameS+"XML - "+name);
	       if (nameS.contains(name)==true) {
	    	   cell2.setCellValue(1);
	    	   System.out.println("В УСЛОВИИИ");
	       }
	       inputStream.close();
	       FileOutputStream out = new FileOutputStream(file);
	       workbook.write(out);
	       out.close();
	       workbook.close();
	}
	public void UpFormula(double mass[]) throws IOException{
		FileInputStream inputStream = new FileInputStream(NameXLSX);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(4);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		System.out.println("Обновляем значения формул в основной таблице.");
		for (int i=16; i<49; i++) {
			for (int j=4; j<14;j++) {
			Cell cell = sheet.getRow(i).getCell(j);
			if (cell.getCellType()==CellType.FORMULA) {
							String S = cell.getCellFormula();
							evaluator.evaluateFormulaCell (cell);
							cell.setCellFormula(S);
				}
			}
		}
		System.out.println("Обновляем значения формул в таблице итогов.");
		for (int i=16; i<20; i++) {
			Cell cell1 = sheet.getRow(16).getCell(i);
			if (cell1.getCellType()==CellType.FORMULA) {
				String S1 = cell1.getCellFormula();
				evaluator.evaluateFormulaCell (cell1);
				cell1.setCellFormula(S1);
				mass[i-16]=cell1.getNumericCellValue();
			}
		}
		inputStream.close();
	    FileOutputStream out = new FileOutputStream(NameXLSX);
	    workbook.write(out);
	    out.close();
	    System.out.println("Обновления завершены. Изменения сохранены");
	    workbook.close();
	}
	public void AdditionFinal(double mass[], String NameTZ, String NumberTZ) throws IOException{
		System.out.println("Запись данных в итоговую таблицую");
		FileInputStream inputStream = new FileInputStream(NameEndXLSX);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(2);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Cell cell = sheet.getRow(10).getCell(3);
		cell.setCellValue(NameTZ+". "+NumberTZ);
		cell = sheet.getRow(10).getCell(5);
		cell.setCellValue(mass[1]);
		cell = sheet.getRow(10).getCell(6);
		cell.setCellValue(mass[0]);
		cell = sheet.getRow(10).getCell(11);
		cell.setCellValue(mass[2]);
		cell = sheet.getRow(10).getCell(15);
		String S1 = cell.getCellFormula();
		evaluator.evaluateFormulaCell (cell);
		cell.setCellFormula(S1);
		inputStream.close();
		FileOutputStream out = new FileOutputStream(NameEndXLSX);
	    workbook.write(out);
	    out.close();
	    System.out.println("Данные сохранены в итоговую таблицу.");
	    workbook.close();
	}
}
