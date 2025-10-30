package mariogame;

import java.awt.*;

class Platform {
    private int x, y, width, height;
    private boolean hasPowerUp;
    private boolean powerUpUsed;
    private int powerUpType;
    
    public Platform(int x, int y, int width, int height) {
        this(x, y, width, height, false, 0);
    }
    
    public Platform(int x, int y, int width, int height, boolean hasPowerUp, int powerUpType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hasPowerUp = hasPowerUp;
        this.powerUpUsed = false;
        this.powerUpType = powerUpType;
    }
    
    public void render(Graphics2D g) {
        if (hasPowerUp && !powerUpUsed) {
            g.setColor(new Color(255, 215, 0));
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("?", x + width/2 - 5, y + height/2 + 5);
        } else {
            g.setColor(new Color(139, 69, 19));
            g.fillRect(x, y, width, height);
            g.setColor(new Color(101, 67, 33));
            g.drawRect(x, y, width, height);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean hasPowerUp() { return hasPowerUp; }
    public boolean isPowerUpUsed() { return powerUpUsed; }
    public void setPowerUpUsed(boolean used) { this.powerUpUsed = used; }
    public int getPowerUpType() { return powerUpType; }
    public int getX() { return x; }
    public int getY() { return y; }
}