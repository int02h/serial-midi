package com.dpforge.serialmidi;

import com.dpforge.serialmidi.midi.MidiMessageHandler;
import com.dpforge.serialmidi.serial.SerialThread;
import com.dpforge.serialmidi.util.TextUtils;
import gnu.io.NRSerialPort;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class MainController {
    @FXML
    ComboBox<String> serialCombo;

    @FXML
    TextField baudRate;

    @FXML
    Button startStop;

    @FXML
    TextArea messageLog;

    @FXML
    Button clearLog;

    private SerialThread serialThread;

    @FXML
    private void initialize() {
        serialCombo.setItems(FXCollections.observableArrayList(NRSerialPort.getAvailableSerialPorts()));
        serialCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            checkStartStopEnabled();
        });

        baudRate.textProperty().addListener((observable, oldValue, newValue) -> {
            checkStartStopEnabled();
        });

        startStop.setDisable(true);
        startStop.setOnAction(event -> onStartStopClick());

        clearLog.setOnAction(event -> messageLog.clear());
    }

    private void checkStartStopEnabled() {
        startStop.setDisable(TextUtils.isNullOrEmpty(serialCombo.getValue()) || TextUtils.isNullOrEmpty(baudRate.getText()));
    }

    private void onStartStopClick() {
        if (serialThread == null) {
            final String port = serialCombo.getValue();
            final int baudRate = tryParseInt(this.baudRate.getText(), 0);
            if (baudRate > 0) {
                try {
                    serialThread = new SerialThread(port, baudRate, getMessageHandler());
                    serialThread.start();
                    startStop.setText("Stop");
                } catch (MidiUnavailableException e) {
                    showError(e.getMessage());
                }
            } else {
                showError("Invalid baud rate");
            }
        } else {
            serialThread.finish();
            serialThread = null;
            startStop.setText("Start");
        }
    }

    private void showError(final String message) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error occured");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private MidiMessageHandler getMessageHandler() throws MidiUnavailableException {
        final Receiver receiver = MidiSystem.getReceiver();
        return new MidiMessageHandler(message -> {
            messageLog.appendText(message.toString() + "\n");
            receiver.send(message, -1);
        });
    }

    private static int tryParseInt(final String value, final int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
