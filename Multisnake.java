import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author josefiina
 * CLASS HANDLES TWO PLAYER GAME LOGIC
 */

public class Multisnake extends Snakegame {
    private Player luigi;
    private Player mario;
    
    public Multisnake(int width, int heigth) {
	super(width, heigth);
	this.luigi = new Player(0, 0, Direction.DOWN, width, heigth, Color.GREEN);
	this.mario = new Player(this.width-1, this.heigth-1, Direction.UP,
				width, heigth, Color.BLUE);
        
    }

    public Player getWinner() {
	return (luigi.getWin()) ? luigi : mario;
    }
    
    public Player getLoser() {
	return (luigi.getWin()) ? mario : luigi;
    }

    public Player getLuigi() { return this.luigi; }

    public Player getMario() { return this.mario; }

    @Override
    public boolean newEnd() {
	mario.win(this.luigi.getSnake().hitsSelf());
	luigi.win(this.mario.getSnake().hitsSelf());

	playerHitsPlayer(mario, luigi);
	playerHitsPlayer(luigi, mario);
	
	return (luigi.getWin() || mario.getWin());
    }

    @Override
    public boolean end() { return (this.basic) ? basicEnd() : newEnd(); }

    @Override
    public boolean basicEnd() {
	mario.win(playerHitsWall(luigi));
        luigi.win(playerHitsWall(mario));

	return (luigi.getWin() || mario.getWin()) ? true : newEnd();
    }

    public boolean playerHitsWall(Player one) {
	Part part = one.getSnake().getHead();
        return ((part.getX()>this.width-1 || part.getX()<0) ||
                (part.getY()>this.heigth-1 || part.getY()<0));
    }

    public void playerHitsPlayer(Player one, Player two) {
	for(Part p : one.getSnake().getParts()) {
	    if (two.getSnake().getHead().hit(p)) {
		    one.win(true);
	    }    
	}
    }

    public boolean eatsApple(Player one) {
	if (one.getSnake().hit(apple)) {
	    one.getSnake().grow();
	    one.scoreUp();
	    return true;
	}
	return false;
    }

    @Override
    public void refresh() {
	if (this.basic) {
	    luigi.getSnake().basicMove();
	    mario.getSnake().basicMove();
	} else {
	    luigi.getSnake().newMove();
	    mario.getSnake().newMove();
	}
	
        if (!(eatsApple(luigi) || eatsApple(mario))) { return; }

        setApple(new Apple(new Random().nextInt(width),
                    new Random().nextInt(heigth)));
    }

    
}
