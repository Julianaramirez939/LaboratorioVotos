module org.example.laboratoriovotos1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.laboratoriovotos1 to javafx.fxml;
    exports org.example.laboratoriovotos1;
}