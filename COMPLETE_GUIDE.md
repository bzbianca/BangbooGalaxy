# 🎮 Bangboo Galaxy - Complete Asset Customization Guide

## 📁 **Complete Folder Structure**

```
BangbooGalaxy/
├── songs/                    # 🎵 Music files (12 default songs)
├── arrows/                   # 🎯 Arrow images (8 files with glow)
├── backgrounds/              # 🖼️ Background images (4 files)
├── fonts/                    # 🔤 Custom fonts (2 files)
├── sounds/                   # 🔊 Sound effects (5 files)
├── maps/                     # 🗺️ Song maps/beatmaps (.map files)
├── config/                   # ⚙️ Configuration files
├── ui/                       # 🎨 Complete UI asset system
│   ├── buttons/              # 🔘 Button images (14 files)
│   ├── numbers/              # 🔢 Number images (0-9, 10 files)
│   ├── text/                 # 📝 Text images (14 files)
│   └── meters/               # 📊 Meter images (5 files)
├── RhythmGame.java          # 📜 Main game code
└── COMPLETE_GUIDE.md        # 📖 This comprehensive guide
```

---

## 🎨 **Complete Asset Replacement System**

### **🔘 Button Images - `ui/buttons/` (14 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `play_button.png` | Main menu play button | 350x90px |
| `play_button_hover.png` | Play button hover state | 350x90px |
| `settings_button.png` | Main menu settings button | 350x90px |
| `settings_button_hover.png` | Settings button hover | 350x90px |
| `back_button.png` | Back navigation button | 250x60px |
| `back_button_hover.png` | Back button hover | 250x60px |
| `resume_button.png` | Pause menu resume | 250x60px |
| `resume_button_hover.png` | Resume button hover | 250x60px |
| `quit_button.png` | Pause menu quit | 250x60px |
| `quit_button_hover.png` | Quit button hover | 250x60px |
| `song_selection_button.png` | Song selection button | 350x90px |
| `song_selection_button_hover.png` | Song selection hover | 350x90px |
| `fever_button.png` | Fever mode button | 250x60px |
| `fever_button_hover.png` | Fever button hover | 250x60px |

### **🔢 Number Images - `ui/numbers/` (10 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `number_0.png` through `number_9.png` | Score/combo display | 30x40px |

### **📝 Text Images - `ui/text/` (14 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `title_text.png` | Main menu game title | 600x100px |
| `score_text.png` | Game "Score:" label | 150x40px |
| `combo_text.png` | Game "Combo:" label | 150x40px |
| `fever_text.png` | Fever mode indicator | 100x40px |
| `paused_text.png` | Pause menu "PAUSED" | 300x80px |
| `settings_title.png` | Settings screen title | 400x80px |
| `song_selection_title.png` | Song selection title | 400x80px |
| `music_volume_text.png` | "Music Volume" label | 200x40px |
| `sfx_volume_text.png` | "SFX Volume" label | 200x40px |
| `keybinds_text.png` | "Keybinds" label | 200x40px |
| `lane_1_text.png` through `lane_4_text.png` | Lane labels | 100x30px |

### **📊 Meter Images - `ui/meters/` (5 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `fever_meter_bg.png` | Fever meter background | 20x200px |
| `fever_meter_fill.png` | Fever meter fill | 20x200px |
| `fever_meter_glow.png` | Fever meter glow effect | 20x200px |
| `health_meter_bg.png` | Health meter background | 20x200px |
| `health_meter_fill.png` | Health meter fill | 20x200px |

### **🎯 Arrow Images - `arrows/` (4 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `arrow_left.png`, `arrow_up.png`, `arrow_down.png`, `arrow_right.png` | Gameplay arrows | 64x64px |
| `arrow_left_glow.png`, `arrow_up_glow.png`, `arrow_down_glow.png`, `arrow_right_glow.png` | Arrow glow effects (when key pressed) | 74x74px (slightly larger for glow effect) |

### **🖼️ Background Images - `backgrounds/` (4 files)**

| File | Usage | Recommended Size |
|------|--------|------------------|
| `main_menu_bg.png` | Main menu background | 1920x1080px |
| `game_bg.png` | Gameplay background | 1920x1080px |
| `settings_bg.png` | Settings screen background | 1920x1080px |
| `song_selection_bg.png` | Song selection background | 1920x1080px |

