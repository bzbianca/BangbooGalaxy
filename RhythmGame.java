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
        public static final int NOTE_SIZE = 35;
        public static final int ARROW_SIZE = 25;
        
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
        
        // === ASSET FILES ===
        // Arrow images
        public static final String[] ARROW_FILES = {
            "arrow_left.png",
            "arrow_up.png", 
            "arrow_down.png",
            "arrow_right.png"
        };
        
        // Background images
        public static final String[] BACKGROUND_FILES = {
            "main_menu_bg.png",
            "game_bg.png",
            "settings_bg.png",
            "song_selection_bg.png"
        };
        
        // Sound effects
        public static final String[] SFX_FILES = {
            "hit.wav",
            "miss.wav",
            "fever_start.wav",
            "menu_click.wav"
        };
        
        // === LOADED ASSETS ===
        // Cached loaded assets
        private static BufferedImage[] arrowImages = new BufferedImage[4];
        private static BufferedImage[] backgroundImages = new BufferedImage[4];
        private static boolean assetsLoaded = false;
        
        // === ASSET LOADING METHODS ===
        public static void loadAllAssets() {
            if (assetsLoaded) return;
            
            // Create folders if they don't exist
            createAssetFolders();
            
            // Load arrow images
            loadArrowImages();
            
            // Load background images
            loadBackgroundImages();
            
            // Load configuration
            loadConfiguration();
            
            assetsLoaded = true;
        }
        
        private static void createAssetFolders() {
            String[] folders = {SONGS_FOLDER, ARROWS_FOLDER, BACKGROUNDS_FOLDER, 
                               FONTS_FOLDER, SOUNDS_FOLDER, CONFIG_FOLDER};
            
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
        
        public static BufferedImage getBackgroundImage(int screen) {
            if (!assetsLoaded) loadAllAssets();
            return backgroundImages[screen];
        }
        
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
        
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(songSelectionPanel, "SongSelection");
        
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

    public void backToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    public void startGameWithSong(Song song) {
        gamePanel.resetGame();
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

            // Create quit button with customizable color
            JButton quitButton = createModernButton(GameAssets.QUIT_BUTTON_TEXT, GameAssets.MAIN_BUTTON_COLOR, GameAssets.MAIN_BUTTON_HOVER_COLOR);
            quitButton.addActionListener(e -> System.exit(0));
            quitButton.setPreferredSize(GameAssets.MAIN_BUTTON_SIZE);
            gbc.gridy = 2;
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
        private JPanel songsGridPanel;
        private JButton playButton;
        private Song selectedSong;

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
                    
                    GradientPaint gradient = new GradientPaint(0, 0, GameAssets.PRIMARY_COLOR, getWidth(), getHeight(), GameAssets.SECONDARY_COLOR);
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            titlePanel.setOpaque(false);
            titlePanel.setPreferredSize(new Dimension(800, 100));
            
            JLabel titleLabel = new JLabel("Select Song", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            titleLabel.setForeground(GameAssets.TEXT_COLOR);
            titlePanel.add(titleLabel);
            
            // Create top panel with title and back button
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            topPanel.add(titlePanel, BorderLayout.CENTER);
            
            // Create back button panel at top
            JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topButtonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            topButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            
            JButton backButton = createModernButton("🔙 Back to Main", new Color(30, 30, 30), new Color(60, 60, 60));
            backButton.addActionListener(e -> backToMenu());
            topButtonPanel.add(backButton);
            topPanel.add(topButtonPanel, BorderLayout.NORTH);
            
            add(topPanel, BorderLayout.NORTH);

            // Create scrollable grid panel for songs
            songsGridPanel = new JPanel();
            songsGridPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            songsGridPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            
            loadSongs();
            
            JScrollPane scrollPane = new JScrollPane(songsGridPanel);
            scrollPane.setBackground(GameAssets.BACKGROUND_COLOR);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            add(scrollPane, BorderLayout.CENTER);

            // Create button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(GameAssets.BACKGROUND_COLOR);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            
            playButton = createModernButton("Play", GameAssets.SUCCESS_COLOR, new Color(100, 250, 150));
            playButton.addActionListener(e -> {
                if (selectedSong != null) {
                    startGameWithSong(selectedSong);
                }
            });
            playButton.setEnabled(false);
            
            buttonPanel.add(playButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadSongs() {
            // Create default songs if folder doesn't exist
            Song[] defaultSongs = {
                new Song("Tutorial Song", "Bangboo Galaxy", "tutorial.mp3", 1),
                new Song("Easy Beat", "Bangboo Galaxy", "easy.mp3", 2),
                new Song("Medium Rhythm", "Bangboo Galaxy", "medium.mp3", 3),
                new Song("Hard Challenge", "Bangboo Galaxy", "hard.mp3", 4)
            };
            
            for (Song song : defaultSongs) {
                addSongToGrid(song);
            }
        }
        
        private void addSongToGrid(Song song) {
            JPanel songPanel = new JPanel(new BorderLayout());
            songPanel.setBackground(GameAssets.SURFACE_COLOR);
            songPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            songPanel.setPreferredSize(new Dimension(150, 200));
            
            // Load song image or create default
            BufferedImage songImage = song.loadImage();
            JLabel imageLabel;
            
            if (songImage != null) {
                // Scale image to fit
                BufferedImage scaled = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaled.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(songImage, 0, 0, 120, 120, null);
                g2d.dispose();
                imageLabel = new JLabel(new ImageIcon(scaled));
            } else {
                // Create default image
                BufferedImage defaultImage = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = defaultImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw music note as default
                GradientPaint gradient = new GradientPaint(0, 0, GameAssets.PRIMARY_COLOR, 120, 120, GameAssets.SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(10, 10, 100, 100, 15, 15);
                
                g2d.setColor(GameAssets.TEXT_COLOR);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
                g2d.drawString("♪", 45, 75);
                g2d.dispose();
                imageLabel = new JLabel(new ImageIcon(defaultImage));
            }
            
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            songPanel.add(imageLabel, BorderLayout.CENTER);
            
            // Add title and artist below image
            JLabel textLabel = new JLabel("<html><center><b>" + song.title + "</b><br/>" +
                                       "<small style='color: #888;'>" + song.artist + "</small></center></html>");
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textLabel.setForeground(GameAssets.TEXT_COLOR);
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            songPanel.add(textLabel, BorderLayout.SOUTH);
            
            // Add click listener
            songPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Deselect previous song
                    if (selectedSong != null) {
                        for (Component comp : songsGridPanel.getComponents()) {
                            comp.setBackground(GameAssets.SURFACE_COLOR);
                            if (comp instanceof JPanel) {
                                ((JPanel) comp).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                            }
                        }
                    }
                    
                    // Select new song
                    selectedSong = song;
                    songPanel.setBackground(GameAssets.ACCENT_COLOR);
                    songPanel.setBorder(BorderFactory.createLineBorder(GameAssets.ACCENT_COLOR, 3));
                    playButton.setEnabled(true);
                }
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (selectedSong != song) {
                        songPanel.setBackground(new Color(100, 100, 150));
                        songPanel.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 200), 2));
                        songPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (selectedSong != song) {
                        songPanel.setBackground(GameAssets.SURFACE_COLOR);
                        songPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        songPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });
            
            songsGridPanel.add(songPanel);
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
        private boolean isPaused = false;
        private JPanel pauseOverlay;
        private JButton resumeButton;
        private JButton songSelectionButton;
        private JButton mainMenuButton;
        private JButton quitButton;

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
            isPaused = false;
            pauseOverlay.setVisible(false);
            remove(pauseOverlay);
            timer.start();
        }

        private void spawnNote(int lane) {
            notes.add(new Note(lane, lane)); // direction = lane
        }

        private void update() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSpawnTime > GameAssets.SPAWN_INTERVAL) {
                int lane = random.nextInt(NUM_LANES);
                spawnNote(lane);
                lastSpawnTime = currentTime;
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
                        // Add MISS rating for notes that pass without being hit
                        timingRatings.add(new TimingRating("MISS", note.lane));
                    }
                    return true;
                }
                return false;
            });
            if (feverActive && System.currentTimeMillis() - feverStartTime > FEVER_DURATION) {
                feverActive = false;
                combo = 0; // Reset combo when fever ends so it can be restarted
            }
        }

        private void checkHits() {
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            
            // Reset all notes glowing state
            notes.forEach(note -> note.glowing = false);
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.isInHitZone() && keysPressed[keyMap[note.direction]]) {
                    // Make note glow
                    note.glowing = true;
                    
                    // Calculate timing accuracy
                    int distanceFromHitZone = Math.abs(note.y + GameAssets.NOTE_SIZE / 2 - HIT_ZONE_Y);
                    String rating;
                    int points;
                    
                    if (distanceFromHitZone <= PERFECT_WINDOW) {
                        rating = "PERFECT";
                        points = PERFECT_SCORE;
                    } else if (distanceFromHitZone <= GOOD_WINDOW) {
                        rating = "GOOD";
                        points = GOOD_SCORE;
                    } else if (distanceFromHitZone <= OKAY_WINDOW) {
                        rating = "OKAY";
                        points = OKAY_SCORE;
                    } else {
                        rating = "MISS";
                        points = MISS_SCORE;
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
        }

        public void backToSongSelection() {
            ((RhythmGame) SwingUtilities.getWindowAncestor(this)).showSongSelection();
        }

        // Helper method to draw arrow shapes with custom image support
        private void drawArrow(Graphics2D g2d, int x, int y, int size, int direction, boolean isHitZone) {
            // Try to load custom arrow image first
            BufferedImage arrowImage = loadArrowImage(direction);
            
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
