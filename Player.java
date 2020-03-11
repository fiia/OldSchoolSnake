import javafx.scene.paint.Color;

/*
 * @author josefiina
 *
 */

public class Player {
    private int score;
    private Snake snake;
    private boolean winner;
    private Color color;

    public Player(int startX, int startY, Direction startDirection,
		  int width, int heigth, Color color) {
	this.score = 0;
	this.snake = new Snake(startX, startY, startDirection, width, heigth);
	this.winner = false;
	this.color = color;
    }

    public int getScore() { return this.score; }

    public void scoreUp() { this.score++; }

    public void win(boolean w) { this.winner = w; }

    public boolean getWin() { return this.winner; }

    public Snake getSnake() { return this.snake; }

    public Color getColor() { return this.color; }

    public void reset(int startX, int startY, Direction startDirection) {
	this.score = 0;
	this.snake.reset(startX, startY, startDirection);
	this.winner = false;
    }
}
