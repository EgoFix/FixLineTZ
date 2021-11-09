package com.example.fixlinetz;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button buttonReview1;

    @FXML
    private TextField textReview1;

    @FXML
    private Button buttonReview2;

    @FXML
    private TextField textReview2;

    @FXML
    private Button buttonReview3;

    @FXML
    private TextField textReview3;

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonClose;

    @FXML
    void initialize() {
        buttonClose.setOnAction(event->{
            Stage stage = (Stage) buttonClose.getScene().getWindow();
            stage.close();
        });
        buttonReview1.setOnAction(event->{
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("PDF files (*.pdf)","*.PDF","*.pdf");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview1.setText(file.getAbsolutePath());
            }
        });
        buttonReview2.setOnAction(event->{
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.XLSX", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview2.setText(file.getAbsolutePath());
            }
        });
        buttonReview3.setOnAction(event->{
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.XLSX", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview3.setText(file.getAbsolutePath());
            }
        });
        buttonStart.setOnAction(event->{

            String str1 = textReview1.getText();
            String str2 = textReview2.getText();
            String str3 = textReview3.getText();
            if ((str1==null) || (str2==null) || (str3==null) ) {
                System.out.println("FISOIDOSIDps");
            }
            else {
                System.out.println(str1);
                System.out.println(str2);
                System.out.println(str3);
                Bot bot = new Bot();
                try {
                    bot.AlWorkBot(str1, str2, str3);
                    System.out.println("ВЩТУ");
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}
