package mariogame;

import java.awt.*;
import java.util.List;

class FireFlower extends PowerUp {
    
    public FireFlower(float x, float y) {
        super(x, y);
        this.type = 1;
    }
    
    @Override
    public void update(List<Platform> platforms) {
        if (!active) return;
        
        velY += 0.2f;
        if (velY > 3f) velY = 3f;
        
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
        
        if (y > 600) active = false;
    }
    
    @Override
    public void render(Graphics2D g) {
        if (!active) return;
        
        // Caule verde
        g.setColor(new Color(34, 139, 34));
        g.fillRect((int)x + width/2 - 2, (int)y + height/2, 4, height/2);
        
        // Centro da flor
        g.setColor(Color.YELLOW);
        g.fillOval((int)x + 5, (int)y + 5, width - 10, height - 10);
        
        // Pétalas
        g.setColor(Color.RED);
        g.fillOval((int)x + 8, (int)y - 5, width - 16, 15);
        g.fillOval((int)x + 8, (int)y + height - 10, width - 16, 15);
        g.fillOval((int)x - 5, (int)y + 8, 15, height - 16);
        g.fillOval((int)x + width - 10, (int)y + 8, 15, height - 16);
        
        // Detalhes do centro
        g.setColor(new Color(255, 165, 0));
        g.fillOval((int)x + 8, (int)y + 8, width - 16, height - 16);
        
        // Contorno
        g.setColor(Color.BLACK);
        g.drawOval((int)x + 5, (int)y + 5, width - 10, height - 10);
    }
    
    @Override
    public void applyEffect(Player player) {
        if (!active) return;
        
        player.becomeFire();
        player.addScore(800);
        active = false;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}