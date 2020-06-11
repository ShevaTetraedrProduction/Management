package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class StudentController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane, editPane;
    @FXML
    private Label nameGroup, adm_label;
    @FXML
    private JFXComboBox<String> group_comboBox;

    @FXML
    private TableView<Student> students_table;

    @FXML
    private TableColumn<Student, Integer> id_col, year_col;
    @FXML
    private TableColumn<Student, String> name_col, lastName_col, group_col;
    @FXML
    private TableColumn<Student, JFXButton> del_col;


    @FXML
    private JFXButton classes_btn, groups_btn, journal_btn, menu_btn, exit_btn, addStudent_btn, delGroup_btn;

    @FXML
    private TextField search_field;
    @FXML
    private ImageView search_img;


    int ID = new authenticController().getId();
    String GROUP_NAME = "";
    int GROUP_ID = -1;
    Map<String, String> mapInf = HandlerDb.getInformationUser(ID);


    // navigation
    @FXML
    void setClasses_btn(ActionEvent event) { HelpMethod.makeFadeOut(mainPane, WINDOWS.CLASSES);}
    @FXML
    void setExit_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.Authentic);    }
    @FXML
    void setGroups_btn(ActionEvent event) { HelpMethod.makeFadeOut(mainPane, WINDOWS.GROUPS);  }
    @FXML
    void setJournal_btn(ActionEvent event) {  HelpMethod.makeFadeOut(mainPane, WINDOWS.JOURNAL); }
    @FXML
    void setMenu_btn(ActionEvent event) {   HelpMethod.makeFadeOut(mainPane, WINDOWS.MENU);   }
    @FXML
    void setAddStudent_btn(ActionEvent event) {
        HelpMethod.makeFadeOut(mainPane, WINDOWS.ADDSTUDENT);
    }





    public static ObservableList<Student> studentsData;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        HelpMethod.setImage("search", search_img);
        editPane.setVisible(false);
        if (mapInf.get("Access").equals("0")) {
            HelpMethod.setInvisible(new Region[]{group_comboBox, adm_label, addStudent_btn});
            HelpMethod.setVisible(new Region[]{editPane ,students_table});
            del_col.setVisible(false);
            GROUP_NAME = mapInf.get("Group");
            nameGroup.setText(GROUP_NAME);
            initTable();
        }


        HelpMethod.fillGroup(group_comboBox);
        students_table.setItems(studentsData);
        //search();

        //Method  comboBox to always look on  the changes in value
        group_comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                System.out.println(newValue);
                HelpMethod.setVisible(new Region[]{editPane});

                nameGroup.setText("Стеденти групи: " + newValue);
                GROUP_NAME = newValue;
                GROUP_ID = HandlerDb.getOneValue("SELECT group_id FROM groups_table WHERE group_name = ?;", new String[]{GROUP_NAME});
                HelpMethod.fillClasses(group_comboBox, GROUP_ID);
                group_comboBox.getItems().clear();
                initTable();
                search();

            }
        });


    }
    private void initTable() {
        initCols();
    }

    private void initCols() {
        studentsData = FXCollections.observableArrayList();
        String query = "SELECT s.student_id,s.first_name, s.last_name, g.group_name, s.year FROM students_table s" +
                " LEFT JOIN groups_table g on s.group_id = g.group_id WHERE g.group_name = ?;";
        ResultSet resultSet = HandlerDb.getResultSet(query, new String[]{GROUP_NAME});

        try {
            int numb = 1;
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(numb++);
                student.setName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGroup(resultSet.getString("group_name"));
                student.setYear(resultSet.getInt("year"));
                studentsData.add(student);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
            name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
            lastName_col.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            group_col.setCellValueFactory(new PropertyValueFactory<>("group"));
            year_col.setCellValueFactory(new PropertyValueFactory<>("year"));

            Callback<TableColumn<Student, JFXButton>, TableCell<Student, JFXButton>> cellFactory = (param) -> {
                final TableCell<Student, JFXButton> cell = new TableCell<Student, JFXButton>() {
                    @Override
                    protected void updateItem(JFXButton iteam, boolean empty) {
                        super.updateItem(iteam, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        }
                        else {
                            final  JFXButton delButton = new JFXButton("DEL");
                            delButton.setStyle("-fx-background-color: orangered");
                            //    delButton.setStyle("-fx-text-fill: whitesmoke");
                            delButton.setOnAction(event -> {
                                Student s = getTableView().getItems().get(getIndex());
                                delElement(s.getId());
                                HelpMethod.Message(COLORS.GREEN, "Студента " + s.getName() + " " + s.getLastName() +
                                        " Виключено з університету");
                                initTable();
                                search();
                            });
                            setGraphic(delButton);
                            setText(null);
                        }
                    }
                };
                return cell;
            };
            del_col.setCellFactory(cellFactory);
            students_table.setItems(studentsData);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        editTableCols();
        //usersData.add(new Groups(2, "Alex"));
    }

    private void editTableCols() {
        //id_col.setCellFactory(TextFieldTableCell.forTableColumn());
     /*   id_col.setCellFactory(TextFieldTableCell.<Student, Integer>forTableColumn(new IntegerStringConverter()));
        id_col.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setId(e.getNewValue());
                }
                );
*/
        name_col.setCellFactory(TextFieldTableCell.forTableColumn());
        name_col.setOnEditCommit(e -> {
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
                }
        );

        group_col.setCellFactory(TextFieldTableCell.forTableColumn());
        name_col.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setGroup(e.getNewValue());
        });
    }

    void delElement(int student_id) {
        String query = "DELETE FROM grades_table WHERE student_id = ?";
        HandlerDb.executeQuery(query, new Integer[]{student_id});
        int user_id = HandlerDb.getOneValue("SELECT user_id FROM students_table WHERE student_id = ?;", new Integer[]{student_id});
        query = "DELETE FROM students_table WHERE student_id= ?;";
        HandlerDb.executeQuery(query, new Integer[]{student_id});
        query = "DELETE FROM users_table WHERE user_id= ?;";
        HandlerDb.executeQuery(query, new Integer[]{user_id});
        //*student_comboBox.getItems().clear();
        //*HelpMethod.fillStudent(student_comboBox);
    }


    void search() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Student> filteredData = new FilteredList<>(studentsData, p -> true);

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
                if (s.getName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches first name.
               /* else if (Integer.toString(s.getId()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;*/ // Filter matches last name.
                else if (s.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (s.getGroup().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (Integer.toString(s.getYear()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Student> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(students_table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        students_table.setItems(sortedData);
    }
}