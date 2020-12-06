Overview
--------
If you have ended up reading this without already knowing the overview, you are most certainly in the wrong place.

But now that you're here, you should read on, unmute your audio, and play with it anyway.

Requirements
-----------
Much of this code hasn't been tested in a decade or more.

What testing has been done has worked fine for compiling and running in java 7, 8, and 11.

The code itself, according to the pom, is adhering to source target standards of 1.6.

Using java 7 or earlier comes with its own problems, but if you're doing that, you are already aware of this fact, or you've lost touch with the consensus reality anyway.

The requirements are minimal and will be taken care of by maven.

Speaking of which, maven is required.

Building and Running
--------------------
Building is simply

    mvn compile
and then there are several classes with main methods that can be invoked with the exec plugin.

However, the ones that are known to work currently are the One Groove Wonder apps that are most easily invoked with the
`run-ogw.sh`
script.

These are described below in the section on "The Wrapper Script".



The Wrapper Script
------------------
For now, to keep things moving, the One Groove Wonder apps (all moved into the ogws package) can be fired off with an included run-ogws.sh.

The syntax is:

    ./run-ogw.sh ClassName
   or

    ./run-ogw.sh ClassName output-file.mid

*(Currently, the wrapper script does **not** pass further args through to the java main method.)*

**OWGS Apps** (as of the time of this writing):
* DrumSolo
* FolkMusic
* PianoSolo
* RockMusic
* TripHopMusic

So, an example would be:

    ./run-ogw.sh FolkMusic ~/Desktop/Folk-Hit.mid
to save a midi file, or

    ./run-ogw.sh TripHopMusic
to simply listen for pleasure.


Ticks, Beats, Positions and Patterns
------------------------------------
An event can be fired on a "tick". The app uses a set value of 128 ticks per beat* to trigger events. This gives a finite, limited resolution of 128th notes. At 120 BPM with 128 ticks per beat, a tick can trigger an event every 1/256ths of a second. A "beat" is a measure of musical timing - in 4/4 time, there are 4 beats in a measure. The random midi machine does not really care about "beats" when calculating ticks, patterns, and positions.

A track "pattern" can have an arbitrary length, measured in ticks. A track pattern whose length is 2048 ticks long would represent 16 beats: 2048 ticks / 128 ticks per beat = 16 beats. Since an event can be fired on any tick, the core API allows a configuration for each of those 2048 tick positions. In the UI, however, there is only so much "screen real estate", and so subdivisions of those 2048 tick positions are used.

This subdivision of ticks for use in the UI represent "positions". As a user, you can set the pattern length in ticks as well as the "positions" as subdivisions of 2048 ticks. For example, let's say you choose a pattern length of 2048 ticks (that's at 128 ticks per beat). Now, you can choose to break those 2048 ticks into equally-spaced positions. You might choose to subdivide those 2048 ticks into 32 positions. This gives you 1 position for every 64 ticks. A position at every 64 ticks at 128 ticks per beat represent 2 positions per "beat", or eighth notes in a 4/4 time signature. Or, you could choose to subdivide 2048 ticks into 64 positions, giving you 1 position for every 32 ticks, or 4 positions per "beat" - sixteenth notes. Subdividing your pattern length up by more positions allows you to configure faster notes.

This can be useful for programming a drum pattern with a length of 2048 ticks into eighth notes, while a bass with a pattern length of 2048 ticks might only require quarter notes, and a piano solo pattern of 2048 ticks might want thirty-second notes.

The UI defaults to 32 positions visible at a time. As the clock moves through each tick, the indicator will light up the position. If more than 32 positions are configured, the pattern will move to the next "bank" of positions automatically (unless the don't auto progress button is set).

* Presently there is no reason that this needs to be the limit - it just happens to be easier to deal in a set number of ticks per beat in order to calculate note-on and note-off events.

Some Math
---------
2048 / 32 = 64
