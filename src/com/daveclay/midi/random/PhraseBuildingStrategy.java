package com.daveclay.midi.random;

import com.daveclay.midi.sequence.Measure;
import com.daveclay.midi.sequence.Phrase;

/**
*/
public class PhraseBuildingStrategy {

	private int numberOfMeasuresInPhrase = -1;

	public void addToMeasure(RandomInstrument randomInstrument, Phrase songPart, int measureInPhrase, int numberOfMeasuresInSongPart, Measure measure) {

		int adjustedNumberOfMeasuresInPhrase = numberOfMeasuresInPhrase;
		if (numberOfMeasuresInSongPart < numberOfMeasuresInPhrase) {
			adjustedNumberOfMeasuresInPhrase = numberOfMeasuresInSongPart;
		}

		if (adjustedNumberOfMeasuresInPhrase < 0 || measureInPhrase < adjustedNumberOfMeasuresInPhrase) {
			randomInstrument.addToMeasure(measure);
			songPart.addMeasure(measure);
		} else {
			int whichPhraseIsNowBeingLooped = measureInPhrase % adjustedNumberOfMeasuresInPhrase;
			songPart.loopMeasureAt(whichPhraseIsNowBeingLooped);
		}
	}

	public int getNumberOfMeasuresInPhrase() {
		return numberOfMeasuresInPhrase;
	}

	public void setNumberOfMeasuresInPhrase(int numberOfMeasuresInPhrase) {
		this.numberOfMeasuresInPhrase = numberOfMeasuresInPhrase;
	}
}
