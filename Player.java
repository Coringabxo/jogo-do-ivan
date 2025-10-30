package mariogame;

import java.awt.*;
import java.util.List;

class Player {
    private static final float GRAVITY = 0.5f;
    private static final float JUMP_FORCE = -12f;
    private static final float PLAYER_SPEED = 5f;
    private static final float SUPER_SPEED = 6f;
    private static final float FRICTION = 0.8f;
    private static final float ENEMY_BOUNCE = -8f;
    
    private float x, y;
    private float velX, velY;
    private int width = 32;
    private int height = 48;
    private int superWidth = 36;
    private int superHeight = 72;
    private boolean onGround = false;
    private boolean facingRight = true;
    private boolean canJump = true;
    
    private int score = 0;
    private int lives = 3;
    private boolean invincible = false;
    private int invincibleTimer = 0;
    
    private boolean superMode = false;
    private boolean fireMode = false;
    
    public Player(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void update(List<Platform> platforms) {
        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }
        
        velY += GRAVITY;
        if (velY > 15f) velY = 15f;
        
        y += velY;
        
        onGround = false;
        Rectangle playerBounds = getBounds();
        
        for (Platform platform : platforms) {
            if (playerBounds.intersects(platform.getBounds())) {
                resolveVerticalCollision(platform);
            }
        }
        
        x += velX;
        
        playerBounds = getBounds();
        for (Platform platform : platforms) {
            if (playerBounds.intersects(platform.getBounds())) {
                resolveHorizontalCollision(platform);
            }
        }
        
        velX *= FRICTION;
        if (Math.abs(velX) < 0.1f) velX = 0;
        
        if (x < 0) x = 0;
        
        if (onGround) {
            canJump = true;
        }
    }
    
    private void resolveVerticalCollision(Platform platform) {
        Rectangle playerBounds = getBounds();
        Rectangle platformBounds = platform.getBounds();
        
        if (velY > 0 && 
            playerBounds.y + playerBounds.height > platformBounds.y &&
            playerBounds.y < platformBounds.y) {
            
            y = platformBounds.y - getCurrentHeight();
            velY = 0;
            onGround = true;
            canJump = true;
        }
        else if (velY < 0 && 
                 playerBounds.y < platformBounds.y + platformBounds.height &&
                 playerBounds.y + playerBounds.height > platformBounds.y + platformBounds.height) {
            
            y = platformBounds.y + platformBounds.height;
            velY = 0;
        }
    }
    
    private void resolveHorizontalCollision(Platform platform) {
        Rectangle playerBounds = getBounds();
        Rectangle platformBounds = platform.getBounds();
        
        if (velX > 0 && 
            playerBounds.x + playerBounds.width > platformBounds.x &&
            playerBounds.x < platformBounds.x) {
            
            x = platformBounds.x - getCurrentWidth();
            velX = 0;
        }
        else if (velX < 0 && 
                 playerBounds.x < platformBounds.x + platformBounds.width &&
                 playerBounds.x + playerBounds.width > platformBounds.x + platformBounds.width) {
            
            x = platformBounds.x + platformBounds.width;
            velX = 0;
        }
    }
    
    public void render(Graphics2D g) {
        if (invincible && (invincibleTimer / 5) % 2 == 0) {
            return;
        }
        
        // Cores base do Mario
        Color hatColor = Color.RED;
        Color overallsColor = Color.BLUE;
        Color skinColor = new Color(255, 204, 153); // Pele
        Color shirtColor = fireMode ? Color.ORANGE : Color.RED;
        
        int currentWidth = getCurrentWidth();
        int currentHeight = getCurrentHeight();
        
        if (superMode || fireMode) {
            // Mario SUPER - mais detalhado
            renderSuperMario(g, currentWidth, currentHeight, hatColor, overallsColor, skinColor, shirtColor);
        } else {
            // Mario pequeno - mais fiel ao original
            renderSmallMario(g, currentWidth, currentHeight, hatColor, overallsColor, skinColor, shirtColor);
        }
    }
    
