/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
/* http://cs.stanford.edu/people/eroberts/jtf/index.html */

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  On some platforms 
  * these may NOT actually be the dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  On some platforms these may NOT actually
  * be the dimensions of the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private static final int DELAY = 10;


/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */

		int life = NTURNS;
		while (life > 0){
			counter = 100;
			setupBrick();
			setupPaddle();
			playball();
			feedback();
			life = life - 1;
			waitForClick();
			removeAll();
		}
		GLabel flabel = new GLabel("No Turns Left!");
		flabel.setFont("SansSerif-36");
		add(flabel,  WIDTH / 2 - flabel.getWidth() / 2, HEIGHT / 2);
	}
	private void playball(){
		setupball();
		vx = rgen.nextDouble(1.0, 3.0); 
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 3.0;
		GLabel stlabel = new GLabel("Click to start!");
		stlabel.setFont("SansSerif-36");
		stlabel.setColor(Color.BLUE);
		add(stlabel,  WIDTH / 2 - stlabel.getWidth() / 2, HEIGHT / 2);
		waitForClick();
		remove(stlabel);
		while(!gameover()){
			moveball();
			checkCollision();
		}
	}
	private boolean gameover(){
		return (ball.getY() > HEIGHT - BALL_RADIUS) || counter == 0;
		/*return counter == 0;*/
	}
	private void feedback(){
		if (counter == 0){
			label = new GLabel("YOU WON!");
			label.setFont("SansSerif-36");
			label.setColor(Color.RED);
			add(label,  WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2);
		}
		else {
			label = new GLabel("YOU LOST!");
			label.setFont("SansSerif-36");	
			add(label, WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2);
		}
	}
			
	private void setupBrick (){
		/* set up Bricks*/
		double x = WIDTH / 2 - BRICK_SEP /2 - 5 * BRICK_WIDTH - 4 * BRICK_SEP;
		double y = BRICK_Y_OFFSET;
		prepBrick(x, y, Color.RED);
		prepBrick(x, y + 2 * (BRICK_HEIGHT + BRICK_SEP), Color.ORANGE);
		prepBrick(x, y + 4 * (BRICK_HEIGHT + BRICK_SEP), Color.YELLOW);
		prepBrick(x, y + 6 * (BRICK_HEIGHT + BRICK_SEP), Color.GREEN);
		prepBrick(x, y + 8 * (BRICK_HEIGHT + BRICK_SEP), Color.CYAN);
	}
	private void prepBrick(double x, double y, Color c){
		for (int i = 0; i < 2; i++) {
			addBrick(x, y, c);
			y = y + BRICK_HEIGHT + BRICK_SEP;
		}
	}
	private void addBrick(double x, double y, Color c){
		for (int j = 0; j < 10; j++) {
			
			brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setColor(c);
			brick.setFilled(true);
			brick.setFillColor(c);
			add(brick);
			x = x + BRICK_WIDTH + BRICK_SEP;
		}
	}
	private void setupPaddle() {
		double x = (WIDTH - PADDLE_WIDTH) / 2;
		double y = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
		addMouseListeners();
	}
	public void mouseMoved (MouseEvent e){
		double mouseX = e.getX();
		movePaddle(mouseX, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		/*movePaddle(mouseX, BRICK_Y_OFFSET)*/;
	}
	private void movePaddle(double x, double y){
		if (x < 0)
			paddle.setLocation(0, y);
		else if (x > WIDTH - PADDLE_WIDTH)
			paddle.setLocation(WIDTH - PADDLE_WIDTH, y);
		else
			paddle.setLocation(x, y);
	}
	
	private void setupball(){
		double x = WIDTH / 2 - BALL_RADIUS;
		double y = BRICK_Y_OFFSET + 10 * BRICK_HEIGHT + 9 * BRICK_SEP;
		ball = new GOval(x, y, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}
	private void moveball(){
		ball.move(vx, vy);
		if (counter > 90)
			pause(2 * DELAY);
		else if (counter < 90 || counter > 50)
			pause(DELAY);
		if (ball.getX() > WIDTH - 2 * BALL_RADIUS)
			vx = - vx;
		else if (ball.getX() < 0)
			vx = - vx;
		else if (ball.getY() > HEIGHT - BALL_RADIUS)
			vy = - vy;
		else if (ball.getY() < 0)
			vy = - vy;
		
	}
	
	private void checkCollision(){
		GObject cllider = getCollidingObject();
		if (cllider == paddle){
			vy = - vy;
		}
		else if (cllider != null){
			vy = - vy;
			remove(cllider);
			bounceClip.play();
			counter = counter - 1;
		}
	}
	
	private GObject getCollidingObject(){
		double x = ball.getX();
		double y = ball.getY();
		if (y > paddle.getY())
			return null;
		GObject collObj = getElementAt(x, y);
		if (collObj == null){
			x = x + 2 * BALL_RADIUS;
			collObj = getElementAt(x, y);
		}
		if (collObj == null){
			y = y + 2 * BALL_RADIUS;
			collObj = getElementAt(x, y);
		}
		if (collObj == null){
			x = x - 2 * BALL_RADIUS;
			collObj = getElementAt(x, y);
		}
		if (collObj == null)
			return null;
		else
			return collObj;
	}
	/*private instant variable*/
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private GLabel label;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx;
	private double vy;
	private int counter;
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
}
