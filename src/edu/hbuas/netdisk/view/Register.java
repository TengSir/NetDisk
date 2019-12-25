package edu.hbuas.netdisk.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Register extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Group  g= FXMLLoader.load(new File("resources/fxml/Register.fxml").toURL());
            Scene  s=new Scene(g,800,500);
            primaryStage.setTitle("Java网盘");
            primaryStage.setScene(s);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
