## NOTE: AUDIO IS BROKEN.

# 🎵 Bangboo Galaxy

A retro-style rhythm game featuring Eous and HappyEous as your companions while you hit notes to the beat!

## 🎮 Game Features

- **4-Lane Rhythm Gameplay**: Hit notes as they fall down the lanes using your keyboard
- **Companion Characters**: Eous on the left and HappyEous on the right cheer you on!
- **Dynamic Fever Mode**: Build up combo to activate fever mode for double points
- **Multiple Songs**: Play with various songs and difficulty levels
- **Retro Visual Style**: Orange and black theme with modern UI elements
- **Customizable Controls**: Change keybinds to your preference

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher installed on your system

### Running the Game
1. Open a terminal/command prompt
2. Navigate to the BangbooGalaxy directory
3. Run the game with:
   ```bash
   java RhythmGame.java
   ```

That's it! The game will start automatically.

## 🎯 How to Play

### Controls
- **Default Keybinds**:
  - Lane 1 (Red): `Z`
  - Lane 2 (Green): `X`
  - Lane 3 (Blue): `,`
  - Lane 4 (Yellow): `.`

- **Menu Navigation**:
  - Use mouse to click buttons
  - `ESC` to pause during gameplay

### Gameplay
1. **Select a Song**: Choose from available songs in the song selection menu
2. **Hit the Notes**: Press the corresponding key when notes reach the hit zone at the bottom
3. **Build Combo**: Hit consecutive notes to build your combo
4. **Activate Fever Mode**: Reach 15 combo to activate fever mode for double points!
5. **Score High**: Aim for perfect hits to maximize your score

### Scoring System
- **Perfect**: 50 points (tight timing window)
- **Good**: 30 points (medium timing window)
- **Okay**: 10 points (loose timing window)
- **Miss**: 0 points

## 📁 Project Structure

```
BangbooGalaxy/
├── RhythmGame.java              # Main game file
├── BangbooGalaxyIMAGES/         # Game images and UI elements
├── backgrounds/                 # Background images for different screens
├── arrows/                      # Arrow images for note lanes
├── ui/                          # User interface elements
│   ├── buttons/                 # Button images
│   ├── text/                    # Text images
│   ├── numbers/                 # Number images for scoring
│   └── meters/                  # Progress bar images
├── fonts/                       # Custom fonts
├── sounds/                      # Sound effects (add your own!)
├── songs/                       # Music files
├── maps/                        # Song map files for note patterns
├── config/                      # Game configuration files
└── README.md                    # This file
```

## ⚙️ Customization

### Adding Custom Songs
1. Place your music files in the `songs/` folder
2. The game will automatically generate note patterns
3. Or create custom maps in the `maps/` folder

### Changing Keybinds
1. Go to Settings from the main menu
2. Modify the keybinds to your preference
3. Settings are automatically saved

### Adding Custom Images
- Replace images in the respective folders to customize the look
- Supported formats: PNG, JPG, JPEG
- Maintain similar dimensions for best results

## 🎨 Visual Features

- **Animated Companions**: Eous and HappyEous float beside the note lanes
- **Dynamic Effects**: Glow effects during fever mode
- **Smooth Animations**: 60 FPS gameplay with optimized rendering
- **Retro Theme**: Orange and black color scheme with modern UI

## 🔧 Troubleshooting

### Game Won't Start
- Ensure you have Java 8 or higher installed
- Check that you're in the correct directory
- Try running with `java -cp . RhythmGame` if the above doesn't work

### Performance Issues
- Close other applications to free up memory
- The game automatically caches images for better performance
- Lower graphics settings if needed (in code)

### Missing Images/Sounds
- The game will use fallback graphics if images are missing
- Add your own images to the respective folders to customize
- Sound files are optional - the game works without them

## 📝 Game Development

This game was developed using:
- **Java Swing** for the UI framework
- **Custom game engine** built from scratch
- **Retro design philosophy** with modern optimizations

## 🤝 Contributing

Feel free to:
- Add new songs and maps
- Improve the graphics
- Suggest new features
- Report bugs or issues

## 📄 License

This project is open source. Feel free to modify and distribute as you wish!

---

**Enjoy playing Bangboo Galaxy! 🎮🎵**

*Made with ❤️ by Bianca*
