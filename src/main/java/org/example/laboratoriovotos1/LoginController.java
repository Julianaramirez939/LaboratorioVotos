package org.example.laboratoriovotos1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController {

    @FXML
    private Label mensajeLbl; // Etiqueta para mostrar mensaje de validación de ingreso al usuario

    @FXML
    private Button ingresarBtn; // Botón para ingresar a votar

    @FXML
    private TextField idTxt; // Campo de texto para ingresar ID

    @FXML
    private Button salirBtn; // Botón para salir

    // Archivo donde se guardan los IDs que ya han votado
    private static final String RUTA_ARCHIVO_VOTOS = "identificaciones.txt";

    @FXML
    public void salirBtnOnAction(ActionEvent event) {
        // Cierra la ventana actual cuando se hace clic en el botón "Salir"
        Stage stage = (Stage) salirBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ingresarBtnOnAction(ActionEvent event) {
        // Obtiene el ID ingresado por el usuario y elimina espacios en blanco al inicio y al final
        String idIngresado = idTxt.getText().trim();

        // Verifica si el ID está vacío
        if (idIngresado.isBlank()) {
            mensajeLbl.setText("Su ID es incorrecto");
        } else {
            // Verifica si el ID ya ha votado
            if (verificarID(idIngresado)) {
                mensajeLbl.setText("Usted ya ha votado");
            } else {
                // Guarda el ID en el archivo y abre la ventana de votación
                guardarID(idIngresado);
                mensajeLbl.setText("Ingresando...");
                abrirNuevaVentana();
            }
        }
    }

    private boolean verificarID(String id) {
        // Lee el archivo de IDs y verifica si el ID ingresado ya está registrado
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_VOTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().equals(id)) {
                    return true; // ID ya registrado
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // ID no registrado
    }

    private void guardarID(String id) {
        // Guarda el ID en el archivo
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO_VOTOS, true))) {
            writer.println(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirNuevaVentana() {
        // Abre la ventana de votación
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("votos-view.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual (ventana de login)
            Stage stageActual = (Stage) ingresarBtn.getScene().getWindow();

            // Crear un nuevo Stage para la ventana de votación
            Stage stageVotos = new Stage();
            stageVotos.setTitle("Nueva Ventana");
            stageVotos.setScene(new Scene(root));

            // Configurar el controlador de la ventana de votación
            VotosController votosController = loader.getController();
            votosController.setStageLogin(stageActual);

            // Ocultar la ventana de inicio
            stageActual.hide();

            // Mostrar la ventana de votación
            stageVotos.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}