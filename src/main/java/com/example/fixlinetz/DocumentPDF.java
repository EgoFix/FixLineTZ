package com.example.fixlinetz;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.commons.lang3.ArrayUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DocumentPDF {
    private static String namePDF;
    public static String nameXLSX;
    public static String nameEndXLSX;

    //public static String ContentPDF;
    public double totalResult[] = new double[4];
    private int count, countLast, countSave;

    public DocumentPDF(String NamePdf, String NameXlsx, String NameFin) {
        namePDF = NamePdf;
        nameXLSX = NameXlsx;
        nameEndXLSX = NameFin;
    }

    //СОДЕРЖИМОЕ (БЕЗ ОКР)
    public void WithoutTESS() {
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;
        File file = new File(namePDF);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            int count1 = pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(3);// установили начало обработки документа
            pdfStripper.setEndPage(count1);// установили конец
            String ContentPDF = pdfStripper.getText(pdDoc); // хранит все данные из файла
            FileWriter writer = new FileWriter("ContentOfPDF.txt", false); //файл для хранения всего
            writer.write(ContentPDF);
            writer.flush();
            writer.close();
            System.out.println("Запись завершена!");
            randomAccessFile.close();
        } catch (IOException e) {
            //catch
        }
    }


    //ПОИСК ИСПЫТАНИЙ
    public void SearchTrials(ArrayList<String> PDFList) throws FileNotFoundException {
        File ContentOF = new File("ContentOfPDF.txt");
        int k = 0;
        count = 0;
        Scanner Scan = new Scanner(ContentOF);
        countLast = 0;
        countSave = 0;
        while (Scan.hasNextLine()) {
            count++;
            String str = Scan.nextLine();
            PDFList.add(str);
            if (str.contains("Требования")) {
                if (str.contains(" к ")) {
                    if (str.contains("испытания")) {
                        k++;
                    }
                }
            }
            if (k == 1) {
                countSave = count;
            }
            k = 0;
        }
        Scan.close();

        String str;
        for (int i = 0; i < count; i++) {
            str = PDFList.get(i);
            if (str.contains("Испытания")) {
                if (str.contains("несколько")) {
                    if (str.contains("этап")) {
                        k++;
                    }
                }
            }
            if (k == 1) {
                countLast = i;
            }
            k = 0;
        }
        //str="";
        int j, i;
        for (i = 0; i < count; i++) {
            //str = PDFList.get(i);
            if (i == countLast) {
                if (PDFList.get(i + 1).contains("- ")) {
                    for (j = countLast + 1; j < count; j++) {
                        if (PDFList.get(j).contains("- ")) {
                            System.out.println("Найдено в строке: " + PDFList.get(j) + "(" + j + ")");
                        } else break;
                    }
                } else {
                    if (!PDFList.get(i + 1).contains("- ")) {
                        for (j = countLast + 1; j < count; j++) {
                            if (PDFList.get(j).contains("- ")) {
                                countSave = j;
                                break;
                            }
                        }
                        for (j = countSave; j < count; j++) {
                            if (PDFList.get(j).contains("- ")) {
                                System.out.println("Найдено в строке: " + PDFList.get(j) + "(" + j + ")");
                            } else break;
                        }
                    }
                }
            }
        }
        Bot.count = count;
    }


    //ПОИСК ТО
    public void SearchTO(ArrayList<String> PDFList, int count) throws ParserConfigurationException, SAXException, IOException {
        int i, j;
        int k = 0;
        ArrayList<Integer> NumDIC = new ArrayList<>();
        String str;
        for (i = 0; i < count; i++) {
            str = PDFList.get(i);
            if (str.contains("Перечень")) {
                if (str.contains("сигналов") || str.contains("параметров")) {
                    k++;
                }
            }
            if (k == 1) {
                countLast = i;
            }
            k = 0;
        }
        int n = 0;
        // выводим в консоль
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("dictionary.xml")); // словарь
        Element docElem = document.getDocumentElement(); // заполняем из словаря
        NodeList docElemNodes = docElem.getElementsByTagName("WordR"); // заполняем docElemNodes полями из словаря name/value по тегу WordR

        int valueWR = 0;
        String nameWordR;
        String valueWordR;
        ArrayList<String> rowElementsToClining = new ArrayList<String>(); //пустой массив для строк, которые совпадают по паттерну

        for (i = 0; i < docElemNodes.getLength(); i++) {
            Node DocItem = docElemNodes.item(i);
            NamedNodeMap attributes = DocItem.getAttributes();
            nameWordR = attributes.getNamedItem("name").getNodeValue();
            valueWordR = attributes.getNamedItem("value").getNodeValue();
            Pattern p = Pattern.compile("(?i).*?\\b" + nameWordR + "*№\\b.*?");
            for (j = countLast + 1; j < count; j++) {
                str = PDFList.get(j);
                Matcher m = p.matcher(str);
                if (m.find()) {
                    valueWR = Integer.parseInt(valueWordR);
                    System.out.println(nameWordR + "--" + j + "--" + valueWordR + "--" + p + "--" + str);
                    rowElementsToClining.add(str);
                }
            }
            p = Pattern.compile("(?i).*?\\b" + nameWordR + ".№\\b.*?");
            for (j = countLast + 1; j < count; j++) {
                str = PDFList.get(j);
                Matcher m = p.matcher(str);
                if (m.find()) {
                    valueWR = Integer.parseInt(valueWordR);
                    System.out.println(nameWordR + "--" + j + "--" + valueWordR + "--" + p + "--" + str);
                    rowElementsToClining.add(str);
                }
            }
            if (!(valueWR == 0)) {
                NumDIC.add(valueWR);
                n++;
            }
            valueWR = 0;
        }

        deleteDuplicate(rowElementsToClining); //вычищаем повторяющиеся элементы
        Collections.sort(rowElementsToClining); //сортируем в алфавитном порядке
        deleteContains(rowElementsToClining); //вычищаем элементы, которые содержат повторяющиеся элементы
        System.out.println("rowElementsToClining:\n" + rowElementsToClining);

