package mariogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MarioGameSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow();
            gameWindow.setVisible(true);
        });
    }
}

class GameWindow extends JFrame {
    private GamePanel gamePanel;
    
    public GameWindow() {
        setTitle("Super Mario Swing - 2 FASES!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        gamePanel = new GamePanel();
        add(gamePanel);
        
        pack();
        setLocationRelativeTo(null);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 500;
    private static final int DELAY = 16;
    
    private Timer gameTimer;
    private Player player;
    private List<Platform> platforms;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private List<PowerUp> powerUps;
    private Flag flag;
    private Camera camera;
    
    private boolean gameRunning = true;
    private boolean levelCompleted = false;
    private boolean gameOver = false;
    private int currentLevel = 1;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean jumpPressed = false;
    private boolean jumpJustPressed = false;
    
    // Botões
    private JButton restartButton;
    private JButton nextLevelButton;
    
    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(new Color(135, 206, 235));
        setFocusable(true);
        setLayout(null); // Para posicionar botões manualmente
        addKeyListener(this);
        
        createButtons();
        initGame();
        startGame();
    }
    
    private void createButtons() {
        // Botão de reiniciar
        restartButton = new JButton("Jogar Novamente");
        restartButton.setBounds(SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 + 80, 200, 40);
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setBackground(Color.GREEN);
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusable(false);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        
        // Botão próxima fase
        nextLevelButton = new JButton("Próxima Fase");
        nextLevelButton.setBounds(SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 + 80, 200, 40);
        nextLevelButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextLevelButton.setBackground(Color.BLUE);
        nextLevelButton.setForeground(Color.WHITE);
        nextLevelButton.setFocusable(false);
        nextLevelButton.setVisible(false);
        nextLevelButton.addActionListener(e -> nextLevel());
        
        add(restartButton);
        add(nextLevelButton);
    }
    
    private void initGame() {
        player = new Player(100, 300);
        camera = new Camera();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        
        createLevel(currentLevel);
        
        restartButton.setVisible(false);
        nextLevelButton.setVisible(false);
    }
    
    private void createLevel(int level) {
        platforms.clear();
        coins.clear();
        enemies.clear();
        powerUps.clear();
        
        if (level == 1) {
            createLevel1();
        } else {
            createLevel2();
        }
        
        System.out.println("Fase " + level + " carregada!");
    }
    
    private void createLevel1() {
        // Chão principal
        platforms.add(new Platform(0, 450, 2000, 50));
        
        // Plataformas
        platforms.add(new Platform(300, 350, 200, 20));
        platforms.add(new Platform(600, 300, 150, 20));
        platforms.add(new Platform(900, 250, 200, 20));
        platforms.add(new Platform(1200, 350, 200, 20));
        platforms.add(new Platform(1500, 280, 180, 20));
        platforms.add(new Platform(1800, 350, 200, 20));
        
        // Plataformas com power-ups
        platforms.add(new Platform(400, 250, 40, 40, true, 0)); // Cogumelo
        platforms.add(new Platform(800, 200, 40, 40, true, 1)); // Flor
        
        // Moedas
        coins.add(new Coin(350, 320));
        coins.add(new Coin(650, 270));
        coins.add(new Coin(950, 220));
        coins.add(new Coin(1250, 320));
        coins.add(new Coin(400, 320));
        coins.add(new Coin(1550, 250));
        coins.add(new Coin(500, 320));
        coins.add(new Coin(1850, 320));
        
        // Inimigos
        enemies.add(new Enemy(500, 410, 100, 400));
        enemies.add(new Enemy(1000, 410, 900, 1100));
        enemies.add(new Enemy(1400, 410, 1300, 1500));
        
        // Bandeira
        flag = new Flag(1900, 350);
    }
    
    private void createLevel2() {
        // Chão principal mais curto
        platforms.add(new Platform(0, 450, 1800, 50));
        
        // Plataformas mais desafiadoras
        platforms.add(new Platform(250, 380, 150, 20));
        platforms.add(new Platform(500, 320, 120, 20));
        platforms.add(new Platform(750, 280, 100, 20));
        platforms.add(new Platform(950, 320, 150, 20));
        platforms.add(new Platform(1200, 250, 180, 20));
        platforms.add(new Platform(1450, 320, 120, 20));
        
        // Plataformas móveis (simuladas com inimigos que não matam)
        platforms.add(new Platform(600, 200, 80, 20, true, 0));
        platforms.add(new Platform(1100, 180, 80, 20, true, 1));
        
        // Mais moedas
        for (int i = 0; i < 12; i++) {
            coins.add(new Coin(200 + i * 120, 300 + (i % 3) * 30));
        }
        
        // Mais inimigos
        enemies.add(new Enemy(400, 410, 350, 450));
        enemies.add(new Enemy(700, 410, 650, 750));
        enemies.add(new Enemy(1000, 410, 950, 1050));
        enemies.add(new Enemy(1300, 410, 1250, 1350));
        enemies.add(new Enemy(1600, 410, 1550, 1650));
        
        // Bandeira
        flag = new Flag(1700, 350);
    }
    
    private void startGame() {
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }
    
    private void update() {
        if (!gameRunning || levelCompleted || gameOver) return;
        
        // Sistema de pulo responsivo
        if (jumpJustPressed) {
            player.jump();
            jumpJustPressed = false;
        } else if (jumpPressed) {
            player.jump();
        }
        
        // Movimento horizontal
        if (leftPressed && !rightPressed) {
            player.moveLeft();
        } else if (rightPressed && !leftPressed) {
            player.moveRight();
        } else {
            player.stop();
        }
        
        player.update(platforms);
        camera.update(player);
        
        // Atualizar inimigos
        for (Enemy enemy : enemies) {
            enemy.update(platforms);
        }
        
        // Atualizar power-ups
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update(platforms);
            
            if (powerUp.getY() > SCREEN_HEIGHT + 100) {
                powerUps.remove(i);
                continue;
            }
            
            if (powerUp.isActive() && player.getBounds().intersects(powerUp.getBounds())) {
                powerUp.applyEffect(player);
                powerUps.remove(i);
            }
        }
        
        // Verificar colisão com moedas
        for (int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (coin.isActive() && player.getBounds().intersects(coin.getBounds())) {
                coin.collect();
                player.addScore(100);
                coins.remove(i);
            }
        }
        
        // Verificar colisão com blocos de power-up
        for (Platform platform : platforms) {
            if (platform.hasPowerUp() && !platform.isPowerUpUsed() && 
                player.hitsBlockFromBelow(platform)) {
                platform.setPowerUpUsed(true);
                spawnPowerUp(platform.getX(), platform.getY() - 40, platform.getPowerUpType());
            }
        }
        
        // Verificar colisão com inimigos
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (player.getBounds().intersects(enemy.getBounds())) {
                if (player.isJumpingOnEnemy(enemy)) {
                    player.addScore(200);
                    enemies.remove(i);
                    player.bounceFromEnemy();
                } else {
                    if (player.isSuper()) {
                        player.setSuper(false);
                    } else {
                        player.loseLife();
                        player.respawn();
                        if (player.getLives() <= 0) {
                            gameOver();
                        }
                    }
                }
            }
        }
        
