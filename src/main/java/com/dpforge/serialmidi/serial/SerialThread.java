package com.dpforge.serialmidi.serial;

import com.dpforge.serialmidi.midi.MidiMessageHandler;
import gnu.io.NRSerialPort;

import java.io.DataInputStream;
import java.io.IOException;

public class SerialThread extends Thread {

    private volatile boolean running;

    private final String port;
    private final int baudRate;
    private final MidiMessageHandler messageHandler;

    public SerialThread(final String port, final int baudRate, final MidiMessageHandler messageHandler) {
        this.port = port;
        this.baudRate = baudRate;
        this.messageHandler = messageHandler;
    }

    public void finish() {
        running = false;
    }

    @Override
    public synchronized void start() {
        running = true;
        super.start();
    }

    @Override
    public void run() {
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();

        System.out.println("Start reading loop");
        try {
            startLoop(serial);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            serial.disconnect();
        }
        System.out.println("Reading loop finished");
    }

    private void startLoop(final NRSerialPort serial) throws IOException {
        try (DataInputStream ins = new DataInputStream(serial.getInputStream())) {
            while (running) {
                byte b = (byte) ins.read();
                if (b != -1) {
                    messageHandler.handle(b);
                }
            }
        }
    }
}
