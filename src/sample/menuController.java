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
import java.util.Map;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane;
    @FXML
    private Label info_label1, info_label2, info_label3;
    @FXML
    private JFXButton option_btn, exit_btn, classes_btn, teachers_btn, groups_btn, journal_btn;
    @FXML
    private ImageView exit_img, student_img, teachers_img, groups_img, options_img, classes_img, journal_img;

    int ID = new authenticController().getId();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        Map<String, String> mapInf = HandlerDb.getInformationStudent(ID);
        info_label1.setText(info_label1.getText() + " " + mapInf.get("Name"));
        String access = mapInf.get("Access").equals("0") ? "студент" : (mapInf.get("Access").equals("1") ? "викладач" : "завкафедри");
        info_label2.setText(info_label2.getText() + " " + mapInf.get("Group") + ", " + mapInf.get("Year") + " курс");

        if (!mapInf.get("Access").equals("0"))
            info_label2.setVisible(false);

            info_label3.setText("Рівень доступу: " + access);

        HelpMethod.setImage("exit", exit_img);
        HelpMethod.setImage("student", student_img);
        HelpMethod.setImage("teacher", teachers_img);
        HelpMethod.setImage("groups", groups_img);
        HelpMethod.setImage("journal", journal_img);
        HelpMethod.setImage("classes", classes_img);
        HelpMethod.setImage("options", options_img);
    }

    @FXML
    void setStudents_btn(ActionEvent event) {HelpMethod.makeFadeOut(mainPane, WINDOWS.STUDENTS);}
    @FXML
    void setExit_btn(ActionEvent event) { HelpMethod.makeFadeOut(mainPane,WINDOWS.Authentic);   }
    @FXML
    void setClose_btn(ActionEvent event) {  HelpMethod.closeWindow(mainPane); }
    @FXML
    void setGroups_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane,WINDOWS.GROUPS); }
    @FXML
    void setClasses_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.CLASSES); }
    @FXML
    void setJournal_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.JOURNAL);  }
    @FXML
    void setOption_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.OPTION);}
    
}
