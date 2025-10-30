package mariogame;

import java.awt.*;
import java.util.List;

class Enemy {
    private float x, y;
    private float velX = 1.5f;
    private int width = 32;
    private int height = 32;
    private int minX, maxX;
    
    public Enemy(float startX, float startY, int minX, int maxX) {
        this.x = startX;
        this.y = startY;
        this.minX = minX;
        this.maxX = maxX;
    }
    
    public void update(List<Platform> platforms) {
        x += velX;
        
        if (x <= minX || x >= maxX) {
            velX = -velX;
        }
        
        y += 2;
        Rectangle enemyBounds = getBounds();
        
        for (Platform platform : platforms) {
            if (enemyBounds.intersects(platform.getBounds())) {
                y = platform.getBounds().y - height;
                break;
            }
        }
    }
    
    public void render(Graphics2D g) {
        g.setColor(new Color(139, 69, 19));
        g.fillOval((int)x, (int)y, width, height);
        
        g.setColor(Color.BLACK);
        g.drawOval((int)x, (int)y, width, height);
        
        g.setColor(Color.WHITE);
        g.fillOval((int)x + 8, (int)y + 8, 6, 6);
        g.fillOval((int)x + width - 14, (int)y + 8, 6, 6);
        
        g.setColor(Color.BLACK);
        g.fillOval((int)x + 10, (int)y + 10, 3, 3);
        g.fillOval((int)x + width - 12, (int)y + 10, 3, 3);
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public float getY() { return y; }
    public int getHeight() { return height; }
}