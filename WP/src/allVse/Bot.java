package allVse;

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

	public void AlWorkBot(String NamePdf, String NameDic, String NameFin) throws ParserConfigurationException, SAXException, IOException {
		String NamePDFDOC = NamePdf;
		String NameDIC = NameDic;
		String NameFIN = NameFin;
		//
		GetCharLocationAndSize GCLAS= new GetCharLocationAndSize(NamePDFDOC);
		GCLAS.SeatchTZN();
		DocumentPDF document = new DocumentPDF(NamePDFDOC, NameDIC, NameFIN);
		document.WithoutTESS();
		document.SearchTrials(PDFList);
		document.SearchTO(PDFList,count);
	}

}
