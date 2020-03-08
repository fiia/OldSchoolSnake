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

    public void buttonLayout(ArrayList<Button> btns, int numButtons,
			     HBox hbox, double width) {
	btns.stream().forEach(b -> {
	    b.setMinHeight(hbox.getPrefHeight());
	    b.setMinWidth(width/numButtons);
	    b.setFont(new Font("Impact", (grids * gridsize)/20));
	    b.setStyle("-fx-background-color:black;-fx-text-fill:white;-fx-border-style:solid;");
	    });
    }

    public void endButtons(ArrayList<Button>ebtns, AnchorPane ap) {
	HBox hboxEnd = new HBox();
	hboxEnd.setPrefSize(grids * gridsize, (grids * gridsize)/3);
	hboxEnd.setAlignment(Pos.CENTER);
	
	ebtns.stream().forEach(b -> {
    	    b.setFont(new Font("Impact", (grids * gridsize)/12));
	    b.setStyle("-fx-background-color:black;-fx-text-fill:white;-fx-border-style:solid;");
	    hboxEnd.getChildren().add(b);
	    });
        
       	ap.setBottomAnchor(hboxEnd, 0.0);
	ap.getChildren().addAll(hboxEnd);
    }

    public void showText(String text, GraphicsContext gc, Canvas canvas, boolean end) {
	double y = (text.length() > 20) ? canvas.getHeight()/3 : canvas.getHeight()/2;
	double fsize = (text.length() > 20) ? grids * gridsize/8 : grids * gridsize/6;
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


}
