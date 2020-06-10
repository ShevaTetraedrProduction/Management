package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Class extends RecursiveTreeObject<Class> {
    private final IntegerProperty idC = new SimpleIntegerProperty();
    private final StringProperty nameC = new SimpleStringProperty();
    private final StringProperty groupC = new SimpleStringProperty();
    private final StringProperty teacherC = new SimpleStringProperty();


    public int getId() {
        return idC.get();
    }

    public IntegerProperty idProperty() {
        return idC;
    }

    public void setId(int idC) {
        this.idC.set(idC);
    }

    public String getName() {
        return nameC.get();
    }

    public StringProperty nameProperty() {
        return nameC;
    }

    public void setName(String nameC) {
        this.nameC.set(nameC);
    }

    public String getGroup() {
        return groupC.get();
    }

    public StringProperty groupProperty() {
        return groupC;
    }

    public void setGroup(String groupC) {
        this.groupC.set(groupC);
    }

    public String getTeacher() { return teacherC.get(); }

    public StringProperty teacherProperty() { return teacherC; }

    public void setTeacher(String teacherC) { this.teacherC.set(teacherC); }
}
