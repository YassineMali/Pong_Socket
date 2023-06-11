
import java.awt.*;
public class BootPaddle extends Rectangle{

	 int yVelocity;
	 int speed = 10;
	 
	 BootPaddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
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
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void move() {
		y= y + yVelocity;
	}
	public void draw(Graphics g) {
			g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
	}
}