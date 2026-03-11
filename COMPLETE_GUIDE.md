# Bangboo Galaxy - Complete Asset Customization Guide

## Complete Folder Structure

```
BangbooGalaxy/
├── songs/                    # Music files (12 default songs)
├── arrows/                   # Arrow images (8 files with glow)
├── backgrounds/              # Background images (5 files including credits)
├── fonts/                    # Custom fonts (2 files)
├── sounds/                   # Sound effects (5 files)
├── maps/                     # Song maps/beatmaps (.map files)
├── config/                   # Configuration files
├── ui/                       # Complete UI asset system
│   ├── buttons/              # Button images (14 files)
│   ├── numbers/              # Number images (0-9, 10 files)
│   ├── text/                 # Text images (14 files)
│   └── meters/               # Meter images (5 files)
├── RhythmGame.java          # Main game code
└── COMPLETE_GUIDE.md        # This comprehensive guide
```

## Game Features

### Core Gameplay
- **4-Lane Rhythm System**: Hit notes as they reach the hit zone
- **Timing-Based Scoring**: Perfect, Good, Okay, Miss ratings
- **Combo System**: Build combos to trigger fever mode
- **Visual Feedback**: Glowing notes and particle effects
- **Dynamic UI**: Responsive design that adapts to screen size

### Visual Design
- **Zenless Zone Zero Theme**: Dark cyberpunk aesthetic with orange accents
- **Modern UI**: Rounded buttons with orange borders
- **Clean Interface**: No emojis, minimalist design
- **Smooth Animations**: Hover effects and transitions

### Song Selection
- **Carousel Interface**: Navigate through songs visually
- **Difficulty Display**: Star-based difficulty rating
- **Large Song Cards**: Bigger cards for better visibility
- **Clean Text Display**: No text stacking issues

## Asset Specifications

### Background Images
- **main_menu_bg.png**: Main menu background
- **game_bg.png**: Gameplay background
- **settings_bg.png**: Settings menu background
- **song_selection_bg.png**: Song selection background
- **credits_bg.png**: Credits screen background

### Arrow Images
- **arrow_left.png**: Left arrow note
- **arrow_up.png**: Up arrow note
- **arrow_down.png**: Down arrow note
- **arrow_right.png**: Right arrow note
- **arrow_*_glow.png**: Glowing versions for hit effects

### Audio Files
- **hit.wav**: Note hit sound
- **miss.wav**: Note miss sound
- **fever_start.wav**: Fever mode activation
- **menu_click.wav**: Menu navigation sound

## Configuration

### Settings File (config/settings.txt)
```
music_volume=1.0
sfx_volume=1.0
```

### Keybinds
- Default: Z, X, ,, . (Arrow keys equivalent)
- Customizable in settings menu

## Scoring System

### Timing Windows
- **Perfect**: ±20 pixels (50 points)
- **Good**: ±40 pixels (30 points)
- **Okay**: ±60 pixels (10 points)
- **Miss**: Beyond 60 pixels (0 points)

### Combo System
- **Fever Mode**: Triggered at 20 combo
- **Duration**: 5 seconds
- **Effects**: Double points, special visual effects

## Technical Notes

### Screen Resolution
- **Dynamic Lanes**: Adapts to any screen size
- **Lane Width**: Takes 1/3 of screen center
- **Responsive UI**: All elements scale properly

### Performance
- **60 FPS Target**: Smooth animations
- **Optimized Rendering**: Efficient graphics pipeline
- **Memory Management**: Proper cleanup of resources

## Customization

### Adding New Songs
1. Place .mp3 file in songs/ folder
2. Create corresponding .map file in maps/ folder
3. Add background image in backgrounds/ folder (optional)

### Custom Themes
- Modify colors in GameAssets class
- Update button styles in createModernButton method
- Change fonts and UI elements as needed

### UI Modifications
- Button sizes: Modify BUTTON_WIDTH and BUTTON_HEIGHT
- Colors: Update color constants in GameAssets
- Fonts: Add custom fonts to fonts/ folder

## Troubleshooting

### Common Issues
- **Text Stacking**: Fixed with proper label initialization
- **Missing Images**: Creates placeholder graphics automatically
- **Audio Issues**: Check file formats and paths
- **Performance**: Reduce particle count or note speed

### File Formats
- **Images**: PNG format recommended
- **Audio**: MP3, WAV supported
- **Maps**: Custom .map format for beatmaps

This guide provides comprehensive information for customizing and extending Bangboo Galaxy with your own assets and themes.
