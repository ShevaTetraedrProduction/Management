package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import java.net.URL;
import java.util.ResourceBundle;

public class authenticController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane;
    @FXML
    private TextField email, password;
    @FXML
    private JFXButton cancel_btn, accept_btn, guest_btn;
    @FXML
    private Label showPassword_label, tip_error, star1, star2;
    @FXML
    private JFXCheckBox show_checkBox;

    //id user
    static int ID;

    // it's to don't write password every time (delete this shit when i done all)
    @FXML
    void setGuest_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane, WINDOWS.MENU);
    }
    @FXML
    void setCancel_btn(ActionEvent event) { HelpMethod.closeWindow(mainPane); }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        checkBoxSelected();
    }

    @FXML
    void setAccept_btn(ActionEvent event) {
        StringBuilder tip = new StringBuilder("");
        if (email.getLength() == 0) {
            star1.setVisible(true);
            HelpMethod.Message(COLORS.RED, "Логін не заповнений");
            tip.append("Логін не запонений \n");
        } else
            star1.setVisible(false);
        if (password.getLength() == 0) {
            star2.setVisible(true);
            HelpMethod.Message(COLORS.RED, "Пароль не заповнений");
            tip.append("Пароль не заповнений");
        } else
            star2.setVisible(false);
        if ((!star2.isVisible() && !star1.isVisible())) {
            if (checkAuth()) {
                HelpMethod.makeFadeOut(mainPane, WINDOWS.MENU);
                return;
            }
            JOptionPane.showMessageDialog(null, "Такого користувача не існує");
            tip = new StringBuilder("логін чи пароль не співпадають");
        }
        tip_error.setText(tip.toString());
        tip_error.setVisible(true);
    }


    void checkBoxSelected() {
        show_checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                HelpMethod.Message(COLORS.GREEN, "Видимість паролю -> " + Boolean.toString(show_checkBox.isSelected()));
                if (show_checkBox.isSelected()) {
                    showPassword_label.setText("Пароль: " + password.getText());
                    showPassword_label.setVisible(true);
                } else {
                    showPassword_label.setText("");
                    showPassword_label.setVisible(false);
                }
            }
        });
    }

    private boolean checkAuth() {
        String query = "SELECT  user_id FROM  users_table WHERE  login = ? && hash_password = ?;";
        if (!HandlerDb.checkIsUnique(query, new String[]{email.getText(), HelpMethod.getMd5(password.getText())})) {
            ID = HandlerDb.getOneValue(query, new String[]{email.getText(), HelpMethod.getMd5(password.getText())});
            return true;
        }
            return false;
    }

    public int getId() {   return ID;   }
}