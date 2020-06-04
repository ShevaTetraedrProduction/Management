package sample;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class classesController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane, editPane;
    @FXML
    private Label nameGroup, adm_label;

    @FXML
    private TableView<Class> classes_table;
    @FXML
    private TableColumn<Class, Integer> col_numb;
    @FXML
    private TableColumn<Class, String> col_group, col_class;
    @FXML
    private TableColumn<Class, JFXButton> col_del;

    @FXML
    private JFXComboBox<String> subject_comboBox, group_comboBox;

    @FXML
    private JFXButton delSubject_btn, addSubject_btn,
            groupes_btn, students_btn, journal_btn, menu_btn, exit_btn;

    @FXML
    private TextField subject_field, search_field;
    @FXML
    private ImageView search_img;

    //private ObservableList<Class> classesData;;// = FXCollections.observableArrayList();
    private ObservableList<Class> classesData = FXCollections.observableArrayList();

    // IT is parameters which are currently active(SELECTED in ComboBox)
    String GROUP_NAME = "";
    int GROUP_ID = -1;
    static final int ID = new authenticController().getId();

    // navigation
    @FXML
    void setMenu_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.MENU); }
    @FXML
    void setJournal_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.JOURNAL); }
    @FXML
    void setExit_btn(ActionEvent event) {     HelpMethod.makeFadeOut(mainPane, WINDOWS.Authentic); }
    @FXML
    void setGroups_btn(ActionEvent event) {        HelpMethod.makeFadeOut(mainPane, WINDOWS.GROUPS); }
    @FXML
    void setStudents_btn(ActionEvent event) {    HelpMethod.makeFadeOut(mainPane, WINDOWS.STUDENTS); }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        HelpMethod.fillGroup(group_comboBox);
        HelpMethod.setImage("search", search_img);
        String[] infStd = HandlerDb.getInformationStudent(ID);
            //Method  comboBox to always look on  the changes in value
            group_comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    //classes_table.getColumns().clear();
                    System.out.println(newValue);
                    editPane.setVisible(true);
                    nameGroup.setText("Предмети для групи: " + newValue);
                    GROUP_NAME = newValue;
                    GROUP_ID = HandlerDb.getOneValue("SELECT group_id FROM groups_table WHERE group_name = ?;", new String[]{GROUP_NAME});

                    HelpMethod.fillClasses(subject_comboBox, GROUP_ID);
                    editPane.setVisible(true);
                    subject_comboBox.getItems().clear();
                    initTable();
                    search();
                }
            });

        if (infStd[5].equals("0")){
            group_comboBox.getSelectionModel().select(infStd[3]);
            group_comboBox.setVisible(false);
            addSubject_btn.setVisible(false);
            delSubject_btn.setVisible(false);
            adm_label.setVisible(false);
            col_del.setVisible(false);
            col_group.setVisible(false);
        }

    }

    @FXML
    void setAddSubject_btn(ActionEvent event) {
        if (!subject_field.isVisible()) {
            subject_field.setVisible(true);
            subject_comboBox.setVisible(false);
        } else {
            String subject = subject_field.getText().toUpperCase();
            if (subject.length() == 0) return;
            //if subject is new that need to add it to a classes table first
            if (HandlerDb.checkIsUnique("SELECT name FROM classes_table WHERE name = ?;", new String[]{subject})) {
                HandlerDb.autoIncZero("classes_table");
                String query = "INSERT INTO classes_table(name) VALUES (?);";
                HandlerDb.executeQuery(query, new String[]{subject});
            }
            else
                HelpMethod.Message(COLORS.YELLOW, "Така дисципліна уже додана в базу");

            int class_id = HandlerDb.getOneValue("SELECT class_id FROM classes_table WHERE name = ?;", new String[]{subject});

            if (!HandlerDb.checkIsUnique("SELECT * FROM groupClasses_table WHERE class_id = ? AND group_id = ?", new Integer[]{class_id, GROUP_ID}))
                HelpMethod.Message(COLORS.YELLOW, "Така дисципліна уже додана в групу");
            else {
                HandlerDb.executeQuery("INSERT INTO groupClasses_table(group_id, class_id) VALUES (?, ?);", new Integer[]{GROUP_ID, class_id});
                String query = "INSERT INTO grades_table(student_id, group_id, class_id) " +
                        "SELECT std.student_id, gc.group_id, class_id FROM groupClasses_table gc  CROSS JOIN students_table std WHERE  gc.group_id = ? AND gc.class_id = ?;";
                HandlerDb.executeQuery(query, new Integer[]{GROUP_ID, class_id});
            }
            subject_field.setText("");
            initTable();
        }
        search();
    }

    @FXML
    void setDelSubject_btn(ActionEvent event) {
        if (!subject_comboBox.isVisible()) {
            subject_comboBox.setVisible(true);
            subject_comboBox.getItems().clear();
            HelpMethod.fillClasses(subject_comboBox, GROUP_ID);
            subject_field.setVisible(false);
        } else {
            String name = subject_comboBox.getValue().toUpperCase();
            if (name.length() != 0)
                delSubject(name);
        }
        search();
    }

    void initTable() {
       // classes_table.getColumns().clear();
       initCols();
        // add method in future
        //updateDate()
    }


    // init data in columns
    void initCols() {
        classesData = FXCollections.observableArrayList();
        String query = "SELECT gt.group_name,c.name FROM groupClasses_table gc " +
                "LEFT JOIN classes_table c on gc.class_id = c.class_id " +
                "LEFT JOIN groups_table gt on gc.group_id = gt.group_id " +
                "WHERE gt.group_name = ?;";
        ResultSet resultSet = HandlerDb.getResultSet(query, new String[]{GROUP_NAME});
        try {
            int numb = 1;
            while (resultSet.next()) {
                Class subject = new Class();
                subject.setId(numb++);
                subject.setGroup(resultSet.getString(1));
                subject.setName(resultSet.getString(2));
                classesData.add(subject);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // INSERT DATA INTO COLUMNS
        col_numb.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_group.setCellValueFactory(new PropertyValueFactory<>("group"));
        col_class.setCellValueFactory(new PropertyValueFactory<>("name"));
        // add button 'DEL' to every row
        Callback<TableColumn<Class, JFXButton>, TableCell<Class, JFXButton>> cellFactory = (param) -> {
            final TableCell<Class, JFXButton> cell = new TableCell<Class, JFXButton>() {
                @Override
                protected void updateItem(JFXButton iteam, boolean empty) {
                    super.updateItem(iteam, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final JFXButton delButton = new JFXButton("DEL");
                        delButton.setStyle("-fx-background-color: orangered");
                        //    delButton.setStyle("-fx-text-fill: whitesmoke"); (not work ;( )
                        // add event to every button
                        delButton.setOnAction(event -> {
                            Class c = getTableView().getItems().get(getIndex());
                            delSubject(c.getName());
                        });
                        setGraphic(delButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        col_del.setCellFactory(cellFactory);

        classes_table.setItems(classesData);
    }


    void delSubject(String class_name) {
        String query = "SELECT class_id FROM classes_table WHERE name = ?;";
        int class_id = HandlerDb.getOneValue(query, new String[]{class_name});
        query = "DELETE FROM grades_table WHERE group_id = ? AND class_id = ?;";
        HandlerDb.executeQuery(query, new Integer[]{GROUP_ID, class_id});
        HandlerDb.executeQuery("DELETE FROM groupClasses_table " +
                "WHERE class_id = ? AND group_id = ?;", new Integer[]{class_id, GROUP_ID});
            //DELETE FROM classes_table it's not important. I need think about this process later
        HandlerDb.executeQuery("DELETE FROM classes_table WHERE class_id = ?", new Integer[]{class_id});
        initTable();
        subject_comboBox.getItems().clear();
        HelpMethod.fillClasses(subject_comboBox, GROUP_ID);
        search();
    }

    // Method that avoid to search by some parameters (not my copy paste)
    void search() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Class> filteredData = new FilteredList<>(classesData, c -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cl -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                // maybe should be use contains instead of indexOf but it's a example from book ;)
                if (cl.getName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches first name.
                else if (Integer.toString(cl.getId()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches last name.
                else if (cl.getGroup().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Class> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(classes_table.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        classes_table.setItems(sortedData);
    }

}