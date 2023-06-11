
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	static final List<String> MOVES = new ArrayList<String>(Arrays.asList("Rup", "Rdown", "Pup", "Pdown","BRup", "BRdown", "BPup", "BPdown"));
	Thread gameThread;
	Image image;
	Graphics graphics;
	//Random random;
	Paddle paddle1;
	BootPaddle paddle2;
	Ball ball;
	Score score;
	Socket server;
	int id;
	GamePanel(Socket socket) throws IOException{
		server = socket;
	
		InputStream inputStream = server.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		id=dataInputStream.readInt();
		dataInputStream=null;
		inputStream=null;

		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH,GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newBall() {
		
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER/2),BALL_DIAMETER,BALL_DIAMETER);
		
	}
	public void newPaddles() {
		if(id==1){
			paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,server);
			paddle2 = new BootPaddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT);
		}
		if(id==2){
			paddle1 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,server);
			paddle2 = new BootPaddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT);
		}
	}
	public void paint(Graphics g) {
		ImageIcon backgroundImageIcon = new ImageIcon("pong.jpg");
        image= resizeImage(backgroundImageIcon.getImage(), getWidth(),getHeight());
		//image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
Toolkit.getDefaultToolkit().sync(); // I forgot to add this line of code in the video, it helps with the animation

	}
	public void move() {
		ball.move();
		paddle1.move();
		paddle2.move();
		
	}
	private Image resizeImage(Image image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

	public void checkCollision() {
		try {
		//bounce ball off top & bottom window edges
		if(ball.y <=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//bounce ball off paddles
		if(id==1){
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}

		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}}else{
			if(ball.intersects(paddle1)) {
				ball.xVelocity = Math.abs(ball.xVelocity);
				ball.xVelocity++; //optional for more difficulty
				if(ball.yVelocity>0)
					ball.yVelocity++; //optional for more difficulty
				else
					ball.yVelocity--;
				ball.setXDirection(-ball.xVelocity);
				ball.setYDirection(ball.yVelocity);
			}
			if(ball.intersects(paddle2)) {
				ball.xVelocity = Math.abs(ball.xVelocity);
				ball.xVelocity++; //optional for more difficulty
				if(ball.yVelocity>0)
					ball.yVelocity++; //optional for more difficulty
				else
					ball.yVelocity--;
				ball.setXDirection(ball.xVelocity);
				ball.setYDirection(ball.yVelocity);
			}
		}
		//stops paddles at window edges
		if(paddle1.y<=0)
			paddle1.y=0;
		if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		if(paddle2.y<=0)
			paddle2.y=0;
		if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		//give a player 1 point and creates new paddles & ball
	
		if(ball.x <=0) {
			//score.player2++;

			sendGoal(server,"p2");

		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			//score.player1++;

			sendGoal(server,"p1");

		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public void run() {
		//game loop
		try {
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();
				InputStream in = server.getInputStream();
				if(in.available()>0)
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String move = reader.readLine();
						System.out.println(move);
					switch(move) {
						case "Pup": paddle1.movePUp();
								break;
						case "Pdown": paddle1.movePDown();
								break;
						case "Rup": paddle1.moveR();
								break;
						case "Rdown": paddle1.moveR();
								break;
						case "BPup": paddle2.movePUp();
								break;
						case "BPdown": paddle2.movePDown();
								break;
						case "BRup": paddle2.moveR();
								break;
						case "BRdown": paddle2.moveR();
								break;
						case "resetp1":score.player1++;
									newBall();
									newPaddles();
									System.out.println("get goal 2");	
								break;
						case "resetp2":score.player2++;
									newBall();
									newPaddles();
									System.out.println("get goal 2");	
							break;	
					}
					reader=null;
				}
				in=null;
				repaint();
				//getGoal(server);
				checkCollision();
				
				delta--;
				
					
				
			}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public void sendGoal(Socket server_,String player) throws IOException{
		OutputStream out = server_.getOutputStream();
		PrintWriter writer = new PrintWriter(out, true);

		writer.println("goal"+player);
		writer=null;

	}

	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
		}
	}
}