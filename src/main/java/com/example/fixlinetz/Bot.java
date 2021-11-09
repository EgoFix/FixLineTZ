package com.example.fixlinetz;


import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Bot {
    public static String NameTZ;
    public static String NumTZ;
    public static ArrayList<String> PDFList = new ArrayList<>();
    public ArrayList<String> ListTrials = new ArrayList<>();
    public ArrayList<String> ListTO = new ArrayList<>();
    public static int count;
    public static int countLast;
    public static int countSave;

    //функция для запуска обработки файлов
    public void AlWorkBot(String NamePdf, String NameDic, String NameFin) throws ParserConfigurationException, SAXException, IOException {
//        String NamePDFDOC = NamePdf; //входящий
//        String NameDIC = NameDic; //словарь
//        String NameFIN = NameFin; //выходящий

        String NamePDFDOC = "D:\\\\Файлы для ТЗ\\\\Г.9.0000.20148-ТУР_ГТП-500.000-ЕСДУ.ТЗ.pdf"; //входящий
        String NameDIC = "D:\\\\Файлы для ТЗ\\\\Таблица для расчётов доработок.xlsx"; //словарь
        String NameFIN = "D:\\\\Файлы для ТЗ\\\\Таблица для расчётов доработок.xlsx"; //выходящий

        //хз, вроде начало и конец документа определяет
        GetCharLocationAndSize GCLAS= new GetCharLocationAndSize(NamePDFDOC);
        GCLAS.SearchTZN();
        //конструктором класса создаем объект с заполненными полями
        DocumentPDF document = new DocumentPDF(NamePDFDOC, NameDIC, NameFIN);

        System.out.println("document.WithoutTESS() - start");
//        document.WithoutTESS(); // создает текстовый файл в котором все данные из pdf-ки
        System.out.println("document.WithoutTESS() - stop");

        System.out.println("document.SearchTrials(PDFList) - start");
        document.SearchTrials(PDFList);
        System.out.println("document.SearchTrials(PDFList) - stop");

        System.out.println("document.SearchTO(PDFList,count) - start");
        document.SearchTO(PDFList,count);
        System.out.println("document.SearchTO(PDFList,count) - stop");
    }

}
