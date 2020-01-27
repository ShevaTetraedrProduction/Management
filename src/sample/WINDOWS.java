package sample;

public enum WINDOWS{
    Authentic("authentic"),
    MENU("menu"),
    GROUPS("groups"),
    STUDENTS("students"),
    CLASSES("classes"),
    OPTION("option"),
    JOURNAL("journal"),
    ADDSTUDENT("addStudent");

    private String name;
    WINDOWS(String name) {this.name = name;}
    public String getName(){ return "fxml/" + name + ".fxml";}

}
