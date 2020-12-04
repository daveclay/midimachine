Ticks, Beats, Positions and Patterns
------------------------------------

An event can be fired on a "tick". The app uses a set value of 128 ticks per beat* to trigger events. This gives a finite, limited resolution of 128th notes. At 120 BPM with 128 ticks per beat, a tick can trigger an event every 1/256ths of a second. A "beat" is a measure of musical timing - in 4/4 time, there are 4 beats in a measure. The random midi machine does not really care about "beats" when calculating ticks, patterns, and positions.

A track "pattern" can have an arbitrary length, measured in ticks. A track pattern whose length is 2048 ticks long would represent 16 beats: 2048 ticks / 128 ticks per beat = 16 beats. Since an event can be fired on any tick, the core API allows a configuration for each of those 2048 tick positions. In the UI, however, there is only so much "screen real estate", and so subdivisions of those 2048 tick positions are used.

This subdivision of ticks for use in the UI represent "positions". As a user, you can set the pattern length in ticks as well as the "positions" as subdivisions of 2048 ticks. For example, let's say you choose a pattern length of 2048 ticks (that's at 128 ticks per beat). Now, you can choose to break those 2048 ticks into equally-spaced positions. You might choose to subdivide those 2048 ticks into 32 positions. This gives you 1 position for every 64 ticks. A position at every 64 ticks at 128 ticks per beat represent 2 positions per "beat", or eighth notes in a 4/4 time signature. Or, you could choose to subdivide 2048 ticks into 64 positions, giving you 1 position for every 32 ticks, or 4 positions per "beat" - sixteenth notes. Subdividing your pattern length up by more positions allows you to configure faster notes.

This can be useful for programming a drum pattern with a length of 2048 ticks into eighth notes, while a bass with a pattern length of 2048 ticks might only require quarter notes, and a piano solo pattern of 2048 ticks might want thirty-second notes.

The UI defaults to 32 positions visible at a time. As the clock moves through each tick, the indicator will light up the position. If more than 32 positions are configured, the pattern will move to the next "bank" of positions automatically (unless the don't auto progress button is set).


* Presently there is no reason that this needs to be the limit - it just happens to be easier to deal in a set number of ticks per beat in order to calculate note-on and note-off events.


------------------------------------

2048 / 32 = 64