/*
         // здесь хуйня какая-то произошла
        // выводим в EXCEL
        DocumentEXCEL docEL = new DocumentEXCEL(nameXLSX, nameEndXLSX);
        NodeList docElemNodes1 = docElem.getElementsByTagName("Word");
        for (i = 0; i < docElemNodes1.getLength(); i++) {
            Node DocItem1 = docElemNodes1.item(i);
            NamedNodeMap attributes1 = DocItem1.getAttributes();
            String nameOWord = null;
            String nameWord = attributes1.getNamedItem("name").getNodeValue();
            String valueWord = attributes1.getNamedItem("value").getNodeValue();
            valueWR = Integer.parseInt(valueWord);
            for (j = 0; j < n; j++) {
                if (NumDIC.get(j) == valueWR) {
                    nameOWord = nameWord;
                }
            }
            if (!(nameOWord == null)) {
                docEL.AccountEXCELL(i, nameOWord);
            }
        }
        docEL.UpFormula(totalResult);
        docEL.AdditionFinal(totalResult, Bot.NameTZ, Bot.NumTZ);
        */
    }


    public void deleteDuplicate(ArrayList<String> myArray) {
        for (int i = 0; i < myArray.size() - 1; i++) {
            for (int j = i + 1; j < myArray.size(); j++) {
                if (myArray.get(i).contains(myArray.get(j))) {
                    myArray.remove(j);
                }
            }
        }
    }


    public void deleteContains(ArrayList<String> myArray) {
        for (int i = 0; i < myArray.size() - 1; i++) {
            for (int j = i + 1; j < myArray.size(); j++) {
                if (myArray.get(j).contains(myArray.get(i))) {
                    myArray.remove(j);
                }
            }
        }
    }


//конец класса DocumentPDF
}
