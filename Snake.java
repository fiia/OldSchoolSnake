import java.util.List;
import java.util.ArrayList;

/**
 * @author josefiina
 *
 */

public class Snake {
    private int x;
    private int y;
    private int width;
    private int heigth;
    private boolean growth;
    private Direction direction;
    private List<Part>snakeParts;
    
    public Snake(int startX, int startY, Direction startDirection, int width, int heigth) {
        this.x = startX;
        this.y = startY;
	this.width = width;
	this.heigth = heigth;
        this.growth = false;
        this.direction = startDirection;
        this.snakeParts = new ArrayList<>();
        start(startX, startY, direction);
    }
    
    public void start(int startX, int startY, Direction direction) {
        for(int i=0; i<4; i++) {
            this.snakeParts.add(new Part(startX, startY+i));
        }
    }
    
    public Direction getDirection() { return this.direction; }
    
    public void setDirection(Direction direction) { this.direction = direction; }
    
    public int getLength() { return this.snakeParts.size(); }

    public Part getHead() { return this.snakeParts.get(this.snakeParts.size()-1); }
    
    //HEAD AT THE END OF LIST
    public List<Part> getParts() { return this.snakeParts; }

    public void grow() { this.growth = true; }
    
    public void basicMove() {
        //CREATE A NEW HEAD ACCORDING TO DIRECTION
	int x = getHead().getX();
	int y = getHead().getY();
        
        if (this.direction==Direction.RIGHT) { x++; }
        if (this.direction==Direction.LEFT) { x--; }
        if (this.direction==Direction.DOWN) { y++; }
        if (this.direction==Direction.UP) { y--; }
        
        this.snakeParts.add(new Part(x, y));
        keepSize();
    }

    public void newMove() {
	//IF GOES THROUGH WALL MOVE TO ANOTHER SIDE
	int x = getHead().getX();
	int y = getHead().getY();

	if(x == 0 && this.direction == Direction.LEFT) { x = this.width-1; }
	else if (x == this.width-1 && this.direction == Direction.RIGHT) { x = 0; }
	else if (y == 0 && this.direction == Direction.UP) { y = this.heigth-1; }
	else if (y == this.heigth-1 && this.direction == Direction.DOWN) { y = 0; }
	else {
	    basicMove();
	    return;
	}
	
	this.snakeParts.add(new Part(x, y));
        keepSize();
    }

    public void keepSize() {
        if (!this.growth) { this.snakeParts.remove(0); }
	this.growth=false;
    }
    
    public boolean hit(Part part) {
        for(Part p : this.snakeParts) {
	    if (p.hit(part)) {
                return true;
            }
        }
        return false;
    }

    //CHECK IF SNAKES HEAD HITS ITS BODY
    //TODO: DIES IF LEFT ARROW IS PRESSED WHILE DIRECTION IS RIGHT
    //THIS IS ANNOYING
    public boolean hitsSelf() {
	for (int i=0; i<this.snakeParts.size()-1; i++) {
	    if (getHead().hit(this.snakeParts.get(i))) {
                return true;
            }
	 }
        return false;
    }
}
