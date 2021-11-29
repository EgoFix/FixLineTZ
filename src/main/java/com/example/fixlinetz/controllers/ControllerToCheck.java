package com.example.fixlinetz.controllers;

import com.example.fixlinetz.Main;
import com.example.fixlinetz.classes.DocWord;
import com.example.fixlinetz.classes.PanelWord;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class ControllerToCheck extends Application {

    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Объекты wind_to_check
    /////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private TreeTableView<PanelWord> leftTable;
    @FXML
    private TableView rightTable;
    @FXML
    private Button upperPaneButton1;
    Stage stage;


    public ControllerToCheck() {
        stage = new Stage();
    }

    @Override
    public void start(Stage stageToCheck) throws Exception {
        System.out.println("\"To check\" Scene started");

        BorderPane windToCheck = new BorderPane();
        windToCheck = FXMLLoader.load(getClass().getResource("windToCheck.fxml"));
        Scene to_check_scene = new Scene(windToCheck, 1280, 800);

        stageToCheck.setTitle("wind to check");
        stageToCheck.setScene(to_check_scene);
        stageToCheck.show();
        stageToCheck.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }


    @FXML
    public void initialize() {
        System.out.println("\"To check\"Scene initialized");

        // SearchTOCheck button
        upperPaneButton1.setOnAction(event -> {
            System.out.println("upperPaneButton1");
            try {
                Main.getBot().document.SearchTOCheck(Main.getBot().PDFList, Main.getBot().count);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // инициализируем колонку для номера
        TreeTableColumn<PanelWord, String> numColumn = new TreeTableColumn<>("№");
        numColumn.setPrefWidth(50);
        numColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<PanelWord, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getNum()));

        // инициализируем колонку для текста
        TreeTableColumn<PanelWord, String> textColumn = new TreeTableColumn<>("Объект");
        textColumn.setPrefWidth(250);
        textColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<PanelWord, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getText()));


        DocWord[] docs = new DocWord[5];
        DocWord doc1 = new DocWord("adasd", "2", "asdasdad", 0, 0);
        PanelWord pan1 = new PanelWord("0", doc1.getWordName());

        TreeItem<PanelWord> item = new TreeItem<PanelWord>(pan1);


        // добавили худ
        leftTable.getColumns().setAll(numColumn, textColumn);
        leftTable.setRoot(item);

        // initialize() finish
    }
    // TODO: 29.11.2021 ПРИ ПЕРЕНОСЕ СТРОКИ из ЛЕВОЙ таблицы в правую - нужно анализировать строку и
    // TODO: 29.11.2021 РАСПРЕДЕЛЯТЬ ПО ТИПАМ объектов строки в ПРАВОЙ таблице
// ControllerToCheck finish
}

