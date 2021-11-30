package com.example.fixlinetz.controllers;

import com.example.fixlinetz.Main;
import com.example.fixlinetz.classes.DocWord;
import com.example.fixlinetz.classes.PanelWord;
import com.example.fixlinetz.documents.DocumentPDF;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
        //        stage = new Stage();
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TreeTableView<PanelWord> getLeftTable() {
        return leftTable;
    }



    @Override
    public void start(Stage stageToCheck) throws Exception {
        System.out.println("\"To check\" Scene started");
    }

    @FXML
    public void initialize() {
        System.out.println("\"To check\" Scene initialized");

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
            for (int i = 0; i < Main.getRowElementsToCleaning().size(); i++){
                setRootLeftTable(i,Main.getRowElementsToCleaning().get(i));
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

        // добавили худ
        leftTable.getColumns().setAll(numColumn, textColumn);

        // установили рут ветку в которую будем добавлять child
        PanelWord pan = new PanelWord(" № "," Раскрой меня ");
        TreeItem<PanelWord> item = new TreeItem<PanelWord>(pan);
        leftTable.setRoot(item);

        // initialize() finish
    }
    // TODO: 29.11.2021 ПРИ ПЕРЕНОСЕ СТРОКИ из ЛЕВОЙ таблицы в правую - нужно анализировать строку и
    // TODO: 29.11.2021 РАСПРЕДЕЛЯТЬ ПО ТИПАМ объектов строки в ПРАВОЙ таблице
// ControllerToCheck finish

    public void setRootLeftTable(int i, String text) {
        PanelWord panelWord = new PanelWord(Integer.toString(i), text);
        TreeItem<PanelWord> treeItem = new TreeItem<PanelWord>(panelWord);
        leftTable.getRoot().getChildren().add(treeItem);
    }

    public static void main(String[] args) {
        launch(args);
    }

}

