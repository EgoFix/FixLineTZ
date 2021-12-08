package com.example.fixlinetz.controllers;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.example.fixlinetz.Main;
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
    public void start() {
        System.out.println("\"Main window\" Scene started");
    }

    @FXML
    public void initialize() {
        buttonClose.setStyle("-fx-background-color: #B22222");
        buttonStart.setStyle("-fx-background-color: #00ff00");

        System.out.println("\"Main window\" Scene initialized");

        buttonClose.setOnAction(event -> {
            Stage stage = (Stage) buttonClose.getScene().getWindow();
            stage.close();

        });
        buttonReview1.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.PDF", "*.pdf");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview1.setText(file.getAbsolutePath());
            }
            buttonStart.setStyle("-fx-background-color: #00ff00"); // green
        });
        buttonReview2.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.XLSX", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview2.setText(file.getAbsolutePath());
            }
        });
        buttonReview3.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFil = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.XLSX", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFil);
            File file = fileChooser.showOpenDialog(null);
            if (!(file == null)) {
                textReview3.setText(file.getAbsolutePath());
            }
        });


        buttonStart.setOnAction(event -> {
            Main.clearRowElementsToCleaning();

            buttonStart.setStyle("-fx-background-color: #808080"); // gray
            String str1 = textReview1.getText();//pdf
            String str2 = textReview2.getText();//input
            String str3 = textReview3.getText();//output
            if ((str1 == null) || (str2 == null) || (str3 == null)) {
                System.out.println("FISOIDOSIDps");
            } else {
                System.out.println(str1);
                System.out.println(str2);
                System.out.println(str3);
                try {

                    Main.getBot().setNames(str1, str2, str3); // записали названия документов
                    Main.getBot().AlWorkBot(); // запускаем обработку PDF
                    System.out.println("Controller - processing stopped\n");

                } catch (ParserConfigurationException e) {
                    //
                    e.printStackTrace();
                } catch (SAXException e) {
                    //
                    e.printStackTrace();
                } catch (IOException e) {
                    //
                    e.printStackTrace();
                }
            }
        });
    }

}
