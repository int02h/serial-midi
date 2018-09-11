package com.dpforge.serialmidi.midi;

class CCMessage extends BaseMidiMessage {

    static final int STATUS = 0b1011;

    CCMessage(final int channel) {
        super(STATUS, channel, 2);
    }

    @Override
    public Object clone() {
        final CCMessage copy = new CCMessage(getChannel());
        copy.setData(0, data[1]);
        copy.setData(1, data[2]);
        return copy;
    }

    @Override
    public String toString() {
        return "[" + getChannel() + "] CC controller=" + data[1] + "; value=" + data[2];
    }
}
