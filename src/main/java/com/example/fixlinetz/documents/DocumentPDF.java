package com.example.fixlinetz.documents;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.example.fixlinetz.Bot;
import com.example.fixlinetz.classes.DocWord;
import com.example.fixlinetz.controllers.ControllerToCheck;
import com.example.fixlinetz.documents.DocumentEXCEL;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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

    private DocWord mass;

    public DocumentPDF(String NamePdf, String NameXlsx, String NameFin) {
        namePDF = NamePdf;
        nameXLSX = NameXlsx;
        nameEndXLSX = NameFin;
    }

    //СОДЕРЖИМОЕ (БЕЗ ОКР)
    public void WithoutTESS() {
        System.out.println("document.WithoutTESS() - start");
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
            pdDoc.close();

            //

        } catch (IOException e) {
            //catch
        }
        System.out.println("document.WithoutTESS() - stop");
    }


    //ПОИСК ИСПЫТАНИЙ
    public void SearchTrials(ArrayList<String> PDFList) throws FileNotFoundException {
        System.out.println("document.SearchTrials(PDFList) - start");
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
        System.out.println("document.SearchTrials(PDFList) - stop");
    }

    //ПОИСК ТО ДЛЯ ПРОВЕРКИ
    public void SearchTOCheck(ArrayList<String> PDFList, int count) throws ParserConfigurationException, SAXException, IOException {
        System.out.println("document.SearchTOCheck(PDFList,count) - start");
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
        DocumentBuilder builder = factory.newDocumentBuilder(); // получили билдер, который парсит XML и создает структуру документа
        Document document = builder.parse(new File("dictionary.xml")); // запарсили XML, создав структуру Document
        Element docElem = document.getDocumentElement(); // получаем тип Word из словаря
        NodeList docElemNodes = docElem.getElementsByTagName("WordR"); // заполняем docElemNodes полями словаря name/value по тегу WordR

        int valueWR = 0;
        String nameWordR;
        String valueWordR;
        ArrayList<String> rowElementsToCleaning = new ArrayList<String>(); //пустой массив для строк, которые совпадают по паттерну

        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //              Здесь начинается сборка массива для обработки\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

        for (i = 0; i < docElemNodes.getLength(); i++) {
            Node DocItem = docElemNodes.item(i);
            NamedNodeMap attributes = DocItem.getAttributes();
            nameWordR = attributes.getNamedItem("name").getNodeValue(); // получаем родительский элемент по типу (Word)
            valueWordR = attributes.getNamedItem("value").getNodeValue(); // получаем номер типа элемента из родительского (WordR)
            makeRowElementsToCleaning(PDFList, rowElementsToCleaning, valueWR, nameWordR, valueWordR, "(?i).*?\\b" + nameWordR + "*\\b.*?"); //

            makeRowElementsToCleaning(PDFList, rowElementsToCleaning, valueWR, nameWordR, valueWordR, "(?i).*?\\b" + nameWordR + ".№\\b.*?"); //задвижки
            System.out.println(nameWordR);
            if (!(valueWR == 0)) {
                NumDIC.add(valueWR);
                n++;
            }
            valueWR = 0;
        }
    }

    //ПОИСК ТО
    public void SearchTO(ArrayList<String> PDFList, int count) throws ParserConfigurationException, SAXException, IOException {
        System.out.println("document.SearchTO(PDFList,count) - start");
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
        DocumentBuilder builder = factory.newDocumentBuilder(); // получили билдер, который парсит XML и создает структуру документа
        Document document = builder.parse(new File("dictionary.xml")); // запарсили XML, создав структуру Document
        Element docElem = document.getDocumentElement(); // получаем тип Word из словаря
        NodeList docElemNodes = docElem.getElementsByTagName("WordR"); // заполняем docElemNodes полями словаря name/value по тегу WordR

        int valueWR = 0;
        String nameWordR;
        String valueWordR;
        ArrayList<String> rowElementsToCleaning = new ArrayList<String>(); //пустой массив для строк, которые совпадают по паттерну

        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //              Здесь начинается сборка массива для обработки\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

        for (i = 0; i < docElemNodes.getLength(); i++) {
            Node DocItem = docElemNodes.item(i);
            NamedNodeMap attributes = DocItem.getAttributes();
            nameWordR = attributes.getNamedItem("name").getNodeValue(); // получаем родительский элемент по типу (Word)
            valueWordR = attributes.getNamedItem("value").getNodeValue(); // получаем номер типа элемента из родительского (WordR)
            makeRowElementsToCleaning(PDFList, rowElementsToCleaning, valueWR, nameWordR, valueWordR, "(?i).*?\\b" + nameWordR + "*№\\b.*?"); //

            makeRowElementsToCleaning(PDFList, rowElementsToCleaning, valueWR, nameWordR, valueWordR, "(?i).*?\\b" + nameWordR + ".№\\b.*?"); //задвижки
            System.out.println(nameWordR);
            if (!(valueWR == 0)) {
                NumDIC.add(valueWR);
                n++;
            }
            valueWR = 0;
        }

        // для панельки toCheck дальнейшая обработка не требуется

        deleteDuplicate(rowElementsToCleaning); //вычищаем повторяющиеся элементы
        Collections.sort(rowElementsToCleaning); //сортируем в алфавитном порядке
        deleteContains(rowElementsToCleaning); //вычищаем элементы, которые содержат повторяющиеся элементы


        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //              Здесь начинается обработка собранного массива в соответствии со словарем\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");


        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //                              Здесь начинается обработка по типу Word\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

        docElemNodes = docElem.getElementsByTagName("Word"); // получаем только типы (Word)
        System.out.println("docElemNodes.getLength = " + docElemNodes.getLength());
        DocWord[] mass = new DocWord[docElemNodes.getLength()];

        for (i = 0; i < docElemNodes.getLength(); i++) {
            Node node = docElemNodes.item(i);
            System.out.println("Найден элемент: " + node.getAttributes().getNamedItem("name") + ", его атрибут: " + node.getAttributes().getNamedItem("size"));

            // формируем массив типа DocWord для формирования рабочего словаря, записываем параметры первого уровня
            mass[i] = new DocWord(String.valueOf(node.getAttributes().getNamedItem("name")),
                    String.valueOf(node.getAttributes().getNamedItem("size")));
        }
        System.out.println("mass.length = " + mass.length);

        for (DocWord word : mass) {
            System.out.println(word.toString());
        }

        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //                              Здесь начинается обработка по типу WordR\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

        docElemNodes = docElem.getElementsByTagName("WordR"); // получаем только типы (Word)
        System.out.println("docElemNodes.getLength =  " + docElemNodes.getLength());

        for (k = 0; k < docElemNodes.getLength(); ) { // перебираем строки
            for (i = 0; i < mass.length; i++) { // проходим по всем типам
                for (j = 0; j < mass[i].getSize(); j++, k++) { // перебираем WordR
                    Node node = docElemNodes.item(k);
                    mass[i].setWordRname(String.valueOf(node.getAttributes().getNamedItem("name")), j);
                }
            }
        }

        for (DocWord word : mass) {
            System.out.println(word.toString());
        }

        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //                            Здесь начинается подсчет уникальных элементов\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

        for (i = 0; i < mass.length; i++) { // проходим по всем типам
            int hitCounter = 0;
            for (k = 0; k < rowElementsToCleaning.size(); k++) { // перебираем строки
                int tempCounter = 0;
                for (j = 0; j < mass[i].getSize(); j++) { // перебираем WordR
                    String s = rowElementsToCleaning.get(k);
                    String w = mass[i].getWordRname(j);
                    if (s.contains(w)) {
                        tempCounter++;
                    }
                }
                if (tempCounter > 0)
                    hitCounter++;
            }
            // выводим количество элементов в соответствии с их типом
//            System.out.println("mass[" + (i + 1) + "] " + mass[i].getWordName() + " hitCounter = " + hitCounter);
            mass[i].setValue(hitCounter);
        }

        for (DocWord word : mass) {
            System.out.println(word.toString());
        }

        System.out.println("rowElementsToCleaning:\n" + rowElementsToCleaning);
        // заменяем элементы в соответствии с их типом в словаре

        System.out.println(
                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                        "        //                    Здесь начинается вывод количества уникальных элементов в Excel\n" +
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");


        DocumentEXCEL docEL = new DocumentEXCEL(nameXLSX, nameEndXLSX);
        for (i = 0; i < mass.length; i++) { // проходимся по всем Word
            docEL.AccountEXCELL(i, mass[i].getWordName(), mass[i].getValue()); // пишем в Excel
        }
        System.out.println("Записано");
        docEL.UpFormula(totalResult);
        docEL.AdditionFinal(totalResult, Bot.NameTZ, Bot.NumTZ);

        System.out.println("document.SearchTO(PDFList,count) - stop");
        //конец функции SearchTO()

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


    public void makeRowElementsToCleaning(ArrayList<String> PDFList,// файл для обработки
                                          ArrayList<String> rowArray, // массив для объектов
                                          int valueWR,
                                          String nameWordR, // переменная имени типа объекта
                                          String valueWordR, // номер типа объекта
                                          String pattern) { //паттерн поиска объекта в файле
        Pattern p = Pattern.compile(pattern);
        for (int j = countLast + 1; j < count; j++) {
            String str = PDFList.get(j);// получаем строку из файла
            Matcher m = p.matcher(str);// формируем паттерн
//            System.out.println(p);
            if (m.find()) {
                //valueWR = Integer.parseInt(valueWordR); //переводим номер типа элемента в int
                System.out.println(nameWordR + "--" + j + "--" + valueWordR + "--" + p + "--" + str);
                rowArray.add(str); // добавляем элементы в массив по паттерну
            }
        }
    }


//конец класса DocumentPDF
}
