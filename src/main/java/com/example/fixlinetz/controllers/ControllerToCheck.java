package com.example.fixlinetz.controllers;

import com.example.fixlinetz.classes.Word;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class ControllerToCheck {
    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Объекты wind_to_check
    /////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private TreeTableView left_table;

    @FXML
    private TableView right_table;

    @FXML
    private Button upper_pane_button1;


//    @FXML
//    private void ButtonClicked(){
//        System.out.println("afafaggag");
//    }

    @FXML
    void initialize(Stage stageToCheck) {
        System.out.println("\"To check\"Scene initialized");

        // создаем массив строк
        List<Word> words = Arrays.<Word>asList(new Word("1", "word1"),
                new Word("2", "word2"));


        // создаем худ для таблицы
        final TreeItem<Word> rootTreeTableView = new TreeItem<>(new Word("№", "Объект"));

        words.stream().forEach((word) -> {
            rootTreeTableView.getChildren().add(new TreeItem<>(word));
        });
        // инициализируем колонку для номера
        TreeTableColumn<Word, String> numColumn = new TreeTableColumn<>(
                "№");
        numColumn.setPrefWidth(50);
        numColumn
                .setCellValueFactory((
                        TreeTableColumn.CellDataFeatures<Word, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNum()));

        // инициализируем колонку для текста
        TreeTableColumn<Word, String> textColumn = new TreeTableColumn<>(
                "Объект");
        textColumn.setPrefWidth(250);
        textColumn
                .setCellValueFactory((
                        TreeTableColumn.CellDataFeatures<Word, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getText()));

        TreeTableView<Word> treeTableView = new TreeTableView<>(rootTreeTableView);
        treeTableView.getColumns().setAll(numColumn, textColumn);
        // initialize() finish
    }

// ControllerToCheck finish
}

