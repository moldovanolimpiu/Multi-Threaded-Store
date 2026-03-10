module multithreadstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens MultiThreadStore to javafx.fxml;
    exports MultiThreadStore.GUI;

}