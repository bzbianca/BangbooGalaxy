# 🎵 Bangboo Galaxy - Customization Guide

## 📁 Asset Folders

### 🎵 Songs Folder (`songs/`)
Add your own music files here! The game automatically loads:
- **Supported formats:** `.mp3`, `.wav`
- **How to add:** Simply drag and drop music files into the `songs/` folder
- **Naming:** Use descriptive names (they will appear in the song selection menu)

### 🎯 Arrows Folder (`arrows/`)
Customize arrow appearances with your own images:
- **Required files:**
  - `arrow_left.png` - Left arrow (Lane 1)
  - `arrow_up.png` - Up arrow (Lane 2)  
  - `arrow_down.png` - Down arrow (Lane 3)
  - `arrow_right.png` - Right arrow (Lane 4)
- **Recommended size:** 64x64 pixels (scales automatically)
- **Format:** PNG with transparency support

## 🎨 Easy Code Customization

### Colors (in `GameAssets` class)
```java
// Change note colors
public static final Color[] NOTE_COLORS = {
    new Color(255, 100, 100), // Red - Lane 1
    new Color(100, 255, 100), // Green - Lane 2
    new Color(100, 100, 255), // Blue - Lane 3
    new Color(255, 255, 100)  // Yellow - Lane 4
};

// Change UI colors
public static final Color PRIMARY_COLOR = new Color(138, 43, 226);
public static final Color ACCENT_COLOR = new Color(255, 20, 147);
```

### Gameplay (in `GameAssets` class)
```java
// Adjust game speed
public static final int NOTE_SPEED = 5;        // Higher = faster
public static final int SPAWN_INTERVAL = 800;  // Lower = more frequent

// Change note sizes
public static final int NOTE_SIZE = 35;
public static final int ARROW_SIZE = 25;

// Visual effects
public static final int PARTICLE_COUNT = 15;
public static final float PARTICLE_GRAVITY = 0.3f;
```

## 🎮 How to Play

### Controls
- **Z, X, ,, .** - Default keys for 4 lanes
- **ESC** - Pause/Resume game
- **Customize keys** in Settings menu

### Features
- ✅ **Arrow-shaped notes** (osu! style)
- ✅ **Pause menu with buttons** (Resume, Menu, Quit)
- ✅ **Pause icon** in top-right corner
- ✅ **Smaller fever meter** on right side
- ✅ **Song selection menu**
- ✅ **Customizable assets**

## 🚀 Quick Start

1. **Add songs:** Drop `.mp3` or `.wav` files into `songs/` folder
2. **Customize arrows:** Add PNG files to `arrows/` folder
3. **Run:** `java RhythmGame`
4. **Enjoy!**

## 📝 Tips

- **Arrow images:** Use transparent PNGs for best results
- **Song files:** Keep file names short and descriptive
- **Performance:** Optimize images for faster loading
- **Backup:** Keep original assets safe when customizing

---

🎮 **Happy Gaming!** 🎵
