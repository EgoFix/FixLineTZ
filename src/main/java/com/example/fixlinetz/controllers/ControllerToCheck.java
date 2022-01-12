package com.example.fixlinetz.controllers;

import com.example.fixlinetz.Main;
import com.example.fixlinetz.classes.DocWord;
import com.example.fixlinetz.classes.PanelWord;
import com.example.fixlinetz.classes.ToExcelSaver;
import com.example.fixlinetz.documents.DocumentEXCEL;
import com.example.fixlinetz.documents.DocumentPDF;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class ControllerToCheck extends Application {

    /////////////////////////////////////////////////////////////////////////////////////////
    ////                    Объекты wind_to_check
    /////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private Button upperPaneButton1; // кнопка верхней панели
    @FXML
    private TreeView<PanelWord> leftView; // левая таблица
    @FXML
    private Button ButtonToCheck; // кнопка для отметки выделенных объектов
    @FXML
    private TableView rightTable; // правая таблица
    @FXML
    private Button transferButton; // перенос элементов
    @FXML
    private Button calcButton; // подсчет элементов
    @FXML
    private Button saveToExcelButton; // сохрание в эксельке
    @FXML
    private Label saveToExcelLabel1; // инфа по обработке эксельки
    @FXML
    private Text ToExcelText; // время обработки эксельки
    @FXML
    private ProgressBar toExcelPb; // прогресс бар обработки эксельки


    Stage stage; // сцена панели
    DocWord[] mass; // массив элементов из словаря
    public double totalResult[] = new double[4];
    private ObservableList<PanelWord> checkedArray = FXCollections.observableArrayList();
    CheckBoxTreeItem<PanelWord> leftViewRoot; // рут элемент левой таблицы
    DocumentEXCEL docEL = new DocumentEXCEL(DocumentPDF.getNameXLSX(), DocumentPDF.getNameEndXLSX());
    ToExcelSaver toExcelSaver; // экземпляр класса-сохранителя данных в эксель
    PanelWord[] leftViewTempMass; // массив элементов, которые пользователь выделяет в левой таблице для их отметки галочкой

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
    }


    @FXML
    public void initialize() {
        System.out.println("\"To check\" Scene initialized");

        /////////////////////////////////////////////////////////////////////////////////////////
        ////                    Собираем leftView для сырого массива
        /////////////////////////////////////////////////////////////////////////////////////////
        // устанавливаем рут
        PanelWord pan = new PanelWord("№ ", " Объект"); // худ левой таблицы
        leftView.setCellFactory(CheckBoxTreeCell.<PanelWord>forTreeView()); // тип ячейки checkBox
        CheckBoxTreeItem<PanelWord> root = new CheckBoxTreeItem<PanelWord>(pan); // вписываем в рут элемент худ
        leftView.setRoot(root); // устанавливаем рут


        /////////////////////////////////////////////////////////////////////////////////////////
        ////                    windToCheck panel EVENTS
        /////////////////////////////////////////////////////////////////////////////////////////
        // обработчик выбранных элементов для добавления в массив
        // добавление в checkedArray выделенных галочкой объектов
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
        ////                    windToCheck panel BUTTONS
        /////////////////////////////////////////////////////////////////////////////////////////
        // добавляем в левую таблицу значения элементов, которые удовлетворяют маске
        upperPaneButton1.setOnAction(event -> {
            if (leftViewRoot != null)
                leftViewRoot.getChildren().clear();
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


        transferButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                /////////////////////////////////////////////////////////////////////////////////////////
                ////                    Собираем rightTable из выборки пользователя
                /////////////////////////////////////////////////////////////////////////////////////////
                rightTable.getItems().clear(); // чистим таблицу
                rightTable.getColumns().clear(); // чистим столбцы
                // столбец для вывода номера
                TableColumn<PanelWord, String> numColumn = new TableColumn<>("№");
                numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
                rightTable.getColumns().add(numColumn);

                // столбец для вывода объекта
                TableColumn<PanelWord, String> ObjColumn = new TableColumn<>("Объект");
                ObjColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
                rightTable.getColumns().add(ObjColumn);

                ObservableList<PanelWord> temp = FXCollections.observableArrayList();

                // обертка для передачи элементов в таблицу по значению
                Integer num = 0;
                for (PanelWord tempItem : checkedArray) {
                    temp.add(num, tempItem);
                    num++;
                }
                rightTable.setItems(temp); // добавляем элементы в правую таблицу из temp
            }
        });


        calcButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null; // получили билдер, который парсит XML и создает структуру документа
                try {
                    builder = factory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document document = null; // запарсили XML, создав структуру Document
                try {
                    document = builder.parse(new File("dictionary.xml"));
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Element docElem = document.getDocumentElement(); // получаем тип Word из словаря
                NodeList docElemNodes = docElem.getElementsByTagName("WordR"); // заполняем docElemNodes полями словаря name/value по тегу WordR

                int i, j, k;

                System.out.println(
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                                "        //                              Здесь начинается обработка по типу Word\n" +
                                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

                docElemNodes = docElem.getElementsByTagName("Word"); // получаем только типы (Word)
                System.out.println("docElemNodes.getLength = " + docElemNodes.getLength());
                mass = new DocWord[docElemNodes.getLength()];

                for (i = 0; i < docElemNodes.getLength(); i++) {
                    Node node = docElemNodes.item(i);
                    System.out.println("Найден элемент: " + node.getAttributes().getNamedItem("name") + ", его атрибут: " + node.getAttributes().getNamedItem("size"));

                    // формируем массив типа DocWord для формирования рабочего словаря, записываем параметры первого уровня
                    mass[i] = new DocWord(String.valueOf(node.getAttributes().getNamedItem("name")),
                            String.valueOf(node.getAttributes().getNamedItem("size")));
                }
                System.out.println("mass.length = " + mass.length);

                for (DocWord word : mass) {
                    System.out.println(word.toString());
                }

                System.out.println(
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                                "        //                              Здесь начинается обработка по типу WordR\n" +
                                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

                docElemNodes = docElem.getElementsByTagName("WordR"); // получаем только типы (Word)
                System.out.println("docElemNodes.getLength =  " + docElemNodes.getLength());

                for (k = 0; k < docElemNodes.getLength(); ) { // перебираем строки
                    for (i = 0; i < mass.length; i++) { // проходим по всем типам
                        for (j = 0; j < mass[i].getSize(); j++, k++) { // перебираем WordR
                            Node node = docElemNodes.item(k);
                            mass[i].setWordRname(String.valueOf(node.getAttributes().getNamedItem("name")), j);
                        }
                    }
                }

//                for (DocWord word : mass) {
//                    System.out.println(word.toString());
//                }

                System.out.println(
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                                "        //                            Здесь начинается подсчет уникальных элементов\n" +
                                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");

                for (i = 0; i < mass.length; i++) { // проходим по всем типам
                    int hitCounter = 0;
                    for (k = 0; k < checkedArray.size(); k++) { // перебираем строки
                        int tempCounter = 0;
                        for (j = 0; j < mass[i].getSize(); j++) { // перебираем WordR
                            String s = checkedArray.get(k).getText();
                            String w = mass[i].getWordRname(j);
                            if (s.contains(w)) {
                                tempCounter++;
                            }
                        }
                        if (tempCounter > 0)
                            hitCounter++;
                    }
                    // выводим количество элементов в соответствии с их типом
//            System.out.println("mass[" + (i + 1) + "] " + mass[i].getWordName() + " hitCounter = " + hitCounter);
                    mass[i].setValue(hitCounter);
                }

                // выводим инфу о кол-ве элементов
                rightTable.getItems().clear(); // чистим таблицу
                rightTable.getColumns().clear(); // чистим столбцы

                /////////////////////////////////////////////////////////////////////////////////////////
                ////                    Собираем rightTable из выборки пользователя
                /////////////////////////////////////////////////////////////////////////////////////////
                // столбец для вывода типа
                TableColumn<DocWord, String> wordColumn = new TableColumn<>("Тип");
                wordColumn.setCellValueFactory(new PropertyValueFactory<>("wordName"));
                rightTable.getColumns().add(wordColumn);

                // столбец для вывода кол-ва
                TableColumn<DocWord, Integer> HintColumn = new TableColumn<>("Количество");
                HintColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                rightTable.getColumns().add(HintColumn);

                ObservableList<DocWord> temp = FXCollections.observableArrayList();
                // обертка для передачи элементов в таблицу по значению
                Integer num = 0;
                for (DocWord tempItem : mass) {
                    temp.add(num, tempItem);
                    num++;
                }
                rightTable.setItems(temp);
            }
        });


        saveToExcelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(
                        "        /////////////////////////////////////////////////////////////////////////////////////////////////////////\n" +
                                "        //                    Здесь начинается вывод количества уникальных элементов в Excel\n" +
                                "        /////////////////////////////////////////////////////////////////////////////////////////////////////////");
//                ToExcelText.setText(ToExcelText.getText() + "\n" + new Date());
                if (toExcelSaver != null && toExcelSaver.isRunning()) {
                    toExcelSaver.cancel();
                }

                toExcelSaver = new ToExcelSaver(DocumentPDF.getNameXLSX(), DocumentPDF.getNameEndXLSX(), mass);
                Thread thread = new Thread(toExcelSaver);
                thread.setDaemon(true);
                thread.start();

                toExcelPb.progressProperty().bind(toExcelSaver.progressProperty());
                saveToExcelButton.disableProperty().bind(toExcelSaver.runningProperty());
//                ToExcelText.setText(ToExcelText.getText() + "\n" + new Date());
            }
        });

        rightTable.setRowFactory( tv -> {
            TableRow<DocWord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DocWord rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            return row ;
        });

        MultipleSelectionModel<TreeItem<PanelWord>> leftViewSelectionModel = leftView.getSelectionModel();
        leftViewSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        // устанавливаем слушатель для отслеживания изменений
        leftViewSelectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<PanelWord>>(){

            @Override
            public void changed(ObservableValue<? extends TreeItem<PanelWord>> observableValue, TreeItem<PanelWord> panelWordTreeItem, TreeItem<PanelWord> t1) {
                String selectedItems = "";
                int counter = 0;
                ObservableList<TreeItem<PanelWord>> selected = leftViewSelectionModel.getSelectedItems();
                for (TreeItem<PanelWord> item : selected){
                    selectedItems += item + " ";
                    counter++;
                }
//                leftViewTempMass = new PanelWord[counter];
//                for(int i = 0; i < counter; i++){
//                    leftViewTempMass[i].setNum();
//                }
                System.out.println("Selected: " + selectedItems);
            }

        });

        ButtonToCheck.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // выделенные добавить в массив, нажать на кнопку, проставить галочки всем, очистить массив
            }
        });
        // initialize() finish
    }

    public void setLeftView(int i, String text) {
        /////////////////////////////////////////////////////////////////////////////////////////
        ////                    Функция для записи строки в leftView
        /////////////////////////////////////////////////////////////////////////////////////////
        PanelWord panelWord = new PanelWord(Integer.toString(i), text);
        CheckBoxTreeItem<PanelWord> treeItem = new CheckBoxTreeItem<>(panelWord);
        leftView.getRoot().getChildren().add(treeItem);

    }



    public static void main(String[] args) {
        launch(args);
    }

    // ControllerToCheck finish
}

// TODO: 29.11.2021 ПРИ ПЕРЕНОСЕ СТРОКИ из ЛЕВОЙ таблицы в правую - нужно анализировать строку и
//  РАСПРЕДЕЛЯТЬ ПО ТИПАМ объектов строки в ПРАВОЙ таблице+

// TODO: 02.12.2021 Подсчет объектов+

// TODO: 02.12.2021 Нужно добавить отображение инфы во время обработки PDF+

// TODO: 02.12.2021 Нужно добавить перекидывание в эксельку+  //перенесено на доп поток

// TODO: 02.12.2021 Нужно добавить обработку другого файла без перекомпиляции

// TODO: 10.01.2022 Нужно сформировать задачу в класс и поместить экземпляр класса в отдельный поток  !!!

// TODO: 12.01.2022 Нужно добавить отображение элементов, входящих в правую таблицу при нажатии на строку
//  и возможность удалить выбранный элемент

// TODO: 12.01.2022 Добавить многострочное выделение
//  - отдельно добавляются через галочку ( основной массив )
//  - дополнительно добавляются через кнопку ( среди тех, что выделены )
//  - если есть повторяющиеся значения по полю value, то игнорировать добавление в основной массив

// FIXME: 08-Dec-21 pattern matching
