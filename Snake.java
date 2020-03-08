import java.util.List;
import java.util.ArrayList;

/**
 * @author josefiina
 *
 */

//START CREATES ONLY ONE PART NOW

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

    //ADDS 4 PARTS TO SNAKE, NOW START DIR CAN ONLY BE UP OR DOWN
    public void start(int startX, int startY, Direction dir) {
	 for(int i=0; i<4; i++) {
	     int newY = (dir == Direction.DOWN) ? startY+i : startY-i;
             this.snakeParts.add(new Part(startX, newY));
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

	switch(this.direction) {
	    case RIGHT: x++; break;
	    case LEFT: x--; break;
	    case DOWN: y++; break;
  	    case UP: y--; break;
	    default: break;
	}
	
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

	//return getHead().hit(part);

	
        for(Part p : this.snakeParts) {
	    if (p.hit(part)) {
                return true;
            }
        }
        return false;
	
    }

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
