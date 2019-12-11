package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class groupsController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane myPane;

    @FXML
    private JFXButton journlal_btn, classes_btn, students_btn, menu_btn, exit_btn;
    @FXML
    private JFXButton delGroup_btn, addGroup_btn, search_btn;

    @FXML
    private TableView<Group> groups_table;
    @FXML
    private TableColumn<Group, Integer> group_id;
    @FXML
    private TableColumn<Group, String> group_name;
    @FXML
    private TableColumn<Group, JFXButton> group_delete;
    @FXML
    private JFXComboBox<String> delGroup_comboBox;

    @FXML
    private TextField group_field, search_field;

    @FXML
    private ImageView search_img;

    // navigation
    @FXML
    void setJournal_btn(ActionEvent event) {     HelpMethod.makeFadeOut(mainPane, "journal.fxml"); }
    @FXML
    void setClasses_btn(ActionEvent event) {     HelpMethod.makeFadeOut(mainPane, "classes.fxml"); }
    @FXML
    void setExit_btn(ActionEvent event) {       HelpMethod.makeFadeOut(mainPane, "authentic.fxml");}
    @FXML
    void setMenu_btn(ActionEvent event) {       HelpMethod.makeFadeOut(mainPane, "menu.fxml");  }
    @FXML
    void setStudents_btn(ActionEvent event) {      HelpMethod.makeFadeOut(mainPane, "students.fxml"); }


    private ObservableList<Group> groupsData = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelpMethod.rippler(mainPane, myPane);
        HelpMethod.setImage("search", search_img);
        initTable();
        search();
    }

    public void initTable() {
        initCols();
        //later
        //updateDate
    }

    // init data in columns
    private void initCols() {
        groupsData = FXCollections.observableArrayList();
        Connection connection = HandlerDb.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM groups_table";
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Group group = new Group();
                group.setGroup_id(resultSet.getInt("group_id"));
                group.setGroup_name(resultSet.getString("group_name"));

                groupsData.add(group);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // INSERT DATA INTO COLUMNS
        group_id.setCellValueFactory(new PropertyValueFactory<>("group_id"));
        group_name.setCellValueFactory(new PropertyValueFactory<>("group_name"));

        Callback<TableColumn<Group, JFXButton>, TableCell<Group, JFXButton>> cellFactory = (param) -> {
            final TableCell<Group, JFXButton> cell = new TableCell<Group, JFXButton>() {
                @Override
                protected void updateItem(JFXButton iteam, boolean empty) {
                    super.updateItem(iteam, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final JFXButton delButton = new JFXButton("DEL");
                        delButton.setStyle("-fx-background-color: orangered");
                        // not work
                        //    delButton.setStyle("-fx-text-fill: whitesmoke");
                        delButton.setOnAction(event -> {
                            Group g = getTableView().getItems().get(getIndex());
                            delElement(g.getGroup_id(), g.getGroup_name());
                            initTable();
                        });
                        setGraphic(delButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        search();
        group_delete.setCellFactory(cellFactory);
        groups_table.setItems(groupsData);

    }

    boolean checkDelElement(int id) {
        if (!HandlerDb.checkIsUnique("SELECT * FROM students_table WHERE group_id = ?;", new int[]{id})) {
            HelpMethod.Message(HelpMethod.RED, "Групу не можливо видалити");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Групу не можна видалити");
            // Header Text: null
            alert.setHeaderText(null);
            alert.setContentText("Група не є порожньою!");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    @FXML
    void setAddGroup_btn(ActionEvent event) {
        if (!group_field.isVisible()) {
            group_field.setVisible(true);
            delGroup_comboBox.setVisible(false);
        } else {
            if (group_field.getText().length() != 0) {
                String name = group_field.getText().toUpperCase();

                if (!HandlerDb.checkIsUnique("SELECT * FROM groups_table WHERE group_name = ?;", new String[]{name})) {
                    HelpMethod.Message(HelpMethod.YELLOW, "Не можливо добавити групу так як група уже існує " + name);
                    group_field.setText("");
                    group_field.setVisible(false);
                    return;
                }
                HandlerDb.autoIncZero("groups_table");
                HandlerDb.executeQuery("INSERT INTO groups_table(group_name) VALUE (?);", new String[]{name});
                initTable();
                HelpMethod.Message(HelpMethod.YELLOW, "Група добавлена до бази " + name);
            }
            group_field.setText("");
            group_field.setVisible(false);
            search();
        }
    }

    //DELETE FROM TABLE
    void delElement(int group_id, String name) {
        if (!checkDelElement(group_id))
            return;
        String query = "DELETE FROM groups_table WHERE group_id = ?;";
        HandlerDb.executeQuery(query, new int[]{group_id});
        HelpMethod.Message(HelpMethod.GREEN, "Групу " + name + " видалено");
        delGroup_comboBox.getItems().clear();
        HelpMethod.fillGroup(delGroup_comboBox);
    }

    @FXML
    void setDeGroup_btn(ActionEvent event) {
        if (!delGroup_comboBox.isVisible()) {
            delGroup_comboBox.setVisible(true);
            delGroup_comboBox.getItems().clear();
            HelpMethod.fillGroup(delGroup_comboBox);
            group_field.setVisible(false);
        } else {
            String name = delGroup_comboBox.getValue().toUpperCase();
            if (name.length() != 0) {
                HandlerDb.autoIncZero("groups_table");
                int group_id = HandlerDb.getOneValue("SELECT group_id FROM  groups_table WHERE group_name = ?", new String[]{name});
                delElement(group_id, name);
                initTable();
            }
            delGroup_comboBox.setVisible(false);
        }
        search();
    }

    // Method that avoid to search by some parameters (not my copy paste)
    void search() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Group> filteredData = new FilteredList<>(groupsData, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(g -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                // maybe should be use contains instead of indexOf but it's a example from book ;)
                if (g.getGroup_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } /*else if (Integer.toString(g.getGroup_id()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }*/
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Group> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(groups_table.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        groups_table.setItems(sortedData);
    }
}