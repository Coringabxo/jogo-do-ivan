package mariogame;

import java.awt.*;
import java.util.List;

class Mushroom extends PowerUp {
    
    public Mushroom(float x, float y) {
        super(x, y);
        this.velX = 1.5f;
        this.type = 0;
    }
    
    @Override
    public void update(List<Platform> platforms) {
        if (!active) return;
        
        velY += 0.3f;
        if (velY > 8f) velY = 8f;
        
        y += velY;
        
        Rectangle bounds = getBounds();
        for (Platform platform : platforms) {
            if (bounds.intersects(platform.getBounds())) {
                if (velY > 0 && y + height > platform.getBounds().y && y < platform.getBounds().y) {
                    y = platform.getBounds().y - height;
                    velY = 0;
                }
                else if (velY < 0 && y < platform.getBounds().y + platform.getBounds().height) {
                    y = platform.getBounds().y + platform.getBounds().height;
                    velY = 0;
                }
            }
        }
        
        x += velX;
        
        bounds = getBounds();
        for (Platform platform : platforms) {
            if (bounds.intersects(platform.getBounds())) {
                if (velX > 0 && x + width > platform.getBounds().x && x < platform.getBounds().x) {
                    x = platform.getBounds().x - width;
                    velX = -velX;
                }
                else if (velX < 0 && x < platform.getBounds().x + platform.getBounds().width && x + width > platform.getBounds().x + platform.getBounds().width) {
                    x = platform.getBounds().x + platform.getBounds().width;
                    velX = -velX;
                }
            }
        }
        
        if (x < 50) {
            x = 50;
            velX = Math.abs(velX);
        }
        if (x > 2400) {
            x = 2400;
            velX = -Math.abs(velX);
        }
        
        if (y > 600) active = false;
    }
    
    @Override
    public void render(Graphics2D g) {
        if (!active) return;
        
        g.setColor(new Color(220, 0, 0));
        g.fillRoundRect((int)x, (int)y, width, height - 10, 15, 15);
        
        g.setColor(Color.WHITE);
        g.fillRoundRect((int)x + 2, (int)y + height - 15, width - 4, 10, 8, 8);
        
        g.setColor(Color.WHITE);
        g.fillOval((int)x + 6, (int)y + 5, 8, 8);
        g.fillOval((int)x + width - 14, (int)y + 5, 8, 8);
        g.fillOval((int)x + width/2 - 4, (int)y + 8, 8, 8);
        
        g.setColor(Color.BLACK);
        g.drawRoundRect((int)x, (int)y, width, height - 10, 15, 15);
    }
    
    @Override
    public void applyEffect(Player player) {
        if (!active) return;
        
        player.becomeSuper();
        player.addScore(500);
        active = false;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}