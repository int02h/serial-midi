package com.dpforge.serialmidi.midi;

import javax.sound.midi.MidiMessage;

abstract class BaseMidiMessage extends MidiMessage {

    BaseMidiMessage(final int status, final int channel, final int dataCount) {
        super(new byte[dataCount + 1]);
        data[0] = (byte) ((status << 4) | (channel & 0x0F));
    }

    int getDataCount() {
        return data.length - 1;
    }

    int getChannel() {
        return data[0] & 0x0F;
    }

    void setData(final int index, final byte b) {
        data[index + 1] = b;
    }
}
