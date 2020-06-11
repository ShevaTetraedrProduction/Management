package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class addStudentController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane;

    @FXML
    private TextField nickName_field, name_field, lastName_field;

    @FXML
    private JFXButton cancel_btn, addStudent_btn;

    @FXML
    private JFXComboBox<String> group_comboBox;

    @FXML
    private JFXSlider year_slider;

    @FXML
    private Label star1, star2, star3, star4, star5, error_message;


    @FXML
    void setAddStudent_btn(ActionEvent event) {
        String name = name_field.getText();
        String lastName = lastName_field.getText();
        String nickName = nickName_field.getText();
        String group = group_comboBox.getValue();
        int year = (int) (year_slider.getValue() + 0.5);

        int userId;
        clearStar();
        HandlerDb.autoIncZero("users_table");
        HandlerDb.autoIncZero("students_table");
        if (check(name, lastName, nickName, group, year)) {
            HelpMethod.Message(COLORS.GREEN, "Всі поля заповнені");
            if (!HandlerDb.checkIsUnique("SELECT * FROM users_table WHERE login = ?;", new String[]{nickName})) {
                clearStar();
                star3.setVisible(true);
                error_message.setText("Нікнейм зайнятий");
                return;
            }
            error_message.setVisible(false);
            //Auto Increment to zero that  to avoid any missing numbers during deletion
            HandlerDb.autoIncZero("users_table");
            String query = "INSERT INTO users_table(login, accessLevel, hash_password) VALUES (?, 0, ?);";
            HandlerDb.executeQuery(query, new String[]{nickName, HelpMethod.getMd5(lastName.toLowerCase())});

            userId = HandlerDb.getOneValue("SELECT user_id FROM users_table WHERE login = ?", new String[]{nickName});
            int groupId = HandlerDb.getOneValue("SELECT group_id FROM groups_table WHERE  group_name = ?", new String[]{group});

            HandlerDb.autoIncZero("students_table");
            query = "INSERT INTO students_table(user_id, first_name, last_name, group_id, year) " +
                    "VALUES (" + userId + ",?,?," + groupId + "," + year + ");";
            HandlerDb.executeQuery(query, new String[]{name, lastName});
            query = "SELECT student_id FROM students_table WHERE user_id = ?";
            int std_id = HandlerDb.getOneValue(query, new Integer[]{userId});

            query = "INSERT INTO grades_table(student_id, group_id, class_id) SELECT std.student_id, gc.group_id, class_id" +
                    " FROM groupClasses_table gc  CROSS JOIN students_table std WHERE std.group_id = gc.group_id AND std.student_id = ?;";
            HandlerDb.executeQuery(query, new Integer[]{std_id});
            clearFields();
        }
    }

    @FXML
    void setCancel_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane, WINDOWS.STUDENTS);
    }

    void clearFields() {
        name_field.setText("");
        lastName_field.setText("");
        nickName_field.setText("");
        group_comboBox.setValue("");
        year_slider.setValue(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        HelpMethod.fillGroup(group_comboBox);
        System.out.println(StudentController.getGROUP_NAME());
        group_comboBox.getSelectionModel().select(StudentController.getGROUP_NAME());
        year_slider.setValue(0);
    }

    private boolean isEmpty(String s) {
        return s.length() == 0;
    }
    private boolean isEmpty(int i) {
        return i == 0;
    }

    private boolean check(String name, String last, String nick, String group, int year) {
        star1.setVisible(isEmpty(name));
        star2.setVisible(isEmpty(last));
        star3.setVisible(isEmpty(nick));
        group = group == null ? "" : group;
        star4.setVisible(isEmpty(group));
        star5.setVisible(isEmpty(year));
        return !(star1.isVisible() || star2.isVisible() || star3.isVisible() || star4.isVisible() || star5.isVisible());
    }

    private void clearStar() {
        star1.setVisible(false);
        star2.setVisible(false);
        star3.setVisible(false);
        star4.setVisible(false);
        star5.setVisible(false);
    }
}