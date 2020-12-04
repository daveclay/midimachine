package com.daveclay.midi.realtime.ui;

import com.daveclay.midi.NoteValueRange;
import com.daveclay.midi.random.RandomNoteStrategy;
import com.daveclay.midi.realtime.PositionNoteStrategy;
import com.daveclay.midi.realtime.RealtimeTrack;
import com.daveclay.midi.realtime.ui.knob.DKnob;

import javax.swing.*;

import static com.daveclay.midi.realtime.ui.util.Position.position;
import static com.daveclay.midi.realtime.ui.util.Size.fit;

/**
 * Todo: this needs to be reusable for an existing RandomNoteStrategy instance, which will require setting all the panel
 * values when setRandomNoteStrategy(..) is called
*/
public class RandomNoteStrategyPanel extends JPanel {

	public static final int MIN_DURATION_VALUE = 2;
	public static final int MAX_DURATION_VALUE = 2048;
	public static final int MIN_VELOCITY_VALUE = 0;
	public static final int MAX_VELOCITY_VALUE = 127;
	public static final int MIN_NOTES_VALUE = 1;
	public static final int MAX_NOTES_VALUE = 16;

	private RealtimeTrack track;
	
	private DKnob maxDurationField;
	private DKnob minDurationField;
	private MinMaxPanel durationPanel;

	private DKnob maxVelocityField;
	private DKnob minVelocityField;
	private MinMaxPanel velocityPanel;

	private DKnob maxNotesField;
	private DKnob minNotesField;
	private MinMaxPanel notesPanel;

	private NoteValueRangeConfigPanel noteValueRangeConfigPanel;

	public RandomNoteStrategyPanel(RealtimeTrack track) {
		this.track = track;
		buildGUI();
	}

	public void buildGUI() {
		buildDurationPanel();
		buildVelocityPanel();
		buildNotesPanel();

		noteValueRangeConfigPanel = new NoteValueRangeConfigPanel(track);

		position(durationPanel).atOrigin().in(this);
		position(velocityPanel).toTheRightOf(durationPanel).in(this);
		position(notesPanel).toTheRightOf(velocityPanel).in(this);
		position(noteValueRangeConfigPanel).toTheRightOf(notesPanel).in(this);

		fit(durationPanel, velocityPanel, notesPanel, noteValueRangeConfigPanel).in(this);
	}

	private void buildDurationPanel() {
		this.maxDurationField = newDurationKnob();
		this.minDurationField = newDurationKnob();
		durationPanel = new MinMaxPanel("DURATION", this.minDurationField, this.maxDurationField);
	}

	private DKnob newDurationKnob() {
		return new DKnob(MIN_DURATION_VALUE, MAX_DURATION_VALUE);
	}

	private void buildVelocityPanel() {
		this.maxVelocityField = newVelocityKnob();
		this.minVelocityField = newVelocityKnob();
		velocityPanel = new MinMaxPanel("VELOCITY", this.minVelocityField, this.maxVelocityField);
	}


	private DKnob newVelocityKnob() {
		return new DKnob(MIN_VELOCITY_VALUE, MAX_VELOCITY_VALUE);
	}

	private void buildNotesPanel() {
		this.maxNotesField = newNotesKnob();
		this.minNotesField = newNotesKnob();
		notesPanel = new MinMaxPanel("NOTES", this.minNotesField, this.maxNotesField);
	}

	private DKnob newNotesKnob() {
		return new DKnob(MIN_NOTES_VALUE, MAX_NOTES_VALUE);
	}

	public void updateForStrategy(PositionNoteStrategy positionNoteStrategy) {
		// Todo: casting here - for now, it's the only one. if not, we're going to have to do some Visitor style work
		RandomNoteStrategy randomNotesStrategy = (RandomNoteStrategy) positionNoteStrategy.getRuntimeNotesStrategy();

		minNotesField.setMappedValue(randomNotesStrategy.getMinNumberOfNotes());
		maxNotesField.setMappedValue(randomNotesStrategy.getMaxNumberOfNotes());
		minDurationField.setMappedValue(randomNotesStrategy.getMinDuration());
		maxDurationField.setMappedValue(randomNotesStrategy.getMaxDuration());
		minVelocityField.setMappedValue(randomNotesStrategy.getMinVelocity());
		maxVelocityField.setMappedValue(randomNotesStrategy.getMaxVelocity());

		noteValueRangeConfigPanel.updateForRange(randomNotesStrategy.getNoteValueRange());
	}

	/**
	 * Return this RandomNoteStrategyPanel as a RandomNoteStrategy implementation that pulls values from the
	 * GUI knobs.
	 */
	public RandomNoteStrategy asRandomNoteStrategy() {
		return randomNoteStrategy;
	}

	private RandomNoteStrategy randomNoteStrategy = new RandomNoteStrategy() {
		
		@Override
		public int getMinDuration() {
			return minDurationField.getMappedValue();
		}

		@Override
		public int getMaxDuration() {
			return maxDurationField.getMappedValue();
		}

		@Override
		public int getMinVelocity() {
			return minVelocityField.getMappedValue();
		}

		@Override
		public int getMaxVelocity() {
			return maxVelocityField.getMappedValue();
		}

		@Override
		public int getMinNumberOfNotes() {
			return minNotesField.getMappedValue();
		}

		@Override
		public int getMaxNumberOfNotes() {
			return maxNotesField.getMappedValue();
		}

		@Override
		public NoteValueRange getNoteValueRange() {
			return noteValueRangeConfigPanel.getNoteValueRange();
		}
	};
}
