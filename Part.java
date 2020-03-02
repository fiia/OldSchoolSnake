/**
 *
 * @author josefiina
 */

public class Part {
    private int x;
    private int y;
    
    public Part(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return this.x; }
    
    public int getY() { return this.y; }
    
    public void setX(int x) { this.x = x; }
    
    public void setY(int y) { this.y = y; }
    
    public boolean hit(Part part) {
        return (part.getX()==this.x && part.getY()==this.y);
    }

}
