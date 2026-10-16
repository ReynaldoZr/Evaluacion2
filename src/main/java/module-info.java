module com.example.pruebacorte2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.pruebacorte2 to javafx.fxml;
    opens com.example.pruebacorte2.controller to javafx.fxml;
    opens com.example.pruebacorte2.model to javafx.base;
    exports com.example.pruebacorte2;
}