### **�️ Song Maps - `maps/` (Custom beatmaps)**

| File | Usage | Format |
|------|--------|--------|
| `song-name.map` | Custom beatmap for song | Text file with timing data |

#### **Map File Format:**
```
# Song Map File
bpm=120.0
offset=0.0
# Format: time|lane (time in seconds, lane: 0-3)
# Lane mapping: 0=Left, 1=Up, 2=Down, 3=Right
2.000|0
3.000|1
4.000|2
5.000|3
```

#### **Features:**
- ✅ **Auto-generation** - Maps created automatically for all songs
- ✅ **Custom editing** - Edit .map files for precise note placement
- ✅ **BPM support** - Adjustable tempo per song
- ✅ **Offset timing** - Sync notes perfectly with audio
- ✅ **Persistent** - Maps saved and reused automatically

### **🎵 Music Files - `songs/` (12 default songs)**

| File | Usage | Format |
|------|--------|--------|
| `placeholder1.mp3` | Placeholder song 1 | MP3/WAV |
| `placeholder2.mp3` | Placeholder song 2 | MP3/WAV |
| `placeholder3.mp3` | Placeholder song 3 | MP3/WAV |
| `placeholder4.mp3` | Placeholder song 4 | MP3/WAV |
| `placeholder5.mp3` | Placeholder song 5 | MP3/WAV |
| `placeholder6.mp3` | Placeholder song 6 | MP3/WAV |
| `placeholder7.mp3` | Placeholder song 7 | MP3/WAV |
| `placeholder8.mp3` | Placeholder song 8 | MP3/WAV |
| `placeholder9.mp3` | Placeholder song 9 | MP3/WAV |
| `placeholder10.mp3` | Placeholder song 10 | MP3/WAV |
| `placeholder11.mp3` | Placeholder song 11 | MP3/WAV |
| `placeholder12.mp3` | Placeholder song 12 | MP3/WAV |
| *Your songs* | Custom songs | MP3/WAV |

### **🔊 Sound Effects - `sounds/` (5 files)**

| File | Usage | Format |
|------|--------|--------|
| `hit.wav` | Note hit sound | WAV/MP3 |
| `miss.wav` | Note miss sound | WAV/MP3 |
| `fever_start.wav` | Fever mode activation | WAV/MP3 |
| `menu_click.wav` | Button click sound | WAV/MP3 |
| `key_press.wav` | Key press sound (when pressing lane keys) | WAV/MP3 |

---

## ⚙️ **Configuration System**

### **Settings File** `config/settings.txt`

```
# Volume Settings (automatically managed)
music_volume=1.0
sfx_volume=0.8
```

### **Features:**
- ✅ **Auto-save** volume settings when changed
- ✅ **Auto-load** on game startup
- ✅ **Persistent** between sessions
- ✅ **Manual editing** possible

---

## 🎮 **Game Features**

### **🎵 Music System**
- **12 placeholder songs:** Placeholder 1 through Placeholder 12
- **Variable difficulty:** Songs rated 1-6 difficulty levels
- **Folder-based:** Drop `.mp3`/`.wav` files in `songs/` for custom songs
- **Auto-detection:** Songs appear in selection menu automatically
- **Image support:** `backgrounds/song-name.png` for custom album art
- **Beat mapping:** Custom maps auto-generated and saved for each song
- **Map editing:** Edit `.map` files for precise note placement
- **Dynamic timing:** Song duration calculated from map data
- **Auto-completion:** Songs end automatically after all notes

### **🗺️ Song Mapping System**
- **Auto-generation:** Creates basic beat patterns for all songs
- **Custom maps:** Edit `.map` files for professional-level charts
- **BPM support:** Adjustable tempo per song
- **Offset timing:** Perfect sync with audio
- **Persistent storage:** Maps saved and reused automatically
- **Lane mapping:** 0=Left, 1=Up, 2=Down, 3=Right
- **Precise timing:** Notes placed to 0.001 second accuracy

