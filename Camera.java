package mariogame;

class Camera {
    private float x;
    
    public Camera() {
        this.x = 0;
    }
    
    public void update(Player player) {
        x = player.getX() - 400;
        if (x < 0) x = 0;
    }
    
    public float getX() {
        return x;
    }
}