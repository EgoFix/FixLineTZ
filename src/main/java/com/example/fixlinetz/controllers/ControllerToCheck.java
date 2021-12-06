package com.example.fixlinetz.controllers;

import com.example.fixlinetz.Main;
import com.example.fixlinetz.classes.PanelWord;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;


public class ControllerToCheck extends Application {

    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Объекты wind_to_check
    /////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TreeView<PanelWord> leftView;
    @FXML
    private TableView rightTable;
    @FXML
    private Button upperPaneButton1;
    @FXML
    private Button transferButton;


    Stage stage;

    private ObservableList<PanelWord> checkedArray = FXCollections.observableArrayList();
    CheckBoxTreeItem<PanelWord> leftViewRoot;

    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Методы wind_to_check
    /////////////////////////////////////////////////////////////////////////////////////////
    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TreeView<PanelWord> getLeftView() {
        return leftView;
    }

    public Button getUpperPaneButton1() {
        return upperPaneButton1;
    }

    @Override
    public void start(Stage stageToCheck) throws Exception {
        System.out.println("\"To check\" Scene started");
        upperPaneButton1.setStyle("-fx-background-color: #808080");
    }

    @FXML
    public void initialize() {
        System.out.println("\"To check\" Scene initialized");

        /////////////////////////////////////////////////////////////////////////////////////////
        ////                            SearchTOCheck button
        /////////////////////////////////////////////////////////////////////////////////////////
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
            for (int i = 0; i < Main.getRowElementsToCleaning().size(); i++) {
                setLeftView(i, Main.getRowElementsToCleaning().get(i));
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
        ////                  button to transfer data from leftView to rightTable
        /////////////////////////////////////////////////////////////////////////////////////////

        transferButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<PanelWord> temp = FXCollections.observableArrayList();

                // обертка для передачи элементов в таблицу по значению
                Integer num = 0;
                for (PanelWord tempItem : checkedArray) {
                    temp.add(num, tempItem);
                    num++;
                }
                rightTable.setItems(temp);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////
        ////                    Собираем leftView для сырого массива
        /////////////////////////////////////////////////////////////////////////////////////////
        // устанавливаем рут
        PanelWord pan = new PanelWord("№ ", " Объект");
        leftView.setCellFactory(CheckBoxTreeCell.<PanelWord>forTreeView());
        CheckBoxTreeItem<PanelWord> root = new CheckBoxTreeItem<PanelWord>(pan);
        leftView.setRoot(root);

        // обработчик выбранных элементов для добавления в массив
        root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), (CheckBoxTreeItem.TreeModificationEvent<PanelWord> evt) -> {
            CheckBoxTreeItem<PanelWord> item = evt.getTreeItem();

            if (evt.wasIndeterminateChanged()) {
                if (item.isIndeterminate()) {
                    checkedArray.remove(item.getValue());
                } else if (item.isSelected()) {
                    checkedArray.add(item.getValue());
                }
            } else if (evt.wasSelectionChanged()) {
                if (item.isSelected()) {
                    checkedArray.add(item.getValue());
                } else {
                    checkedArray.remove(item.getValue());
                }
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
        ////                    Собираем rightTable из выборки пользователя
        /////////////////////////////////////////////////////////////////////////////////////////
        // столбец для вывода имени
        TableColumn<PanelWord, String> numColumn = new TableColumn<>("№");

        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        numColumn.setMinWidth(20);
        numColumn.setMaxWidth(100);
        numColumn.setPrefWidth(200);
        rightTable.getColumns().add(numColumn);

        // столбец для вывода возраста
        TableColumn<PanelWord, String> ObjColumn = new TableColumn<>("Объект");
        ObjColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        ObjColumn.setMinWidth(250);
        ObjColumn.setMaxWidth(300);
        ObjColumn.setPrefWidth(250);
        rightTable.getColumns().add(ObjColumn);


        // initialize() finish
    }
    // TODO: 29.11.2021 ПРИ ПЕРЕНОСЕ СТРОКИ из ЛЕВОЙ таблицы в правую - нужно анализировать строку и
    //  РАСПРЕДЕЛЯТЬ ПО ТИПАМ объектов строки в ПРАВОЙ таблице

    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Функция для записи строки в leftView
    /////////////////////////////////////////////////////////////////////////////////////////
    public void setLeftView(int i, String text) {
        PanelWord panelWord = new PanelWord(Integer.toString(i), text);
        CheckBoxTreeItem<PanelWord> treeItem = new CheckBoxTreeItem<>(panelWord);
        leftView.getRoot().getChildren().add(treeItem);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ControllerToCheck finish
}

// TODO: 02.12.2021 Нужно сделать так чтобы при добавлении элемента в правую таблицу происходила проверка на тип объекта,
//  записывалось кол-во объектов как рут ветка и в нее добавлялись уже строки которые подходят под этот тип

// TODO: 02.12.2021 Нужно добавить обработку другого файла без перекомпиляции

// TODO: 02.12.2021 Нужно добавить перекидывание в эксельку

// TODO: 02.12.2021 Нужно добавить отображение инфы во время обработки PDF+