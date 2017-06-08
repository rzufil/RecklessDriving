import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Obstacle {
	
	Image image;
	int x, y; // top-left corner
	final static int SPEED = 6;
	
	public Obstacle(int x1, int y1, Image i) {
		x = x1;
		y = y1;
		image = i;
	}
	
	public void move() {
		y += SPEED;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void render(GraphicsContext gc) {
		gc.drawImage(image, x, y);
	}
	
	public void reset() {
		if (y >= RecklessDriving.HEIGHT) {
			y = -220;
		}
	}

	public boolean isActive() {
		if(y > -220) {
			return true;
		}
		return false;
	}

}