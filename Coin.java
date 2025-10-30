package mariogame;

import java.awt.*;

class Coin {
    private int x, y;
    private boolean collected = false;
    private int width = 20, height = 20;
    
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void render(Graphics2D g) {
        if (!collected) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
            g.setColor(Color.ORANGE);
            g.drawOval(x, y, width, height);
            g.setColor(new Color(255, 215, 0));
            g.fillOval(x + 5, y + 5, width - 10, height - 10);
        }
    }
    
    public void collect() {
        collected = true;
    }
    
    public boolean isActive() {
        return !collected;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}