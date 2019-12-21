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
        primaryStage.setTitle("Журнал успішності студента");
        //with = 1525, height = 850
        //900 500
        primaryStage.setScene(new Scene(root, 500, 400));
        //System.out.println(primaryStage.getWidth());
        primaryStage.centerOnScreen();
        // забирає стандартну рамочку
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        //primaryStage.hide();
    }

//jdbc:mysql://localhost:3306/managment?serverTimezone=UTC
    public static void main(String[] args) {
        launch(args);
    }
}
