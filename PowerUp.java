package mariogame;

import java.awt.*;
import java.util.List;

abstract class PowerUp {
    protected float x, y;
    protected float velX = 0, velY = 0;
    protected int width = 30;
    protected int height = 30;
    protected boolean active = true;
    protected int type;
    
    public PowerUp(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public abstract void update(List<Platform> platforms);
    public abstract void render(Graphics2D g);
    public abstract void applyEffect(Player player);
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public boolean isActive() { return active; }
    public void collect() { active = false; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getType() { return type; }
}