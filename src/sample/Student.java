package sample;

//import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.awt.*;

public class Student  {
    private final IntegerProperty idS = new SimpleIntegerProperty();
    private  StringProperty nameS = new SimpleStringProperty();
    private  StringProperty lastNameS = new SimpleStringProperty();
    private  StringProperty groupS = new SimpleStringProperty();
    private  IntegerProperty yearS = new SimpleIntegerProperty();


    public Student(StringProperty nameS, StringProperty lastNameS, StringProperty groupS, IntegerProperty yearS, JFXButton update) {
        this.nameS = nameS;
        this.lastNameS = lastNameS;
        this.groupS = groupS;
        this.yearS = yearS;
    }

    public Student() {

    }

    public int getId() {
        return idS.get();
    }

    public IntegerProperty idProperty() {
        return idS;
    }

    public void setId(int idS) {
        this.idS.set(idS);
    }

    public String getName() {
        return nameS.get();
    }

    public StringProperty nameProperty() {
        return nameS;
    }

    public void setName(String nameS) {
        this.nameS.set(nameS);
    }

    public String getLastName() {
        return lastNameS.get();
    }

    public StringProperty lastNameProperty() {
        return lastNameS;
    }

    public void setLastName(String lastNameS) {
        this.lastNameS.set(lastNameS);
    }

    public String getGroup() {
        return groupS.get();
    }

    public StringProperty groupProperty() {
        return groupS;
    }

    public void setGroup(String groupS) {
        this.groupS.set(groupS);
    }

    public int getYear() {
        return yearS.get();
    }

    public IntegerProperty yearProperty() {
        return yearS;
    }

    public void setYear(int yearS) {
        this.yearS.set(yearS);
    }
}