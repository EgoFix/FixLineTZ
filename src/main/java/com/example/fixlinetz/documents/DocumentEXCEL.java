package com.example.fixlinetz.documents;


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


    public void AccountEXCELL(int i, String name, int value) throws IOException {
        FileInputStream inputStream = new FileInputStream(NameXLSX);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(4); //выбрали лист, куда пишем

        XSSFCell cell0 = sheet.getRow(i + 16).getCell(0); // получили ссылку на ячейку с WordR
        XSSFCell cell1 = sheet.getRow(i + 16).getCell(1); // получили ссылку на ячейку с кол-вом WordR

        cell0.setCellValue(name); // пишем WordR
        cell1.setCellValue(value); // пишем кол-во WordR

        inputStream.close();
        FileOutputStream out = new FileOutputStream(NameXLSX);
        workbook.write(out);
        out.close();
//        System.out.println("Обновления завершены. Изменения сохранены");
        workbook.close();
    }


    public void UpFormula(double mass[]) throws IOException{
        FileInputStream inputStream = new FileInputStream(NameXLSX);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(4);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        System.out.println("Обновляем значения формул в основной таблице."); //заполняется лист "Справка по затратам СДКУ"
        for (int i = 16; i < 49; i++) {
            for (int j = 4; j < 14; j++) {
                Cell cell = sheet.getRow(i).getCell(j);
                if (cell.getCellType() == CellType.FORMULA) {
                    String S = cell.getCellFormula();
                    evaluator.evaluateFormulaCell (cell);
//                    cell.setCellValue("Знач");
                    cell.setCellFormula(S);
                }
            }
        }

        System.out.println("Обновляем значения формул в таблице итогов."); // заполняется на листе "Справка по затратам СДКУ" таблица "ИТОГ!"
        for (int i = 16; i < 20; i++) {
            Cell cell1 = sheet.getRow(16).getCell(i);
            if (cell1.getCellType() == CellType.FORMULA) {
                String S1 = cell1.getCellFormula();
                evaluator.evaluateFormulaCell (cell1);
                cell1.setCellFormula(S1);
                mass[i - 16] = cell1.getNumericCellValue();
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
        cell.setCellValue(NameTZ + ". " + NumberTZ);
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