    private void renderSmallMario(Graphics2D g, int width, int height, Color hatColor, Color overallsColor, Color skinColor, Color shirtColor) {
        // Chapéu
        g.setColor(hatColor);
        g.fillRoundRect((int)x - 3, (int)y - 5, width + 6, 12, 8, 8);
        
        // Cabelo/ombros
        g.setColor(Color.BLACK);
        g.fillRect((int)x + 2, (int)y + 5, width - 4, 3);
        
        // Rosto
        g.setColor(skinColor);
        g.fillRect((int)x + 4, (int)y + 8, width - 8, height - 25);
        
        // Bigode
        g.setColor(Color.BLACK);
        g.fillRect((int)x + 8, (int)y + 18, width - 16, 2);
        
        // Olhos
        g.setColor(Color.BLACK);
        if (facingRight) {
            g.fillRect((int)x + width - 12, (int)y + 12, 2, 2);
        } else {
            g.fillRect((int)x + 10, (int)y + 12, 2, 2);
        }
        
        // Macacão
        g.setColor(overallsColor);
        g.fillRect((int)x + 2, (int)y + height - 20, width - 4, 15);
        
        // Alças do macacão
        g.fillRect((int)x + 8, (int)y + 15, 6, 10);
        g.fillRect((int)x + width - 14, (int)y + 15, 6, 10);
        
        // Botões
        g.setColor(Color.YELLOW);
        g.fillRect((int)x + width/2 - 2, (int)y + height - 15, 4, 4);
        
        // Braços
        g.setColor(skinColor);
        g.fillRect((int)x - 2, (int)y + 15, 4, 8);
        g.fillRect((int)x + width - 2, (int)y + 15, 4, 8);
        
        // Pernas
        g.setColor(overallsColor);
        g.fillRect((int)x + 6, (int)y + height - 5, 8, 5);
        g.fillRect((int)x + width - 14, (int)y + height - 5, 8, 5);
    }
    
