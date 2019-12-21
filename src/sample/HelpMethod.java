package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

enum COLORS{
    Black("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLEU("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m");

    private String code;
    COLORS(String code) {
        this.code = code;
    }
    public String getCode(){return code;}
}

enum WINDOWS{
    Authentic("authentic"),
    MENU("menu"),
    GROUPS("groups"),
    STUDENTS("students"),
    CLASSES("classes"),
    OPTION("option"),
    JOURNAL("journal"),
    ADDSTUDENT("addStudent");

    private String name;
    WINDOWS(String name) {this.name = name;}
    public String getName(){ return name + ".fxml";}

}

public class HelpMethod {

    //it is animation when user miss click
    public static void rippler(AnchorPane mainPane, Pane myPane) {
        JFXRippler rippler = new JFXRippler(myPane);
        rippler.setRipplerRadius(40);
        rippler.setRipplerFill(Paint.valueOf("#000000"));
        mainPane.getChildren().add(rippler);
        /*rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
        JFXRippler rippler = new JFXRippler(myPane);
        rippler.setRipplerRadius(40);
        rippler.setRipplerFill(Paint.valueOf("#000000"));
        rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
        mainPane.getChildren().add(rippler); */
    }

    // it's animation when we open new Window or transit
    public static void makeFadeOut(AnchorPane mainPane, WINDOWS w) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(mainPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openWindow(mainPane, w.getName());
            }
        });
        fadeTransition.play();
    }

    // the process of opening a window
    private static void openWindow(AnchorPane mainPane, String s) {
        Parent Window;
        try {
            FXMLLoader loader = new FXMLLoader(HelpMethod.class.getResource(s));
            Window = loader.load();
            //MainController mainController = loader.getController();
            //mainController.setParameters(ID,loginFieldLI.getText());
            Scene mainScene = new Scene(Window);
            Stage curStage = (Stage) mainPane.getScene().getWindow();
            curStage.setResizable(false);
            curStage.setScene(mainScene);
            curStage.centerOnScreen();
            //curStage.setX(250);
            //curStage.setY(150);
            Message(COLORS.PURPLE, "Перехід до " + s);
            curStage.setTitle("Журнал оцінювання");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //the process of opening a window and not close the previous
    public static void openNewWindow(AnchorPane mainPane, String s, String title) {
        Parent Window;
        try {
            FXMLLoader loader = new FXMLLoader(HelpMethod.class.getResource(s));
            Window = loader.load();
            Scene mainScene = new Scene(Window);
            Stage newWindow = new Stage();
            newWindow.setTitle(title);
            newWindow.setScene(mainScene);
            newWindow.centerOnScreen();
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeWindow(AnchorPane mainPane) {
        // get a handle to the stage
        Stage stage = (Stage) mainPane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    // Help to direct to right folders
    public static void setImage(String nameImg, ImageView nameObj) {
        File file = new File("C:\\Users\\Xiaomi\\IdeaProjects\\Management\\img\\" + nameImg + ".png");
        Image image_icon = new Image(file.toURI().toString());
        nameObj.setImage(image_icon);
    }





    // return hash password by means of md5(not my)
    public static String getMd5(String input) {
        input = "qazwsxedcrfvtgbyhnujm" + input;
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    //Help write in console with color
    public static void Message(COLORS col, String str) {
        final String ANSI_RESET = "\u001B[0m";
        System.out.println(col.getCode() + str + ANSI_RESET);
    }

    // to fill some comBox date from sql table
    public static void fillGroup(JFXComboBox comboBox) {
        String sql = "SELECT group_name FROM groups_table";
        HandlerDb.fillComboBox(comboBox, sql, 0, 1);
    }
    public static void fillStudent(JFXComboBox comboBox) {
        String sql = "SELECT  student_id, first_name, last_name FROM students_table;";
        HandlerDb.fillComboBox(comboBox, sql, 1,2);
    }
    public static void fillClasses(JFXComboBox comboBox, int group_id) {
        String sql = "SELECT name FROM groupClasses_table gc " +
                "LEFT JOIN classes_table ct on gc.class_id = ct.class_id WHERE group_id = " + group_id + ";";
        HandlerDb.fillComboBox(comboBox, sql, 0, 1);
    }
}
