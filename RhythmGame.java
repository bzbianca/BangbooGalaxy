import javax.swing.*;
import javax.swing.OverlayLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
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
        
        // UI Colors - Retro orange and black theme
        public static final Color BACKGROUND_COLOR = new Color(10, 10, 15); // Very dark blue-black
        public static final Color PRIMARY_COLOR = new Color(255, 140, 0); // Bright orange
        public static final Color SECONDARY_COLOR = new Color(25, 25, 35); // Dark blue-gray
        public static final Color ACCENT_COLOR = new Color(255, 200, 50); // Light orange
        public static final Color SUCCESS_COLOR = new Color(255, 100, 0); // Orange success
        public static final Color WARNING_COLOR = new Color(255, 220, 0); // Yellow-orange
        public static final Color DANGER_COLOR = new Color(200, 50, 0); // Dark orange-red
        public static final Color RETRO_GRID_COLOR = new Color(255, 150, 0, 60); // Semi-transparent orange
        public static final Color RETRO_SCANLINE_COLOR = new Color(0, 0, 0, 120); // Dark scanlines
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
        // Orange style buttons with orange borders
        public static final Color MAIN_BUTTON_COLOR = new Color(255, 150, 0); // Orange background
        public static final Color MAIN_BUTTON_HOVER_COLOR = new Color(255, 200, 50); // Lighter orange hover
        public static final Color BUTTON_BORDER_COLOR = new Color(255, 100, 0); // Darker orange border
        
        // === FONTS ===
        // Custom font implementation
        private static Font customFont;
        private static Font customFontBold;
        private static Font customFontLarge;
        private static boolean fontsLoaded = false;
        
        // Font loading method
        public static void loadCustomFonts() {
            if (fontsLoaded) return;
            
            try {
                // Load custom font from fonts folder
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/default.ttf"));
                
                // Register the font
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(baseFont);
                
                // Create different sizes
                customFont = baseFont.deriveFont(Font.PLAIN, 16f);
                customFontBold = baseFont.deriveFont(Font.BOLD, 16f);
                customFontLarge = baseFont.deriveFont(Font.BOLD, 24f);
                
                fontsLoaded = true;
                System.out.println("Custom font loaded successfully!");
                
            } catch (Exception e) {
                System.err.println("Failed to load custom font, using default: " + e.getMessage());
                // Fallback to default fonts
                customFont = new Font("Segoe UI", Font.PLAIN, 16);
                customFontBold = GameAssets.getCustomFontBold().deriveFont(16f);
                customFontLarge = GameAssets.getCustomFontBold().deriveFont(24f);
                fontsLoaded = true;
            }
        }
        
        // Font getters
        public static Font getCustomFont() {
            if (!fontsLoaded) loadCustomFonts();
            return customFont;
        }
        
        public static Font getCustomFontBold() {
            if (!fontsLoaded) loadCustomFonts();
            return customFontBold;
        }
        
        public static Font getCustomFontLarge() {
            if (!fontsLoaded) loadCustomFonts();
            return customFontLarge;
        }
        
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
        private static BufferedImage[] backgroundImages = new BufferedImage[5]; // Fixed: was 4, now 5 to match BACKGROUND_FILES
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
            
            // Load custom fonts first
            loadCustomFonts();
            
            // Ensure maps folder exists for robust map loading
            File mapsFolder = new File(MAPS_FOLDER);
            if (!mapsFolder.exists()) {
                mapsFolder.mkdirs();
                System.out.println("Created maps folder: " + MAPS_FOLDER);
            }
            
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
            for (int i = 0; i < BACKGROUND_FILES.length && i < backgroundImages.length; i++) {
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
            if (direction >= 0 && direction < arrowImages.length) {
                return arrowImages[direction];
            }
            return null; // Safe fallback for invalid indices
        }
        
        public static BufferedImage getArrowGlowImage(int direction) {
            if (!assetsLoaded) loadAllAssets();
            if (direction >= 0 && direction < arrowGlowImages.length) {
                return arrowGlowImages[direction];
            }
            return null; // Safe fallback for invalid indices
        }
        
        public static BufferedImage getBackgroundImage(int screen) {
            if (!assetsLoaded) loadAllAssets();
            if (screen >= 0 && screen < backgroundImages.length) {
                return backgroundImages[screen];
            }
            return null; // Safe fallback for invalid indices
        }
        
        public static BufferedImage getButtonImage(int buttonIndex) {
            if (!assetsLoaded) loadAllAssets();
            if (buttonIndex >= 0 && buttonIndex < buttonImages.length) {
                return buttonImages[buttonIndex];
            }
            return null; // Safe fallback for invalid indices
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
        
        // === UI CUSTOMIZATION ===
        // Timer and accuracy display settings
        public static boolean SHOW_COUNTDOWN_TIMER = true;
        public static boolean SHOW_ACCURACY = true;
        public static boolean TIMER_DISPLAY_SECONDS = true;
        public static String TIMER_FORMAT = "%.1fs"; // Format: "%.1fs" for decimal, "%.0fs" for whole seconds
        
        // Results screen settings
        public static boolean SHOW_IN_GAME_RESULTS = true;
        public static boolean SHOW_RESULTS_TIME = true;
        public static boolean SHOW_RESULTS_ACCURACY = true;
        public static boolean SHOW_RESULTS_MAX_COMBO = true;
        public static boolean SHOW_RESULTS_DETAILED_STATS = true;
        
        // Map duration settings
        public static double MAP_END_BUFFER_SECONDS = 2.0; // Time after last note before map ends
        public static double MINIMUM_MAP_DURATION = 15.0; // Minimum map duration in seconds
        public static double DEFAULT_MAP_DURATION = 30.0; // Default duration for empty maps
        
        // Note glow settings
        public static boolean ENABLE_NOTE_GLOW = false; // Set to false to disable note glow completely
        public static boolean ENABLE_HIT_ZONE_GLOW = true; // Hit zone arrows glow when keys pressed
        
        // Ranking thresholds (percentage for perfect hits)
        public static double S_RANK_THRESHOLD = 0.95; // 95% perfect for S rank
        public static double A_RANK_THRESHOLD = 0.85; // 85% perfect for A rank
        public static double B_RANK_THRESHOLD = 0.70; // 70% perfect for B rank
        public static double C_RANK_THRESHOLD = 0.50; // 50% perfect for C rank
        
        // === RESULTS SCREEN CUSTOMIZATION ===
        // Results panel dimensions
        public static int RESULTS_PANEL_WIDTH = 550;
        public static int RESULTS_PANEL_HEIGHT = 450;
        public static int RESULTS_BORDER_RADIUS = 25;
        
        // Results colors and effects
        public static Color RESULTS_OVERLAY_COLOR = new Color(0, 0, 0, 220);
        public static Color RESULTS_PANEL_COLOR = GameAssets.SURFACE_COLOR;
        public static Color RESULTS_PANEL_GRADIENT_END = new Color(40, 40, 50);
        public static Color RESULTS_BORDER_COLOR = GameAssets.BUTTON_BORDER_COLOR;
        public static Color RESULTS_INNER_BORDER_COLOR = new Color(255, 165, 0, 100);
        public static int RESULTS_BORDER_WIDTH = 4;
        public static int RESULTS_INNER_BORDER_WIDTH = 2;
        
        // Results fonts
        public static String RESULTS_TITLE_FONT = "Segoe UI";
        public static int RESULTS_TITLE_SIZE = 28;
        public static String RESULTS_RANK_FONT = "Segoe UI";
        public static int RESULTS_RANK_SIZE = 72;
        public static String RESULTS_LABEL_FONT = "Segoe UI";
        public static int RESULTS_LABEL_SIZE = 20;
        public static String RESULTS_STATS_FONT = "Segoe UI";
        public static int RESULTS_STATS_SIZE = 18;
        
        // Results positioning
        public static int RESULTS_TITLE_Y_OFFSET = 50;
        public static int RESULTS_TITLE_LINE_Y = 65;
        public static int RESULTS_SEPARATOR_MARGIN = 50;
        public static int RESULTS_CONTENT_Y_OFFSET = 120;
        public static int RESULTS_RANK_X_OFFSET = 80;
        public static int RESULTS_RANK_Y_OFFSET = 50;
        public static int RESULTS_SCORE_RIGHT_MARGIN = 80;
        public static int RESULTS_STATS_PANEL_ALPHA = 150;
        public static int RESULTS_STATS_PANEL_RADIUS = 15;
        
        // Results buttons
        public static int RESULTS_BUTTON_WIDTH = 140;
        public static int RESULTS_BUTTON_HEIGHT = 40;
        public static int RESULTS_BUTTON_SPACING = 20;
        public static int RESULTS_BUTTON_BOTTOM_MARGIN = 60;
        
        // Results background images
        public static String RESULTS_BACKGROUND_IMAGE = "results_bg.png";
        public static String RESULTS_PANEL_IMAGE = "results_panel.png";
        public static boolean USE_RESULTS_BACKGROUND_IMAGE = true;
        public static boolean USE_RESULTS_PANEL_IMAGE = true;
        
        // === CREDITS SCREEN CUSTOMIZATION ===
        // Credits panel dimensions
        public static int CREDITS_PANEL_WIDTH = 650;
        public static int CREDITS_PANEL_HEIGHT = 550;
        public static int CREDITS_BORDER_RADIUS = 20;
        
        // Credits colors and effects
        public static Color CREDITS_OVERLAY_COLOR = new Color(0, 0, 0, 230);
        public static Color CREDITS_PANEL_COLOR = GameAssets.SURFACE_COLOR;
        public static Color CREDITS_BORDER_COLOR = GameAssets.BUTTON_BORDER_COLOR;
        public static int CREDITS_BORDER_WIDTH = 3;
        
        // Credits fonts
        public static String CREDITS_TITLE_FONT = "Segoe UI";
        public static int CREDITS_TITLE_SIZE = 36;
        public static String CREDITS_SECTION_FONT = "Segoe UI";
        public static int CREDITS_SECTION_SIZE = 20;
        public static String CREDITS_TEXT_FONT = "Segoe UI";
        public static int CREDITS_TEXT_SIZE = 18;
        
        // Credits positioning
        public static int CREDITS_TITLE_Y_OFFSET = 60;
        public static int CREDITS_SEPARATOR_Y = 80;
        public static int CREDITS_CONTENT_Y_OFFSET = 120;
        public static int CREDITS_LINE_HEIGHT = 30;
        public static int CREDITS_LEFT_MARGIN = 70;
        public static int CREDITS_SECTION_SPACING = 20;
        public static int CREDITS_SEPARATOR_MARGIN = 50;
        
        // Credits button
        public static int CREDITS_BUTTON_WIDTH = 120;
        public static int CREDITS_BUTTON_HEIGHT = 40;
        public static int CREDITS_BUTTON_BOTTOM_MARGIN = 60;
        
        // Credits background images
        public static String CREDITS_BACKGROUND_IMAGE = "credits_bg.png";
        public static String CREDITS_PANEL_IMAGE = "credits_panel.png";
        public static boolean USE_CREDITS_BACKGROUND_IMAGE = false;
        public static boolean USE_CREDITS_PANEL_IMAGE = false;
        
        // Credits content customization
        public static String CREDITS_GAME_TITLE = "BANGBOO GALAXY";
        public static String CREDITS_VERSION = "VERSION 1.0";
        public static String CREDITS_COPYRIGHT = "© 2026 Bangboo Galaxy";
        public static String CREDITS_BACK_BUTTON_TEXT = "Back";
        
        // Credits sections (can be customized)
        public static String[] CREDITS_GAME_DEV_SECTION = {
            "GAME DEVELOPMENT",
            "Developer: Bianca",
            "Design: Bianca", 
            "Vibecoded with Windsurf"
        };
        public static String[] CREDITS_MUSIC_SECTION = {
            "MUSIC & SOUNDS",
            "Retro Music Files",
            "Default Sound Effects"
        };
        public static String[] CREDITS_THANKS_SECTION = {
            "SPECIAL THANKS",
            "Java Swing Framework",
            "Open Source Community",
            "Game Development Resources"
        };
        
        // Image loading utility method
        public static BufferedImage loadImage(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    return ImageIO.read(file);
                }
            } catch (IOException e) {
                System.err.println("Failed to load image: " + imagePath + " - " + e.getMessage());
            }
            return null;
        }
        
        // Retro gradient pattern helper method
        public static void paintRetroGradient(Graphics2D g2d, int width, int height) {
            // Base gradient
            GradientPaint baseGradient = new GradientPaint(0, 0, PRIMARY_COLOR, width, height, SECONDARY_COLOR);
            g2d.setPaint(baseGradient);
            g2d.fillRect(0, 0, width, height);
            
            // Add retro grid pattern
            g2d.setColor(RETRO_GRID_COLOR);
            for (int x = 0; x < width; x += 20) {
                g2d.drawLine(x, 0, x, height);
            }
            for (int y = 0; y < height; y += 20) {
                g2d.drawLine(0, y, width, y);
            }
            
            // Add retro scanlines
            g2d.setColor(RETRO_SCANLINE_COLOR);
            for (int y = 0; y < height; y += 3) {
                g2d.drawLine(0, y, width, y);
            }
            
            // Add retro geometric shapes
            g2d.setColor(ACCENT_COLOR);
            for (int i = 0; i < 5; i++) {
                int x = (int) (Math.random() * (width - 60)) + 30;
                int y = (int) (Math.random() * (height - 60)) + 30;
                int size = (int) (Math.random() * 30) + 20;
                g2d.drawRect(x, y, size, size);
            }
        }
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
            try {
                String fileName = new File(filePath).getName();
                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                return GameAssets.MAPS_FOLDER + baseName + ".map";
            } catch (Exception e) {
                // Fallback if file name parsing fails
                return GameAssets.MAPS_FOLDER + "default.map";
            }
        }
        
        public boolean hasMapFile() {
            String mapPath = getMapFilePath();
            File mapFile = new File(mapPath);
            return mapFile.exists() && mapFile.canRead();
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
            if (mapFilePath == null || mapFilePath.trim().isEmpty()) {
                return null;
            }
            
            File file = new File(mapFilePath);
            if (!file.exists() || !file.canRead()) {
                return null;
            }
            
            SongMap map = new SongMap(""); // Create with empty path, will be set later
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue; // Skip comments and empty lines
                    }
                    
                    try {
                        if (line.startsWith("bpm=")) {
                            String value = line.substring(4).trim();
                            if (!value.isEmpty()) {
                                map.bpm = Double.parseDouble(value);
                            }
                        } else if (line.startsWith("offset=")) {
                            String value = line.substring(7).trim();
                            if (!value.isEmpty()) {
                                map.offset = Double.parseDouble(value);
                            }
                        } else if (line.contains("|")) {
                            // Parse note data (format: time|lane)
                            String[] parts = line.split("\\|");
                            if (parts.length >= 2) {
                                try {
                                    double time = Double.parseDouble(parts[0].trim());
                                    int lane = Integer.parseInt(parts[1].trim());
                                    if (lane >= 0 && lane <= 3) {
                                        map.addNote(time, lane);
                                    }
                                } catch (NumberFormatException e) {
                                    // Skip invalid note data
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Skip malformed lines and continue
                        continue;
                    }
                }
                
                // If we successfully loaded some data, return the map
                if (map.notes.size() > 0) {
                    return map;
                }
                
            } catch (IOException e) {
                // File reading failed
                return null;
            }
            
            // No valid data found
            return null;
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
        songSelectionPanel = new SongSelectionPanel();
        gamePanel = new GamePanel();
        settingsPanel = new SettingsPanel();
        
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(songSelectionPanel, "SongSelection");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(settingsPanel, "Settings");
        
        add(mainPanel);
        
        setVisible(true);
    }
    
    public void showSettings() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> showSettings());
            return;
        }
        cardLayout.show(mainPanel, "Settings");
        // Request focus for keybind input
        if (settingsPanel != null) {
            settingsPanel.requestFocusInWindow();
        }
    }

    public void showSongSelection() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> showSongSelection());
            return;
        }
        cardLayout.show(mainPanel, "SongSelection");
    }

    public void showCreditsDialog() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> showCreditsDialog());
            return;
        }
        // Show credits in-game instead of popup
        if (gamePanel != null) {
            // Switch to game panel first, then show credits
            cardLayout.show(mainPanel, "Game");
            gamePanel.showCreditsInGame();
        }
    }

    public void backToMenu() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> backToMenu());
            return;
        }
        cardLayout.show(mainPanel, "Menu");
    }

    public void startGameWithSong(Song song) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> startGameWithSong(song));
            return;
        }
        
        try {
            gamePanel.startGameWithSong(song);
            cardLayout.show(mainPanel, "Game");
            if (gamePanel != null) {
                gamePanel.requestFocus();
            }
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
            backToMenu();
        }
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
        // Ensure the entire application runs on the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                new RhythmGame();
            } catch (Exception e) {
                System.err.println("Fatal error starting application: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
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

                    // Load and draw retro background image
                    BufferedImage bgImage = GameAssets.loadImage("main_menu_bg.png");
                    if (bgImage != null) {
                        g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                    } else {
                        // Fallback to retro gradient pattern if image not found
                        GameAssets.paintRetroGradient(g2d, getWidth(), getHeight());
                    }
                }
            };
            titlePanel.setOpaque(false);
            titlePanel.setPreferredSize(new Dimension(800, 200));

            JLabel titleLabel = new JLabel(GameAssets.GAME_TITLE, SwingConstants.CENTER);
            titleLabel.setFont(GameAssets.getCustomFontLarge().deriveFont(56f));
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

            // Create credits button with simple text dialog
            JButton creditsButton = createModernButton("Credits", GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            creditsButton.addActionListener(e -> showCreditsDialog());
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

            button.setFont(GameAssets.getCustomFontBold().deriveFont(28f));
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
                    
                    // Load and draw retro background image
                    BufferedImage bgImage = GameAssets.loadImage("song_selection_bg.png");
                    if (bgImage != null) {
                        g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                    } else {
                        // Fallback to retro gradient pattern if image not found
                        GameAssets.paintRetroGradient(g2d, getWidth(), getHeight());
                    }
                    
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
            JLabel titleLabel = new JLabel("Select Song", SwingConstants.CENTER);
            titleLabel.setFont(GameAssets.getCustomFontLarge().deriveFont(32f));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            // Add back button to title panel
            JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topButtonPanel.setBackground(new Color(0, 0, 0, 0));
            topButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            JButton backButton = createModernButton("Back to Main", GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
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
            
            playButton = createModernButton("Play Song", GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            playButton.setFont(GameAssets.getCustomFontBold().deriveFont(20f));
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
            
            // Create 12 retro-themed songs with varying difficulties
            Song[] defaultSongs = {
                new Song("Neon Rush", "Bangboo Galaxy", "neon_rush.mp3", 1),
                new Song("Pixel Fever", "Bangboo Galaxy", "pixel_fever.mp3", 2),
                new Song("Arcade Dream", "Bangboo Galaxy", "arcade_dream.mp3", 3),
                new Song("Orange Night", "Bangboo Galaxy", "orange_night.mp3", 4),
                new Song("Black Beats", "Bangboo Galaxy", "black_beats.mp3", 5),
                new Song("Retro Wave", "Bangboo Galaxy", "retro_wave.mp3", 6),
                new Song("Digital Fall", "Bangboo Galaxy", "digital_fall.mp3", 3),
                new Song("Pixel Storm", "Bangboo Galaxy", "pixel_storm.mp3", 4),
                new Song("Neon Highway", "Bangboo Galaxy", "neon_highway.mp3", 3),
                new Song("Orange Pulse", "Bangboo Galaxy", "orange_pulse.mp3", 5),
                new Song("Black Out", "Bangboo Galaxy", "black_out.mp3", 4),
                new Song("Pixel End", "Bangboo Galaxy", "pixel_end.mp3", 2)
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
        
        private boolean isDefaultSong(String fileName) {
            String[] defaultSongs = {"neon_rush.mp3", "pixel_fever.mp3", "arcade_dream.mp3", "orange_night.mp3", 
                                   "black_beats.mp3", "retro_wave.mp3", "digital_fall.mp3", "pixel_storm.mp3",
                                   "neon_highway.mp3", "orange_pulse.mp3", "black_out.mp3", "pixel_end.mp3"};
            
            for (String defaultSong : defaultSongs) {
                if (defaultSong.equalsIgnoreCase(fileName)) {
                    return true;
                }
            }
            return false;
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
        
        private JButton createNavigationButton(String text, int direction) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw background
                    if (getModel().isRollover()) {
                        g2d.setColor(GameAssets.ACCENT_COLOR);
                    } else if (getModel().isPressed()) {
                        g2d.setColor(GameAssets.SECONDARY_COLOR);
                    } else {
                        g2d.setColor(GameAssets.PRIMARY_COLOR);
                    }
                    
                    // Draw smaller circle button
                    int size = Math.min(getWidth(), getHeight());
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;
                    g2d.fillOval(x, y, size, size);
                    
                    // Draw orange border
                    g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x, y, size, size);
                    
                    // Draw arrow text
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(text)) / 2;
                    int textY = (getHeight() + fm.getAscent()) / 2;
                    g2d.drawString(text, textX, textY);
                }
                
                @Override
                public void setContentAreaFilled(boolean b) {}
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            button.setPreferredSize(new Dimension(40, 40));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setMargin(new Insets(0, 0, 0, 0));
            
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
    
// Make songs bigger to fit the screen
int imageSize = isCurrent ? 400 : 280;
int cardWidth = isCurrent ? 450 : 320;
int cardHeight = isCurrent ? 520 : 380;
    
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
JPanel imagePanel = new JPanel(new BorderLayout()) {
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2d = (Graphics2D) g;
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
// Draw shadow for floating effect
if (isCurrent) {
g2d.setColor(new Color(0, 0, 0, 100));
g2d.fillRoundRect(5, 5, getWidth(), getHeight(), 15, 15);
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
g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    
// Draw border with hover effect
Color borderColor = (isCurrent && isCurrentCardHovered) ? 
new Color(255, 220, 100) : // Brighter on hover
(isCurrent ? GameAssets.ACCENT_COLOR : new Color(100, 100, 120, 150));
g2d.setColor(borderColor);
g2d.setStroke(new BasicStroke(isCurrent ? 3 : 2));
g2d.drawRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    
// Draw song image or music note icon
BufferedImage songImage = (BufferedImage) getClientProperty("songImage");
if (songImage != null) {
// Fill the entire panel area with the image
g2d.drawImage(songImage, 0, 0, getWidth(), getHeight(), null);
} else {
// Draw music note icon as fallback
g2d.setColor(isCurrent ? Color.WHITE : new Color(200, 200, 200, 200));
g2d.setFont(GameAssets.getCustomFontBold().deriveFont(isCurrent ? 60f : 40f));
FontMetrics fm = g2d.getFontMetrics();
String note = "♪";
int x = (getWidth() - fm.stringWidth(note)) / 2;
int y = (getHeight() + fm.getAscent()) / 2;
g2d.drawString(note, x, y);
}
}
                
@Override
public boolean isOpaque() {
return false;
}
};
            
imagePanel.setPreferredSize(new Dimension(imageSize, imageSize));
            
// Add image panel to card
card.add(imagePanel, BorderLayout.CENTER);
            
// Create completely transparent overlay for title and difficulty
JPanel overlayPanel = new JPanel(new BorderLayout()) {
@Override
protected void paintComponent(Graphics g) {
// Don't paint any background - completely transparent
super.paintComponent(g);
}
                
@Override
public boolean isOpaque() {
return false;
}
};
overlayPanel.setOpaque(false);
overlayPanel.setBackground(new Color(0, 0, 0, 0));
            
// Create a semi-transparent background panel for text readability
JPanel textBgPanel = new JPanel(new BorderLayout()) {
@Override
protected void paintComponent(Graphics g) {
Graphics2D g2d = (Graphics2D) g;
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
// Draw semi-transparent background only for current song
if (isCurrent) {
g2d.setColor(new Color(0, 0, 0, 120));
g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
}
                }
                
                @Override
                public boolean isOpaque() {
                    return false;
                }
            };
            
            // Create text labels
            JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
            titleLabel.setFont(GameAssets.getCustomFontBold().deriveFont(isCurrent ? 22f : 16f));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setOpaque(false);
            
            JLabel difficultyLabel = new JLabel("", SwingConstants.CENTER);
            difficultyLabel.setFont(GameAssets.getCustomFont().deriveFont(isCurrent ? 16f : 12f));
            difficultyLabel.setForeground(new Color(255, 200, 100));
            difficultyLabel.setOpaque(false);
            
            textBgPanel.setOpaque(false);
            textBgPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            
            imagePanel.setPreferredSize(new Dimension(imageSize, imageSize));
            
            // Add image panel to card
            card.add(imagePanel, BorderLayout.CENTER);
            
            // Add text to existing overlay panel
            textBgPanel.add(titleLabel, BorderLayout.CENTER);
            textBgPanel.add(difficultyLabel, BorderLayout.SOUTH);
            
            overlayPanel.add(textBgPanel, BorderLayout.SOUTH);
            
            // Add overlay on top of image
            card.add(overlayPanel, BorderLayout.SOUTH);
            
            // Store labels and image panel in client properties
            card.putClientProperty("titleLabel", titleLabel);
            card.putClientProperty("difficultyLabel", difficultyLabel);
            card.putClientProperty("imagePanel", imagePanel);
            
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
                            updateCardImage(card, prevSong);
                        } else if ("current".equals(cardType) && currentSong != null) {
                            updateCardTitle(card, currentSong.title, true);
                            updateCardDifficulty(card, currentSong.difficulty, true);
                            updateCardImage(card, currentSong);
                        } else if ("next".equals(cardType) && nextSong != null) {
                            updateCardTitle(card, nextSong.title, false);
                            updateCardDifficulty(card, nextSong.difficulty, false);
                            updateCardImage(card, nextSong);
                        }
                    }
                }
            }
        }
        
        private void updateCardImage(JPanel card, Song song) {
            // Find the image panel in the card using client properties
            JPanel imagePanel = (JPanel) card.getClientProperty("imagePanel");
            
            if (imagePanel != null) {
                // Clear previous image completely
                imagePanel.putClientProperty("songImage", null);
                imagePanel.repaint();
                
                // Load and set new image
                BufferedImage songImage = song.loadImage();
                imagePanel.putClientProperty("songImage", songImage);
                imagePanel.repaint();
            }
        }
        
        private void updateCardTitle(JPanel card, String title, boolean isCurrent) {
            // Find the title label in the card using client properties
            JLabel titleLabel = (JLabel) card.getClientProperty("titleLabel");
            
            if (titleLabel != null) {
                // Complete reset of label
                titleLabel.setText("");
                titleLabel.setFont(GameAssets.getCustomFontBold().deriveFont(isCurrent ? 22f : 16f));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setOpaque(false);
                
                // Force complete repaint
                titleLabel.revalidate();
                titleLabel.repaint();
                
                // Set new title
                titleLabel.setText(title);
                titleLabel.revalidate();
                titleLabel.repaint();
                
                // Force parent panel repaint
                if (titleLabel.getParent() != null) {
                    titleLabel.getParent().repaint();
                }
            }
        }
        
        private void updateCardDifficulty(JPanel card, int difficulty, boolean isCurrent) {
            // Find the difficulty label in the card
            JLabel difficultyLabel = (JLabel) card.getClientProperty("difficultyLabel");
            
            if (difficultyLabel != null) {
                // Complete reset of label
                difficultyLabel.setText("");
                difficultyLabel.setFont(GameAssets.getCustomFont().deriveFont(isCurrent ? 16f : 12f));
                difficultyLabel.setForeground(new Color(255, 200, 100));
                difficultyLabel.setOpaque(false);
                
                // Force complete repaint
                difficultyLabel.revalidate();
                difficultyLabel.repaint();
                
                // Set new difficulty
                String difficultyText = getDifficultyStars(difficulty);
                difficultyLabel.setText(difficultyText);
                difficultyLabel.revalidate();
                difficultyLabel.repaint();
                
                // Force parent panel repaint
                if (difficultyLabel.getParent() != null) {
                    difficultyLabel.getParent().repaint();
                }
            }
        }
        
        private String getDifficultyStars(int difficulty) {
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                if (i < difficulty) {
                    stars.append("★");
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
            
            button.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
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
                    
                    // Load and draw retro background image
                    BufferedImage bgImage = GameAssets.loadImage("settings_bg.png");
                    if (bgImage != null) {
                        g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                    } else {
                        // Fallback to retro gradient pattern if image not found
                        GameAssets.paintRetroGradient(g2d, getWidth(), getHeight());
                    }
                }
            };
            titlePanel.setOpaque(false);
            titlePanel.setPreferredSize(new Dimension(800, 100));
            
            JLabel titleLabel = new JLabel(GameAssets.SETTINGS_TITLE, SwingConstants.CENTER);
            titleLabel.setFont(GameAssets.getCustomFontBold().deriveFont(48f));
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
            musicLabel.setFont(GameAssets.getCustomFontBold().deriveFont(24f));
            musicLabel.setForeground(GameAssets.TEXT_COLOR);
            musicPanel.add(musicLabel, BorderLayout.NORTH);
            
            JPanel musicSliderPanel = new JPanel(new BorderLayout());
            musicSliderPanel.setBackground(GameAssets.SURFACE_COLOR);
            
            musicSlider = new JSlider(0, 100, (int)(GameAssets.musicVolume * 100));
            musicSlider.setBackground(GameAssets.SURFACE_COLOR);
            musicSlider.setForeground(GameAssets.PRIMARY_COLOR);
            musicSlider.setFocusable(false);
            
            musicValueLabel = new JLabel((int)(GameAssets.musicVolume * 100) + "%");
            musicValueLabel.setFont(GameAssets.getCustomFontBold().deriveFont(20f));
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
            sfxLabel.setFont(GameAssets.getCustomFontBold().deriveFont(24f));
            sfxLabel.setForeground(GameAssets.TEXT_COLOR);
            sfxPanel.add(sfxLabel, BorderLayout.NORTH);
            
            JPanel sfxSliderPanel = new JPanel(new BorderLayout());
            sfxSliderPanel.setBackground(GameAssets.SURFACE_COLOR);
            
            sfxSlider = new JSlider(0, 100, (int)(GameAssets.sfxVolume * 100));
            sfxSlider.setBackground(GameAssets.SURFACE_COLOR);
            sfxSlider.setForeground(GameAssets.PRIMARY_COLOR);
            sfxSlider.setFocusable(false);
            
            sfxValueLabel = new JLabel((int)(GameAssets.sfxVolume * 100) + "%");
            sfxValueLabel.setFont(GameAssets.getCustomFontBold().deriveFont(20f));
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
            titleLabel.setFont(GameAssets.getCustomFontBold().deriveFont(28f));
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
                label.setFont(GameAssets.getCustomFontBold().deriveFont(20f));
                label.setForeground(GameAssets.TEXT_COLOR);
                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.WEST;
                section.add(label, gbc);
                
                // Key button with better visibility
                JButton button = createModernButton(KEY_NAMES[i], GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
                button.setPreferredSize(new Dimension(150, 60));
                button.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
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
        private static final int MAX_COMBO_FOR_FEVER = 15; // Lower threshold for easier activation
        private static final int FEVER_DURATION = 8000; // 8 seconds
        private static final int FEVER_SCORE_MULTIPLIER = 2; // Double score during fever
        private static final int FEVER_SPAWN_BONUS = 2; // Extra notes during fever
        
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
        private int maxCombo; // Track maximum combo achieved
        private List<TimingRating> timingRatings;
        private boolean[] laneGlowActive;
        private long[] laneGlowStartTime;
        private boolean isPaused = false;
        private boolean showResults = false; // New state for showing results in-game
        private boolean showCredits = false; // New state for showing credits in-game
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
            
            // Add mouse listener for results buttons
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (showResults) {
                        handleResultsClick(e.getX(), e.getY());
                    } else if (showCredits) {
                        handleCreditsClick(e.getX(), e.getY());
                    }
                }
            });
            
            // Add mouse motion listener for button hover effects
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (showResults || showCredits) {
                        repaint(); // Repaint to update hover effects
                    }
                }
            });
        }
        
        private void handleResultsClick(int x, int y) {
            // Calculate button positions for new results screen layout
            int panelWidth = 550;
            int panelHeight = 450;
            int panelX = (WIDTH - panelWidth) / 2;
            int panelY = (HEIGHT - panelHeight) / 2;
            
            int buttonY = panelY + panelHeight - 60;
            int buttonWidth = 140;
            int buttonHeight = 40;
            int buttonSpacing = 20;
            
            // Calculate total width and center buttons
            int totalButtonWidth = 3 * buttonWidth + 2 * buttonSpacing;
            int startX = panelX + (panelWidth - totalButtonWidth) / 2;
            
            // Check Retry button
            if (x >= startX && x <= startX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                showResults = false;
                startGameWithSong(currentSong);
                return;
            }
            
            // Check Song Selection button
            int songSelectionX = startX + buttonWidth + buttonSpacing;
            if (x >= songSelectionX && x <= songSelectionX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                showResults = false;
                ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSongSelection();
                return;
            }
            
            // Check Main Menu button
            int mainMenuX = startX + 2 * (buttonWidth + buttonSpacing);
            if (x >= mainMenuX && x <= mainMenuX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                showResults = false;
                ((RhythmGame) SwingUtilities.getWindowAncestor(this)).backToMenu();
                return;
            }
        }
        
        public void showCreditsInGame() {
            showCredits = true;
            // Don't pause timer if we're just showing credits from menu (not during gameplay)
            if (timer != null && timer.isRunning() && isPlayingWithMap && !songCompleted) {
                timer.stop(); // Only pause if we're actually playing
            }
        }
        
        private void handleCreditsClick(int x, int y) {
            // Calculate back button position for updated credits layout
            int panelWidth = 650;
            int panelHeight = 550;
            int panelX = (WIDTH - panelWidth) / 2;
            int panelY = (HEIGHT - panelHeight) / 2;
            
            int buttonWidth = 120;
            int buttonHeight = 40;
            int buttonX = panelX + (panelWidth - buttonWidth) / 2;
            int buttonY = panelY + panelHeight - 60;
            
            // Check if back button was clicked
            if (x >= buttonX && x <= buttonX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                showCredits = false;
                // Return to main menu since credits were accessed from menu
                ((RhythmGame) SwingUtilities.getWindowAncestor(this)).backToMenu();
            }
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
                    
                    g2d.setColor(GameAssets.PRIMARY_COLOR);
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
            resumeButton.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            resumeButton.setFocusPainted(false);
            resumeButton.addActionListener(e -> togglePause());

            songSelectionButton = new JButton("Song Selection") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(GameAssets.PRIMARY_COLOR);
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
            songSelectionButton.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            songSelectionButton.setFocusPainted(false);
            songSelectionButton.addActionListener(e -> backToSongSelection());

            mainMenuButton = new JButton("Main Menu") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(GameAssets.PRIMARY_COLOR);
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
            mainMenuButton.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            mainMenuButton.setFocusPainted(false);
            mainMenuButton.addActionListener(e -> backToMenu());

            quitButton = new JButton("Quit") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(GameAssets.PRIMARY_COLOR);
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
            quitButton.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
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
            
            // Set background to solid black
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Draw lanes with modern styling
            for (int i = 0; i < NUM_LANES; i++) {
                int x = LANE_POSITIONS[i];
                
                // Lane background with fever mode effects
                Color laneColor;
                if (feverActive) {
                    // Pulsing orange effect during fever
                    long pulse = (System.currentTimeMillis() / 200) % 10;
                    int alpha = 20 + ((int)pulse * 5);
                    laneColor = new Color(255, 150, 0, alpha);
                } else {
                    laneColor = new Color(255, 255, 255, 15);
                }
                g2d.setColor(laneColor);
                g2d.fillRoundRect(x, 0, LANE_WIDTH, HEIGHT, 10, 10);
                
                // Lane border with fever mode effects
                if (feverActive) {
                    g2d.setColor(GameAssets.ACCENT_COLOR);
                    g2d.setStroke(new BasicStroke(3));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 60));
                    g2d.setStroke(new BasicStroke(2));
                }
                g2d.drawRoundRect(x, 0, LANE_WIDTH, HEIGHT, 10, 10);
            }
            
            // Draw game elements (always visible, even when paused)
            // Draw score panel
            drawScorePanel(g2d);
            
            // Draw fever bar
            drawFeverBar(g2d);
            
            // Always draw these elements (but they won't update when paused)
            // Draw accuracy and timer in top right
            drawAccuracyAndTimer(g2d);
            
            // Draw hit zone with arrow shapes
            drawHitZone(g2d);
            
            // Draw timing ratings
            timingRatings.forEach(rating -> rating.draw(g2d));
            
            // Draw notes
            if (!showResults) {
                notes.forEach(note -> note.draw(g2d));
            }
            
            // Draw in-game results screen if active
            if (showResults) {
                drawInGameResults(g2d);
            }
            
            // Draw in-game credits screen if active
            if (showCredits) {
                drawInGameCredits(g2d);
            }
            
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
            g2d.setFont(GameAssets.getCustomFontBold().deriveFont(24f));
            g2d.drawString("Score: " + score, 20, 40);
            
            // Combo text with color based on combo level
            if (feverActive) {
                // Pulsing fever effect
                long pulse = (System.currentTimeMillis() / 100) % 10;
                if (pulse < 5) {
                    g2d.setColor(GameAssets.ACCENT_COLOR);
                } else {
                    g2d.setColor(GameAssets.WARNING_COLOR);
                }
                g2d.setFont(GameAssets.getCustomFontBold().deriveFont(28f));
                g2d.drawString("FEVER MODE! " + combo, 20, 70);
            } else if (combo >= MAX_COMBO_FOR_FEVER - 5) {
                g2d.setColor(GameAssets.ACCENT_COLOR);
                g2d.drawString("Combo: " + combo, 20, 70);
            } else if (combo >= MAX_COMBO_FOR_FEVER / 2) {
                g2d.setColor(GameAssets.WARNING_COLOR);
                g2d.drawString("Combo: " + combo, 20, 70);
            } else {
                g2d.setColor(GameAssets.SUCCESS_COLOR);
                g2d.drawString("Combo: " + combo, 20, 70);
            }
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
        
        private void drawInGameResults(Graphics2D g2d) {
            // Calculate rank
            String rank = calculateRank();
            
            // Draw semi-transparent overlay
            g2d.setColor(GameAssets.RESULTS_OVERLAY_COLOR);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Draw custom background image if enabled
            if (GameAssets.USE_RESULTS_BACKGROUND_IMAGE) {
                BufferedImage bgImage = GameAssets.loadImage(GameAssets.RESULTS_BACKGROUND_IMAGE);
                if (bgImage != null) {
                    g2d.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, null);
                }
            }
            
            // Draw results panel with customizable dimensions
            int panelWidth = GameAssets.RESULTS_PANEL_WIDTH;
            int panelHeight = GameAssets.RESULTS_PANEL_HEIGHT;
            int panelX = (WIDTH - panelWidth) / 2;
            int panelY = (HEIGHT - panelHeight) / 2;
            
            // Panel background with gradient effect or custom image
            if (GameAssets.USE_RESULTS_PANEL_IMAGE) {
                BufferedImage panelImage = GameAssets.loadImage(GameAssets.RESULTS_PANEL_IMAGE);
                if (panelImage != null) {
                    g2d.drawImage(panelImage, panelX, panelY, panelWidth, panelHeight, null);
                } else {
                    // Fallback to gradient
                    GradientPaint panelGradient = new GradientPaint(panelX, panelY, GameAssets.RESULTS_PANEL_COLOR, panelX, panelY + panelHeight, GameAssets.RESULTS_PANEL_GRADIENT_END);
                    g2d.setPaint(panelGradient);
                    g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.RESULTS_BORDER_RADIUS, GameAssets.RESULTS_BORDER_RADIUS);
                    g2d.setPaint(null);
                }
            } else {
                // Gradient background
                GradientPaint panelGradient = new GradientPaint(panelX, panelY, GameAssets.RESULTS_PANEL_COLOR, panelX, panelY + panelHeight, GameAssets.RESULTS_PANEL_GRADIENT_END);
                g2d.setPaint(panelGradient);
                g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.RESULTS_BORDER_RADIUS, GameAssets.RESULTS_BORDER_RADIUS);
                g2d.setPaint(null);
            }
            
            // Panel border with glow effect
            g2d.setColor(GameAssets.RESULTS_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(GameAssets.RESULTS_BORDER_WIDTH));
            g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.RESULTS_BORDER_RADIUS, GameAssets.RESULTS_BORDER_RADIUS);
            
            // Inner border for depth
            g2d.setStroke(new BasicStroke(GameAssets.RESULTS_INNER_BORDER_WIDTH));
            g2d.setColor(GameAssets.RESULTS_INNER_BORDER_COLOR);
            g2d.drawRoundRect(panelX + 5, panelY + 5, panelWidth - 10, panelHeight - 10, GameAssets.RESULTS_BORDER_RADIUS - 5, GameAssets.RESULTS_BORDER_RADIUS - 5);
            
            // Set up fonts with customization
            Font titleFont = new Font(GameAssets.RESULTS_TITLE_FONT, Font.BOLD, GameAssets.RESULTS_TITLE_SIZE);
            Font rankFont = new Font(GameAssets.RESULTS_RANK_FONT, Font.BOLD, GameAssets.RESULTS_RANK_SIZE);
            Font labelFont = new Font(GameAssets.RESULTS_LABEL_FONT, Font.BOLD, GameAssets.RESULTS_LABEL_SIZE);
            Font statFont = new Font(GameAssets.RESULTS_STATS_FONT, Font.BOLD, GameAssets.RESULTS_STATS_SIZE);
            
            // Song title at top
            g2d.setFont(titleFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            String titleText = currentSong.title;
            int titleWidth = titleMetrics.stringWidth(titleText);
            g2d.drawString(titleText, panelX + (panelWidth - titleWidth) / 2, panelY + GameAssets.RESULTS_TITLE_Y_OFFSET);
            
            // Decorative line under title
            g2d.setColor(GameAssets.RESULTS_BORDER_COLOR);
            g2d.drawLine(panelX + GameAssets.RESULTS_SEPARATOR_MARGIN, panelY + GameAssets.RESULTS_TITLE_LINE_Y, panelX + panelWidth - GameAssets.RESULTS_SEPARATOR_MARGIN, panelY + GameAssets.RESULTS_TITLE_LINE_Y);
            
            // Main content area - Rank and Score side by side
            int contentY = panelY + GameAssets.RESULTS_CONTENT_Y_OFFSET;
            
            // Rank on the left
            g2d.setFont(rankFont);
            g2d.setColor(getRankColor(rank));
            String rankText = rank;
            g2d.drawString(rankText, panelX + GameAssets.RESULTS_RANK_X_OFFSET, contentY + GameAssets.RESULTS_RANK_Y_OFFSET);
            
            // Score on the right
            g2d.setFont(labelFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            String scoreText = "Score: " + score;
            FontMetrics scoreMetrics = g2d.getFontMetrics();
            int scoreWidth = scoreMetrics.stringWidth(scoreText);
            g2d.drawString(scoreText, panelX + panelWidth - GameAssets.RESULTS_SCORE_RIGHT_MARGIN - scoreWidth, contentY + 30);
            
            // Max Combo below score
            if (GameAssets.SHOW_RESULTS_MAX_COMBO) {
                g2d.setFont(new Font(GameAssets.RESULTS_LABEL_FONT, Font.BOLD, 18));
                g2d.setColor(GameAssets.ACCENT_COLOR);
                String comboText = "Max Combo: " + maxCombo;
                FontMetrics comboMetrics = g2d.getFontMetrics();
                int comboWidth = comboMetrics.stringWidth(comboText);
                g2d.drawString(comboText, panelX + panelWidth - GameAssets.RESULTS_SCORE_RIGHT_MARGIN - comboWidth, contentY + 60);
            }
            
            // Statistics section at bottom
            if (GameAssets.SHOW_RESULTS_DETAILED_STATS) {
                int statsY = contentY + 120;
                
                // Stats background panel
                g2d.setColor(new Color(30, 30, 40, GameAssets.RESULTS_STATS_PANEL_ALPHA));
                g2d.fillRoundRect(panelX + 50, statsY - 25, panelWidth - 100, 80, GameAssets.RESULTS_STATS_PANEL_RADIUS, GameAssets.RESULTS_STATS_PANEL_RADIUS);
                
                // Perfect and Good on top row
                g2d.setFont(statFont);
                g2d.setColor(new Color(100, 255, 100));
                g2d.drawString("Perfect: " + perfectCount, panelX + 70, statsY);
                
                g2d.setColor(new Color(100, 200, 255));
                g2d.drawString("Good: " + goodCount, panelX + 280, statsY);
                
                // Okay and Miss on bottom row
                g2d.setColor(new Color(255, 255, 100));
                g2d.drawString("Okay: " + okayCount, panelX + 70, statsY + 35);
                
                g2d.setColor(new Color(255, 100, 100));
                g2d.drawString("Miss: " + missCount, panelX + 280, statsY + 35);
            }
            
            // Draw buttons
            drawResultsButtons(g2d, panelX, panelY, panelWidth, panelHeight);
        }
        
        private void drawResultsButtons(Graphics2D g2d, int panelX, int panelY, int panelWidth, int panelHeight) {
            Font buttonFont = GameAssets.getCustomFontBold().deriveFont(18f);
            g2d.setFont(buttonFont);
            
            int buttonY = panelY + panelHeight - GameAssets.RESULTS_BUTTON_BOTTOM_MARGIN;
            int buttonWidth = GameAssets.RESULTS_BUTTON_WIDTH;
            int buttonHeight = GameAssets.RESULTS_BUTTON_HEIGHT;
            int buttonSpacing = GameAssets.RESULTS_BUTTON_SPACING;
            
            // Calculate total width and center buttons
            int totalButtonWidth = 3 * buttonWidth + 2 * buttonSpacing;
            int startX = panelX + (panelWidth - totalButtonWidth) / 2;
            
            // Retry button
            drawButton(g2d, startX, buttonY, buttonWidth, buttonHeight, "Retry Song", GameAssets.SUCCESS_COLOR);
            
            // Song Selection button
            drawButton(g2d, startX + buttonWidth + buttonSpacing, buttonY, buttonWidth, buttonHeight, "Song Selection", GameAssets.PRIMARY_COLOR);
            
            // Main Menu button
            drawButton(g2d, startX + 2 * (buttonWidth + buttonSpacing), buttonY, buttonWidth, buttonHeight, "Main Menu", GameAssets.MAIN_BUTTON_COLOR);
        }
        
        private void drawButton(Graphics2D g2d, int x, int y, int width, int height, String text, Color bgColor) {
            // Check if mouse is hovering over button
            Point mousePos = getMousePosition();
            boolean isHovering = mousePos != null && 
                               mousePos.x >= x && mousePos.x <= x + width &&
                               mousePos.y >= y && mousePos.y <= y + height;
            
            // Draw button background
            if (isHovering) {
                g2d.setColor(bgColor.brighter());
            } else {
                g2d.setColor(bgColor);
            }
            g2d.fillRoundRect(x, y, width, height, 10, 10);
            
            // Draw button border
            g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, width, height, 10, 10);
            
            // Draw button text
            g2d.setColor(Color.WHITE);
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();
            g2d.drawString(text, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - 2);
        }
        
        private void drawInGameCredits(Graphics2D g2d) {
            // Draw semi-transparent overlay
            g2d.setColor(GameAssets.CREDITS_OVERLAY_COLOR);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Draw custom background image if enabled
            if (GameAssets.USE_CREDITS_BACKGROUND_IMAGE) {
                BufferedImage bgImage = GameAssets.loadImage(GameAssets.CREDITS_BACKGROUND_IMAGE);
                if (bgImage != null) {
                    g2d.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, null);
                }
            }
            
            // Draw credits panel with customizable dimensions
            int panelWidth = GameAssets.CREDITS_PANEL_WIDTH;
            int panelHeight = GameAssets.CREDITS_PANEL_HEIGHT;
            int panelX = (WIDTH - panelWidth) / 2;
            int panelY = (HEIGHT - panelHeight) / 2;
            
            // Panel background or custom image
            if (GameAssets.USE_CREDITS_PANEL_IMAGE) {
                BufferedImage panelImage = GameAssets.loadImage(GameAssets.CREDITS_PANEL_IMAGE);
                if (panelImage != null) {
                    g2d.drawImage(panelImage, panelX, panelY, panelWidth, panelHeight, null);
                } else {
                    // Fallback to solid color
                    g2d.setColor(GameAssets.CREDITS_PANEL_COLOR);
                    g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.CREDITS_BORDER_RADIUS, GameAssets.CREDITS_BORDER_RADIUS);
                }
            } else {
                // Solid background
                g2d.setColor(GameAssets.CREDITS_PANEL_COLOR);
                g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.CREDITS_BORDER_RADIUS, GameAssets.CREDITS_BORDER_RADIUS);
            }
            
            // Panel border
            g2d.setColor(GameAssets.CREDITS_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(GameAssets.CREDITS_BORDER_WIDTH));
            g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, GameAssets.CREDITS_BORDER_RADIUS, GameAssets.CREDITS_BORDER_RADIUS);
            
            // Set up fonts with customization
            Font titleFont = new Font(GameAssets.CREDITS_TITLE_FONT, Font.BOLD, GameAssets.CREDITS_TITLE_SIZE);
            Font sectionFont = new Font(GameAssets.CREDITS_SECTION_FONT, Font.BOLD, GameAssets.CREDITS_SECTION_SIZE);
            Font textFont = new Font(GameAssets.CREDITS_TEXT_FONT, Font.PLAIN, GameAssets.CREDITS_TEXT_SIZE);
            
            // Title
            g2d.setFont(titleFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            String titleText = GameAssets.CREDITS_GAME_TITLE;
            int titleWidth = titleMetrics.stringWidth(titleText);
            g2d.drawString(titleText, panelX + (panelWidth - titleWidth) / 2, panelY + GameAssets.CREDITS_TITLE_Y_OFFSET);
            
            // Separator
            g2d.setColor(GameAssets.CREDITS_BORDER_COLOR);
            g2d.drawLine(panelX + GameAssets.CREDITS_SEPARATOR_MARGIN, panelY + GameAssets.CREDITS_SEPARATOR_Y, panelX + panelWidth - GameAssets.CREDITS_SEPARATOR_MARGIN, panelY + GameAssets.CREDITS_SEPARATOR_Y);
            
            // Credits content with customizable spacing
            int currentY = panelY + GameAssets.CREDITS_CONTENT_Y_OFFSET;
            int lineHeight = GameAssets.CREDITS_LINE_HEIGHT;
            int leftMargin = GameAssets.CREDITS_LEFT_MARGIN;
            
            // Game Development section
            g2d.setFont(sectionFont);
            g2d.setColor(GameAssets.PRIMARY_COLOR);
            g2d.drawString(GameAssets.CREDITS_GAME_DEV_SECTION[0], panelX + leftMargin, currentY);
            currentY += lineHeight;
            
            g2d.setFont(textFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            for (int i = 1; i < GameAssets.CREDITS_GAME_DEV_SECTION.length; i++) {
                g2d.drawString(GameAssets.CREDITS_GAME_DEV_SECTION[i], panelX + leftMargin, currentY);
                currentY += lineHeight;
            }
            currentY += GameAssets.CREDITS_SECTION_SPACING;
            
            // Music & Sounds section
            g2d.setFont(sectionFont);
            g2d.setColor(GameAssets.PRIMARY_COLOR);
            g2d.drawString(GameAssets.CREDITS_MUSIC_SECTION[0], panelX + leftMargin, currentY);
            currentY += lineHeight;
            
            g2d.setFont(textFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            for (int i = 1; i < GameAssets.CREDITS_MUSIC_SECTION.length; i++) {
                g2d.drawString(GameAssets.CREDITS_MUSIC_SECTION[i], panelX + leftMargin, currentY);
                currentY += lineHeight;
            }
            currentY += GameAssets.CREDITS_SECTION_SPACING;
            
            // Special Thanks section
            g2d.setFont(sectionFont);
            g2d.setColor(GameAssets.PRIMARY_COLOR);
            g2d.drawString(GameAssets.CREDITS_THANKS_SECTION[0], panelX + leftMargin, currentY);
            currentY += lineHeight;
            
            g2d.setFont(textFont);
            g2d.setColor(GameAssets.TEXT_COLOR);
            for (int i = 1; i < GameAssets.CREDITS_THANKS_SECTION.length; i++) {
                g2d.drawString(GameAssets.CREDITS_THANKS_SECTION[i], panelX + leftMargin, currentY);
                currentY += lineHeight;
            }
            currentY += GameAssets.CREDITS_SECTION_SPACING;
            
            // Version info
            g2d.setFont(textFont);
            g2d.setColor(GameAssets.ACCENT_COLOR);
            g2d.drawString(GameAssets.CREDITS_VERSION, panelX + leftMargin, currentY);
            currentY += lineHeight;
            g2d.drawString(GameAssets.CREDITS_COPYRIGHT, panelX + leftMargin, currentY);
            
            // Back button
            drawCreditsBackButton(g2d, panelX, panelY, panelWidth, panelHeight);
        }
        
        private void drawCreditsBackButton(Graphics2D g2d, int panelX, int panelY, int panelWidth, int panelHeight) {
            // Button properties with customization
            int buttonWidth = GameAssets.CREDITS_BUTTON_WIDTH;
            int buttonHeight = GameAssets.CREDITS_BUTTON_HEIGHT;
            int buttonX = panelX + (panelWidth - buttonWidth) / 2;
            int buttonY = panelY + panelHeight - GameAssets.CREDITS_BUTTON_BOTTOM_MARGIN;
            
            // Check if mouse is hovering over button
            Point mousePos = getMousePosition();
            boolean isHovering = mousePos != null && 
                               mousePos.x >= buttonX && mousePos.x <= buttonX + buttonWidth &&
                               mousePos.y >= buttonY && mousePos.y <= buttonY + buttonHeight;
            
            // Draw button background
            if (isHovering) {
                g2d.setColor(GameAssets.MAIN_BUTTON_HOVER_COLOR);
            } else {
                g2d.setColor(GameAssets.MAIN_BUTTON_COLOR);
            }
            g2d.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 10, 10);
            
            // Draw button border
            g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 10, 10);
            
            // Draw button text with customization
            g2d.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            g2d.setColor(Color.WHITE);
            FontMetrics metrics = g2d.getFontMetrics();
            String buttonText = GameAssets.CREDITS_BACK_BUTTON_TEXT;
            int textWidth = metrics.stringWidth(buttonText);
            int textHeight = metrics.getHeight();
            g2d.drawString(buttonText, buttonX + (buttonWidth - textWidth) / 2, buttonY + (buttonHeight + textHeight) / 2 - 2);
        }

        private void drawAccuracyAndTimer(Graphics2D g2d) {
            // Calculate remaining time and accuracy
            double remainingTime = 0.0;
            double accuracy = 0.0;
            
            if (isPlayingWithMap && songStartTime > 0 && !songCompleted) {
                double elapsedTime = (System.currentTimeMillis() - songStartTime) / 1000.0;
                remainingTime = Math.max(0.0, songDuration - elapsedTime);
                
                int totalHits = perfectCount + goodCount + okayCount + missCount;
                if (totalHits > 0) {
                    accuracy = (double) (perfectCount * 100 + goodCount * 75 + okayCount * 50) / (totalHits * 100) * 100;
                }
            }
            
            // Set up font and colors
            g2d.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Position for top right
            int x = WIDTH - 200;
            int y = 30;
            
            // Calculate panel height based on what's shown
            int panelHeight = 70;
            if (!GameAssets.SHOW_COUNTDOWN_TIMER) panelHeight -= 25;
            if (!GameAssets.SHOW_ACCURACY) panelHeight -= 25;
            
            if (panelHeight > 0) {
                // Draw semi-transparent background
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRoundRect(x - 10, y - 5, 190, panelHeight, 10, 10);
                
                // Draw border
                g2d.setColor(GameAssets.BUTTON_BORDER_COLOR);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x - 10, y - 5, 190, panelHeight, 10, 10);
                
                int currentY = y + 20;
                
                // Draw countdown timer if enabled
                if (GameAssets.SHOW_COUNTDOWN_TIMER) {
                    g2d.setColor(GameAssets.PRIMARY_COLOR);
                    g2d.drawString(String.format(GameAssets.TIMER_FORMAT, remainingTime), x, currentY);
                    currentY += 25;
                }
                
                // Draw accuracy if enabled
                if (GameAssets.SHOW_ACCURACY) {
                    Color accuracyColor = getAccuracyColor(accuracy);
                    g2d.setColor(accuracyColor);
                    g2d.drawString("Accuracy: " + String.format("%.1f%%", accuracy), x, currentY);
                }
            }
        }
        
        private Color getAccuracyColor(double accuracy) {
            if (accuracy >= 95) return new Color(255, 215, 0); // Gold
            if (accuracy >= 85) return new Color(192, 192, 192); // Silver
            if (accuracy >= 70) return new Color(205, 127, 50); // Bronze
            if (accuracy >= 50) return new Color(100, 150, 255); // Blue
            return new Color(255, 100, 100); // Red
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
                drawArrow(g2d, x, HIT_ZONE_Y, GameAssets.ARROW_SIZE, i, true, false);
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
            maxCombo = 0; // Initialize max combo
            timingRatings = new ArrayList<>();
            laneGlowActive = new boolean[NUM_LANES];
            laneGlowStartTime = new long[NUM_LANES];
            isPaused = false;
            showResults = false; // Reset results state
            showCredits = false; // Reset credits state
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
            // Ensure this runs on the EDT
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> startGameWithSong(song));
                return;
            }
            
            try {
                // Validate song
                if (song == null) {
                    System.err.println("Error: Song is null");
                    return;
                }
                
                // Set current song
                currentSong = song;
                
                // Try to load existing map
                currentMap = null;
                if (song.hasMapFile()) {
                    currentMap = SongMap.loadFromFile(song.getMapFilePath());
                    if (currentMap != null) {
                        System.out.println("Loaded custom map for: " + song.title);
                    }
                }
                
                // Generate auto-map if no valid map exists
                if (currentMap == null) {
                    currentMap = SongMap.generateAutoMap(song.filePath);
                    try {
                        currentMap.saveToFile(); // Save for future use
                        System.out.println("Generated and saved auto-map for: " + song.title);
                    } catch (Exception e) {
                        System.err.println("Warning: Could not save auto-map: " + e.getMessage());
                    }
                }
                
                // Validate map
                if (currentMap == null || currentMap.notes.isEmpty()) {
                    System.err.println("Error: No valid map available for song: " + song.title);
                    // Create a minimal fallback map
                    currentMap = new SongMap(song.filePath);
                    // Add a few default notes
                    currentMap.addNote(2.0, 0);
                    currentMap.addNote(3.0, 1);
                    currentMap.addNote(4.0, 2);
                    currentMap.addNote(5.0, 3);
                }
                
                // Calculate song duration based on map
                if (currentMap.notes.size() > 0) {
                    NoteData lastNote = currentMap.notes.get(currentMap.notes.size() - 1);
                    songDuration = Math.max(lastNote.time + GameAssets.MAP_END_BUFFER_SECONDS, GameAssets.MINIMUM_MAP_DURATION);
                } else {
                    songDuration = GameAssets.DEFAULT_MAP_DURATION;
                }
                
                // Initialize mapping system
                currentNoteIndex = 0;
                songStartTime = System.currentTimeMillis();
                isPlayingWithMap = true;
                
                // Reset game state safely on EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        resetGame();
                        System.out.println("Successfully started game with song: " + song.title + 
                                         " (" + currentMap.notes.size() + " notes)");
                    } catch (Exception e) {
                        System.err.println("Error in resetGame: " + e.getMessage());
                        backToMenu();
                    }
                });
                
            } catch (Exception e) {
                System.err.println("Error starting game with song: " + e.getMessage());
                e.printStackTrace();
                // Try to recover by going back to menu on EDT
                SwingUtilities.invokeLater(() -> backToMenu());
            }
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
            
            // Update timing ratings
            timingRatings.forEach(TimingRating::update);
            timingRatings.removeIf(TimingRating::isDead);
            
            notes.forEach(Note::update);
            checkHits();
            
            // Update fever system
            if (feverActive) {
                long elapsed = System.currentTimeMillis() - feverStartTime;
                if (elapsed >= FEVER_DURATION) {
                    feverActive = false;
                    feverStartTime = 0;
                }
            }
            
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
            if (GameAssets.SHOW_IN_GAME_RESULTS) {
                showResults = true; // Show results in-game instead of popup
                timer.stop(); // Stop the game timer
            } else {
                // Could add popup results here if needed in future
                timer.stop();
            }
        }
        
        private void showResults() {
            // Calculate final time
            double finalTime = (System.currentTimeMillis() - songStartTime) / 1000.0;
            
            // Calculate rank
            String rank = calculateRank();
            
            // Create results dialog
            JDialog resultsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Song Complete!", true);
            resultsDialog.setSize(500, 600);
            resultsDialog.setLocationRelativeTo(this);
            resultsDialog.setLayout(new BorderLayout());
            
            // Results panel
            JPanel resultsPanel = new JPanel();
            resultsPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Title
            JLabel titleLabel = new JLabel(currentSong.title);
            titleLabel.setFont(GameAssets.getCustomFontBold().deriveFont(28f));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Time completed
            JLabel timeLabel = new JLabel(String.format("Time: %.1f seconds", finalTime));
            timeLabel.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
            timeLabel.setForeground(GameAssets.PRIMARY_COLOR);
            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Rank
            JLabel rankLabel = new JLabel("Rank: " + rank);
            rankLabel.setFont(GameAssets.getCustomFontBold().deriveFont(48f));
            rankLabel.setForeground(getRankColor(rank));
            rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Score
            JLabel scoreLabel = new JLabel("Score: " + score);
            scoreLabel.setFont(GameAssets.getCustomFontBold().deriveFont(24f));
            scoreLabel.setForeground(GameAssets.TEXT_COLOR);
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Max Combo
            JLabel comboLabel = new JLabel("Max Combo: " + maxCombo);
            comboLabel.setFont(GameAssets.getCustomFontBold().deriveFont(20f));
            comboLabel.setForeground(GameAssets.ACCENT_COLOR);
            comboLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Stats panel
            JPanel statsPanel = new JPanel();
            statsPanel.setBackground(GameAssets.SURFACE_COLOR);
            statsPanel.setLayout(new GridLayout(2, 2, 10, 10));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel perfectLabel = new JLabel("Perfect: " + perfectCount);
            perfectLabel.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
            perfectLabel.setForeground(new Color(100, 255, 100));
            perfectLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel goodLabel = new JLabel("Good: " + goodCount);
            goodLabel.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
            goodLabel.setForeground(new Color(100, 200, 255));
            goodLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel okayLabel = new JLabel("Okay: " + okayCount);
            okayLabel.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
            okayLabel.setForeground(new Color(255, 255, 100));
            okayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel missLabel = new JLabel("Miss: " + missCount);
            missLabel.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
            missLabel.setForeground(new Color(255, 100, 100));
            missLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            statsPanel.add(perfectLabel);
            statsPanel.add(goodLabel);
            statsPanel.add(okayLabel);
            statsPanel.add(missLabel);
            
            // Add components to panel
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(titleLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(timeLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            resultsPanel.add(rankLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            resultsPanel.add(scoreLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            resultsPanel.add(comboLabel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            resultsPanel.add(statsPanel);
            
            // Button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            
            JButton retryButton = createResultsButton("Retry Song", GameAssets.MAIN_BUTTON_COLOR);
            JButton songSelectionButton = createResultsButton("Song Selection", GameAssets.MAIN_BUTTON_COLOR);
            JButton mainMenuButton = createResultsButton("Main Menu", GameAssets.MAIN_BUTTON_COLOR);
            
            mainMenuButton.addActionListener(e -> {
                resultsDialog.dispose();
                ((RhythmGame) SwingUtilities.getWindowAncestor(this)).backToMenu();
            });
            
            buttonPanel.add(retryButton);
            buttonPanel.add(songSelectionButton);
            buttonPanel.add(mainMenuButton);
            
            resultsDialog.add(resultsPanel, BorderLayout.CENTER);
            resultsDialog.add(buttonPanel, BorderLayout.SOUTH);
            resultsDialog.setVisible(true);
        }
        
        private JButton createResultsButton(String text, Color bgColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isRollover()) {
                        g2d.setColor(bgColor.brighter());
                    } else {
                        g2d.setColor(bgColor);
                    }
                    
                    RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10);
                    g2d.fill(roundedRect);
                    
                    // Draw border
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
            
            button.setFont(GameAssets.getCustomFontBold().deriveFont(16f));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(140, 40));
            
            return button;
        }
        
        private String calculateRank() {
            int totalNotes = perfectCount + goodCount + okayCount + missCount;
            if (totalNotes == 0) return "F";
            
            double perfectRatio = (double) perfectCount / totalNotes;
            
            if (perfectRatio >= GameAssets.S_RANK_THRESHOLD) return "S";
            if (perfectRatio >= GameAssets.A_RANK_THRESHOLD) return "A";
            if (perfectRatio >= GameAssets.B_RANK_THRESHOLD) return "B";
            if (perfectRatio >= GameAssets.C_RANK_THRESHOLD) return "C";
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

        private void checkHits() {
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            
            // Reset all notes glowing state
            // notes.forEach(note -> note.glowing = false); // Removed this line
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (!note.hit && note.isInHitZone() && keysPressed[keyMap[note.direction]]) {
                    // Note hit - make it glow briefly
                    note.hit = true;
                    note.glowing = true; // Set glowing when hit
                    
                    // Calculate timing accuracy
                    int distanceFromHitZone = Math.abs(note.y + GameAssets.NOTE_SIZE / 2 - HIT_ZONE_Y);
                    String rating;
                    int points;
                    
                    // Apply fever mode benefits
                    int currentPerfectWindow = feverActive ? PERFECT_WINDOW + 10 : PERFECT_WINDOW;
                    int currentGoodWindow = feverActive ? GOOD_WINDOW + 10 : GOOD_WINDOW;
                    int currentOkayWindow = feverActive ? OKAY_WINDOW + 10 : OKAY_WINDOW;
                    int scoreMultiplier = feverActive ? 2 : 1;
                    
                    if (distanceFromHitZone <= currentPerfectWindow) {
                        rating = "PERFECT";
                        points = PERFECT_SCORE * scoreMultiplier;
                        perfectCount++;
                    } else if (distanceFromHitZone <= currentGoodWindow) {
                        rating = "GOOD";
                        points = GOOD_SCORE * scoreMultiplier;
                        goodCount++;
                    } else if (distanceFromHitZone <= currentOkayWindow) {
                        rating = "OKAY";
                        points = OKAY_SCORE * scoreMultiplier;
                        okayCount++;
                    } else {
                        rating = "MISS";
                        points = MISS_SCORE;
                        missCount++;
                        combo = 0;
                        // Fever ends on miss
                        if (feverActive) {
                            feverActive = false;
                            feverStartTime = 0;
                        }
                    }
                    
                    // Add rating display
                    timingRatings.add(new TimingRating(rating, note.lane));
                    
                    // Update score and combo
                    score += points;
                    if (!rating.equals("MISS")) {
                        combo++;
                        // Update max combo if current combo is higher
                        if (combo > maxCombo) {
                            maxCombo = combo;
                        }
                        if (combo >= MAX_COMBO_FOR_FEVER && !feverActive) {
                            feverActive = true;
                            feverStartTime = System.currentTimeMillis();
                        }
                    }
                    
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
        private void drawArrow(Graphics2D g2d, int x, int y, int size, int direction, boolean isHitZone, boolean shouldGlow) {
            // Try to load custom arrow image first
            BufferedImage arrowImage = loadArrowImage(direction);
            
            // Check if this lane should glow (key is pressed) for hit zone OR note should glow
            boolean hitZoneShouldGlow = isHitZone && GameAssets.ENABLE_HIT_ZONE_GLOW && direction < laneGlowActive.length && laneGlowActive[direction];
            boolean finalShouldGlow = shouldGlow || hitZoneShouldGlow;
            
            if (finalShouldGlow) {
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
            // Disable anti-aliasing for pixelated look
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            
            // Pixel size for blocky appearance
            int pixelSize = Math.max(2, size / 16);
            int halfSize = size / 2;
            
            // Set color
            if (isHitZone) {
                g2d.setColor(new Color(255, 255, 255, 100));
            } else {
                g2d.setColor(GameAssets.NOTE_COLORS[direction]);
            }
            
            // Calculate arrow bounds
            int startX = x - halfSize;
            int startY = y - halfSize;
            int endX = x + halfSize;
            int endY = y + halfSize;
            
            // Draw pixelated arrow based on direction
            switch (direction) {
                case 0: // Left arrow
                    // Arrow pointing left (◀)
                    for (int py = startY; py <= endY; py += pixelSize) {
                        for (int px = startX; px <= endX; px += pixelSize) {
                            // Arrow shaft
                            if (py >= y - pixelSize && py <= y + pixelSize && px >= x - pixelSize && px <= x + halfSize - pixelSize) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                            // Arrow head
                            if (px >= startX && px <= startX + halfSize && 
                                ((py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize && 
                                  px >= startX && px <= startX + (py - startY) / 2) ||
                                 (py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize && 
                                  px >= startX && px <= startX + (endY - py) / 2))) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                        }
                    }
                    break;
                    
                case 1: // Up arrow
                    // Arrow pointing up (▲)
                    for (int py = startY; py <= endY; py += pixelSize) {
                        for (int px = startX; px <= endX; px += pixelSize) {
                            // Arrow shaft
                            if (px >= x - pixelSize && px <= x + pixelSize && py >= y - halfSize + pixelSize && py <= y + pixelSize) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                            // Arrow head
                            if (py >= startY && py <= startY + halfSize && 
                                ((px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize && 
                                  py >= startY && py <= startY + (px - startX) / 2) ||
                                 (px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize && 
                                  py >= startY && py <= startY + (endX - px) / 2))) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                        }
                    }
                    break;
                    
                case 2: // Down arrow
                    // Arrow pointing down (▼)
                    for (int py = startY; py <= endY; py += pixelSize) {
                        for (int px = startX; px <= endX; px += pixelSize) {
                            // Arrow shaft
                            if (px >= x - pixelSize && px <= x + pixelSize && py >= y - pixelSize && py <= y + halfSize - pixelSize) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                            // Arrow head
                            if (py >= startY + halfSize && py <= endY && 
                                ((px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize && 
                                  py >= endY - (px - startX) / 2 && py <= endY) ||
                                 (px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize && 
                                  py >= endY - (endX - px) / 2 && py <= endY))) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                        }
                    }
                    break;
                    
                case 3: // Right arrow
                    // Arrow pointing right (▶)
                    for (int py = startY; py <= endY; py += pixelSize) {
                        for (int px = startX; px <= endX; px += pixelSize) {
                            // Arrow shaft
                            if (py >= y - pixelSize && py <= y + pixelSize && px >= x - halfSize + pixelSize && px <= x + pixelSize) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                            // Arrow head
                            if (px >= startX + halfSize && px <= endX && 
                                ((py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize && 
                                  px >= endX - (py - startY) / 2 && px <= endX) ||
                                 (py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize && 
                                  px >= endX - (endY - py) / 2 && px <= endX))) {
                                g2d.fillRect(px, py, pixelSize, pixelSize);
                            }
                        }
                    }
                    break;
            }
            
            // Add pixelated border if not hit zone
            if (!isHitZone) {
                g2d.setColor(GameAssets.TEXT_COLOR);
                // Draw pixelated border
                for (int py = startY; py <= endY; py += pixelSize) {
                    for (int px = startX; px <= endX; px += pixelSize) {
                        // Check if this pixel is on the edge of the arrow
                        boolean isEdge = false;
                        switch (direction) {
                            case 0: // Left
                                if ((py == startY || py == endY || px == startX) && 
                                    ((px >= startX && px <= startX + halfSize && py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize) ||
                                     (py >= y - pixelSize && py <= y + pixelSize && px >= x - pixelSize && px <= x + halfSize - pixelSize))) {
                                    isEdge = true;
                                }
                                break;
                            case 1: // Up
                                if ((px == startX || px == endX || py == startY) && 
                                    ((py >= startY && py <= startY + halfSize && px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize) ||
                                     (px >= x - pixelSize && px <= x + pixelSize && py >= y - halfSize + pixelSize && py <= y + pixelSize))) {
                                    isEdge = true;
                                }
                                break;
                            case 2: // Down
                                if ((px == startX || px == endX || py == endY) && 
                                    ((py >= startY + halfSize && py <= endY && px >= x - halfSize + pixelSize && px <= x + halfSize - pixelSize) ||
                                     (px >= x - pixelSize && px <= x + pixelSize && py >= y - pixelSize && py <= y + halfSize - pixelSize))) {
                                    isEdge = true;
                                }
                                break;
                            case 3: // Right
                                if ((py == startY || py == endY || px == endX) && 
                                    ((px >= startX + halfSize && px <= endX && py >= y - halfSize + pixelSize && py <= y + halfSize - pixelSize) ||
                                     (py >= y - pixelSize && py <= y + pixelSize && px >= x - halfSize + pixelSize && px <= x + pixelSize))) {
                                    isEdge = true;
                                }
                                break;
                        }
                        if (isEdge) {
                            g2d.fillRect(px, py, pixelSize, pixelSize);
                        }
                    }
                }
            }
        }

        class Note {
            int lane;
            int direction; // 0: left, 1: up, 2: down, 3: right
            int y = 0;
            int x;
            boolean hit = false;
            boolean glowing = false; // Track if note should glow when hit

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
                // Only draw glow if note is glowing (when being hit)
                boolean shouldGlow = glowing && GameAssets.ENABLE_NOTE_GLOW;
                
                // Draw arrow centered in the lane
                drawArrow(g2d, x, y + GameAssets.NOTE_SIZE / 2, GameAssets.ARROW_SIZE, direction, false, shouldGlow);
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
                g2d.setFont(GameAssets.getCustomFontBold().deriveFont(18f));
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
