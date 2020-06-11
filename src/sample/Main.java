package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/authentic.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("fxml/test.fxml"));
        primaryStage.setTitle("Журнал успішності студента");
        //with = 1525, height = 850   //900 500
        //System.out.println(primaryStage.getWidth());
        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.centerOnScreen();
        // забирає стандартну рамку
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        //primaryStage.hide();
    }

//jdbc:mysql://localhost:3306/managment?serverTimezone=UTC
    public static void main(String[] args) {
        launch(args);
    }
}
