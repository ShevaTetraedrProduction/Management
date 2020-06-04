package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Group {
    private final IntegerProperty idG = new SimpleIntegerProperty();
    private final StringProperty nameG = new SimpleStringProperty();


    public int getGroup_id() {
        return idG.get();
    }
    public IntegerProperty group_idProperty() {
        return idG;
    }
    public void setGroup_id(int idG) {
        this.idG.set(idG);
    }
    public String getGroup_name() {
        return nameG.get();
    }
    public StringProperty group_nameProperty() {
        return nameG;
    }
    public void setGroup_name(String nameG) {
        this.nameG.set(nameG);
    }
    public Group() { }
}