### **⏱️ Timer & Results System**
- **Dynamic duration:** Song length calculated from map data
- **Auto-completion:** Song ends when all notes played + 5 seconds
- **Detailed scoring:** Tracks Perfect, Good, Okay, and Miss counts
- **Rank system:** S, A, B, C, F grades based on performance
- **Results dialog:** Shows comprehensive performance breakdown
- **Retry option:** Instant replay of the same song
- **Color-coded ranks:** Gold (S), Silver (A), Bronze (B), Blue (C), Red (F)
- **Performance metrics:** Score, combo, and accuracy tracking

### **🎯 Gameplay**
- **4 lanes:** Customizable arrow graphics with glow effects
- **Dynamic difficulty:** Adjustable note speed and spawn rate
- **Visual feedback:** Particle effects and fever mode
- **Score system:** Number-based scoring display
- **Arrow glow effects:** Arrows glow when lane keys are pressed
- **Key press sounds:** Audio feedback when pressing lane keys
- **Custom glow images:** Replace glow effects with custom graphics

### **⏸️ Pause Menu**
- **ESC key:** Toggle pause during gameplay
- **Centered overlay:** Clean pause interface
- **Navigation:** Resume, song selection, quit options

### **🎵 Song Selection**
- **Horizontal grid:** Visual song cards
- **Custom images:** Album art support
- **Easy browsing:** Left-to-right scrolling

### **⚙️ Settings Panel**
- **Full window:** Uses entire window space
- **Volume sliders:** Music and SFX controls
- **Keybind rebinding:** Customizable lane keys
- **Auto-save:** Settings persist

---

## 🚀 **Quick Start Guide**

### **Step 1: Run Game**
```bash
java RhythmGame
```
- All folders created automatically
- Placeholder files generated
- Default settings applied

### **Step 2: Add Custom Assets**
```bash
# Replace buttons
cp my_play.png ui/buttons/play_button.png
cp my_settings.png ui/buttons/settings_button.png

# Replace numbers
cp my_0.png ui/numbers/number_0.png
cp my_1.png ui/numbers/number_1.png
# ... continue for 0-9

# Add music
cp my_song.mp3 songs/my_song.mp3
cp my_song_art.png backgrounds/my_song.png

# Run game - all assets loaded automatically!
java RhythmGame
```

### **Step 3: Configure Settings**
1. **Go to Settings** → Click "⚙️ Settings" button
2. **Adjust volumes** → Use sliders for music/SFX
3. **Change keybinds** → Click lane buttons, press desired keys
4. **Settings auto-save** → Changes persist between sessions

---

## 🎨 **Design Recommendations**

### **Button Design:**
- **Consistent style** across all buttons
- **Clear hover states** (different from normal)
- **High contrast** for visibility
- **Readable text** or clear icons

### **Number Design:**
- **Equal width** for all digits (0-9)
- **Clear readability** at small sizes
- **Anti-aliased edges** for smoothness
- **Consistent style** with other UI elements

### **Text Design:**
- **Readable fonts** at various sizes
- **Proper contrast** with backgrounds
- **Multi-language support** capability
- **Professional typography**

### **Meter Design:**
- **Clear progress indication**
- **Smooth gradients** for fills
- **Glow effects** for active states
- **Consistent sizing** across all meters

---

## 📋 **Complete Asset Checklist**

### **Total Assets Required: 59 Files + Maps**

#### **Songs (12 default):**
- [ ] `placeholder1.mp3`, `placeholder2.mp3`, `placeholder3.mp3`, `placeholder4.mp3`
- [ ] `placeholder5.mp3`, `placeholder6.mp3`, `placeholder7.mp3`, `placeholder8.mp3`
- [ ] `placeholder9.mp3`, `placeholder10.mp3`, `placeholder11.mp3`, `placeholder12.mp3`
- *Plus any custom songs you add*

#### **Song Maps (auto-generated):**
- [ ] `placeholder1.map`, `placeholder2.map`, `placeholder3.map`, `placeholder4.map`
- [ ] `placeholder5.map`, `placeholder6.map`, `placeholder7.map`, `placeholder8.map`
- [ ] `placeholder9.map`, `placeholder10.map`, `placeholder11.map`, `placeholder12.map`
- *Maps auto-created for all songs, including custom ones*

