package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class journalController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane, editPane;
    @FXML
    private TableView<Journal> journal_table;
    @FXML
    private TableColumn<Journal, String> name_col, lastName_col;
    @FXML
    private TableColumn<Journal, Integer> id_col;
    @FXML
    private TableColumn<Journal, Integer> gpa_col, extraPoints_col;

    @FXML
    private JFXButton classes_btn, students_btn, journlal_btn, menu_btn, exit_btn;
    @FXML
    private JFXComboBox<String> group_comboBox;
    @FXML
    private TextField search_field;
    @FXML
    private ImageView search_img;
    @FXML
    private Label adm_label;

    List<String> CLASSES = new ArrayList<>();
    // IT is parameters which are currently active(SELECTED in ComboBox)
    String GROUP_NAME = "";
    int GROUP_ID = -1;
    static final int ID = new authenticController().getId();



    private ObservableList<Journal> gratesData = FXCollections.observableArrayList();

    // navigation
    @FXML
    void setClasses_btn(ActionEvent event) {     HelpMethod.makeFadeOut(mainPane, WINDOWS.CLASSES);  }
    @FXML
    void setExit_btn(ActionEvent event) {       HelpMethod.makeFadeOut(mainPane, WINDOWS.Authentic); }
    @FXML
    void setGroup_btn(ActionEvent event) {       HelpMethod.makeFadeOut(mainPane, WINDOWS.GROUPS); }
    @FXML
    void setMenu_btn(ActionEvent event) {      HelpMethod.makeFadeOut(mainPane, WINDOWS.MENU); }
    @FXML
    void setStudents_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.STUDENTS); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //id = new authenticController().getId();
        HelpMethod.rippler(mainPane, myPane);
        HelpMethod.setImage("search", search_img);
        HelpMethod.fillGroup(group_comboBox);

        String[] infStd = HandlerDb.getInformationStudent(ID);


        group_comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                System.out.println(newValue);
                editPane.setVisible(true);
                GROUP_NAME = newValue;
                editPane.setVisible(true);
                CLASSES.clear();
                GROUP_ID = HandlerDb.getOneValue(
                        "SELECT group_id FROM groups_table WHERE group_name = ?", new String[]{GROUP_NAME});
                journal_table.getColumns().clear();
                crtTableColumn();
                initCols();
                search();
            }
        });


        if (infStd[5].equals("0")){
            group_comboBox.getSelectionModel().select(infStd[3]);
            group_comboBox.setVisible(false);
            adm_label.setVisible(false);
        }

    }

    void crtTableColumn() {
        String query = "SELECT name FROM groupClasses_table gc " +
                "LEFT JOIN classes_table c on gc.class_id = c.class_id " +
                "LEFT JOIN groups_table gt on gc.group_id = gt.group_id " +
                "WHERE gt.group_name = ?;";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = HandlerDb.getConnection().prepareStatement(query);
            statement.setString(1, GROUP_NAME);
            resultSet = statement.executeQuery();
            TableColumn id_col = new TableColumn<>("id");
            TableColumn name_col = new TableColumn<>("name");
            TableColumn lastName_col = new TableColumn<>("lastName");
            journal_table.getColumns().addAll(id_col, name_col, lastName_col);

            // add all classes to list and set column to every subjects
            while (resultSet.next()) {
                String sub = resultSet.getString(1);
                CLASSES.add(sub);
                journal_table.getColumns().add(new TableColumn<>(sub));
            }
            journal_table.getColumns().addAll(gpa_col, extraPoints_col);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initCols() {
        gratesData = FXCollections.observableArrayList();
        Connection connection = HandlerDb.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT student_id, first_name, last_name FROM students_table WHERE group_id = " + GROUP_ID + ";";
        try {
            statement = connection.prepareStatement(query);
            //statement.setInt(1, GROUP_ID);
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Journal journal = new Journal();
                int ID = resultSet.getInt(1);
                journal.setIdJ(ID);
                query = "SELECT grades FROM grades_table WHERE student_id = " + ID + ";";
                journal.setNameJ(resultSet.getString(2));
                journal.setLast_nameJ(resultSet.getString(3));
                if (HandlerDb.checkIsUnique(query, new int[]{ID}))
                journal.setSubjectsJ(HandlerDb.getList(query));
                gratesData.add(journal);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (gratesData.size() < 1) return;
        journal_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idJ"));
        journal_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nameJ"));
        journal_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("last_nameJ"));
        gpa_col.setCellValueFactory(new PropertyValueFactory<>("AVG"));
        if (CLASSES.size() < 1) return;
        // It's work not right

        for (int i = 0; i < CLASSES.size(); i++) {
            String val = "subject" + (1 + i);
            journal_table.getColumns().get(3 + i).setCellValueFactory(new PropertyValueFactory<>(val));

        }

        //it's test to just try to add some number in cell to column and loop this for all subjects (don't work) ;(
        /*
        for (int i = 3; i < CLASSES.size() + 3; i++) {
        Callback<TableColumn<Journal, Integer>, TableCell<Journal, Integer>> cellFactory = (param) -> {
            final TableCell<Journal, Integer> cell = new TableCell<Journal, Integer>() {
                @Override
                protected void updateItem(Integer iteam, boolean empty) {
                    super.updateItem(iteam, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setGraphic(null);
                        // it's some mark
                        setText("37");
                    }
                }
            };
            return cell;
        };
        journal_table.getColumns().get(i).setCellFactory(cellFactory);
    }
 */
        journal_table.setItems(gratesData);
    }

    double gpa(int id) {
        String query = "SELECT AVG(grades) FROM grades_table WHERE student_id = ?";
        return HandlerDb.getOneValue(query, new int[]{id});
    }

    void search() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Journal> filteredData = new FilteredList<>(gratesData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(s -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                // maybe should be use contains instead of indexOf but it's a example from book ;)
                if (s.getNameJ().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches first name.

                else if (s.getLast_nameJ().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;

                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Journal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(journal_table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        journal_table.setItems(sortedData);
    }

}