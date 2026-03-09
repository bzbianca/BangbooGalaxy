import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RhythmGame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private SettingsPanel settingsPanel;
    private int[] keybinds = {90, 88, 44, 46}; // Z X , . (KeyEvent.VK codes)
    private float musicVolume = 1.0f;
    private float sfxVolume = 1.0f;

    public RhythmGame() {
        setTitle("Rhythm Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel();
        gamePanel = new GamePanel();
        settingsPanel = new SettingsPanel();

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(settingsPanel, "Settings");

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame() {
        gamePanel.resetGame();
        cardLayout.show(mainPanel, "Game");
        gamePanel.requestFocus();
    }

    public void showSettings() {
        cardLayout.show(mainPanel, "Settings");
    }

    public void backToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    public int[] getKeybinds() {
        return keybinds;
    }

    public void setKeybind(int lane, int keyCode) {
        keybinds[lane] = keyCode;
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;
    }

    public void setSfxVolume(float volume) {
        sfxVolume = volume;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RhythmGame());
    }

    class MenuPanel extends JPanel {
        public MenuPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.BLACK);

            JLabel titleLabel = new JLabel("Rhythm Game", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
            titleLabel.setForeground(Color.WHITE);
            add(titleLabel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.BLACK);
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            JButton playButton = new JButton("Play");
            playButton.setFont(new Font("Arial", Font.BOLD, 24));
            playButton.addActionListener(e -> startGame());
            buttonPanel.add(playButton);

            JButton settingsButton = new JButton("Settings");
            settingsButton.setFont(new Font("Arial", Font.BOLD, 24));
            settingsButton.addActionListener(e -> showSettings());
            buttonPanel.add(settingsButton);

            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    class SettingsPanel extends JPanel {
        private JLabel[] keybindLabels;
        private JButton[] keybindButtons;
        private int selectedLane = -1;
        private static final String[] LANE_NAMES = {"Lane 1 (Z)", "Lane 2 (X)", "Lane 3 (,)", "Lane 4 (.)"};
        private static final String[] KEY_NAMES = {"Z", "X", ",", "."};

        public SettingsPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(800, 600));

            JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
            titleLabel.setForeground(Color.WHITE);
            add(titleLabel, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel();
            contentPanel.setBackground(Color.BLACK);
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

            // Music Volume
            JPanel musicPanel = new JPanel();
            musicPanel.setBackground(Color.BLACK);
            JLabel musicLabel = new JLabel("Music Volume:");
            musicLabel.setForeground(Color.WHITE);
            musicLabel.setFont(new Font("Arial", Font.BOLD, 18));
            JSlider musicSlider = new JSlider(0, 100, 100);
            musicSlider.setBackground(Color.BLACK);
            musicSlider.setForeground(Color.WHITE);
            musicSlider.addChangeListener(e -> setMusicVolume(musicSlider.getValue() / 100.0f));
            musicPanel.add(musicLabel);
            musicPanel.add(musicSlider);

            // SFX Volume
            JPanel sfxPanel = new JPanel();
            sfxPanel.setBackground(Color.BLACK);
            JLabel sfxLabel = new JLabel("SFX Volume:");
            sfxLabel.setForeground(Color.WHITE);
            sfxLabel.setFont(new Font("Arial", Font.BOLD, 18));
            JSlider sfxSlider = new JSlider(0, 100, 100);
            sfxSlider.setBackground(Color.BLACK);
            sfxSlider.setForeground(Color.WHITE);
            sfxSlider.addChangeListener(e -> setSfxVolume(sfxSlider.getValue() / 100.0f));
            sfxPanel.add(sfxLabel);
            sfxPanel.add(sfxSlider);

            // Keybinds
            JLabel keybindTitleLabel = new JLabel("Keybinds");
            keybindTitleLabel.setForeground(Color.WHITE);
            keybindTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            keybindTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            keybindLabels = new JLabel[4];
            keybindButtons = new JButton[4];
            for (int i = 0; i < 4; i++) {
                JPanel keybindPanel = new JPanel();
                keybindPanel.setBackground(Color.BLACK);
                keybindPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel label = new JLabel(LANE_NAMES[i] + ":");
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 16));
                keybindLabels[i] = label;

                JButton button = new JButton(KEY_NAMES[i]);
                button.setFont(new Font("Arial", Font.BOLD, 16));
                final int lane = i;
                button.addActionListener(e -> {
                    selectedLane = lane;
                    button.setText("Press a key...");
                });
                keybindButtons[i] = button;

                keybindPanel.add(label);
                keybindPanel.add(button);
                contentPanel.add(keybindPanel);
            }

            contentPanel.add(musicPanel);
            contentPanel.add(sfxPanel);
            contentPanel.add(keybindTitleLabel);
            for (int i = 0; i < 4; i++) {
                JPanel keybindPanel = new JPanel();
                keybindPanel.setBackground(Color.BLACK);
                keybindPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel label = new JLabel(LANE_NAMES[i] + ":");
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 16));

                JButton button = new JButton(KEY_NAMES[i]);
                button.setFont(new Font("Arial", Font.BOLD, 16));
                final int lane = i;
                button.addActionListener(e -> {
                    selectedLane = lane;
                    button.setText("Press a key...");
                });
                keybindButtons[i] = button;

                keybindPanel.add(label);
                keybindPanel.add(button);
                contentPanel.add(keybindPanel);
            }

            add(new JScrollPane(contentPanel), BorderLayout.CENTER);

            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Arial", Font.BOLD, 24));
            backButton.addActionListener(e -> backToMenu());
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.BLACK);
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (selectedLane != -1) {
                        setKeybind(selectedLane, e.getKeyCode());
                        updateKeybindDisplay();
                        selectedLane = -1;
                    }
                }
            });
            setFocusable(true);
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
        private static final int WIDTH = 800;
        private static final int HEIGHT = 600;
        private static final int LANE_WIDTH = 50;
        private static final int LANE_GAP = 20;
        private static final int NUM_LANES = 4;
        private static final int TOTAL_LANES_WIDTH = NUM_LANES * LANE_WIDTH + (NUM_LANES - 1) * LANE_GAP;
        private static final int START_X = (WIDTH - TOTAL_LANES_WIDTH) / 2;
        private static final int[] LANE_POSITIONS = new int[NUM_LANES];
        private static final int HIT_ZONE_Y = 550;
        private static final int NOTE_SPEED = 4;
        private static final int NOTE_SIZE = 40;
        private static final int MAX_COMBO_FOR_FEVER = 20;
        private static final int FEVER_DURATION = 5000; // ms

        static {
            for (int i = 0; i < NUM_LANES; i++) {
                LANE_POSITIONS[i] = START_X + i * (LANE_WIDTH + LANE_GAP);
            }
        }

        private List<Note> notes;
        private boolean[] keysPressed;
        private Timer timer;
        private Random random;
        private boolean feverActive;
        private long feverStartTime;
        private long lastSpawnTime;
        private int score;
        private int combo;

        public GamePanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBackground(Color.BLACK);

            timer = new Timer(16, e -> {
                update();
                repaint();
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPressed(e.getKeyCode());
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    handleKeyReleased(e.getKeyCode());
                }
            });
            setFocusable(true);
            
            resetGame();
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
            timer.start();
        }

        private void spawnNote(int lane) {
            notes.add(new Note(lane, lane)); // direction = lane
        }

        private void update() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSpawnTime > 1000) {
                int lane = random.nextInt(NUM_LANES);
                spawnNote(lane);
                lastSpawnTime = currentTime;
            }
            notes.forEach(Note::update);
            checkHits();
            notes.removeIf(note -> {
                if (note.y >= HEIGHT) {
                    if (!note.hit) {
                        combo = 0;
                    }
                    return true;
                }
                return false;
            });
            if (feverActive && System.currentTimeMillis() - feverStartTime > FEVER_DURATION) {
                feverActive = false;
            }
        }

        private void checkHits() {
            int[] keyMap = ((RhythmGame) SwingUtilities.getWindowAncestor(this)).getKeybinds();
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.isInHitZone() && keysPressed[keyMap[note.direction]]) {
                    int points = feverActive ? 20 : 10;
                    score += points;
                    combo++;
                    if (combo >= MAX_COMBO_FOR_FEVER && !feverActive) {
                        feverActive = true;
                        feverStartTime = System.currentTimeMillis();
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
        }

        public void handleKeyReleased(int keyCode) {
            if (keyCode < keysPressed.length) {
                keysPressed[keyCode] = false;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw score and combo (top left)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("Score: " + score, 10, 30);
            g2d.drawString("Combo: " + combo, 10, 60);

            // Draw fever bar (vertical at top right)
            int barWidth = 20;
            int barX = WIDTH - barWidth - 10;
            g2d.setColor(Color.GRAY);
            g2d.fillRect(barX, 0, barWidth, HEIGHT);
            float progress;
            if (feverActive) {
                long elapsed = System.currentTimeMillis() - feverStartTime;
                progress = Math.max(0.0f, 1.0f - (elapsed / (float) FEVER_DURATION));
                g2d.setColor(Color.YELLOW);
            } else {
                progress = combo / (float) MAX_COMBO_FOR_FEVER;
                g2d.setColor(Color.GREEN);
            }
            int barHeight = (int) (HEIGHT * progress);
            g2d.fillRect(barX, HEIGHT - barHeight, barWidth, barHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(barX, 0, barWidth, HEIGHT);

            // Draw fever text
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(Color.WHITE);
            if (feverActive) {
                g2d.drawString("FEVER!", barX - 60, HEIGHT / 2);
            } else {
                g2d.drawString("Build Combo", barX - 80, HEIGHT / 2);
            }

            // Draw hit zone
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(0, HIT_ZONE_Y, WIDTH, HIT_ZONE_Y);
            g2d.setStroke(new BasicStroke(1));

            // Draw keybinds
            String[] keys = {"Z", "X", ",", "."};
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            for (int i = 0; i < NUM_LANES; i++) {
                int keyX = LANE_POSITIONS[i] + LANE_WIDTH / 2;
                int keyY = HIT_ZONE_Y + 30;
                g2d.drawString(keys[i], keyX - fm.stringWidth(keys[i]) / 2, keyY);
            }

            // Draw notes
            notes.forEach(note -> note.draw(g2d));
        }

        class Note {
            int lane;
            int direction; // 0: left, 1: up, 2: down, 3: right
            int y = 0;
            int x;
            boolean hit = false;

            Note(int lane, int direction) {
                this.lane = lane;
                this.direction = direction;
                this.x = LANE_POSITIONS[lane];
            }

            void update() {
                y += NOTE_SPEED;
            }

            void draw(Graphics2D g2d) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x + (LANE_WIDTH - NOTE_SIZE) / 2, y, NOTE_SIZE, NOTE_SIZE);
            }

            boolean isInHitZone() {
                return y + NOTE_SIZE > HIT_ZONE_Y && y < HEIGHT;
            }
        }
    }
}