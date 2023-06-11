
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Paddle extends Rectangle{

	 int yVelocity;
	 int speed = 10;
	 Socket player;
	 Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT,Socket ss){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		
		player =ss;

	}
	public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				try {
					OutputStream out = player.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);

					writer.println("Pup");
					writer=null;

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				try {
					OutputStream out = player.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);

					writer.println("Pdown");
					writer=null;

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
	}
	public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				try {
					OutputStream out = player.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);

					writer.println("Rup");
					writer=null;

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				try {
					OutputStream out = player.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);

					writer.println("Rdown");
					writer=null;

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
	}
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void movePUp(){
		setYDirection(-speed);
	}
	public void movePDown(){
		setYDirection(speed);
	}
	public void moveR(){
		setYDirection(0);
	}
	
	public void move() {
		y= y + yVelocity;
	}
	public void draw(Graphics g) {
			g.setColor(Color.red);
		g.fillRect(x, y, width, height);
	}
}