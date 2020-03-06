import java.util.Random;

/**
 *
 * @author josefiina
 * CLASS HANDLES GAME LOGIC
 */

public class Snakegame {
    private int width;
    private int heigth;
    private int score;
    private boolean basic;
    private Snake snake;
    private Apple apple;
    
    public Snakegame(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
	this.score = 0;
	this.basic = true; //DIES IF HITS A WALL
        this.snake = new Snake(this.width/2, 0, Direction.DOWN, this.width, this.heigth);
        setApple(new Apple(new Random().nextInt(this.width),
                new Random().nextInt(this.heigth)));
    }

    public void setBasicMode(boolean isBasic) { this.basic = isBasic; }

    public boolean getMode() { return this.basic; }

    public int getWidth() {
	return this.width;
    }

    public int getHeigth() {
	return this.heigth;
    }
    
    public Snake getSnake() { return this.snake; }
    
    public void setSnake(Snake snake) { this.snake = snake; }
    
    public Apple getApple() { return this.apple; }
    
    public void setApple(Apple apple) { this.apple = apple; }

    public int getScore() { return this.score; }

    public boolean end() { return (this.basic) ? basicEnd() : newEnd(); }

    public boolean newEnd() { return this.snake.hitsSelf(); }
    
    public boolean basicEnd() {
        Part part = snake.getHead();
        return ((part.getX()>this.width-1 || part.getX()<0) ||
                (part.getY()>this.heigth-1 || part.getY()<0)) ||
	        this.snake.hitsSelf();
    }
    
    public void refresh() {
	if (this.basic) { this.snake.basicMove(); }
	if (!this.basic) { this.snake.newMove(); }
        if (!snake.hit(apple)) { return; }
	
	snake.grow();
        this.score++;
        setApple(new Apple(new Random().nextInt(this.width),
                    new Random().nextInt(this.heigth)));
    }
}
