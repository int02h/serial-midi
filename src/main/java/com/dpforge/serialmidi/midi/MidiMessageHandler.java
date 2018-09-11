package com.dpforge.serialmidi.midi;

public class MidiMessageHandler {

    private final OnMidiMessageReadyListener listener;

    private int dataIndex;

    private BaseMidiMessage currentMessage;

    public MidiMessageHandler(final OnMidiMessageReadyListener listener) {
        this.listener = listener;
    }

    public void handle(int b) {
        if (currentMessage == null) {
            handleStatus(b);
        } else {
            currentMessage.setData(dataIndex, (byte) (b & 0xFF));
            dataIndex++;
            if (dataIndex == currentMessage.getDataCount()) {
                listener.onMidiMessageReady(currentMessage);
                currentMessage = null;
            }
        }
    }

    private void handleStatus(final int b) {
        switch ((b & 0xF0) >> 4) {
            case CCMessage.STATUS:
                currentMessage = new CCMessage(getChannel(b));
                dataIndex = 0;
                break;
        }
    }

    private static int getChannel(final int b) {
        return (b & 0x0F);
    }
}
