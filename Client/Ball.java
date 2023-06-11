import java.awt.*;

public class Ball extends Rectangle{

	 int xVelocity;
	 int yVelocity;
	 int initialSpeed = 2;
	
	 Ball(int x, int y, int width, int height){
		super(x,y,width,height);
		
		int randomXDirection = 1;
        
		if(randomXDirection == 0)
			randomXDirection--;
		setXDirection(randomXDirection*initialSpeed);
		
		int randomYDirection = 1;
		if(randomYDirection == 0)
			randomYDirection--;
		setYDirection(randomYDirection*initialSpeed);
		
	}
	
	public void setXDirection(int randomXDirection) {
		xVelocity = randomXDirection;
	}
	public void setYDirection(int randomYDirection) {
		yVelocity = randomYDirection;
	}
	public void move() {
		x += xVelocity;
		y += yVelocity;
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}

	
}