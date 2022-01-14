package com.example.fixlinetz.classes;

import com.example.fixlinetz.Bot;
import com.example.fixlinetz.Main;
import com.example.fixlinetz.documents.DocumentEXCEL;
import com.example.fixlinetz.documents.DocumentPDF;
import javafx.concurrent.Task;

import java.io.IOException;

public class ToExcelSaver extends Task {

    final private String nameXLSX;
    final private String nameEndXLSX;
    private DocWord[] mass;
    final private double[] totalResult = new double[4];


    public ToExcelSaver(String nameXLSX, String nameEndXLSX, DocWord[] mass) {
        this.nameXLSX = nameXLSX;
        this.nameEndXLSX = nameEndXLSX;
        this.mass = mass;
    }

    @Override
    public Void call(){
        // метод для сохранения в Эксель
        try {
            saveToExcel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveToExcel() throws IOException {
        DocumentEXCEL docEL = new DocumentEXCEL(DocumentPDF.getNameXLSX(), DocumentPDF.getNameEndXLSX());

        for (int i = 0; i < mass.length; i++) { // проходимся по всем Word
            docEL.AccountEXCELL(i, mass[i].getWordName(), mass[i].getValue()); // пишем в Excel
            updateProgress(i + 1, mass.length);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        docEL.UpFormula(totalResult);
        docEL.AdditionFinal(totalResult, Bot.NameTZ, Bot.NumTZ);
    }

    public DocWord[] getMass() {
        return mass;
    }

    public void setMass(DocWord[] mass) {
        this.mass = mass;
    }
}