#### **Buttons (14):**
- [ ] `play_button.png` + hover
- [ ] `settings_button.png` + hover  
- [ ] `back_button.png` + hover
- [ ] `resume_button.png` + hover
- [ ] `quit_button.png` + hover
- [ ] `song_selection_button.png` + hover
- [ ] `fever_button.png` + hover

#### **Numbers (10):**
- [ ] `number_0.png` through `number_9.png`

#### **Text (14):**
- [ ] `title_text.png`
- [ ] `score_text.png`, `combo_text.png`, `fever_text.png`
- [ ] `paused_text.png`
- [ ] `settings_title.png`, `song_selection_title.png`
- [ ] `music_volume_text.png`, `sfx_volume_text.png`, `keybinds_text.png`
- [ ] `lane_1_text.png` through `lane_4_text.png`

### **Map Creation Guide:**
1. **Auto-generate:** Maps created automatically when you first play a song
2. **Edit maps:** Open `.map` files in text editor for precise editing
3. **BPM setting:** Adjust `bpm=` value to match song tempo
4. **Offset timing:** Use `offset=` to sync notes with audio beats
5. **Note placement:** Add lines like `2.500|1` (time in seconds, lane 0-3)
6. **Test and refine:** Play song and adjust timing as needed

### **Rank System Guide:**
- **S Rank (Gold):** 95%+ Perfect hits - Elite performance!
- **A Rank (Silver):** 85%+ Perfect hits - Excellent playing
- **B Rank (Bronze):** 70%+ Perfect hits - Good performance
- **C Rank (Blue):** 50%+ Perfect hits - Decent playing
- **F Rank (Red):** Below 50% Perfect - Keep practicing!

#### **Scoring Breakdown:**
- **Perfect:** Maximum points, maintains combo
- **Good:** High points, maintains combo
- **Okay:** Medium points, maintains combo
- **Miss:** No points, breaks combo

#### **Results Screen Features:**
- **Song title display** - Shows completed song
- **Rank with color** - Visual performance indicator
- **Total score** - Points earned
- **Detailed stats** - Perfect/Good/Okay/Miss counts
- **Retry button** - Play same song again
- **Song Selection** - Return to song menu

### **Advanced Mapping:**
- **Double notes:** Place multiple notes at same time for challenges
- **Hold notes:** (Future feature) Extended note timing
- **BPM changes:** (Future feature) Variable tempo within songs
- **Difficulty curves:** Start easy, increase complexity gradually

#### **Meters (5):**
- [ ] `fever_meter_bg.png`, `fever_meter_fill.png`, `fever_meter_glow.png`
- [ ] `health_meter_bg.png`, `health_meter_fill.png`

#### **Arrows (8):**
- [ ] `arrow_left.png`, `arrow_up.png`, `arrow_down.png`, `arrow_right.png`
- [ ] `arrow_left_glow.png`, `arrow_up_glow.png`, `arrow_down_glow.png`, `arrow_right_glow.png`

#### **Backgrounds (4):**
- [ ] `main_menu_bg.png`, `game_bg.png`, `settings_bg.png`, `song_selection_bg.png`

#### **Sounds (5):**
- [ ] `hit.wav`, `miss.wav`, `fever_start.wav`, `menu_click.wav`
- [ ] `key_press.wav` (key press audio feedback)

---

## 🔧 **Technical Implementation**

### **Asset Access Methods:**
```java
// Get any custom asset
BufferedImage playButton = GameAssets.getButtonImage(GameAssets.PLAY_BUTTON);
BufferedImage digit5 = GameAssets.getNumberImage(5);
BufferedImage titleText = GameAssets.getTextImage(GameAssets.TITLE_TEXT);
BufferedImage feverMeter = GameAssets.getMeterImage(GameAssets.FEVER_METER_FILL);
```

### **Configuration Methods:**
```java
// Save settings (automatic)
GameAssets.saveConfiguration();

// Load configuration (automatic on startup)
GameAssets.loadAllAssets();
```

### **Game Constants:**
```java
// Easy customization in GameAssets class
public static final int NOTE_SPEED = 5;
public static final int BUTTON_WIDTH = 350;
public static final Color PRIMARY_COLOR = new Color(255, 150, 0);
public static final String GAME_TITLE = "Bangboo Galaxy";
```

