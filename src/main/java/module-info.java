module com.p11.practica11 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.p11.practica11 to javafx.fxml;
    exports com.p11.practica11;
}