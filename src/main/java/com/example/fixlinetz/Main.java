package com.example.fixlinetz;


import com.example.fixlinetz.controllers.ControllerToCheck;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stageToCheck) {
        try {
//            BorderPane main_wnd = new BorderPane();
//            main_wnd = FXMLLoader.load(getClass().getResource("Sample.fxml"));
//            Scene main_scene = new Scene(main_wnd,350,350);
//            primaryStage.setTitle("main wind");
//            primaryStage.setScene(main_scene);
//            primaryStage.show();
//            primaryStage.setResizable(false);



            BorderPane wind_to_check = new BorderPane();
            wind_to_check = FXMLLoader.load(getClass().getResource("wind_to_check.fxml"));
            Scene to_check_scene = new Scene(wind_to_check,1280,800);
//            Stage stageToCheck = new Stage();
            stageToCheck.setTitle("wind to check");
            stageToCheck.setScene(to_check_scene);
            stageToCheck.show();
            stageToCheck.setResizable(false);




        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


