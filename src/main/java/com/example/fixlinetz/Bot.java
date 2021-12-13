package com.example.fixlinetz;


import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import com.example.fixlinetz.documents.DocumentPDF;
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

    //вынесли из функции для дальнейшего использования
    public static String NamePdf;
    public static String NameDic;
    public static String NameFin;
    public DocumentPDF document;


    public Bot() {
        //
    }

    public void setNames(String NamePDF, String NameDIC, String NameFIN){
        this.NamePdf = NamePDF;
        this.NameDic = NameDIC;
        this.NameFin = NameFIN;
    }


    //функция для запуска обработки файлов
    public void AlWorkBot() throws ParserConfigurationException, SAXException, IOException {
        System.out.println("Main.getBot().AlWorkBot() - start");
        String NamePDFDOC = this.NamePdf; //входящий
//        String NameDIC = this.NameDic; //словарь
//        String NameFIN = this.NameFin; //выходящий

//        String NamePDFDOC = "D:\\\\Файлы для ТЗ\\\\Г.9.0000.20148-ТУР_ГТП-500.000-ЕСДУ.ТЗ.pdf"; //входящий
//        String NamePDFDOC = "D:\\\\Файлы для ТЗ\\\\Г.9.0000.22179-ТЗС_ГТП-00.000-ЕСДУ.ТЗ1.pdf"; //входящий
        String NameDIC = "D:\\\\Файлы для ТЗ\\\\Таблица для расчётов доработок.xlsx"; //словарь
        String NameFIN = "D:\\\\Файлы для ТЗ\\\\Таблица для расчётов доработок.xlsx"; //выходящий

        //хз, вроде начало и конец документа определяет
        GetCharLocationAndSize GCLAS = new GetCharLocationAndSize(NamePDFDOC);
        GCLAS.SearchTZN();

        //конструктором класса создаем объект с заполненными полями
        this.document = new DocumentPDF(NamePDFDOC, NameDIC, NameFIN);
        this.document.WithoutTESS(); // создает текстовый файл в котором все данные из pdf-ки
        this.document.SearchTrials(PDFList); // ищет испытания, при нахождении выводит строку и испытание
        //this.document.SearchTOCheck(PDFList, count); // ищет технологические объекты и выводит при совпадении со словарем
        System.out.println("Main.getBot().AlWorkBot() - stop");
    }

}
