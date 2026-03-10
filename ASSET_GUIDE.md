# 🎨 Bangboo Galaxy - Complete Asset Customization Guide

## 📁 Folder Structure

The game now uses a comprehensive folder-based asset system for easy customization:

```
BangbooGalaxy/
├── songs/              # 🎵 Music files (.mp3, .wav)
├── arrows/             # 🎯 Arrow images (.png)
├── backgrounds/        # 🖼️ Background images (.png, .jpg)
├── fonts/              # 🔤 Custom fonts (.ttf, .otf)
├── sounds/             # 🔊 Sound effects (.wav, .mp3)
├── config/             # ⚙️ Configuration files
├── RhythmGame.java     # 📜 Main game code
└── README.md           # 📖 Basic guide
```

---

## 🎵 Songs Folder (`songs/`)

### How to Add Songs:
1. **Drag and drop** `.mp3` or `.wav` files into the `songs/` folder
2. **Restart the game** or go to song selection
3. Songs will **automatically appear** in the menu

### File Naming:
- Use descriptive names: `"electronic-beats.mp3"`, `"rock-anthem.wav"`
- Avoid special characters: `!@#$%^&*()`
- Keep names reasonably short

---

## 🎯 Arrows Folder (`arrows/`)

### Required Files:
| File | Lane | Description |
|------|------|-------------|
| `arrow_left.png` | Lane 1 | Left arrow (default: Red) |
| `arrow_up.png` | Lane 2 | Up arrow (default: Green) |
| `arrow_down.png` | Lane 3 | Down arrow (default: Blue) |
| `arrow_right.png` | Lane 4 | Right arrow (default: Yellow) |

### Image Specifications:
- **Format:** PNG with transparency
- **Recommended Size:** 64x64 pixels
- **Style:** Any design that fits your theme
- **Fallback:** If missing, game uses drawn arrows

---

## 🖼️ Backgrounds Folder (`backgrounds/`)

### Background Files:
| File | Screen | Description |
|------|--------|-------------|
| `main_menu_bg.png` | Main Menu | Background for title screen |
| `game_bg.png` | Gameplay | Background during game |
| `settings_bg.png` | Settings | Background for settings |
| `song_selection_bg.png` | Song Selection | Background for song menu |

### Image Specifications:
- **Format:** PNG, JPG, JPEG
- **Recommended Size:** 1920x1080 (scales automatically)
- **Style:** Any theme that matches your vision
- **Fallback:** If missing, game uses gradient backgrounds

---

## 🔤 Fonts Folder (`fonts/`)

### Custom Fonts (Future Feature):
- **Format:** TTF, OTF
- **Usage:** Will be used for text rendering
- **Fallback:** System fonts if custom fonts missing

---

## 🔊 Sounds Folder (`sounds/`)

### Sound Effects:
| File | Purpose | Description |
|------|---------|-------------|
| `hit.wav` | Hit Note | Sound when hitting notes correctly |
| `miss.wav` | Miss Note | Sound when missing notes |
| `fever_start.wav` | Fever Mode | Sound when fever mode activates |
| `menu_click.wav` | Menu Click | Sound for button interactions |

### Audio Specifications:
- **Format:** WAV, MP3
- **Quality:** 44.1kHz recommended
- **Volume:** Will be affected by SFX volume settings

---

## ⚙️ Config Folder (`config/`)

### Configuration Files:
- `settings.txt` - Automatically created with volume settings
- **Format:** Key-value pairs
- **Auto-saved:** Settings are saved when changed

### Example `settings.txt`:
```
music_volume=0.8
sfx_volume=0.6
```

---

## 🎨 Easy Code Customization

All visual elements can be customized in the `GameAssets` class:

### Colors (Lines 38-46):
```java
public static final Color PRIMARY_COLOR = new Color(138, 43, 226);    // Main theme
public static final Color SECONDARY_COLOR = new Color(75, 0, 130);    // Secondary theme
public static final Color ACCENT_COLOR = new Color(255, 20, 147);     // Highlights
public static final Color BACKGROUND_COLOR = new Color(25, 25, 35);   // Main background
public static final Color SURFACE_COLOR = new Color(40, 40, 55);      // Panel backgrounds
public static final Color TEXT_COLOR = new Color(255, 255, 255);      // Text color
```

### Text (Lines 72-82):
```java
public static final String GAME_TITLE = "🎵 Bangboo Galaxy 🎵";
public static final String PLAY_BUTTON_TEXT = "🎮 Play Game";
public static final String SETTINGS_BUTTON_TEXT = "⚙️ Settings";
// ... and more!
```

### Sizes (Lines 48-58):
```java
public static final int NOTE_SIZE = 35;           // Arrow note size
public static final int ARROW_SIZE = 25;          // Hit zone arrow size
public static final int BUTTON_WIDTH = 350;       // Button width
public static final int BUTTON_HEIGHT = 90;       // Button height
```

### Gameplay (Lines 61-68):
```java
public static final int NOTE_SPEED = 5;           // How fast notes fall
public static final int SPAWN_INTERVAL = 800;     // Time between notes (ms)
public static final int PARTICLE_COUNT = 15;      // Hit effect particles
```

---

## 🚀 Quick Start

1. **Add Music:** Drop songs into `songs/`
2. **Customize Arrows:** Add PNG files to `arrows/`
3. **Change Backgrounds:** Add images to `backgrounds/`
4. **Run Game:** `java RhythmGame`
5. **Enjoy!** 🎮

---

## 💡 Tips & Tricks

### For Best Performance:
- **Optimize images:** Keep file sizes reasonable
- **Use appropriate formats:** PNG for transparency, JPG for photos
- **Test regularly:** Check if assets load correctly

### Creative Ideas:
- **Theme packs:** Create folders with coordinated assets
- **Seasonal themes:** Change assets for holidays/events
- **Personal style:** Match your favorite color scheme
- **Minimalist vs. Complex:** Simple or detailed as you prefer

### Troubleshooting:
- **Assets not loading?** Check file names and formats
- **Game won't start?** Remove corrupted asset files
- **Performance issues?** Optimize large image files

---

## 🎮 Settings Features

### Volume Controls:
- **Music Volume:** Controls song volume (0-100%)
- **SFX Volume:** Controls sound effects (0-100%)
- **Auto-save:** Settings saved automatically
- **Applied globally:** Volume affects all songs

### Keybinds:
- **Customizable:** Change lane keys to your preference
- **4 Lanes:** Each lane can have any key
- **Easy setup:** Click button, press desired key

---

## 🎯 Advanced Customization

### Complete Theme Creation:
1. **Choose color scheme** in `GameAssets`
2. **Create matching arrow images**
3. **Design themed backgrounds**
4. **Add appropriate sound effects**
5. **Test and refine**

### Sharing Themes:
- **Export assets:** Share entire folders
- **Include config:** Share settings file
- **Document changes:** Note what you modified

---

🎨 **Happy Customizing!** Create your perfect rhythm game experience! 🎵
