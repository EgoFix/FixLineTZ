package allVse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	
	public static String ContentPDF;
	public double totalResult[] = new double[4];
	private int count,countLast,countSave;
	
	public DocumentPDF(String NamePdf, String NameXlsx, String NameFin) {
		namePDF = NamePdf;
		nameXLSX = NameXlsx;
		nameEndXLSX = NameFin;	
	}
	
	//СОДЕРЖИМОЕ (БЕЗ ОКР)
	public void WithoutTESS()
	{
	        PDFTextStripper pdfStripper = null;
	        PDDocument pdDoc = null;
	        COSDocument cosDoc = null;
	        File file = new File(namePDF);
	        try { 
	            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
	            PDFParser parser = new PDFParser(randomAccessFile);
	            parser.parse();
	            cosDoc = parser.getDocument();
	            pdfStripper = new PDFTextStripper();
	            pdDoc = new PDDocument(cosDoc);
	            int count1 = pdDoc.getNumberOfPages();
	            pdfStripper.setStartPage(3);
	            pdfStripper.setEndPage(count1);
	            String ContentPDF = pdfStripper.getText(pdDoc);
	            FileWriter writer = new FileWriter("ContetnOfPDF.txt", false);
	            writer.write(ContentPDF);
	            writer.flush();
	            writer.close();
	            System.out.println("Запись завершена!");
	            randomAccessFile.close();
	        } catch (IOException e) {
	            
	        }       
	}
	//ПОИСК ИСПЫТАНИЙ
	public void SearchTrials(ArrayList<String> PDFList) throws FileNotFoundException  {
    	File ContentOF = new File("ContetnOfPDF.txt");
    	int k=0;
    	count=0;
    	Scanner Scan = new Scanner(ContentOF);
    	countLast=0;
    	countSave=0;
    	while(Scan.hasNextLine()){
    		count++;
    		String str = Scan.nextLine();
    		PDFList.add(str);
    		if (str.contains("Требования") == true) {
    		    	if (str.contains(" к ") == true) {
    		    		if (str.contains("испытания") == true) {
    		    			k++;
    		    			}
    		    		}
    		   	}
    		   if (k==1) {
    			   countSave=count;
    		   }
    		   k=0;
    	}
    	Scan.close();
    	String str;
    	for (int i=0; i<count; i++) {
    		str = PDFList.get(i);
    		if (str.contains("Испытания") == true) {
    			if (str.contains("несколько") == true) {
    				if (str.contains("этап") == true) {
    					k++;
    				}
    			}
    		}
 		   if (k==1) {
			   countLast=i;
 		   }
 		   k=0;
    	}
    	str="";
    	int j,i;
    	for (i=0; i<count; i++) {
    		str = PDFList.get(i);
    		if (i==countLast) {
    			if (PDFList.get(i+1).contains("- ")) {
    				for (j=countLast+1; j<count; j++) {
    					if(PDFList.get(j).contains("- ")) {
    						System.out.println("Найдено в строке: "+PDFList.get(j)+"("+j+")");
    					}
    					else break;
    				}
    			}
    			else {
    				if (!PDFList.get(i+1).contains("- ")) {
    					for (j=countLast+1; j<count; j++) {
    						if (PDFList.get(j).contains("- ")) {
    							countSave=j;
    							break;
    						}
    					}
    					for (j=countSave; j<count; j++) {
        					if(PDFList.get(j).contains("- ")) {
        						System.out.println("Найдено в строке: "+PDFList.get(j)+"("+j+")");
        					}
        					else break;
    					}
    				}
    			}
    		}
    	}
    	Bot.count = count;
	}
	
	//ПОИСК ТО
	public void SearchTO(ArrayList<String> PDFList, int count) throws ParserConfigurationException, SAXException, IOException {
		int i,j;
		int k=0;
		ArrayList<Integer> NumDIC = new ArrayList<Integer>();
		String str = null;
		for (i=0;i<count; i++) {
    		str = PDFList.get(i);
			if (str.contains("Перечень") == true) {
		    	if (str.contains("сигналов") == true || str.contains("параметров") == true) {
		    			k++;
		    		}
		    	}
		    if (k==1) {
		    	countLast = i;
    		}
		    k=0;
		}
		int n=0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	Document document = builder.parse(new File("dictionary.xml"));
    	Element docElem = document.getDocumentElement();
    	NodeList docElemNodes = docElem.getElementsByTagName("WordR");
    	int valueWR = 0;
    	String nameWordR = null;
    	String valueWordR;	
    	for (i=0; i<docElemNodes.getLength(); i++) {
    		Node DocItem = docElemNodes.item(i);
    		NamedNodeMap attributes = DocItem.getAttributes();
    		nameWordR = new String(attributes.getNamedItem("name").getNodeValue());
    		valueWordR = new String(attributes.getNamedItem("value").getNodeValue());
    		Pattern p = Pattern.compile("(?i).*?\\b"+nameWordR+"\\b.*?");
    		for (j=countLast+1;j<count;j++) {
        		str = PDFList.get(j);
        		Matcher m = p.matcher(str);
        		if (m.find()==true) {
        			valueWR = Integer.parseInt(valueWordR);
        			System.out.println(nameWordR+"--"+j+"--"+valueWordR);
        		}
    		}
    		if(!(valueWR==0)) {
    			NumDIC.add(valueWR);
    			n++;
    		}
    		valueWR=0;
    	}
    	DocumentEXCEL docEL = new DocumentEXCEL(nameXLSX,nameEndXLSX);
    	NodeList docElemNodes1 = docElem.getElementsByTagName("Word");
    	for (i=0; i<docElemNodes1.getLength(); i++) {
    		Node DocItem1 = docElemNodes1.item(i);
    		NamedNodeMap attributes1 = DocItem1.getAttributes();
    		String nameOWord = null;
    		String nameWord = new String(attributes1.getNamedItem("name").getNodeValue());
    		String valueWord = new String(attributes1.getNamedItem("value").getNodeValue());
    		valueWR = Integer.parseInt(valueWord);
    		for (j=0; j<n;j++) {
    			if (NumDIC.get(j)==valueWR) {
    				nameOWord = nameWord;
    			}
    		}
    		if (!(nameOWord == null)) {
    			docEL.AccountEXCELL(i,nameOWord);
    		}
    	}
    	docEL.UpFormula(totalResult);
    	docEL.AdditionFinal(totalResult,Bot.NameTZ,Bot.NumTZ);
    }

}


