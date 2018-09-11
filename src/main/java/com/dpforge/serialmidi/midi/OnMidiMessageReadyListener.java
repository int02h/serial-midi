package com.dpforge.serialmidi.midi;

import javax.sound.midi.MidiMessage;

public interface OnMidiMessageReadyListener {
    void onMidiMessageReady(MidiMessage message);
}
