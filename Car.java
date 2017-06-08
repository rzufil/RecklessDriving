import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car {
	
	Image image;
	int x, y; // top-left corner
	boolean leftKey = false, rightKey = false, upKey = false, downKey = false;
	final static int SPEED = 6;
	
	public Car(int x1, int y1, Image i) {
		x = x1;
		y = y1;
		image = i;
	}

	public void setLeftKey(Boolean val) {
		leftKey = val;
	}

	public void setRightKey(Boolean val) {
		rightKey = val;
	}
	
	public void setUpKey(Boolean val) {
		upKey = val;
	}
	
	public void setDownKey(Boolean val) {
		downKey = val;
	}

	public void move() {
		if (leftKey && x > 140)
			x -= SPEED;
		if (rightKey && x < 408)
			x += SPEED;
		if (upKey && y > 20)
			y -= SPEED;
		if (downKey && y < 782)
			y += SPEED;
	}
	
	public boolean overlap(Obstacle obstacle) {
		if (obstacle.getX() + 68 <= getX() || obstacle.getX() >= getX() + 72 || obstacle.getY() + 170 <= getY() || obstacle.getY() >= getY() + 172) {
			return false;
		}
		return true;
	}

	public void render(GraphicsContext gc) {
		gc.drawImage(image, x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}