        // Verificar se chegou na bandeira
        if (flag.isActive() && player.getBounds().intersects(flag.getBounds())) {
            completeLevel();
        }
        
        // Game over se cair no vazio
        if (player.getY() > SCREEN_HEIGHT + 100) {
            player.loseLife();
            player.respawn();
            if (player.getLives() <= 0) {
                gameOver();
            }
        }
    }
    
    private void spawnPowerUp(int x, int y, int powerUpType) {
        switch (powerUpType) {
            case 0:
                powerUps.add(new Mushroom(x, y));
                break;
            case 1:
                powerUps.add(new FireFlower(x, y));
                break;
        }
    }
    
    private void completeLevel() {
        levelCompleted = true;
        player.addScore(1000);
        flag.collect();
        
        if (currentLevel < 2) {
            nextLevelButton.setVisible(true);
        } else {
            restartButton.setVisible(true);
        }
    }
    
    private void gameOver() {
        gameOver = true;
        gameRunning = false;
        restartButton.setVisible(true);
    }
    
    private void restartGame() {
        currentLevel = 1;
        gameRunning = true;
        levelCompleted = false;
        gameOver = false;
        initGame();
        restartButton.setVisible(false);
        nextLevelButton.setVisible(false);
        requestFocus();
    }
    
    private void nextLevel() {
        currentLevel++;
        gameRunning = true;
        levelCompleted = false;
        initGame();
        nextLevelButton.setVisible(false);
        requestFocus();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(-camera.getX(), 0);
        
        // Fundo diferente para cada fase
        if (currentLevel == 1) {
            g2d.setColor(new Color(135, 206, 235)); // Azul claro - dia
        } else {
            g2d.setColor(new Color(25, 25, 112));   // Azul escuro - noite
            // Estrelas
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < 20; i++) {
                g2d.fillOval(50 + i * 100, 50 + (i % 3) * 30, 3, 3);
            }
        }
        g2d.fillRect(0, 0, 2500, SCREEN_HEIGHT);
        
        // Renderizar plataformas
        for (Platform platform : platforms) {
            platform.render(g2d);
        }
        
        // Renderizar moedas
        for (Coin coin : coins) {
            if (coin.isActive()) {
                coin.render(g2d);
            }
        }
        
        // Renderizar power-ups
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                powerUp.render(g2d);
            }
        }
        
        // Renderizar inimigos
        for (Enemy enemy : enemies) {
            enemy.render(g2d);
        }
        
        // Renderizar bandeira
        flag.render(g2d);
        
        // Renderizar player
        player.render(g2d);
        
        g2d.translate(camera.getX(), 0);
        
        // HUD
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Fase: " + currentLevel, 20, 30);
        g2d.drawString("Score: " + player.getScore(), 20, 60);
        g2d.drawString("Vidas: " + player.getLives(), 20, 90);
        
        // Status do power-up
        if (player.isSuper()) {
            g2d.setColor(Color.RED);
            g2d.drawString("SUPER!", 20, 120);
        }
        if (player.canShootFireballs()) {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("FIRE!", 20, 150);
        }
        
        // Mensagens de estado do jogo
        if (levelCompleted) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("FASE " + currentLevel + " COMPLETA!", SCREEN_WIDTH/2 - 250, SCREEN_HEIGHT/2 - 50);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("Score: " + player.getScore(), SCREEN_WIDTH/2 - 80, SCREEN_HEIGHT/2);
            
            if (currentLevel < 2) {
                g2d.drawString("Clique em 'Próxima Fase' para continuar", SCREEN_WIDTH/2 - 180, SCREEN_HEIGHT/2 + 30);
            } else {
                g2d.drawString("Parabéns! Você completou todas as fases!", SCREEN_WIDTH/2 - 220, SCREEN_HEIGHT/2 + 30);
            }
        } else if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            g2d.setColor(Color.RED);
            g2d.drawString("GAME OVER", SCREEN_WIDTH/2 - 120, SCREEN_HEIGHT/2 - 50);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Score Final: " + player.getScore(), SCREEN_WIDTH/2 - 80, SCREEN_HEIGHT/2);
            g2d.drawString("Clique em 'Jogar Novamente' para recomeçar", SCREEN_WIDTH/2 - 200, SCREEN_HEIGHT/2 + 30);
        }
        
        // Instruções
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Controles: ← → para mover, ESPAÇO para pular", 20, SCREEN_HEIGHT - 30);
        g2d.drawString("Pule nos blocos '?' para power-ups", 20, SCREEN_HEIGHT - 50);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (!jumpPressed) {
                    jumpJustPressed = true;
                }
                jumpPressed = true;
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                jumpPressed = false;
                jumpJustPressed = false;
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}