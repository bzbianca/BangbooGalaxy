import javax.swing.*;
import javax.swing.OverlayLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class RhythmGame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private SettingsPanel settingsPanel;
    private SongSelectionPanel songSelectionPanel;
    private CreditsPanel creditsPanel;
    private int[] keybinds = {90, 88, 44, 46}; // Z X , . (KeyEvent.VK codes)

    // EASY TO MODIFY VISUAL ASSETS
    public static class GameAssets {
        // === COLORS ===
        // Note colors - modify these to change note appearance
        public static final Color[] NOTE_COLORS = {
            new Color(255, 100, 100), // Red - Lane 1
            new Color(100, 255, 100), // Green - Lane 2  
            new Color(100, 100, 255), // Blue - Lane 3
            new Color(255, 255, 100)  // Yellow - Lane 4
        };
        
        // UI Colors - change these to customize the interface
        // Zenless Zone Zero inspired color scheme with orange
        public static final Color BACKGROUND_COLOR = new Color(15, 15, 20); // Very dark blue-black
        public static final Color PRIMARY_COLOR = new Color(255, 150, 0); // Orange
        public static final Color SECONDARY_COLOR = new Color(20, 20, 30); // Dark blue-gray
        public static final Color ACCENT_COLOR = new Color(255, 200, 50); // Light orange
        public static final Color SUCCESS_COLOR = new Color(0, 255, 150); // Cyan-green
        public static final Color WARNING_COLOR = new Color(255, 200, 0); // Yellow
        public static final Color DANGER_COLOR = new Color(255, 50, 50); // Red
        public static final Color TEXT_COLOR = new Color(220, 220, 220); // Light gray
        public static final Color SURFACE_COLOR = new Color(25, 25, 35); // Dark surface
        
        // === SIZES ===
        // Note sizes - modify to change note dimensions
        public static final int NOTE_SIZE = 50;
        public static final int ARROW_SIZE = 40;
        
        // UI Sizes - adjust these to change interface dimensions
        public static final int BUTTON_WIDTH = 350;
        public static final int BUTTON_HEIGHT = 90;
        public static final int SMALL_BUTTON_WIDTH = 250;
        public static final int SMALL_BUTTON_HEIGHT = 50;
        public static final Dimension MAIN_BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        public static final Dimension SMALL_BUTTON_SIZE = new Dimension(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT);
        
        // === GAMEPLAY ===
        // Animation speeds - modify to change game feel
        public static final int NOTE_SPEED = 5;
        public static final int SPAWN_INTERVAL = 800; // milliseconds
        
        // Visual effects - modify to change feedback
        public static final int PARTICLE_COUNT = 15;
        public static final float PARTICLE_GRAVITY = 0.3f;
        
        // === TEXT ===
        // Text content - easily change all text in the game
        public static final String GAME_TITLE = "Bangboo Galaxy";
        public static final String PLAY_BUTTON_TEXT = "Play Game";
        public static final String SETTINGS_BUTTON_TEXT = "Settings";
        public static final String QUIT_BUTTON_TEXT = "Quit";
        public static final String BACK_BUTTON_TEXT = "Back to Main";
        public static final String SONG_SELECTION_TITLE = "Select Song";
        public static final String SETTINGS_TITLE = "Settings";
        public static final String PAUSED_TITLE = "PAUSED";
        public static final String RESUME_BUTTON_TEXT = "Resume";
        public static final String SONG_SELECTION_BUTTON_TEXT = "Song Selection";
        public static final String QUIT_GAME_BUTTON_TEXT = "Quit";
        
        // === BUTTON COLORS ===
        // ZZZ black style buttons with orange borders
        public static final Color MAIN_BUTTON_COLOR = new Color(20, 20, 25); // Very dark
        public static final Color MAIN_BUTTON_HOVER_COLOR = new Color(40, 40, 50); // Dark gray
        public static final Color BUTTON_BORDER_COLOR = new Color(255, 150, 0); // Orange border
        
        // === ASSET FOLDERS ===
        // Asset folders - customize these paths
        public static final String SONGS_FOLDER = "songs/";
        public static final String ARROWS_FOLDER = "arrows/";
        public static final String BACKGROUNDS_FOLDER = "backgrounds/";
        public static final String FONTS_FOLDER = "fonts/";
        public static final String SOUNDS_FOLDER = "sounds/";
        public static final String CONFIG_FOLDER = "config/";
        public static final String MAPS_FOLDER = "maps/";
        public static final String UI_FOLDER = "ui/";
        public static final String BUTTONS_FOLDER = "ui/buttons/";
        public static final String NUMBERS_FOLDER = "ui/numbers/";
        public static final String TEXT_FOLDER = "ui/text/";
        public static final String METERS_FOLDER = "ui/meters/";
        
        // === ASSET FILES ===
        // Arrow images
        public static final String[] ARROW_FILES = {
            "arrow_left.png",
            "arrow_up.png", 
            "arrow_down.png",
            "arrow_right.png"
        };
        
        // Arrow glow effects for key press feedback
        public static final String[] ARROW_GLOW_FILES = {
            "arrow_left_glow.png",
            "arrow_up_glow.png", 
            "arrow_down_glow.png",
            "arrow_right_glow.png"
        };
        
        // Background images
        public static final String[] BACKGROUND_FILES = {
            "main_menu_bg.png",
            "game_bg.png",
            "settings_bg.png",
            "song_selection_bg.png",
            "credits_bg.png"
        };
        
        // Button images - replace all buttons with custom images
        public static final String[] BUTTON_FILES = {
            "play_button.png",
            "play_button_hover.png",
            "settings_button.png", 
            "settings_button_hover.png",
            "back_button.png",
            "back_button_hover.png",
            "resume_button.png",
            "resume_button_hover.png",
            "quit_button.png",
            "quit_button_hover.png",
            "song_selection_button.png",
            "song_selection_button_hover.png",
            "fever_button.png",
            "fever_button_hover.png"
        };
        
        // Number images (0-9) for score and combo display
        public static final String[] NUMBER_FILES = {
            "number_0.png",
            "number_1.png",
            "number_2.png",
            "number_3.png",
            "number_4.png",
            "number_5.png",
            "number_6.png",
            "number_7.png",
            "number_8.png",
            "number_9.png"
        };
        
        // Text images for UI text elements
        public static final String[] TEXT_FILES = {
            "title_text.png",
            "score_text.png",
            "combo_text.png",
            "fever_text.png",
            "paused_text.png",
            "settings_title.png",
            "song_selection_title.png",
            "music_volume_text.png",
            "sfx_volume_text.png",
            "keybinds_text.png",
            "lane_1_text.png",
            "lane_2_text.png",
            "lane_3_text.png",
            "lane_4_text.png"
        };
        
        // Meter images for fever bar and other meters
        public static final String[] METER_FILES = {
            "fever_meter_bg.png",
            "fever_meter_fill.png",
            "fever_meter_glow.png",
            "health_meter_bg.png",
            "health_meter_fill.png"
        };
        
        // Sound effects
        public static final String[] SFX_FILES = {
            "hit.wav",
            "miss.wav",
            "fever_start.wav",
            "menu_click.wav",
            "key_press.wav"
        };
        
        // === LOADED ASSETS ===
        // Cached loaded assets
        private static BufferedImage[] arrowImages = new BufferedImage[4];
        private static BufferedImage[] arrowGlowImages = new BufferedImage[4];
        private static BufferedImage[] backgroundImages = new BufferedImage[4];
        private static BufferedImage[] buttonImages = new BufferedImage[BUTTON_FILES.length];
        private static BufferedImage[] numberImages = new BufferedImage[10];
        private static BufferedImage[] textImages = new BufferedImage[TEXT_FILES.length];
        private static BufferedImage[] meterImages = new BufferedImage[METER_FILES.length];
        private static boolean assetsLoaded = false;
        
        // === ASSET LOADING METHODS ===
        public static void loadAllAssets() {
            if (assetsLoaded) return;
            
            // Create folders if they don't exist
            createAssetFolders();
            
            // Load arrow images
            loadArrowImages();
            
            // Load arrow glow images
            loadArrowGlowImages();
            
            // Load background images
            loadBackgroundImages();
            
            // Load button images
            loadButtonImages();
            
            // Load number images
            loadNumberImages();
            
            // Load text images
            loadTextImages();
            
            // Load meter images
            loadMeterImages();
            
            // Load configuration
            loadConfiguration();
            
            assetsLoaded = true;
        }
        
        private static void createAssetFolders() {
            String[] folders = {SONGS_FOLDER, ARROWS_FOLDER, BACKGROUNDS_FOLDER, 
                               FONTS_FOLDER, SOUNDS_FOLDER, CONFIG_FOLDER, MAPS_FOLDER, UI_FOLDER,
                               BUTTONS_FOLDER, NUMBERS_FOLDER, TEXT_FOLDER, METERS_FOLDER};
            
            for (String folder : folders) {
                File dir = new File(folder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }
        
        private static void loadArrowImages() {
            for (int i = 0; i < ARROW_FILES.length; i++) {
                try {
                    File arrowFile = new File(ARROWS_FOLDER + ARROW_FILES[i]);
                    if (arrowFile.exists()) {
                        arrowImages[i] = ImageIO.read(arrowFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    arrowImages[i] = null;
                }
            }
        }
        
        private static void loadArrowGlowImages() {
            for (int i = 0; i < ARROW_GLOW_FILES.length; i++) {
                try {
                    File arrowGlowFile = new File(ARROWS_FOLDER + ARROW_GLOW_FILES[i]);
                    if (arrowGlowFile.exists()) {
                        arrowGlowImages[i] = ImageIO.read(arrowGlowFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    arrowGlowImages[i] = null;
                }
            }
        }
        
        private static void loadBackgroundImages() {
            for (int i = 0; i < BACKGROUND_FILES.length; i++) {
                try {
                    File bgFile = new File(BACKGROUNDS_FOLDER + BACKGROUND_FILES[i]);
                    if (bgFile.exists()) {
                        backgroundImages[i] = ImageIO.read(bgFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    backgroundImages[i] = null;
                }
            }
        }
        
        private static void loadButtonImages() {
            for (int i = 0; i < BUTTON_FILES.length; i++) {
                try {
                    File buttonFile = new File(BUTTONS_FOLDER + BUTTON_FILES[i]);
                    if (buttonFile.exists()) {
                        buttonImages[i] = ImageIO.read(buttonFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    buttonImages[i] = null;
                }
            }
        }
        
        private static void loadNumberImages() {
            for (int i = 0; i < NUMBER_FILES.length; i++) {
                try {
                    File numberFile = new File(NUMBERS_FOLDER + NUMBER_FILES[i]);
                    if (numberFile.exists()) {
                        numberImages[i] = ImageIO.read(numberFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    numberImages[i] = null;
                }
            }
        }
        
        private static void loadTextImages() {
            for (int i = 0; i < TEXT_FILES.length; i++) {
                try {
                    File textFile = new File(TEXT_FOLDER + TEXT_FILES[i]);
                    if (textFile.exists()) {
                        textImages[i] = ImageIO.read(textFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    textImages[i] = null;
                }
            }
        }
        
        private static void loadMeterImages() {
            for (int i = 0; i < METER_FILES.length; i++) {
                try {
                    File meterFile = new File(METERS_FOLDER + METER_FILES[i]);
                    if (meterFile.exists()) {
                        meterImages[i] = ImageIO.read(meterFile);
                    }
                } catch (IOException e) {
                    // Image loading failed, will use fallback
                    meterImages[i] = null;
                }
            }
        }
        
        private static void loadConfiguration() {
            File configFile = new File(CONFIG_FOLDER + "settings.txt");
            if (configFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("music_volume=")) {
                            musicVolume = Float.parseFloat(line.substring(14));
                        } else if (line.startsWith("sfx_volume=")) {
                            sfxVolume = Float.parseFloat(line.substring(12));
                        }
                    }
                } catch (IOException e) {
                    // Config loading failed, use defaults
                }
            }
        }
        
        public static void saveConfiguration() {
            File configFile = new File(CONFIG_FOLDER + "settings.txt");
            try (PrintWriter writer = new PrintWriter(new FileWriter(configFile))) {
                writer.println("music_volume=" + musicVolume);
                writer.println("sfx_volume=" + sfxVolume);
            } catch (IOException e) {
                // Config saving failed
            }
        }
        
        // === ASSET ACCESS METHODS ===
        public static BufferedImage getArrowImage(int direction) {
            if (!assetsLoaded) loadAllAssets();
            return arrowImages[direction];
        }
        
        public static BufferedImage getArrowGlowImage(int direction) {
            if (!assetsLoaded) loadAllAssets();
            return arrowGlowImages[direction];
        }
        
        public static BufferedImage getBackgroundImage(int screen) {
            if (!assetsLoaded) loadAllAssets();
            return backgroundImages[screen];
        }
        
        public static BufferedImage getButtonImage(int buttonIndex) {
            if (!assetsLoaded) loadAllAssets();
            return buttonImages[buttonIndex];
        }
        
        public static BufferedImage getNumberImage(int digit) {
            if (!assetsLoaded) loadAllAssets();
            if (digit >= 0 && digit < numberImages.length) {
                return numberImages[digit];
            }
            return null;
        }
        
        public static BufferedImage getTextImage(int textIndex) {
            if (!assetsLoaded) loadAllAssets();
            if (textIndex >= 0 && textIndex < textImages.length) {
                return textImages[textIndex];
            }
            return null;
        }
        
        public static BufferedImage getMeterImage(int meterIndex) {
            if (!assetsLoaded) loadAllAssets();
            if (meterIndex >= 0 && meterIndex < meterImages.length) {
                return meterImages[meterIndex];
            }
            return null;
        }
        
        // === CONVENIENCE METHODS ===
        // Button image indices
        public static final int PLAY_BUTTON = 0;
        public static final int PLAY_BUTTON_HOVER = 1;
        public static final int SETTINGS_BUTTON = 2;
        public static final int SETTINGS_BUTTON_HOVER = 3;
        public static final int BACK_BUTTON = 4;
        public static final int BACK_BUTTON_HOVER = 5;
        public static final int RESUME_BUTTON = 6;
        public static final int RESUME_BUTTON_HOVER = 7;
        public static final int QUIT_BUTTON = 8;
        public static final int QUIT_BUTTON_HOVER = 9;
        public static final int SONG_SELECTION_BUTTON = 10;
        public static final int SONG_SELECTION_BUTTON_HOVER = 11;
        
        // Text image indices
        public static final int TITLE_TEXT = 0;
        public static final int SCORE_TEXT = 1;
        public static final int COMBO_TEXT = 2;
        public static final int FEVER_TEXT = 3;
        public static final int PAUSED_TEXT = 4;
        public static final int SETTINGS_TITLE_TEXT = 5;
        public static final int SONG_SELECTION_TITLE_TEXT = 6;
        public static final int MUSIC_VOLUME_TEXT = 7;
        public static final int SFX_VOLUME_TEXT = 8;
        public static final int KEYBINDS_TEXT = 9;
        
        // Meter image indices
        public static final int FEVER_METER_BG = 0;
        public static final int FEVER_METER_FILL = 1;
        public static final int FEVER_METER_GLOW = 2;
        public static final int HEALTH_METER_BG = 3;
        public static final int HEALTH_METER_FILL = 4;
        
        // === AUDIO ===
        // Volume settings (0.0 to 1.0)
        public static float DEFAULT_MUSIC_VOLUME = 1.0f;
        public static float DEFAULT_SFX_VOLUME = 1.0f;
        public static float musicVolume = DEFAULT_MUSIC_VOLUME;
        public static float sfxVolume = DEFAULT_SFX_VOLUME;
    }

    // Song class for easy song management
    static class Song {
        String title;
        String artist;
        String filePath;
        int difficulty;
        String imagePath;
        
        Song(String title, String artist, String filePath, int difficulty) {
            this.title = title;
            this.artist = artist;
            this.filePath = filePath;
            this.difficulty = difficulty;
            // Auto-generate image path based on song file name
            String fileName = new File(filePath).getName();
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            this.imagePath = "backgrounds/" + baseName + ".png";
        }
        
        @Override
        public String toString() {
            return title + " - " + artist;
        }
        
        public BufferedImage loadImage() {
            try {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    return ImageIO.read(imgFile);
                }
            } catch (IOException e) {
                // Image loading failed
            }
            return null;
        }
        
        public String getMapFilePath() {
            String fileName = new File(filePath).getName();
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            return GameAssets.MAPS_FOLDER + baseName + ".map";
        }
    }
    
    // Song mapping system for custom beat maps
    static class SongMap {
        String songFilePath;
        List<NoteData> notes;
        double bpm;
        double offset;
        
        public SongMap(String songFilePath) {
            this.songFilePath = songFilePath;
            this.notes = new ArrayList<>();
            this.bpm = 120.0; // Default BPM
            this.offset = 0.0; // Default offset in seconds
        }
        
        public void addNote(double time, int lane) {
            notes.add(new NoteData(time, lane));
        }
        
        public void saveToFile() {
            String fileName = new File(songFilePath).getName();
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            File mapFile = new File(GameAssets.MAPS_FOLDER + baseName + ".map");
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(mapFile))) {
                writer.println("# Song Map File");
                writer.println("bpm=" + bpm);
                writer.println("offset=" + offset);
                writer.println("# Format: time|lane (time in seconds, lane: 0-3)");
                
                for (NoteData note : notes) {
                    writer.printf("%.3f|%d%n", note.time, note.lane);
                }
            } catch (IOException e) {
                System.err.println("Failed to save map file: " + e.getMessage());
            }
        }
        
        public static SongMap loadFromFile(String mapFilePath) {
            SongMap map = null;
            File file = new File(mapFilePath);
            
            if (!file.exists()) {
                return null;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#") || line.trim().isEmpty()) {
                        continue; // Skip comments and empty lines
                    }
                    
                    if (line.startsWith("bpm=")) {
                        if (map == null) {
                            // Extract song file path from map file path
                            String mapFileName = new File(mapFilePath).getName();
                            String songFileName = mapFileName.substring(0, mapFileName.lastIndexOf('.')) + ".mp3";
                            map = new SongMap(GameAssets.SONGS_FOLDER + songFileName);
                        }
                        map.bpm = Double.parseDouble(line.substring(4));
                    } else if (line.startsWith("offset=")) {
                        if (map == null) {
                            String mapFileName = new File(mapFilePath).getName();
                            String songFileName = mapFileName.substring(0, mapFileName.lastIndexOf('.')) + ".mp3";
                            map = new SongMap(GameAssets.SONGS_FOLDER + songFileName);
                        }
                        map.offset = Double.parseDouble(line.substring(7));
                    } else {
                        // Parse note data
                        String[] parts = line.split("\\|");
                        if (parts.length == 2) {
                            if (map == null) {
                                String mapFileName = new File(mapFilePath).getName();
                                String songFileName = mapFileName.substring(0, mapFileName.lastIndexOf('.')) + ".mp3";
                                map = new SongMap(GameAssets.SONGS_FOLDER + songFileName);
                            }
                            double time = Double.parseDouble(parts[0]);
                            int lane = Integer.parseInt(parts[1]);
                            map.addNote(time, lane);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to load map file: " + e.getMessage());
            }
            
            return map;
        }
        
        // Auto-generate map from audio analysis (simplified beat detection)
        public static SongMap generateAutoMap(String songFilePath) {
            SongMap map = new SongMap(songFilePath);
            
            // This is a simplified auto-generation
            // In a real implementation, you would use audio analysis libraries
            // For now, we'll create a basic pattern based on default BPM
            
            double duration = 120.0; // Assume 2 minutes for demo
            double beatInterval = 60.0 / map.bpm; // Seconds per beat
            
            // Generate a basic pattern
            for (double time = 2.0; time < duration; time += beatInterval) {
                // Add notes with some variation
                int lane = (int)(time * 2) % 4; // Cycle through lanes
                map.addNote(time, lane);
                
                // Occasionally add double notes
                if (Math.random() < 0.3) {
                    int secondLane = (lane + 1 + (int)(Math.random() * 3)) % 4;
                    map.addNote(time + beatInterval * 0.5, secondLane);
                }
            }
            
            return map;
        }
    }
    
    // Note data structure for mapping
    static class NoteData {
        double time; // Time in seconds
        int lane;    // Lane (0-3)
        
        public NoteData(double time, int lane) {
            this.time = time;
            this.lane = lane;
        }
    }

    class Particle {
        float x, y, vx, vy;
        Color color;
        int life, maxLife;
        float size;
        
        Particle(float x, float y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.vx = (float)(Math.random() * 6 - 3);
            this.vy = (float)(Math.random() * 6 - 3);
            this.life = this.maxLife = 30 + (int)(Math.random() * 20);
            this.size = 3 + (float)Math.random() * 4;
        }
        
        void update() {
            x += vx;
            y += vy;
            vy += GameAssets.PARTICLE_GRAVITY;
            life--;
            size *= 0.96f;
        }
        
        void draw(Graphics2D g2d) {
            if (life <= 0) return;
            float alpha = (float)life / maxLife;
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
            g2d.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
        }
        
        boolean isDead() {
            return life <= 0;
        }
    }

    public RhythmGame() {
        setTitle("Bangboo Galaxy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        // Set to fullscreen by default
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Remove window decorations for true fullscreen
        
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        menuPanel = new MenuPanel();
        gamePanel = new GamePanel();
        settingsPanel = new SettingsPanel();
        songSelectionPanel = new SongSelectionPanel();
        creditsPanel = new CreditsPanel();
        
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(songSelectionPanel, "SongSelection");
        mainPanel.add(creditsPanel, "Credits");
        
        add(mainPanel);
        
        setVisible(true);
    }
    
    public void showSettings() {
        cardLayout.show(mainPanel, "Settings");
        // Request focus for keybind input
        settingsPanel.requestFocusInWindow();
    }

    public void showSongSelection() {
        cardLayout.show(mainPanel, "SongSelection");
    }

    public void showCredits() {
        cardLayout.show(mainPanel, "Credits");
    }

    public void backToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    public void startGameWithSong(Song song) {
        gamePanel.startGameWithSong(song);
        cardLayout.show(mainPanel, "Game");
        gamePanel.requestFocus();
    }

    public int[] getKeybinds() {
        return keybinds;
    }

    public void setKeybind(int lane, int keyCode) {
        keybinds[lane] = keyCode;
    }

    public void setMusicVolume(float volume) {
        GameAssets.musicVolume = volume;
    }

    public void setSfxVolume(float volume) {
        GameAssets.sfxVolume = volume;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RhythmGame());
    }

    class MenuPanel extends JPanel {
        public MenuPanel() {
            setLayout(new BorderLayout());
            setBackground(GameAssets.BACKGROUND_COLOR);

            // Create title panel with gradient effect
            JPanel titlePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Create gradient background
                    GradientPaint gradient = new GradientPaint(0, 0, GameAssets.PRIMARY_COLOR, getWidth(), getHeight(), GameAssets.SECONDARY_COLOR);
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            titlePanel.setOpaque(false);
            titlePanel.setPreferredSize(new Dimension(800, 200));

            JLabel titleLabel = new JLabel(GameAssets.GAME_TITLE, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 56));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            titlePanel.add(titleLabel);

            add(titlePanel, BorderLayout.NORTH);

            // Center panel for buttons with better spacing
            JPanel centerPanel = new JPanel();
            centerPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;

            // Create modern play button with customizable color
            JButton playButton = createModernButton(GameAssets.PLAY_BUTTON_TEXT, GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            playButton.addActionListener(e -> ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSongSelection());
            playButton.setPreferredSize(GameAssets.MAIN_BUTTON_SIZE);
            gbc.gridy = 0;
            centerPanel.add(playButton, gbc);

            // Create modern settings button with customizable color
            JButton settingsButton = createModernButton(GameAssets.SETTINGS_BUTTON_TEXT, GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            settingsButton.addActionListener(e -> ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSettings());
            settingsButton.setPreferredSize(GameAssets.MAIN_BUTTON_SIZE);
            gbc.gridy = 1;
            centerPanel.add(settingsButton, gbc);

            // Create credits button with customizable color
            JButton creditsButton = createModernButton("🎵 Credits", GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            creditsButton.addActionListener(e -> ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showCredits());
            creditsButton.setPreferredSize(GameAssets.MAIN_BUTTON_SIZE);
            gbc.gridy = 2;
            centerPanel.add(creditsButton, gbc);

            // Create quit button with customizable color
            JButton quitButton = createModernButton(GameAssets.QUIT_BUTTON_TEXT, GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            quitButton.addActionListener(e -> System.exit(0));
            quitButton.setPreferredSize(GameAssets.MAIN_BUTTON_SIZE);
            gbc.gridy = 3;
            centerPanel.add(quitButton, gbc);

            add(centerPanel, BorderLayout.CENTER);
        }

        private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isRollover()) {
                        g2d.setColor(hoverColor);
                    } else {
                        g2d.setColor(bgColor);
                    }

                    // Draw rounded rectangle with better visibility
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25);
                    g2d.fill(roundedRect);

                    super.paintComponent(g);
                }

                @Override
                public void setContentAreaFilled(boolean b) {
                    // Do nothing to prevent default background
                }

                @Override
                public boolean isOpaque() {
                    return false;
                }
            };

            button.setFont(new Font("Segoe UI", Font.BOLD, 28));
            button.setForeground(GameAssets.TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            return button;
        }
    }

    class SongSelectionPanel extends JPanel {
        private JButton playButton;
        private Song selectedSong;
        private List<Song> songsList;
        private int currentIndex = 0;
        private JPanel songPreviewPanel;
        private JButton leftButton;
        private JButton rightButton;
        private boolean isCurrentCardHovered = false;

        public SongSelectionPanel() {
            setLayout(new BorderLayout());
            setBackground(GameAssets.BACKGROUND_COLOR);

            // Create title panel with gradient
            JPanel titlePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw gradient background
                    GradientPaint titleGradient = new GradientPaint(0, 0, GameAssets.PRIMARY_COLOR, 0, getHeight(), GameAssets.SECONDARY_COLOR);
                    g2d.setPaint(titleGradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    super.paintComponent(g);
                }
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            titlePanel.setPreferredSize(new Dimension(getWidth(), 120));
            titlePanel.setLayout(new BorderLayout());
            
            // Add title text
            JLabel titleLabel = new JLabel("🎵 Select Song", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            // Add back button to title panel
            JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topButtonPanel.setBackground(new Color(0, 0, 0, 0));
            topButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            JButton backButton = createModernButton("🔙 Back to Main", new Color(30, 30, 30), new Color(60, 60, 60));
            backButton.addActionListener(e -> backToMenu());
            topButtonPanel.add(backButton);
            titlePanel.add(topButtonPanel, BorderLayout.NORTH);
            
            add(titlePanel, BorderLayout.NORTH);

            // Create main slideshow panel
            JPanel slideshowPanel = new JPanel(new BorderLayout());
            slideshowPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            slideshowPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            
            // Create navigation buttons
            leftButton = createNavigationButton("◀", SwingConstants.WEST);
            rightButton = createNavigationButton("▶", SwingConstants.EAST);
            
            leftButton.addActionListener(e -> navigateSong(-1));
            rightButton.addActionListener(e -> navigateSong(1));
            
            // Create song preview panel
            songPreviewPanel = createSongPreviewPanel();
            
            // Add components to slideshow panel
            slideshowPanel.add(leftButton, BorderLayout.WEST);
            slideshowPanel.add(songPreviewPanel, BorderLayout.CENTER);
            slideshowPanel.add(rightButton, BorderLayout.EAST);
            
            add(slideshowPanel, BorderLayout.CENTER);

            // Create button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
            
            playButton = createModernButton("🎮 Play Song", GameAssets.PRIMARY_COLOR, GameAssets.ACCENT_COLOR);
            playButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            playButton.setPreferredSize(new Dimension(300, 60));
            playButton.addActionListener(e -> {
                if (selectedSong != null) {
                    startGameWithSong(selectedSong);
                }
            });
            playButton.setEnabled(false);
            
            buttonPanel.add(playButton);
            add(buttonPanel, BorderLayout.SOUTH);
            
            // Load songs and initialize
            loadSongs();
            updateSongDisplay();
            
            // Add keyboard navigation
            setupKeyboardNavigation();
        }

        private void loadSongs() {
            songsList = new ArrayList<>();
            
            // Create 12 placeholder songs with varying difficulties
            Song[] defaultSongs = {
                new Song("Placeholder 1", "Bangboo Galaxy", "placeholder1.mp3", 1),
                new Song("Placeholder 2", "Bangboo Galaxy", "placeholder2.mp3", 2),
                new Song("Placeholder 3", "Bangboo Galaxy", "placeholder3.mp3", 3),
                new Song("Placeholder 4", "Bangboo Galaxy", "placeholder4.mp3", 4),
                new Song("Placeholder 5", "Bangboo Galaxy", "placeholder5.mp3", 5),
                new Song("Placeholder 6", "Bangboo Galaxy", "placeholder6.mp3", 6),
                new Song("Placeholder 7", "Bangboo Galaxy", "placeholder7.mp3", 3),
                new Song("Placeholder 8", "Bangboo Galaxy", "placeholder8.mp3", 4),
                new Song("Placeholder 9", "Bangboo Galaxy", "placeholder9.mp3", 3),
                new Song("Placeholder 10", "Bangboo Galaxy", "placeholder10.mp3", 5),
                new Song("Placeholder 11", "Bangboo Galaxy", "placeholder11.mp3", 4),
                new Song("Placeholder 12", "Bangboo Galaxy", "placeholder12.mp3", 2)
            };
            
            for (Song song : defaultSongs) {
                songsList.add(song);
                
                // Auto-generate map if it doesn't exist
                String mapPath = song.getMapFilePath();
                File mapFile = new File(mapPath);
                if (!mapFile.exists()) {
                    SongMap autoMap = SongMap.generateAutoMap(song.filePath);
                    autoMap.saveToFile();
                    System.out.println("Generated auto-map for: " + song.title);
                }
            }
            
            // Load additional songs from folder
            loadSongsFromFolder();
        }
        
        private void loadSongsFromFolder() {
            File songsFolder = new File(GameAssets.SONGS_FOLDER);
            if (songsFolder.exists() && songsFolder.isDirectory()) {
                File[] songFiles = songsFolder.listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith(".wav"));
                
                if (songFiles != null) {
                    for (File file : songFiles) {
                        String fileName = file.getName();
                        // Skip if it's one of our default songs
                        if (isDefaultSong(fileName)) {
                            continue;
                        }
                        
                        String title = fileName.substring(0, fileName.lastIndexOf('.'));
                        Song customSong = new Song(title, "Custom", fileName, 3);
                        songsList.add(customSong);
                        
                        // Auto-generate map for custom songs too
                        String mapPath = customSong.getMapFilePath();
                        File mapFile = new File(mapPath);
                        if (!mapFile.exists()) {
                            SongMap autoMap = SongMap.generateAutoMap(customSong.filePath);
                            autoMap.saveToFile();
                            System.out.println("Generated auto-map for custom song: " + title);
                        }
                    }
                }
            }
        }
        
        private boolean isDefaultSong(String fileName) {
            String[] defaultSongs = {"placeholder1.mp3", "placeholder2.mp3", "placeholder3.mp3", "placeholder4.mp3", 
                                   "placeholder5.mp3", "placeholder6.mp3", "placeholder7.mp3", "placeholder8.mp3",
                                   "placeholder9.mp3", "placeholder10.mp3", "placeholder11.mp3", "placeholder12.mp3"};
            
            for (String defaultSong : defaultSongs) {
                if (defaultSong.equalsIgnoreCase(fileName)) {
                    return true;
                }
            }
            return false;
        }
        
        private JButton createNavigationButton(String text, int direction) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setPreferredSize(new Dimension(50, 50));
            button.setBackground(GameAssets.PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setMargin(new Insets(0, 0, 0, 0));
            
            // Make perfect square with rounded corners
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                public void installUI(javax.swing.JComponent c) {
                    super.installUI(c);
                    button.setOpaque(false);
                }
                
                public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (button.getModel().isRollover()) {
                        g2d.setColor(GameAssets.ACCENT_COLOR);
                    } else if (button.getModel().isPressed()) {
                        g2d.setColor(GameAssets.SECONDARY_COLOR);
                    } else {
                        g2d.setColor(GameAssets.PRIMARY_COLOR);
                    }
                    
                    // Draw perfect square with rounded corners
                    g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 8, 8);
                    
                    // Draw text centered
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(button.getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = button.getText();
                    int x = (button.getWidth() - fm.stringWidth(text)) / 2;
                    int y = (button.getHeight() + fm.getAscent()) / 2;
                    g2d.drawString(text, x, y);
                }
            });
            
            return button;
        }
        
        private JPanel createSongPreviewPanel() {
            JPanel carouselPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Draw subtle gradient background
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint bgGradient = new GradientPaint(0, 0, new Color(20, 20, 30), 0, getHeight(), new Color(30, 30, 45));
                    g2d.setPaint(bgGradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            carouselPanel.setLayout(new OverlayLayout(carouselPanel));
            carouselPanel.setPreferredSize(new Dimension(600, 400));
            
            // Create layered panel for carousel effect
            JPanel layeredPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
            layeredPanel.setOpaque(false);
            
            // Previous song (smaller, faded)
            JPanel prevPanel = createSongCard(false);
            prevPanel.setName("prev");
            
            // Current song (larger, prominent)
            JPanel currentPanel = createSongCard(true);
            currentPanel.setName("current");
            
            // Next song (smaller, faded)
            JPanel nextPanel = createSongCard(false);
            nextPanel.setName("next");
            
            layeredPanel.add(prevPanel);
            layeredPanel.add(currentPanel);
            layeredPanel.add(nextPanel);
            
            carouselPanel.add(layeredPanel);
            
            return carouselPanel;
        }
        
        private JPanel createSongCard(boolean isCurrent) {
            JPanel card = new JPanel(new BorderLayout());
            
            // Size based on whether this is current or adjacent song
            int imageSize = isCurrent ? 300 : 200;
            int cardWidth = isCurrent ? 350 : 250;
            int cardHeight = isCurrent ? 420 : 300;
            
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
            card.setMaximumSize(new Dimension(cardWidth, cardHeight));
            card.setMinimumSize(new Dimension(cardWidth, cardHeight));
            
            // Create floating effect with shadow and transparency
            if (!isCurrent) {
                card.setOpaque(false);
                card.setBackground(new Color(0, 0, 0, 0));
            } else {
                card.setBackground(new Color(40, 40, 55, 240));
            }
            
            // Create image panel with centered content
            JPanel imagePanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw shadow for floating effect
                    if (isCurrent) {
                        g2d.setColor(new Color(0, 0, 0, 100));
                        g2d.fillRoundRect(5, 5, imageSize, imageSize, 15, 15);
                    }
                    
                    // Draw background with hover effect
                    Color bgColor;
                    if (isCurrent && isCurrentCardHovered) {
                        bgColor = new Color(80, 80, 100); // Lighter on hover
                    } else if (isCurrent) {
                        bgColor = new Color(60, 60, 80);
                    } else {
                        bgColor = new Color(40, 40, 55, 180);
                    }
                    g2d.setColor(bgColor);
                    g2d.fillRoundRect(0, 0, imageSize, imageSize, 15, 15);
                    
                    // Draw border with hover effect
                    Color borderColor = (isCurrent && isCurrentCardHovered) ? 
                        new Color(255, 220, 100) : // Brighter on hover
                        (isCurrent ? GameAssets.ACCENT_COLOR : new Color(100, 100, 120, 150));
                    g2d.setColor(borderColor);
                    g2d.setStroke(new BasicStroke(isCurrent ? 3 : 2));
                    g2d.drawRoundRect(0, 0, imageSize, imageSize, 15, 15);
                    
                    // Draw music note icon
                    g2d.setColor(isCurrent ? Color.WHITE : new Color(200, 200, 200, 200));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, isCurrent ? 48 : 32));
                    FontMetrics fm = g2d.getFontMetrics();
                    String note = "♪";
                    int x = (imageSize - fm.stringWidth(note)) / 2;
                    int y = (imageSize + fm.getAscent()) / 2;
                    g2d.drawString(note, x, y);
                }
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            imagePanel.setPreferredSize(new Dimension(imageSize, imageSize));
            
            // Create title and difficulty panel
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            infoPanel.setBackground(new Color(0, 0, 0, 0));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
            
            // Title label
            JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, isCurrent ? 16 : 12));
            titleLabel.setForeground(isCurrent ? Color.WHITE : new Color(200, 200, 200, 200));
            
            // Difficulty label
            JLabel difficultyLabel = new JLabel("", SwingConstants.CENTER);
            difficultyLabel.setFont(new Font("Segoe UI", Font.PLAIN, isCurrent ? 14 : 10));
            difficultyLabel.setForeground(isCurrent ? new Color(255, 200, 100) : new Color(180, 180, 100, 150));
            
            infoPanel.add(titleLabel);
            infoPanel.add(difficultyLabel);
            
            // Add components
            card.add(imagePanel, BorderLayout.CENTER);
            card.add(infoPanel, BorderLayout.SOUTH);
            
            // Add hover effect for current song
            if (isCurrent) {
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        isCurrentCardHovered = true;
                        imagePanel.repaint();
                    }
                    
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        isCurrentCardHovered = false;
                        imagePanel.repaint();
                    }
                    
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (selectedSong != null) {
                            startGameWithSong(selectedSong);
                        }
                    }
                });
            }
            
            // Store reference to difficulty label for updates
            card.putClientProperty("titleLabel", titleLabel);
            card.putClientProperty("difficultyLabel", difficultyLabel);
            card.putClientProperty("imagePanel", imagePanel);
            
            return card;
        }
        
        private void setupKeyboardNavigation() {
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        navigateSong(-1);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        navigateSong(1);
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedSong != null) {
                        startGameWithSong(selectedSong);
                    }
                }
            });
        }
        
        private void navigateSong(int direction) {
            if (songsList == null || songsList.isEmpty()) return;
            
            currentIndex += direction;
            
            // Wrap around
            if (currentIndex < 0) {
                currentIndex = songsList.size() - 1;
            } else if (currentIndex >= songsList.size()) {
                currentIndex = 0;
            }
            
            updateSongDisplay();
        }
        
        private void updateSongDisplay() {
            if (songsList == null || songsList.isEmpty() || currentIndex < 0 || currentIndex >= songsList.size()) {
                updateCarouselCards(null, null, null);
                playButton.setEnabled(false);
                selectedSong = null;
                return;
            }
            
            selectedSong = songsList.get(currentIndex);
            
            // Get previous, current, and next songs
            Song prevSong = songsList.size() > 1 ? 
                songsList.get((currentIndex - 1 + songsList.size()) % songsList.size()) : null;
            Song nextSong = songsList.size() > 1 ? 
                songsList.get((currentIndex + 1) % songsList.size()) : null;
            
            updateCarouselCards(prevSong, selectedSong, nextSong);
            playButton.setEnabled(true);
            
            // Update navigation buttons
            leftButton.setEnabled(songsList.size() > 1);
            rightButton.setEnabled(songsList.size() > 1);
        }
        
        private void updateCarouselCards(Song prevSong, Song currentSong, Song nextSong) {
            // Find the carousel panel and update its cards
            if (songPreviewPanel != null) {
                JPanel layeredPanel = (JPanel) songPreviewPanel.getComponent(0);
                
                for (Component comp : layeredPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel card = (JPanel) comp;
                        String cardType = card.getName();
                        
                        if ("prev".equals(cardType) && prevSong != null) {
                            updateCardTitle(card, prevSong.title, false);
                            updateCardDifficulty(card, prevSong.difficulty, false);
                        } else if ("current".equals(cardType) && currentSong != null) {
                            updateCardTitle(card, currentSong.title, true);
                            updateCardDifficulty(card, currentSong.difficulty, true);
                        } else if ("next".equals(cardType) && nextSong != null) {
                            updateCardTitle(card, nextSong.title, false);
                            updateCardDifficulty(card, nextSong.difficulty, false);
                        }
                    }
                }
            }
        }
        
        private void updateCardTitle(JPanel card, String title, boolean isCurrent) {
            // Find the title label in the card using client properties
            JLabel titleLabel = (JLabel) card.getClientProperty("titleLabel");
            
            if (titleLabel != null) {
                // Clear previous text and set new title
                titleLabel.setText(title);
                titleLabel.revalidate();
                titleLabel.repaint();
            }
        }
        
        private void updateCardDifficulty(JPanel card, int difficulty, boolean isCurrent) {
            // Find the difficulty label in the card
            JLabel difficultyLabel = (JLabel) card.getClientProperty("difficultyLabel");
            
            if (difficultyLabel != null) {
                String difficultyText = getDifficultyStars(difficulty);
                difficultyLabel.setText(difficultyText);
            }
        }
        
        private String getDifficultyStars(int difficulty) {
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                if (i < difficulty) {
                    stars.append("⭐");
                } else {
                    stars.append("☆");
                }
            }
            return stars.toString();
        }
        
        private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isRollover()) {
                        g2d.setColor(hoverColor);
                    } else {
                        g2d.setColor(bgColor);
                    }
                    
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    // Draw cyan border for ZZZ style
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.setForeground(GameAssets.TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return button;
        }
    }

    class CreditsPanel extends JPanel {
        private JTextField[] songCreditFields;
        private JTextField yourNameField;
        private JButton saveButton;
        private JButton backButton;
        
        public CreditsPanel() {
            setLayout(new BorderLayout());
            setBackground(GameAssets.BACKGROUND_COLOR);
            
            // Create title panel
            JPanel titlePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint titleGradient = new GradientPaint(0, 0, GameAssets.PRIMARY_COLOR, 0, getHeight(), GameAssets.SECONDARY_COLOR);
                    g2d.setPaint(titleGradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    super.paintComponent(g);
                }
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            titlePanel.setPreferredSize(new Dimension(getWidth(), 120));
            titlePanel.setLayout(new BorderLayout());
            
            JLabel titleLabel = new JLabel("🎵 Credits Configuration", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            // Add back button
            JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topButtonPanel.setBackground(new Color(0, 0, 0, 0));
            topButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            backButton = createModernButton("🔙 Back to Main", new Color(30, 30, 30), new Color(60, 60, 60));
            backButton.addActionListener(e -> backToMenu());
            topButtonPanel.add(backButton);
            titlePanel.add(topButtonPanel, BorderLayout.NORTH);
            
            add(titlePanel, BorderLayout.NORTH);
            
            // Create main content panel
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            
            // Create scrollable area for credits
            JPanel creditsPanel = new JPanel();
            creditsPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));
            
            // Your name section
            JPanel yourNamePanel = createSectionPanel("Your Name");
            yourNameField = new JTextField("Your Name Here", 30);
            yourNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            yourNameField.setBackground(GameAssets.SURFACE_COLOR);
            yourNameField.setForeground(GameAssets.TEXT_COLOR);
            yourNameField.setCaretColor(GameAssets.TEXT_COLOR);
            yourNameField.setBorder(BorderFactory.createLineBorder(GameAssets.ACCENT_COLOR, 2));
            yourNamePanel.add(yourNameField);
            creditsPanel.add(yourNamePanel);
            creditsPanel.add(Box.createVerticalStrut(20));
            
            // Song credits section
            JLabel songCreditsLabel = new JLabel("Song Credits:");
            songCreditsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            songCreditsLabel.setForeground(GameAssets.TEXT_COLOR);
            songCreditsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            creditsPanel.add(songCreditsLabel);
            creditsPanel.add(Box.createVerticalStrut(10));
            
            // Create credit fields for all 12 placeholder songs
            songCreditFields = new JTextField[12];
            String[] songNames = {
                "Placeholder 1", "Placeholder 2", "Placeholder 3", "Placeholder 4",
                "Placeholder 5", "Placeholder 6", "Placeholder 7", "Placeholder 8",
                "Placeholder 9", "Placeholder 10", "Placeholder 11", "Placeholder 12"
            };
            
            for (int i = 0; i < 12; i++) {
                JPanel songPanel = createSectionPanel(songNames[i]);
                songCreditFields[i] = new JTextField("Original Artist / License Info", 30);
                songCreditFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
                songCreditFields[i].setBackground(GameAssets.SURFACE_COLOR);
                songCreditFields[i].setForeground(GameAssets.TEXT_COLOR);
                songCreditFields[i].setCaretColor(GameAssets.TEXT_COLOR);
                songCreditFields[i].setBorder(BorderFactory.createLineBorder(GameAssets.PRIMARY_COLOR, 1));
                songPanel.add(songCreditFields[i]);
                creditsPanel.add(songPanel);
                creditsPanel.add(Box.createVerticalStrut(10));
            }
            
            // Load existing credits
            loadCredits();
            
            JScrollPane scrollPane = new JScrollPane(creditsPanel);
            scrollPane.setBackground(GameAssets.BACKGROUND_COLOR);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Create button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            
            saveButton = createModernButton("💾 Save Credits", GameAssets.SUCCESS_COLOR, new Color(100, 250, 150));
            saveButton.addActionListener(e -> saveCredits());
            buttonPanel.add(saveButton);
            
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(contentPanel, BorderLayout.CENTER);
        }
        
        private JPanel createSectionPanel(String title) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(GameAssets.BACKGROUND_COLOR);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            
            JLabel label = new JLabel(title + ":");
            label.setFont(new Font("Segoe UI", Font.BOLD, 16));
            label.setForeground(GameAssets.TEXT_COLOR);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
            
            panel.add(label, BorderLayout.NORTH);
            return panel;
        }
        
        private void loadCredits() {
            File creditsFile = new File("config/credits.txt");
            if (creditsFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(creditsFile))) {
                    String line;
                    int lineIndex = 0;
                    
                    // Read your name
                    if ((line = reader.readLine()) != null) {
                        yourNameField.setText(line);
                    }
                    
                    // Read song credits
                    while ((line = reader.readLine()) != null && lineIndex < 12) {
                        songCreditFields[lineIndex].setText(line);
                        lineIndex++;
                    }
                } catch (IOException e) {
                    System.err.println("Error loading credits: " + e.getMessage());
                }
            }
        }
        
        private void saveCredits() {
            File creditsFile = new File("config/credits.txt");
            creditsFile.getParentFile().mkdirs();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(creditsFile))) {
                // Save your name
                writer.println(yourNameField.getText());
                
                // Save song credits
                for (JTextField field : songCreditFields) {
                    writer.println(field.getText());
                }
                
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "Credits saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving credits: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isRollover()) {
                        g2d.setColor(hoverColor);
                    } else {
                        g2d.setColor(bgColor);
                    }
                    
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
            button.setForeground(GameAssets.TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return button;
        }
    }

    class SettingsPanel extends JPanel {
        private JButton[] keybindButtons;
        private int selectedLane = -1;
        private static final String[] LANE_NAMES = {"Lane 1", "Lane 2", "Lane 3", "Lane 4"};
        private static final String[] KEY_NAMES = {"Z", "X", ",", "."};
        private JSlider musicSlider;
        private JSlider sfxSlider;
        private JLabel musicValueLabel;
        private JLabel sfxValueLabel;

        public SettingsPanel() {
            setLayout(new BorderLayout());
            setBackground(GameAssets.BACKGROUND_COLOR);
            setPreferredSize(new Dimension(800, 600));

            // Create title panel with gradient
            JPanel titlePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gradient = new GradientPaint(0, 0, GameAssets.SECONDARY_COLOR, getWidth(), getHeight(), GameAssets.PRIMARY_COLOR);
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            titlePanel.setOpaque(false);
            titlePanel.setPreferredSize(new Dimension(800, 100));
            
            JLabel titleLabel = new JLabel(GameAssets.SETTINGS_TITLE, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            titlePanel.add(titleLabel);
            add(titlePanel, BorderLayout.NORTH);

            // Main content panel that uses full space
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Volume section - takes top half
            JPanel volumePanel = createVolumeSection();
            contentPanel.add(volumePanel, BorderLayout.NORTH);
            
            // Keybinds section - takes bottom half
            JPanel keybindsPanel = createKeybindsSection();
            contentPanel.add(keybindsPanel, BorderLayout.CENTER);
            
            add(contentPanel, BorderLayout.CENTER);

            // Bottom button panel
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            
            JButton backButton = createModernButton(GameAssets.BACK_BUTTON_TEXT, GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            backButton.addActionListener(e -> backToMenu());
            bottomPanel.add(backButton);
            
            add(bottomPanel, BorderLayout.SOUTH);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (selectedLane != -1) {
                        // Prevent certain keys from being used
                        int keyCode = e.getKeyCode();
                        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER ||
                            keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL ||
                            keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_TAB) {
                            // Don't allow these keys
                            return;
                        }
                        
                        setKeybind(selectedLane, keyCode);
                        updateKeybindDisplay();
                        selectedLane = -1;
                    }
                }
            });
            setFocusable(true);
            setFocusTraversalKeysEnabled(false); // Allow Tab key for keybinds
        }
        
        private JPanel createVolumeSection() {
            JPanel volumePanel = new JPanel(new GridLayout(2, 1, 0, 20));
            volumePanel.setBackground(GameAssets.BACKGROUND_COLOR);
            
            // Music volume section
            JPanel musicPanel = new JPanel(new BorderLayout(10, 5));
            musicPanel.setBackground(GameAssets.SURFACE_COLOR);
            musicPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel musicLabel = new JLabel("🎵 Music Volume");
            musicLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            musicLabel.setForeground(GameAssets.TEXT_COLOR);
            musicPanel.add(musicLabel, BorderLayout.NORTH);
            
            JPanel musicSliderPanel = new JPanel(new BorderLayout());
            musicSliderPanel.setBackground(GameAssets.SURFACE_COLOR);
            
            musicSlider = new JSlider(0, 100, (int)(GameAssets.musicVolume * 100));
            musicSlider.setBackground(GameAssets.SURFACE_COLOR);
            musicSlider.setForeground(GameAssets.PRIMARY_COLOR);
            musicSlider.setFocusable(false);
            
            musicValueLabel = new JLabel((int)(GameAssets.musicVolume * 100) + "%");
            musicValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            musicValueLabel.setForeground(GameAssets.TEXT_COLOR);
            musicValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            musicValueLabel.setPreferredSize(new Dimension(60, 30));
            
            musicSlider.addChangeListener(e -> {
                int value = musicSlider.getValue();
                musicValueLabel.setText(value + "%");
                GameAssets.musicVolume = value / 100.0f;
                setMusicVolume(GameAssets.musicVolume);
                GameAssets.saveConfiguration();
            });
            
            musicSliderPanel.add(musicSlider, BorderLayout.CENTER);
            musicSliderPanel.add(musicValueLabel, BorderLayout.EAST);
            musicPanel.add(musicSliderPanel, BorderLayout.CENTER);
            
            // SFX volume section
            JPanel sfxPanel = new JPanel(new BorderLayout(10, 5));
            sfxPanel.setBackground(GameAssets.SURFACE_COLOR);
            sfxPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel sfxLabel = new JLabel("🔊 SFX Volume");
            sfxLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            sfxLabel.setForeground(GameAssets.TEXT_COLOR);
            sfxPanel.add(sfxLabel, BorderLayout.NORTH);
            
            JPanel sfxSliderPanel = new JPanel(new BorderLayout());
            sfxSliderPanel.setBackground(GameAssets.SURFACE_COLOR);
            
            sfxSlider = new JSlider(0, 100, (int)(GameAssets.sfxVolume * 100));
            sfxSlider.setBackground(GameAssets.SURFACE_COLOR);
            sfxSlider.setForeground(GameAssets.PRIMARY_COLOR);
            sfxSlider.setFocusable(false);
            
            sfxValueLabel = new JLabel((int)(GameAssets.sfxVolume * 100) + "%");
            sfxValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            sfxValueLabel.setForeground(GameAssets.TEXT_COLOR);
            sfxValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            sfxValueLabel.setPreferredSize(new Dimension(60, 30));
            
            sfxSlider.addChangeListener(e -> {
                int value = sfxSlider.getValue();
                sfxValueLabel.setText(value + "%");
                GameAssets.sfxVolume = value / 100.0f;
                setSfxVolume(GameAssets.sfxVolume);
                GameAssets.saveConfiguration();
            });
            
            sfxSliderPanel.add(sfxSlider, BorderLayout.CENTER);
            sfxSliderPanel.add(sfxValueLabel, BorderLayout.EAST);
            sfxPanel.add(sfxSliderPanel, BorderLayout.CENTER);
            
            volumePanel.add(musicPanel);
            volumePanel.add(sfxPanel);
            
            return volumePanel;
        }
        
        private JPanel createKeybindsSection() {
            JPanel section = new JPanel();
            section.setBackground(GameAssets.SURFACE_COLOR);
            section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            section.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            
            JLabel titleLabel = new JLabel("🎮 Keybinds");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            section.add(titleLabel, gbc);
            
            keybindButtons = new JButton[4];
            
            for (int i = 0; i < 4; i++) {
                gbc.gridy = i + 1;
                gbc.gridwidth = 1;
                
                // Lane label
                JLabel label = new JLabel(LANE_NAMES[i] + ":");
                label.setFont(new Font("Segoe UI", Font.BOLD, 20));
                label.setForeground(GameAssets.TEXT_COLOR);
                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.WEST;
                section.add(label, gbc);
                
                // Key button with better visibility
                JButton button = createModernButton(KEY_NAMES[i], GameAssets.PRIMARY_COLOR, GameAssets.ACCENT_COLOR);
                button.setPreferredSize(new Dimension(150, 60));
                button.setFont(new Font("Segoe UI", Font.BOLD, 18));
                final int lane = i;
                button.addActionListener(e -> {
                    selectedLane = lane;
                    button.setText("Press key...");
                    // Request focus to receive key events
                    requestFocusInWindow();
                });
                keybindButtons[i] = button;
                
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.CENTER;
                section.add(button, gbc);
            }
            
            return section;
        }
        
        private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isRollover()) {
                        g2d.setColor(hoverColor);
                    } else {
                        g2d.setColor(bgColor);
                    }
                    
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            button.setForeground(GameAssets.TEXT_COLOR);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return button;
        }

        private void updateKeybindDisplay() {
            String[] keyNames = getKeyName();
            for (int i = 0; i < 4; i++) {
                keybindButtons[i].setText(keyNames[i]);
            }
        }

        private String[] getKeyName() {
            String[] names = new String[4];
            int[] binds = getKeybinds();
            for (int i = 0; i < 4; i++) {
                names[i] = KeyEvent.getKeyText(binds[i]);
            }
            return names;
        }
    }

    class GamePanel extends JPanel {
        private static final int LANE_GAP = 30; // Increased gap for wider lanes
        private static final int NUM_LANES = 4;
        private static final int MAX_COMBO_FOR_FEVER = 20;
        private static final int FEVER_DURATION = 5000; // ms
        
        // Timing windows for scoring (in pixels)
        private static final int PERFECT_WINDOW = 20;
        private static final int GOOD_WINDOW = 40;
        private static final int OKAY_WINDOW = 60;
        
        // Scoring values
        private static final int PERFECT_SCORE = 50;
        private static final int GOOD_SCORE = 30;
        private static final int OKAY_SCORE = 10;
        private static final int MISS_SCORE = 0;
        
        // Dynamic dimensions
        private int WIDTH;
        private int HEIGHT;
        private int TOTAL_LANES_WIDTH;
        private int START_X;
        private int LANE_WIDTH; // Now dynamic, not static
        private static final int[] LANE_POSITIONS = new int[NUM_LANES];
        private int HIT_ZONE_Y; // Will be set to bottom of screen

        private List<Note> notes;
        private boolean[] keysPressed;
        private Timer timer;
        private Random random;
        private boolean feverActive;
        private long feverStartTime;
        private long lastSpawnTime;
        private int score;
        private int combo;
        private List<Particle> particles;
        private List<TimingRating> timingRatings;
        private boolean[] laneGlowActive;
        private long[] laneGlowStartTime;
        private boolean isPaused = false;
        private JPanel pauseOverlay;
        private JButton resumeButton;
        private JButton songSelectionButton;
        private JButton mainMenuButton;
        private JButton quitButton;
        
        // Mapping system variables
        private Song currentSong;
        private SongMap currentMap;
        private int currentNoteIndex;
        private long songStartTime;
        private boolean isPlayingWithMap;
        
        // Scoring system variables
        private int perfectCount;
        private int goodCount;
        private int okayCount;
        private int missCount;
        private double songDuration; // Duration in seconds
        private boolean songCompleted;

        public GamePanel() {
            // Initialize dynamic dimensions
            updateDimensions();
            
            setBackground(GameAssets.BACKGROUND_COLOR);

            timer = new Timer(16, e -> {
                if (!isPaused) {
                    update();
                }
                repaint();
            });
            
            // Add component listener to handle resize
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateDimensions();
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        togglePause();
                        return;
                    }
                    if (!isPaused) {
                        handleKeyPressed(e.getKeyCode());
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (!isPaused) {
                        handleKeyReleased(e.getKeyCode());
                    }
                }
            });
            setFocusable(true);
            
            setupPauseOverlay();
            resetGame();
        }
        
        private void updateDimensions() {
            WIDTH = getWidth();
            HEIGHT = getHeight();
            
            // Make lanes take 1/3 of screen center
            TOTAL_LANES_WIDTH = WIDTH / 3;
            // Calculate individual lane width to fit 4 lanes with gaps
            LANE_WIDTH = (TOTAL_LANES_WIDTH - (NUM_LANES - 1) * LANE_GAP) / NUM_LANES;
            
            START_X = (WIDTH - TOTAL_LANES_WIDTH) / 2;
            HIT_ZONE_Y = HEIGHT - 100; // Set hit zone to very bottom of screen
            
            // Update lane positions
            for (int i = 0; i < NUM_LANES; i++) {
                LANE_POSITIONS[i] = START_X + i * (LANE_WIDTH + LANE_GAP);
            }
            
            // Update pause button positions
            updatePauseButtonPositions();
        }
        
        private void setupPauseOverlay() {
            pauseOverlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Semi-transparent background (no panel)
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(0, 0, WIDTH, HEIGHT);
                }
            };
            pauseOverlay.setOpaque(false);
            pauseOverlay.setVisible(false);
            pauseOverlay.setLayout(null);

            // Create pause buttons - will be positioned dynamically
            resumeButton = new JButton("Resume") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(Color.BLACK);
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    // Draw orange border for ZZZ style
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            resumeButton.setForeground(Color.WHITE);
            resumeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            resumeButton.setFocusPainted(false);
            resumeButton.addActionListener(e -> togglePause());

            songSelectionButton = new JButton("Song Selection") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(Color.BLACK);
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    // Draw orange border for ZZZ style
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            songSelectionButton.setForeground(Color.WHITE);
            songSelectionButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            songSelectionButton.setFocusPainted(false);
            songSelectionButton.addActionListener(e -> backToSongSelection());

            mainMenuButton = new JButton("Main Menu") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(Color.BLACK);
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    // Draw orange border for ZZZ style
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            mainMenuButton.setForeground(Color.WHITE);
            mainMenuButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            mainMenuButton.setFocusPainted(false);
            mainMenuButton.addActionListener(e -> backToMenu());

            quitButton = new JButton("Quit") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(Color.BLACK);
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.fill(roundedRect);
                    
                    // Draw orange border for ZZZ style
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(roundedRect);
                    
                    super.paintComponent(g);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            quitButton.setForeground(Color.WHITE);
            quitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            quitButton.setFocusPainted(false);
            quitButton.addActionListener(e -> System.exit(0));

            pauseOverlay.add(resumeButton);
            pauseOverlay.add(songSelectionButton);
            pauseOverlay.add(mainMenuButton);
            pauseOverlay.add(quitButton);
            
            // Initial positioning
            updatePauseButtonPositions();
        }
        
        private void updatePauseButtonPositions() {
            // Center the pause overlay panel
            if (pauseOverlay != null) {
                pauseOverlay.setBounds(0, 0, WIDTH, HEIGHT);
            }
            
            // Calculate center position for buttons
            int centerX = (WIDTH - 300) / 2;
            int centerY = (HEIGHT - 200) / 2; // Adjusted for 4 buttons
            
            // Update button positions to be perfectly centered
            if (resumeButton != null) {
                resumeButton.setBounds(centerX, centerY, 300, 50);
            }
            if (songSelectionButton != null) {
                songSelectionButton.setBounds(centerX, centerY + 60, 300, 50);
            }
            if (mainMenuButton != null) {
                mainMenuButton.setBounds(centerX, centerY + 120, 300, 50);
            }
            if (quitButton != null) {
                quitButton.setBounds(centerX, centerY + 180, 300, 50);
            }
        }
        
        private void togglePause() {
            isPaused = !isPaused;
            if (isPaused) {
                updatePauseButtonPositions(); // Update positions before showing
                add(pauseOverlay);
                pauseOverlay.setVisible(true);
                setLayout(new OverlayLayout(this));
                revalidate();
                repaint();
            } else {
                pauseOverlay.setVisible(false);
                remove(pauseOverlay);
                setLayout(new BorderLayout());
                revalidate();
                repaint();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw gradient background
            GradientPaint backgroundGradient = new GradientPaint(0, 0, GameAssets.BACKGROUND_COLOR, 0, HEIGHT, GameAssets.SECONDARY_COLOR);
            g2d.setPaint(backgroundGradient);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Draw lanes with modern styling
            for (int i = 0; i < NUM_LANES; i++) {
                int x = LANE_POSITIONS[i];
                
                // Lane background with better visibility
                Color laneColor = new Color(255, 255, 255, 15);
                g2d.setColor(laneColor);
                g2d.fillRoundRect(x, 0, LANE_WIDTH, HEIGHT, 10, 10);
                
                // Lane border with better visibility
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, 0, LANE_WIDTH, HEIGHT, 10, 10);
            }
            
            // Draw game elements (always visible, even when paused)
            // Draw score panel
            drawScorePanel(g2d);
            
            // Draw fever bar
            drawFeverBar(g2d);
            
            // Always draw these elements (but they won't update when paused)
            // Draw hit zone with arrow shapes
            drawHitZone(g2d);
            
            // Draw particles
            particles.forEach(particle -> particle.draw(g2d));
            
            // Draw timing ratings
            timingRatings.forEach(rating -> rating.draw(g2d));
            
            // Draw notes
            notes.forEach(note -> note.draw(g2d));
            
            // Draw pause overlay if paused
            if (isPaused) {
                // Add dimming effect to show game is frozen
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, WIDTH, HEIGHT);
                
                pauseOverlay.paint(g);
            }
        }
        
        private void drawScorePanel(Graphics2D g2d) {
            // Score panel background
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(10, 10, 200, 80, 15, 15);
            
            // Score panel border
            g2d.setColor(GameAssets.PRIMARY_COLOR);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(10, 10, 200, 80, 15, 15);
            
            // Score text
            g2d.setColor(GameAssets.TEXT_COLOR);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2d.drawString("Score: " + score, 20, 40);
            
            // Combo text with color based on combo level
            if (combo >= MAX_COMBO_FOR_FEVER - 5) {
                g2d.setColor(GameAssets.ACCENT_COLOR);
            } else if (combo >= MAX_COMBO_FOR_FEVER / 2) {
                g2d.setColor(GameAssets.WARNING_COLOR);
            } else {
                g2d.setColor(GameAssets.SUCCESS_COLOR);
            }
            g2d.drawString("Combo: " + combo, 20, 70);
        }
        
        private void drawFeverBar(Graphics2D g2d) {
            // Fever bar positioned closer to the lanes on the right
            int barWidth = 20;
            int barHeight = 200;
            int barX = START_X + TOTAL_LANES_WIDTH + 20; // Position right after the lanes
            int barY = HEIGHT - barHeight - 20;
            
            // Bar background
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);
            
            // Bar border
            g2d.setColor(GameAssets.PRIMARY_COLOR);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);
            
            // Progress
            float progress;
            Color progressColor;
            if (feverActive) {
                long elapsed = System.currentTimeMillis() - feverStartTime;
                progress = Math.max(0.0f, 1.0f - (elapsed / (float) FEVER_DURATION));
                progressColor = GameAssets.ACCENT_COLOR;
            } else {
                progress = combo / (float) MAX_COMBO_FOR_FEVER;
                progressColor = GameAssets.SUCCESS_COLOR;
            }
            
            int progressHeight = (int) (barHeight * progress);
            if (progressHeight > 0) {
                // Gradient for progress
                GradientPaint progressGradient = new GradientPaint(barX, barY + barHeight, progressColor, barX, barY + barHeight - progressHeight, GameAssets.SECONDARY_COLOR);
                g2d.setPaint(progressGradient);
                g2d.fillRoundRect(barX + 2, barY + barHeight - progressHeight - 2, barWidth - 4, progressHeight, 8, 8);
            }
        }
        
        private void drawHitZone(Graphics2D g2d) {
            // Hit zone glow effect
            GradientPaint hitZoneGradient = new GradientPaint(0, HIT_ZONE_Y - 10, new Color(255, 255, 255, 0), 0, HIT_ZONE_Y + 10, new Color(255, 255, 255, 50));
            g2d.setPaint(hitZoneGradient);
            g2d.fillRect(0, HIT_ZONE_Y - 5, WIDTH, 10);
            
            // Draw arrow-shaped hit zone markers
            for (int i = 0; i < NUM_LANES; i++) {
                int x = LANE_POSITIONS[i] + LANE_WIDTH / 2;
                g2d.setColor(GameAssets.NOTE_COLORS[i]);
                drawArrow(g2d, x, HIT_ZONE_Y, GameAssets.ARROW_SIZE, i, true);
            }
        }
        
        public void resetGame() {
            if (timer.isRunning()) {
                timer.stop();
            }
            notes = new ArrayList<>();
            keysPressed = new boolean[256];
            random = new Random();
            feverActive = false;
            feverStartTime = 0;
            lastSpawnTime = System.currentTimeMillis();
            score = 0;
            combo = 0;
            particles = new ArrayList<>();
            timingRatings = new ArrayList<>();
            laneGlowActive = new boolean[NUM_LANES];
            laneGlowStartTime = new long[NUM_LANES];
            isPaused = false;
            pauseOverlay.setVisible(false);
            remove(pauseOverlay);
            
            // Initialize scoring
            perfectCount = 0;
            goodCount = 0;
            okayCount = 0;
            missCount = 0;
            songCompleted = false;
            
            timer.start();
        }
        
        public void startGameWithSong(Song song) {
            // Set current song and load its map
            currentSong = song;
            currentMap = SongMap.loadFromFile(song.getMapFilePath());
            
            if (currentMap == null) {
                // Generate auto-map if no custom map exists
                currentMap = SongMap.generateAutoMap(song.filePath);
                currentMap.saveToFile(); // Save for future use
                System.out.println("Generated and saved auto-map for: " + song.title);
            }
            
            // Calculate song duration based on map
            if (currentMap.notes.size() > 0) {
                NoteData lastNote = currentMap.notes.get(currentMap.notes.size() - 1);
                songDuration = lastNote.time + 5.0; // Add 5 seconds after last note
            } else {
                songDuration = 120.0; // Default 2 minutes
            }
            
            // Initialize mapping system
            currentNoteIndex = 0;
            songStartTime = System.currentTimeMillis();
            isPlayingWithMap = true;
            
            // Reset game state
            resetGame();
        }

        private void spawnNote(int lane) {
            notes.add(new Note(lane, lane)); // direction = lane
        }

        private void update() {
            // Check for song completion
            if (isPlayingWithMap && !songCompleted) {
                double elapsedTime = (System.currentTimeMillis() - songStartTime) / 1000.0;
                if (elapsedTime >= songDuration) {
                    completeSong();
                    return;
                }
            }
            
            if (isPlayingWithMap && currentMap != null) {
                updateWithMap();
            } else {
                updateRandom();
            }
            
            // Update particles
            particles.forEach(Particle::update);
            particles.removeIf(Particle::isDead);
            
            // Update timing ratings
            timingRatings.forEach(TimingRating::update);
            timingRatings.removeIf(TimingRating::isDead);
            
            notes.forEach(Note::update);
            checkHits();
            notes.removeIf(note -> {
                if (note.y >= HEIGHT) {
                    if (!note.hit) {
                        combo = 0;
                        missCount++;
                        // Add MISS rating for notes that pass without being hit
                        timingRatings.add(new TimingRating("MISS", note.lane));
                    }
                    return true;
                }
                return false;
            });
        }
        
        private void updateWithMap() {
            long currentTime = System.currentTimeMillis();
            double elapsedTime = (currentTime - songStartTime) / 1000.0; // Convert to seconds
            
            // Spawn notes based on map timing
            while (currentNoteIndex < currentMap.notes.size()) {
                NoteData noteData = currentMap.notes.get(currentNoteIndex);
                double noteTime = noteData.time + currentMap.offset;
                
                // Calculate when to spawn note (time it takes to reach hit zone)
                double timeToReachHitZone = (double) HIT_ZONE_Y / GameAssets.NOTE_SPEED / 60.0; // Rough estimate
                double spawnTime = noteTime - timeToReachHitZone;
                
                if (elapsedTime >= spawnTime) {
                    spawnNote(noteData.lane);
                    currentNoteIndex++;
                } else {
                    break; // Not time to spawn next note yet
                }
            }
        }
        
        private void updateRandom() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSpawnTime > GameAssets.SPAWN_INTERVAL) {
                int lane = random.nextInt(NUM_LANES);
                spawnNote(lane);
                lastSpawnTime = currentTime;
            }
        }
        
        private void completeSong() {
            songCompleted = true;
            showResults();
        }
        
        private void showResults() {
            // Calculate rank
            String rank = calculateRank();
            
            // Create results dialog
            JDialog resultsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Song Complete!", true);
            resultsDialog.setSize(400, 500);
            resultsDialog.setLocationRelativeTo(this);
            resultsDialog.setLayout(new BorderLayout());
            
            // Results panel
            JPanel resultsPanel = new JPanel();
            resultsPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Title
            JLabel titleLabel = new JLabel(currentSong.title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Rank
            JLabel rankLabel = new JLabel("Rank: " + rank);
            rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            rankLabel.setForeground(getRankColor(rank));
            rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Score
            JLabel scoreLabel = new JLabel("Score: " + score);
            scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            scoreLabel.setForeground(GameAssets.TEXT_COLOR);
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Stats
            JLabel perfectLabel = new JLabel("Perfect: " + perfectCount);
            perfectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            perfectLabel.setForeground(new Color(100, 255, 100));
            perfectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel goodLabel = new JLabel("Good: " + goodCount);
            goodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            goodLabel.setForeground(new Color(100, 200, 255));
            goodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel okayLabel = new JLabel("Okay: " + okayCount);
            okayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            okayLabel.setForeground(new Color(255, 255, 100));
            okayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel missLabel = new JLabel("Miss: " + missCount);
            missLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            missLabel.setForeground(new Color(255, 100, 100));
            missLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add components to panel
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            resultsPanel.add(titleLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            resultsPanel.add(rankLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            resultsPanel.add(scoreLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            resultsPanel.add(perfectLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(goodLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(okayLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(missLabel);
            
            // Button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            buttonPanel.setLayout(new FlowLayout());
            
            JButton retryButton = new JButton("Retry");
            retryButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            retryButton.setBackground(GameAssets.PRIMARY_COLOR);
            retryButton.setForeground(Color.WHITE);
            retryButton.setFocusPainted(false);
            retryButton.addActionListener(e -> {
                resultsDialog.dispose();
                startGameWithSong(currentSong);
            });
            
            JButton menuButton = new JButton("Song Selection");
            menuButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            menuButton.setBackground(GameAssets.PRIMARY_COLOR);
            menuButton.setForeground(Color.WHITE);
            menuButton.setFocusPainted(false);
            menuButton.addActionListener(e -> {
                resultsDialog.dispose();
                ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSongSelection();
            });
            
            buttonPanel.add(retryButton);
            buttonPanel.add(menuButton);
            
            resultsDialog.add(resultsPanel, BorderLayout.CENTER);
            resultsDialog.add(buttonPanel, BorderLayout.SOUTH);
            resultsDialog.setVisible(true);
        }
        
        private String calculateRank() {
            int totalNotes = perfectCount + goodCount + okayCount + missCount;
            if (totalNotes == 0) return "F";
            
            double perfectRatio = (double) perfectCount / totalNotes;
            
            if (perfectRatio >= 0.95) return "S";
            if (perfectRatio >= 0.85) return "A";
            if (perfectRatio >= 0.70) return "B";
            if (perfectRatio >= 0.50) return "C";
            return "F";
        }
        
        private Color getRankColor(String rank) {
            switch (rank) {
                case "S": return new Color(255, 215, 0); // Gold
                case "A": return new Color(192, 192, 192); // Silver
                case "B": return new Color(205, 127, 50); // Bronze
                case "C": return new Color(100, 150, 255); // Blue
                default: return new Color(255, 100, 100); // Red
            }
        }
        
        // Check for fever mode timeout

        private void checkHits() {
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            
            // Reset all notes glowing state
            notes.forEach(note -> note.glowing = false);
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.isInHitZone() && keysPressed[keyMap[note.direction]]) {
                    // Note hit - don't make incoming notes glow, only hit zone arrows glow
                    
                    // Calculate timing accuracy
                    int distanceFromHitZone = Math.abs(note.y + GameAssets.NOTE_SIZE / 2 - HIT_ZONE_Y);
                    String rating;
                    int points;
                    
                    if (distanceFromHitZone <= PERFECT_WINDOW) {
                        rating = "PERFECT";
                        points = PERFECT_SCORE;
                        perfectCount++;
                    } else if (distanceFromHitZone <= GOOD_WINDOW) {
                        rating = "GOOD";
                        points = GOOD_SCORE;
                        goodCount++;
                    } else if (distanceFromHitZone <= OKAY_WINDOW) {
                        rating = "OKAY";
                        points = OKAY_SCORE;
                        okayCount++;
                    } else {
                        rating = "MISS";
                        points = MISS_SCORE;
                        missCount++;
                        combo = 0;
                    }
                    
                    // Add rating display
                    timingRatings.add(new TimingRating(rating, note.lane));
                    
                    // Update score and combo
                    score += points;
                    if (!rating.equals("MISS")) {
                        combo++;
                        if (combo >= MAX_COMBO_FOR_FEVER && !feverActive) {
                            feverActive = true;
                            feverStartTime = System.currentTimeMillis();
                        }
                    }
                    
                    // Create hit particles
                    for (int j = 0; j < GameAssets.PARTICLE_COUNT; j++) {
                        particles.add(new Particle(
                            LANE_POSITIONS[note.direction] + LANE_WIDTH / 2,
                            HIT_ZONE_Y,
                            feverActive ? GameAssets.ACCENT_COLOR : GameAssets.SUCCESS_COLOR
                        ));
                    }
                    
                    note.hit = true;
                    notes.remove(i);
                    i--;
                }
            }
        }

        public void handleKeyPressed(int keyCode) {
            if (keyCode < keysPressed.length) {
                keysPressed[keyCode] = true;
            }
            
            // Check if this is a keybind key and activate glow effect
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            for (int lane = 0; lane < keyMap.length; lane++) {
                if (keyCode == keyMap[lane]) {
                    // Activate glow for this lane
                    laneGlowActive[lane] = true;
                    laneGlowStartTime[lane] = System.currentTimeMillis();
                    
                    // Play key press sound
                    playKeyPressSound();
                    break;
                }
            }
            
            // Handle pause menu shortcuts
            if (isPaused) {
                if (keyCode == KeyEvent.VK_R) {
                    backToMenu();
                } else if (keyCode == KeyEvent.VK_Q) {
                    System.exit(0);
                }
            }
        }

        public void handleKeyReleased(int keyCode) {
            if (keyCode < keysPressed.length) {
                keysPressed[keyCode] = false;
            }
            
            // Deactivate glow effect when key is released
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            for (int lane = 0; lane < keyMap.length; lane++) {
                if (keyCode == keyMap[lane]) {
                    laneGlowActive[lane] = false;
                    break;
                }
            }
        }
        
        private void playKeyPressSound() {
            // Play key press sound effect
            try {
                File keyPressFile = new File(GameAssets.SOUNDS_FOLDER + GameAssets.SFX_FILES[4]); // key_press.wav
                if (keyPressFile.exists()) {
                    // In a real implementation, you would play the sound here
                    // Sound is played silently for now
                }
            } catch (Exception e) {
                // Sound loading failed, continue silently
            }
        }

        public void backToSongSelection() {
            ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSongSelection();
        }

        // Helper method to draw arrow shapes with custom image support and glow effects
        private void drawArrow(Graphics2D g2d, int x, int y, int size, int direction, boolean isHitZone) {
            // Try to load custom arrow image first
            BufferedImage arrowImage = loadArrowImage(direction);
            
            // Check if this lane should glow (key is pressed)
            boolean shouldGlow = direction < laneGlowActive.length && laneGlowActive[direction];
            
            if (shouldGlow) {
                // Draw glow effect
                BufferedImage glowImage = GameAssets.getArrowGlowImage(direction);
                if (glowImage != null) {
                    // Draw custom glow image
                    g2d.drawImage(glowImage, x - size/2 - 5, y - size/2 - 5, size + 10, size + 10, null);
                } else {
                    // Fallback glow effect
                    drawGlowEffect(g2d, x, y, size, direction);
                }
            }
            
            if (arrowImage != null) {
                // Draw custom arrow image
                g2d.drawImage(arrowImage, x - size/2, y - size/2, size, size, null);
            } else {
                // Fallback to drawn arrow
                drawCustomArrow(g2d, x, y, size, direction, isHitZone);
            }
        }

        private BufferedImage loadArrowImage(int direction) {
            return GameAssets.getArrowImage(direction);
        }
        
        private void drawGlowEffect(Graphics2D g2d, int x, int y, int size, int direction) {
            // Save original composite
            Composite oldComposite = g2d.getComposite();
            
            // Create glowing effect with transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            
            // Draw glow circles around the arrow
            g2d.setColor(GameAssets.ACCENT_COLOR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw multiple circles for glow effect
            for (int i = 3; i > 0; i--) {
                int glowSize = size + (i * 8);
                float alpha = 0.3f / i;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.fillOval(x - glowSize/2, y - glowSize/2, glowSize, glowSize);
            }
            
            // Restore original composite
            g2d.setComposite(oldComposite);
        }

        private void drawCustomArrow(Graphics2D g2d, int x, int y, int size, int direction, boolean isHitZone) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Save original transform
            AffineTransform oldTransform = g2d.getTransform();
            
            // Move to position and rotate based on direction
            g2d.translate(x, y);
            switch (direction) {
                case 0: // Left arrow
                    g2d.rotate(Math.toRadians(-90));
                    break;
                case 1: // Up arrow
                    g2d.rotate(Math.toRadians(0));
                    break;
                case 2: // Down arrow  
                    g2d.rotate(Math.toRadians(180));
                    break;
                case 3: // Right arrow
                    g2d.rotate(Math.toRadians(90));
                    break;
            }
            
            // Draw arrow shape
            Path2D arrow = new Path2D.Double();
            arrow.moveTo(0, -size/2);
            arrow.lineTo(-size/3, 0);
            arrow.lineTo(-size/6, 0);
            arrow.lineTo(-size/6, size/2);
            arrow.lineTo(size/6, size/2);
            arrow.lineTo(size/6, 0);
            arrow.lineTo(size/3, 0);
            arrow.lineTo(0, -size/2);
            
            if (isHitZone) {
                // Hit zone arrow - outline only
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(arrow);
            } else {
                // Note arrow - filled with gradient
                GradientPaint noteGradient = new GradientPaint(-size/2, -size/2, GameAssets.NOTE_COLORS[direction], size/2, size/2, GameAssets.ACCENT_COLOR);
                g2d.setPaint(noteGradient);
                g2d.fill(arrow);
                
                // Arrow border
                g2d.setPaint(null);
                g2d.setColor(GameAssets.TEXT_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(arrow);
            }
            
            // Restore original transform
            g2d.setTransform(oldTransform);
        }

        class Note {
            int lane;
            int direction; // 0: left, 1: up, 2: down, 3: right
            int y = 0;
            int x;
            boolean hit = false;
            boolean glowing = false; // Track if note should glow

            Note(int lane, int direction) {
                this.lane = lane;
                this.direction = direction;
                // Position note in center of its lane
                this.x = LANE_POSITIONS[lane] + LANE_WIDTH / 2;
            }

            void update() {
                y += GameAssets.NOTE_SPEED;
            }

            void draw(Graphics2D g2d) {
                // Draw glow effect if note is being pressed
                if (glowing) {
                    g2d.setColor(new Color(255, 255, 100, 100));
                    g2d.fillOval(x - GameAssets.ARROW_SIZE, y + GameAssets.NOTE_SIZE / 2 - GameAssets.ARROW_SIZE, 
                               GameAssets.ARROW_SIZE * 2, GameAssets.ARROW_SIZE * 2);
                }
                
                // Draw arrow centered in the lane
                drawArrow(g2d, x, y + GameAssets.NOTE_SIZE / 2, GameAssets.ARROW_SIZE, direction, false);
            }

            boolean isInHitZone() {
                return y + GameAssets.NOTE_SIZE > HIT_ZONE_Y && y < HEIGHT;
            }
        }
        
        class TimingRating {
            String rating;
            int y;
            int targetY;
            int life;
            Color color;
            
            TimingRating(String rating, int lane) {
                this.rating = rating;
                this.y = HIT_ZONE_Y;
                this.targetY = HIT_ZONE_Y - 200; // Same height as fever meter
                this.life = 60; // Frames to display
                
                // Set color based on rating
                switch (rating) {
                    case "PERFECT":
                        color = new Color(255, 215, 0); // Gold
                        break;
                    case "GOOD":
                        color = new Color(0, 255, 150); // Green
                        break;
                    case "OKAY":
                        color = new Color(255, 200, 0); // Yellow
                        break;
                    case "MISS":
                        color = new Color(255, 50, 50); // Red
                        break;
                }
            }
            
            void update() {
                if (y > targetY) {
                    y -= 3; // Move upward
                }
                life--;
            }
            
            void draw(Graphics2D g2d) {
                if (life <= 0) return;
                
                // Calculate opacity based on life
                float opacity = Math.min(1.0f, life / 30.0f);
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * opacity)));
                
                // Draw rating text
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(rating);
                
                // Position on the left side of the lanes
                int x = START_X - textWidth - 20;
                g2d.drawString(rating, x, y);
            }
            
            boolean isDead() {
                return life <= 0;
            }
        }
    }
}
