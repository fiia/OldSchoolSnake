import java.util.Random;

/**
 *
 * @author josefiina
 */

public class Snakegame {
    private int width;
    private int heigth;
    private int score;
    private Snake snake;
    private Apple apple;
    
    public Snakegame(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
	this.score = 0;
        this.snake = new Snake(this.width/2, 0, Direction.DOWN);
        setApple(new Apple(new Random().nextInt(this.width),
                new Random().nextInt(this.heigth)));
    }
    
    public Snake getSnake() { return this.snake; }
    
    public void setSnake(Snake snake) { this.snake = snake; }
    
    public Apple getApple() { return this.apple; }
    
    public void setApple(Apple apple) { this.apple = apple; }

    public int getScore() { return this.score; }
    
    public boolean end() {
        Part part = snake.getHead();
        return ((part.getX()>this.width-1 || part.getX()<0) ||
                (part.getY()>this.heigth-1 || part.getY()<0)) ||
	    this.snake.hitsSelf();
    }
    
    public void refresh() {
        this.snake.move();
        
        if (snake.hit(apple)) {
            snake.grow();
	    this.score++;
            setApple(new Apple(new Random().nextInt(this.width),
                    new Random().nextInt(this.heigth)));
        }
    }
}
