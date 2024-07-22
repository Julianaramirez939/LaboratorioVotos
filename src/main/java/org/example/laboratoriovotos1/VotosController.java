package org.example.laboratoriovotos1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VotosController {

    @FXML
    private Button regresarBtn; // Botón para regresar a la ventana de inicio

    @FXML
    private Button votar1Btn; // Botón para votar por el Candidato 1

    @FXML
    private Button votar2Btn; // Botón para votar por el Candidato 2

    @FXML
    private Button votar3Btn; // Botón para votar en blanco

    @FXML
    private Button verVotosBtn; // Botón para ver los resultados de los votos

    @FXML
    private Label jornadaLbl; // Etiqueta para mostrar la jornada actual

    private Stage stageLogin; // Referencia a la ventana de inicio
    private boolean yaVotado = false; // Variable para verificar si el usuario ya ha votado

    // Ruta del archivo donde se guardan los votos
    private static final String RUTA_ARCHIVO_VOTOS = "votos.txt";

    @FXML
    private void initialize() {
        // Este método se llama automáticamente cuando se inicializa el controlador
        // Actualiza el texto del label de jornada con un valor aleatorio
        actualizarJornada();
    }

    @FXML
    private void votar1BtnOnAction() {
        // Procesa el voto para el Candidato 1
        procesarVoto("Candidato 1");
    }

    @FXML
    private void votar2BtnOnAction() {
        // Procesa el voto para el Candidato 2
        procesarVoto("Candidato 2");
    }

    @FXML
    private void votar3BtnOnAction() {
        // Procesa el voto en blanco
        procesarVoto("Voto en Blanco");
    }

    @FXML
    private void verVotosBtnOnAction() {
        // Muestra los resultados de los votos
        mostrarVotos();
    }

    @FXML
    private void regresarBtnOnAction() {
        // Muestra la ventana de inicio y cierra la ventana actual
        if (stageLogin != null) {
            stageLogin.show(); // Mostrar la ventana de inicio
        }
        // Cerrar la ventana actual
        Stage stageActual = (Stage) regresarBtn.getScene().getWindow();
        stageActual.close();
    }

    private void procesarVoto(String candidato) {
        // Verifica si el usuario ya ha votado
        if (yaVotado) {
            mostrarMensaje("Su voto ya fue realizado, no puede volver a hacerlo");
        } else {
            yaVotado = true;
            guardarVoto(candidato); // Guarda el voto en el archivo
            mostrarMensaje("Voto exitoso para " + candidato);
        }
    }

    private void guardarVoto(String candidato) {
        // Guarda el voto en el archivo
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO_VOTOS, true))) {
            writer.println(candidato);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarVotos() {
        // Muestra los resultados de los votos
        Map<String, Integer> votos = contarVotos();
        StringBuilder mensaje = new StringBuilder("Resultados de Votos:\n");
        for (Map.Entry<String, Integer> entry : votos.entrySet()) {
            mensaje.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        mostrarMensaje(mensaje.toString());
    }

    private Map<String, Integer> contarVotos() {
        // Cuenta los votos leídos desde el archivo
        Map<String, Integer> votos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_VOTOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Asegúrate de que no haya espacios adicionales en la línea
                String voto = linea.trim();
                if (!voto.isEmpty()) {
                    votos.put(voto, votos.getOrDefault(voto, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return votos;
    }

    private void mostrarMensaje(String mensaje) {
        // Muestra un mensaje en una ventana emergente
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Resultado del Voto");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void actualizarJornada() {
        // Actualiza el label de jornada con un valor aleatorio, porque son los mismos candidatos para las 3 jornadas, haciendo una simulación de que el sistema ya sabe al ingresar el ID cual es su jornada
        jornadaLbl.setText("Jornada: " + obtenerJornadaAleatoria());
    }

    private String obtenerJornadaAleatoria() {
        // Devuelve una jornada aleatoria (mañana, tarde, noche)
        String[] jornadas = {"mañana", "tarde", "noche"};
        Random random = new Random();
        int index = random.nextInt(jornadas.length);
        return jornadas[index];
    }

    // Método para configurar la referencia a la ventana de inicio
    public void setStageLogin(Stage stage) {
        this.stageLogin = stage;
    }
}