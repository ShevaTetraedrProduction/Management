module Managment {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires java.sql.rowset;
    requires mysql.connector.java;
    requires com.jfoenix;
    requires java.desktop;

   // opens sample.fxml;
    opens sample;
}