package mariogame;

import java.awt.*;

class Flag {
    private int x, y;
    private int width = 30;
    private int height = 100;
    private boolean collected = false;
    
    public Flag(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void render(Graphics2D g) {
        if (collected) return;
        
        // Mastro da bandeira
        g.setColor(new Color(160, 82, 45));
        g.fillRect(x, y, 5, height);
        
        // Bandeira
        g.setColor(Color.RED);
        int[] xPoints = {x + 5, x + width, x + 5};
        int[] yPoints = {y + 10, y + 30, y + 50};
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Detalhes da bandeira
        g.setColor(Color.WHITE);
        g.drawPolygon(xPoints, yPoints, 3);
        
        // Base
        g.setColor(Color.GRAY);
        g.fillRect(x - 5, y + height - 10, 15, 10);
    }
    
    public void collect() {
        collected = true;
    }
    
    public boolean isActive() {
        return !collected;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - 10, y, width + 20, height);
    }
}