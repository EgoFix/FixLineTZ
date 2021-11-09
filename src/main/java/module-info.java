module com.example.fixlinetz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;


    opens com.example.fixlinetz to javafx.fxml;
    exports com.example.fixlinetz;
}