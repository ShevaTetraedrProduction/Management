package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane;
    @FXML
    private Label info_label1, info_label2, info_label3;
    @FXML
    private JFXButton option_btn, exit_btn, classes_btn, close_btn, groups_btn, journal_btn;
    @FXML
    private ImageView exit_img, student_img, shutdown_img, groups_img, options_img, classes_img, journal_img;

    static int id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id = new authenticController().getId();
        HelpMethod.rippler(mainPane, myPane);
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT  s.user_id, s.first_name, s.last_name, g.group_name, s.year FROM  students_table s LEFT JOIN " +
                "groups_table g on s.group_id = g.group_id WHERE  s.user_id = ?;";
        try {
            connection = HandlerDb.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String str = info_label1.getText() + " " + resultSet.getString(2) + " " + resultSet.getString(3);
                info_label1.setText(str);
                str = info_label2.getText() + " " + resultSet.getString(4) + ", " + resultSet.getInt(5) + " курс";
                info_label2.setText(str);
                resultSet = connection.createStatement().executeQuery("SELECT accessLevel FROM users_table WHERE  user_id = " + id + ";");
                resultSet.last();
                str = info_label3.getText() + (resultSet.getInt(1) == 0 ? "студент" : "адмін");
                info_label3.setText(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HelpMethod.setImage("exit", exit_img);
        HelpMethod.setImage("student", student_img);
        HelpMethod.setImage("shutdown", shutdown_img);
        HelpMethod.setImage("groups", groups_img);
        HelpMethod.setImage("journal", journal_img);
        HelpMethod.setImage("classes", classes_img);
        HelpMethod.setImage("options", options_img);
    }




    @FXML
    void setStudents_btn(ActionEvent event) {HelpMethod.makeFadeOut(mainPane, WINDOWS.STUDENTS);}
    @FXML
    void setExit_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane,WINDOWS.Authentic);
    }
    @FXML
    void setClose_btn(ActionEvent event) {
        HelpMethod.closeWindow(mainPane);
    }
    @FXML
    void setGroups_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane,WINDOWS.GROUPS);
    }
    @FXML
    void setClasses_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane, WINDOWS.CLASSES);
    }
    @FXML
    void setJournal_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.JOURNAL);    }
    @FXML
    void setOption_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.OPTION);}

    int getId() {
        return id;
    }

}