---

## 🎯 **How It Works**

### **Automatic System:**
1. **Game starts** → Creates all folders automatically
2. **Assets loaded** → Each PNG file loaded and cached
3. **Fallbacks used** → Generated graphics if files missing
4. **Configuration saved** → Settings persistent
5. **Instant updates** → No code changes needed

### **Asset Replacement:**
1. **Create image** → Match exact filename
2. **Place in folder** → Correct subfolder
3. **Restart game** → Assets loaded automatically
4. **Complete transformation** → Entire game customized

### **Settings Persistence:**
1. **Change settings** → Volume sliders, keybinds
2. **Auto-save** → Settings saved to `config/settings.txt`
3. **Auto-load** → Settings applied on next startup
4. **Manual edit** → Can edit file directly

---

## 🎮 **Game Controls**

### **Default Keybinds:**
- **Lane 1:** Z key
- **Lane 2:** X key  
- **Lane 3:** , key
- **Lane 4:** . key
- **Pause:** ESC key

### **Menu Navigation:**
- **Mouse:** Click buttons and song cards
- **Keyboard:** Arrow keys for navigation, Enter to select
- **Customizable:** Change keybinds in settings

---

## 🎉 **Summary**

### **What You Get:**
- ✅ **Complete visual customization** - 59 replaceable assets
- ✅ **Arrow glow effects** - Dynamic feedback when keys pressed
- ✅ **Key press sounds** - Audio feedback for lane keys
- ✅ **12 placeholder songs** - Placeholder 1 through Placeholder 12
- ✅ **Song mapping system** - Auto-generated and custom beatmaps
- ✅ **Custom song support** - Add your own music with auto-mapping
- ✅ **Professional beat mapping** - Edit .map files for precise charts
- ✅ **Dynamic timer system** - Songs auto-complete based on map duration
- ✅ **Comprehensive results** - Detailed scoring with Perfect/Good/Okay/Miss
- ✅ **Rank system** - S, A, B, C, F grades with color coding
- ✅ **Results dialog** - Beautiful performance breakdown with retry option
- ✅ **Automatic folder creation** - No setup required
- ✅ **Fallback system** - Works without custom assets
- ✅ **Persistent settings** - Volume and preferences saved
- ✅ **Modern UI** - Square window, horizontal song grid
- ✅ **Professional features** - Pause menu, settings, keybinds
- ✅ **Easy asset management** - Just replace files, no coding

### **Perfect For:**
- **Custom themes** - Create your own visual style
- **Branded games** - Add your logos and branding
- **Educational purposes** - Learn game asset management
- **Portfolio projects** - Showcase customizable game design
- **Fun customization** - Make the game uniquely yours
- **Rhythm game creators** - Design custom beatmaps
- **Music enthusiasts** - Add your favorite songs
- **Game developers** - Study mapping system implementation
- **Performance tracking** - Analyze gameplay with detailed results
- **Competitive play** - Achieve S rank on all songs

### **Total Transformation:**
With this system, you can completely transform every visual element:
- **Every button** custom styled
- **All numbers** in displays  
- **Every text label** custom graphics
- **All meters and gauges** custom design
- **Arrow graphics** custom styled with glow effects
- **Backgrounds** custom themes
- **Sound effects** custom audio including key press sounds
- **12 placeholder songs** with varying difficulty levels
- **Custom beatmaps** for precise note placement
- **Your own music** with auto-generated maps
- **Dynamic timers** based on song duration
- **Beautiful results** with ranking system

**Simply replace the 59 placeholder files with your custom assets, add your music, and the entire game transforms to your unique vision!** 🎮✨

---

## 🚀 **Ready to Play!**

1. **Run the game:** `java RhythmGame`
2. **Explore the interface:** Check all menus and features
3. **Add your assets:** Replace placeholder files with your custom designs
4. **Add your music:** Drop MP3/WAV files in `songs/` folder
5. **Customize maps:** Edit `.map` files for precise note placement
6. **Configure settings:** Adjust volume and keybinds to your preference
7. **Play and rank:** Complete songs to see your performance results
8. **Enjoy your fully customized rhythm game!**

**The game is now a complete canvas for your creative expression!** 🎨🎵
