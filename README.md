# 🎵 melo-chord

A combination of both Java applications, MelodyMaker and ChordGenerator. Used a stacked pane to keep both applications in one place. Logic is the same as previous projects.
- ***MelodyMaker***: A simple Java application that generates a random melody in a chosen key, plays it, and exports it as a MIDI file you can use in DAWs like Ableton, Logic, or FL Studio.
- ***ChordGenerator***: JavaFX desktop application that allows users to generate chord progressions based on a chosen root note, scale type, and genre. The app converts chord progressions into MIDI files using JFugue and supports live playback and MIDI export in Format 1, compatible with DAWs like Ableton.

## Features

### MelodyMaker
- 🎼 Random melody generation based on musical key (e.g., C Major)
- 🎹 Audio playback powered by JFugue
- 💾 MIDI export
- 🖥️ GUI built with JavaFX
- 🎛️ Drag-and-drop compatibility with major DAWs
- 🧩 Modular, extensible Java codebase

### ChordGenerator
- 🎹 Selectable root note and scale type
- 🎼 Choose from genres like Pop, Rock, R&B, Jazz, and Classical
- 🔢 Dynamically update available chord progression templates per genre
- ▶️ Playback generated chord progressions using JFugue
- 💾 Export to **Format 1 MIDI**, playable in all major DAWs
- 🎛️ Simple and responsive JavaFX interface

## 🚀 Getting Started

### 📦 Prerequisites

- Java 17+
- JavaFX SDK
- Maven
- JFugue (automatically included via Maven)

```bash
mvn clean install
mvn exec:java
```
### 📦 Folder Structure
```plaintext
melo-chord/
│
├── .mvn/                          # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.melochord/
│   │   │       ├── app/           # JavaFX entry point & MIDI export logic
│   │   │       │   ├── MainApp.java
│   │   │       │   └── MidiExporter.java
│   │   │       ├── generators/    # Core music-generation code
│   │   │       │   ├── chord/      # Chord generation
│   │   │       │   │   ├── ChordGenerator.java
│   │   │       │   │   └── ChordProgressionGenerator.java
│   │   │       │   └── melody/     # Melody generation (future)
│   │   │       └── ui/            # Swing/JavaFX panels
│   │   │           ├── ChordUIPanel.java
│   │   │           └── MelodyUIPanel.java
│   │   └── resources/             # FXML, icons, config files
│   └── test/                      # Unit & integration tests
├── target/                        # Compiled classes & build artifacts
├── pom.xml                        # Maven project descriptor
└── README.md                      # Project overview & usage instructions

```
## 📤 Exporting to MIDI
The Exported MIDI file:
- Is in Format 1
- Is compatible with all modern DAWs like Ableton, FL Studio, etc.

## 🎵 Dependencies

- JavaFX – GUI framework
- JFugue – MIDI and music pattern manipulation
- javax.sound.midi – MIDI writing
> Maven handles dependencies


### 📜 License
MIT — free to use and share.

### 👋 Author
@chrispugliese
