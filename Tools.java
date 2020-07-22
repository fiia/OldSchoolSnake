import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.geometry.Pos;

import java.util.ArrayList;

/**
 * @author josefiina
 *
 * CLASS CONTAINS VISUAL TOOLS
 */

public class Tools {
    private int gridsize;
    private int grids;
    
    public Tools(int gridsize, int grids) {
	this.gridsize = gridsize;
	this.grids = grids;
    }

    public void buttonLayout(ArrayList<Button> btns, int numButtons, double width) {
	btns.stream().forEach(b -> {
	    b.setMinHeight(grids * gridsize/3);
	    b.setMinWidth(width/numButtons);
	    b.setFont(new Font("Impact", (grids * gridsize)/19));
	    b.setStyle("-fx-background-color:black;-fx-text-fill:white;-fx-border-style:solid;");
	    });
    }

    public void buttonLayout(Button b) {
	    b.setMinHeight(grids * gridsize/12);
	    b.setMinWidth(grids * gridsize);
	    b.setFont(new Font("Impact", (grids * gridsize)/25));
	    b.setAlignment(Pos.BASELINE_LEFT);
	    b.setStyle("-fx-background-color:black; -fx-text-fill:gray;-fx-border-style:solid;");
    }

    public ArrayList<Button> createModeButtons() {
	ArrayList<Button>mbtns = new ArrayList<Button>();
	mbtns.add(new Button("BASIC"));
	mbtns.add(new Button("NEW"));
	mbtns.add(new Button("TWO\nPLAYERS\nBASIC"));
	mbtns.add(new Button("TWO\nPLAYERS\nNEW"));
	buttonLayout(mbtns, mbtns.size(), grids*gridsize);
	return mbtns;
    }

    public ArrayList<Button> createSpeedButtons() {
	ArrayList<Button>sbtns = new ArrayList<Button>();
	sbtns.add(new Button("FAST"));
	sbtns.add(new Button("MID"));
	sbtns.add(new Button("SLOW"));
	buttonLayout(sbtns, sbtns.size(), grids*gridsize);
	return sbtns;
    }

    public ArrayList<Button> createEndButtons() {
	ArrayList<Button>ebtns = new ArrayList<Button>();
        ebtns.add(new Button("NEW GAME"));
	ebtns.add(new Button("MAIN MENU"));
	return ebtns;
    }

    public void endButtons(ArrayList<Button>ebtns, AnchorPane ap) {
	HBox hboxEnd = new HBox();
	hboxEnd.setPrefSize(grids * gridsize, (grids * gridsize)/3);
	hboxEnd.setAlignment(Pos.CENTER);
	
	ebtns.stream().forEach(b -> {
    	    b.setFont(new Font("Impact", (grids * gridsize)/12));
	    b.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-color:transparent;");
	    hboxEnd.getChildren().add(b);
	    });
        
       	ap.setBottomAnchor(hboxEnd, 0.0);
	ap.getChildren().addAll(hboxEnd);
    }

    public void showText(String text, GraphicsContext gc, Canvas canvas, boolean end) {
	double y = (text.length() > 20) ? canvas.getHeight()/3 : canvas.getHeight()/2;
	double fsize = (text.length() > 17) ? grids * gridsize/8 : grids * gridsize/6;
	if(!end) {
	    gc.setFill(Color.BLACK);
	    gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	}
	gc.setFill(Color.WHITE);
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setFont(new Font("Impact", fsize));
	gc.fillText(text, canvas.getWidth()/2, y);
    }

    public void drawBackround(Color color, GraphicsContext gc) {
		gc.setFill(color);
	        gc.fillRect(0, 0, grids * gridsize, grids * gridsize);
	    }

    public void drawSnake(Color color, Snake snake, GraphicsContext gc) {
		gc.setFill(color);
		snake.getParts().stream()
		    .forEach(part -> {
			gc.fillRect(part.getX() * gridsize,
				    part.getY() * gridsize,
				    gridsize, gridsize);
			});
      }

    public void drawApple(Color color, Snakegame snakegame, GraphicsContext gc) {
		 gc.setFill(color);
		 Apple apple = snakegame.getApple();
	         gc.fillRect(apple.getX() * gridsize,
			     apple.getY() * gridsize,
			     gridsize, gridsize);
     }

    public void drawDeadSnakes(Multisnake multisnake, GraphicsContext gc,
			       Canvas canvas) {
	if(multisnake.bothDie()) {
	    drawSnake(Color.GRAY, multisnake.getLuigi().getSnake(), gc);
	    drawSnake(Color.GRAY, multisnake.getMario().getSnake(), gc);
	    String endScore = "EVERYBODY\nDIES";
	    showText(endScore, gc, canvas, true);
	} else {
	    drawSnake(Color.GRAY, multisnake.getLoser().getSnake(), gc);
	    drawSnake(multisnake.getWinner().getColor(),
			    multisnake.getWinner().getSnake(), gc);
			
	    String endScore = "GAME OVER\nWINNER SCORE: " +
			multisnake.getWinner().getScore() + "\nLOSER SCORE: " +
			multisnake.getLoser().getScore();
	    showText(endScore, gc, canvas, true);			
	}
    }

}
