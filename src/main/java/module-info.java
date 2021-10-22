module th.ac.chula.cafetps {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;


    opens th.ac.chula.cafetps to javafx.fxml;
    exports th.ac.chula.cafetps;

    opens th.ac.chula.cafetps.controller to javafx.fxml;
    exports th.ac.chula.cafetps.controller;
    exports th.ac.chula.cafetps.model;
    opens th.ac.chula.cafetps.model to javafx.fxml;
}