    private void renderSuperMario(Graphics2D g, int width, int height, Color hatColor, Color overallsColor, Color skinColor, Color shirtColor) {
        // Chapéu grande
        g.setColor(hatColor);
        g.fillRoundRect((int)x - 4, (int)y - 8, width + 8, 16, 10, 10);
        
        // Aba do chapéu
        g.setColor(Color.BLACK);
        g.drawRoundRect((int)x - 4, (int)y - 8, width + 8, 16, 10, 10);
        
        // Letra M no chapéu
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("M", (int)x + width/2 - 3, (int)y + 2);
        
        // Rosto maior
        g.setColor(skinColor);
        g.fillRect((int)x + 4, (int)y + 8, width - 8, 20);
        
        // Bigode maior
        g.setColor(Color.BLACK);
        g.fillRect((int)x + 8, (int)y + 20, width - 16, 3);
        
        // Olhos
        g.setColor(Color.BLACK);
        if (facingRight) {
            g.fillRect((int)x + width - 14, (int)y + 14, 3, 3);
        } else {
            g.fillRect((int)x + 11, (int)y + 14, 3, 3);
        }
        
        // Camisa
        g.setColor(shirtColor);
        g.fillRect((int)x + 2, (int)y + 28, width - 4, 15);
        
        // Macacão
        g.setColor(overallsColor);
        g.fillRect((int)x + 2, (int)y + 43, width - 4, height - 43);
        
        // Alças do macacão
        g.fillRect((int)x + 10, (int)y + 28, 8, 15);
        g.fillRect((int)x + width - 18, (int)y + 28, 8, 15);
        
        // Botões
        g.setColor(Color.YELLOW);
        g.fillRect((int)x + width/2 - 3, (int)y + 35, 6, 6);
        g.fillRect((int)x + width/2 - 3, (int)y + 45, 6, 6);
        
        // Braços
        g.setColor(skinColor);
        g.fillRect((int)x - 3, (int)y + 30, 5, 12);
        g.fillRect((int)x + width - 2, (int)y + 30, 5, 12);
        
        // Luvas (se for Fire Mario)
        if (fireMode) {
            g.setColor(Color.WHITE);
            g.fillRect((int)x - 3, (int)y + 30, 5, 4);
            g.fillRect((int)x + width - 2, (int)y + 30, 5, 4);
        }
        
        // Pernas
        g.setColor(overallsColor);
        g.fillRect((int)x + 8, (int)y + height - 8, 10, 8);
        g.fillRect((int)x + width - 18, (int)y + height - 8, 10, 8);
        
        // Sapatos
        g.setColor(Color.BLACK);
        g.fillRect((int)x + 6, (int)y + height - 3, 12, 3);
        g.fillRect((int)x + width - 18, (int)y + height - 3, 12, 3);
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, getCurrentWidth(), getCurrentHeight());
    }
    
    // 🔥 MÉTODO ADICIONADO - Para detecção de power-ups
    public float getVelocityY() {
        return velY;
    }
    
    public boolean hitsBlockFromBelow(Platform platform) {
        Rectangle playerBounds = getBounds();
        Rectangle platformBounds = platform.getBounds();
        
        return velY < 0 && 
               playerBounds.y + playerBounds.height > platformBounds.y &&
               playerBounds.y < platformBounds.y &&
               playerBounds.x + playerBounds.width > platformBounds.x &&
               playerBounds.x < platformBounds.x + platformBounds.width;
    }
    
    public boolean isJumpingOnEnemy(Enemy enemy) {
        Rectangle playerBounds = getBounds();
        Rectangle enemyBounds = enemy.getBounds();
        
        return velY > 0 && 
               playerBounds.y + playerBounds.height < enemyBounds.y + enemyBounds.height / 2 &&
               playerBounds.y + playerBounds.height > enemyBounds.y;
    }
    
    public void bounceFromEnemy() {
        velY = ENEMY_BOUNCE;
        onGround = false;
        canJump = false;
    }
    
    public void respawn() {
        x = 100;
        y = 300;
        velX = 0;
        velY = 0;
        invincible = true;
        invincibleTimer = 120;
        onGround = false;
        canJump = true;
        superMode = false;
        fireMode = false;
    }
    
    public void becomeSuper() {
        if (!superMode) {
            float oldBottom = y + getCurrentHeight();
            superMode = true;
            fireMode = false;
            y = oldBottom - getCurrentHeight();
            System.out.println("🎮 Player agora é SUPER MARIO!");
        }
    }
    
    public void becomeFire() {
        if (!fireMode) {
            if (!superMode) {
                float oldBottom = y + getCurrentHeight();
                superMode = true;
                y = oldBottom - getCurrentHeight();
            }
            fireMode = true;
            System.out.println("🎮 Player agora é FIRE MARIO!");
        }
    }
    
    public void setSuper(boolean superMode) {
        this.superMode = superMode;
        if (!superMode) {
            fireMode = false;
            System.out.println("🎮 Player voltou ao tamanho normal");
        }
    }
    
    public void moveLeft() {
        float currentSpeed = (superMode || fireMode) ? SUPER_SPEED : PLAYER_SPEED;
        velX = -currentSpeed;
        facingRight = false;
    }
    
    public void moveRight() {
        float currentSpeed = (superMode || fireMode) ? SUPER_SPEED : PLAYER_SPEED;
        velX = currentSpeed;
        facingRight = true;
    }
    
    public void jump() {
        if (onGround && canJump) {
            velY = JUMP_FORCE;
            onGround = false;
            canJump = false;
        }
    }
    
    public void stop() {
        velX = 0;
    }
    
    private int getCurrentWidth() {
        return (superMode || fireMode) ? superWidth : width;
    }
    
    private int getCurrentHeight() {
        return (superMode || fireMode) ? superHeight : height;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isSuper() { return superMode; }
    public boolean canShootFireballs() { return fireMode; }
    public void addScore(int points) { score += points; }
    public void loseLife() { lives--; }
}