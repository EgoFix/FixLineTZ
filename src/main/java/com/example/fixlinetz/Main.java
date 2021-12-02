package com.example.fixlinetz;


import com.example.fixlinetz.controllers.ControllerToCheck;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;


public class Main extends Application {
    private static Bot bot = new Bot(); // бот для внешнего хранения вызова методов обработки для всех панелей
    private static ControllerToCheck toCheck = new ControllerToCheck(); // контроллер сцены toCheck
    private static ArrayList<String> rowElementsToCleaning = new ArrayList<String>(); //пустой массив для строк, которые совпадают по паттерну

    public static ArrayList<String> getRowElementsToCleaning(){return rowElementsToCleaning;}

    public static void clearRowElementsToCleaning(){ rowElementsToCleaning.clear();};

    public static ControllerToCheck getToCheck() {
        return toCheck;
    }

    public static Bot getBot() {
        return bot;
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            BorderPane main_wnd = new BorderPane();
            main_wnd = FXMLLoader.load(getClass().getResource("Sample.fxml"));
            Scene main_scene = new Scene(main_wnd, 350, 350);

            primaryStage.setTitle("main wind");
            primaryStage.setScene(main_scene);
            primaryStage.show();
            primaryStage.setResizable(false);


            Stage stageToCheck = new Stage();
            BorderPane windToCheck = new BorderPane();
            windToCheck = FXMLLoader.load(getClass().getResource("windToCheck.fxml"));
            Scene to_check_scene = new Scene(windToCheck, 1536, 800);

            stageToCheck.setTitle("wind to check");
            stageToCheck.setScene(to_check_scene);
            stageToCheck.show();
            stageToCheck.setResizable(false);
            toCheck.setStage(stageToCheck);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

