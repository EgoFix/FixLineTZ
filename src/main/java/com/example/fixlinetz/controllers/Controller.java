package com.example.fixlinetz.controllers;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.example.fixlinetz.Main;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;

import javafx.fxml.FXML;
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
    private Label calcLabel;
    @FXML
    private Text InsertDocPi;

    public Label getCalcLabel() {
        return calcLabel;
    }

    @FXML
    public void start() {
        System.out.println("\"Main window\" Scene started");
    }

    @FXML
    public void initialize() {
        buttonClose.setStyle("-fx-background-color: #B22222");
        buttonStart.setStyle("-fx-background-color: #00ff00");
        InsertDocPi.visibleProperty().setValue(false); // видимость состояния работы "готово"
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
                Main.getBot().setNames(str1, str2, str3); // записали названия документов

                final Task AlWorkBotTask;
                AlWorkBotTask = new Task() { // задача для выполнения потоком
                    @Override
                    protected Object call() throws Exception {
                        Main.getBot().AlWorkBot(); // запускаем обработку PDF
                        InsertDocPi.visibleProperty().setValue(true); // видимость состояния работы "готово"
                        return null;
                    }
                };
                Thread AlWorkBotThread = new Thread(AlWorkBotTask); // инициализация потока
                AlWorkBotThread.setDaemon(true); // установка приоритета
                AlWorkBotThread.start(); // запуск потока

                System.out.println("Controller - processing stopped\n");

            }
        });
    }

}
