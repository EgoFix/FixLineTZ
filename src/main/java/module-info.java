module com.example.fixlinetz {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;


    opens com.example.fixlinetz to javafx.fxml;
    exports com.example.fixlinetz;
    exports com.example.fixlinetz.controllers;
    opens com.example.fixlinetz.controllers to javafx.fxml;
    exports com.example.fixlinetz.classes;
    opens com.example.fixlinetz.classes to javafx.fxml;
    exports com.example.fixlinetz.documents;
    opens com.example.fixlinetz.documents to javafx.fxml;
}