package allVse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class GetCharLocationAndSize extends PDFTextStripper {
	private static String namePDF;
	public static String ContentPDF;
	private String NameTZ="";
	private String NumTZ="";
	
		 public GetCharLocationAndSize(String NamePdf) throws IOException {
			 namePDF = NamePdf;
			}
		 
		 public GetCharLocationAndSize() throws IOException {

			}

		public void SeatchTZN() throws IOException {
		        PDDocument document = null;
		        try {
		            document = PDDocument.load( new File(namePDF));
		            PDFTextStripper stripper = new GetCharLocationAndSize();
		            stripper.setSortByPosition( true );
		            stripper.setStartPage(1);
		            stripper.setEndPage(1);
		            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
		            stripper.writeText(document, dummy);
		        }
		        finally {
		            if( document != null ) {
		                document.close();
		            }
		        }
		    }
		@Override
		protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
	        for (TextPosition text : textPositions) {
	        	if ((text.getY()>250 && text.getY()<350) && (text.getFontSize()>14.5 && text.getFontSize()<19)) {
		        	NameTZ=NameTZ+text.getUnicode();
	        	}
	        	if (text.getY()>450 && text.getY()<590) {
		        	NumTZ=NumTZ+text.getUnicode();
		        	}
	        }
	        Bot.NameTZ = this.NameTZ;
	        Bot.NumTZ = this.NumTZ;
	    }
}
