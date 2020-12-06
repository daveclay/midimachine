package com.daveclay.midi.sequence;

import com.daveclay.midi.sequence.Measure;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Phrase {

	private List<Measure> measures;

	public Phrase() {
		this.measures = new ArrayList<Measure>();
	}

	public void loopMeasure(Measure measure, int times) {
		for (int i = 0; i < times; i++) {
			addMeasure(measure);
		}
	}

	public List<Measure> getMeasures() {
		return measures;
	}

	public void addMeasure(Measure measure) {
		measures.add(measure);
	}

	public void loopPreviousMeasuresOnce(int numberOfMeasuresToLoop) {
		loopPreviousMeasures(numberOfMeasuresToLoop, 1);
	}

	public void loopPreviousMeasures(int numberOfMeasuresToLoop, int numberOfTimes) {
		int startIndex = measures.size() - numberOfMeasuresToLoop;
		int endIndex = measures.size();
		List<Measure> measuresToLoop = measures.subList(startIndex, endIndex);
		for (int i = 0; i < numberOfTimes; i++) {
			measures.addAll(measuresToLoop);
		}
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Measure measure : measures) {
			s.append(measure);
		}

		return s.toString();
	}

	public void loopMeasureAt(int measureToLoop) {
		Measure measure = measures.get(measureToLoop);
		addMeasure(measure);
	}
}
