import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Assignment 4
 * Name: Reckless Driving
 * Platform: PC
 * Description: Change lanes to avoid ongoing cars while you drive your black Viper recklessly.
 * Controls: W (move car position up), S (move car position down), A (move car left), D (move car right)
 * Objective: Drive without hitting any cars, the farther you go the more points you get.
 * @author Rafael Leite
 * @version Mar 2016
 */

public class RecklessDriving extends Application {
	
	final String appName = "Reckless Driving";
	final int FPS = 30; // frames per second
	final static double WIDTH = 640;
	final static double HEIGHT = 964;
	Car mainCar;
	Obstacle otherCar1, otherCar2, otherCar3;
	Image road, car, car2, car3, car4;
	int lane;
	double score = 0.0;
	Random rand = new Random();
	boolean active = true;
	long timeStart = System.currentTimeMillis();
	Font font = Font.font("Razer Regular", 18);
	
	/**
	 * Set up initial data structures/values
	 */
	void initialize() {
		road = new Image("road.png");
		car = new Image("viper.png");
		car2 = new Image("audi.png");
		car3 = new Image("audi.png");
		car4 = new Image("audi.png");
		mainCar = new Car(278, 530, car);
		otherCar1 = new Obstacle(158, -220, car2);
		otherCar2 = new Obstacle(278, -220, car2);
		otherCar3 = new Obstacle(396, -220, car2);
		lane = rand.nextInt(3) + 1;
		switch (lane) {
			case 1:
				otherCar1.move();
				break;
			case 2:
				otherCar2.move();
				break;
			case 3:
				otherCar3.move();
				break;
			default:
				break;
		}
	}
	
	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(
			e -> {
				switch (e.getCode())
				{
					case W:
						mainCar.setUpKey(true);
						break;
					case A:
						mainCar.setLeftKey(true);
						break;
					case S:
						mainCar.setDownKey(true);
						break;
					case D:
						mainCar.setRightKey(true);
						break;
					default:
						break;
				}
			}
		);
		scene.setOnKeyReleased(
				e -> {
					switch (e.getCode())
					{
						case W:
							mainCar.setUpKey(false);
							break;
						case A:
							mainCar.setLeftKey(false);
							break;
						case S:
							mainCar.setDownKey(false);
							break;
						case D:
							mainCar.setRightKey(false);
							break;
						default:
							break;
					}
				}
			);
		
	}

	/**
	 *  Update variables for one time step
	 */
	public void update() {
		mainCar.move();
		score = score + 0.5;
		if (otherCar1.isActive()) {
			otherCar1.move();
			otherCar1.reset();
		}
		if (otherCar2.isActive()) {
			otherCar2.move();
			otherCar2.reset();
		}
		if (otherCar3.isActive()) {
			otherCar3.move();
			otherCar3.reset();
		}
		if ((otherCar1.getY() > 426 && !otherCar2.isActive() && !otherCar3.isActive()) || (otherCar2.getY() > 426 && !otherCar1.isActive() && !otherCar3.isActive()) || (otherCar3.getY() > 426 && !otherCar2.isActive() && !otherCar1.isActive())) {
			lane = rand.nextInt(3) + 1;
			switch (lane) {
				case 1:
					otherCar1.move();
					break;
				case 2:
					otherCar2.move();
					break;
				case 3:
					otherCar3.move();
					break;
				default:
					break;
			}
		}
		gameOver();
	}
	

	/**
	 *  Draw the game world
	 */
	void render(GraphicsContext gc) {
		if (gameOver()) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0.0, HEIGHT/2 + 20, WIDTH, 20);
			gc.setFill(Color.WHITE);
			gc.setFont(font);
			gc.fillText("Game Over", WIDTH/2 - 58, HEIGHT/2 + 36);
		}
		else {
			gc.setFill(Color.GREEN);
			gc.fillRect(0.0, 0.0, WIDTH, HEIGHT);
			gc.drawImage(road, 0, 20);
			mainCar.render(gc);
			otherCar1.render(gc);
			otherCar2.render(gc);
			otherCar3.render(gc);
			gc.setFill(Color.BLACK);
			gc.fillRect(0.0, 0.0, WIDTH, 20.0);
			gc.setFill(Color.WHITE);
			gc.setFont(font);
			long timePassed = System.currentTimeMillis() - timeStart;
			gc.fillText("score: " + (int)score, 18, 16);
			gc.fillText("time: "  + (new SimpleDateFormat("mm:ss")).format(new Date(timePassed)), 528, 16);
		}
		
	}
	
	private boolean gameOver() {
		if(mainCar.overlap(otherCar1) || mainCar.overlap(otherCar2) || mainCar.overlap(otherCar3)) {
			active = false;
			return true;
		}
		return false;
	}

	/*
	 * Begin boiler-plate code...
	 * [Animation and events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();
		setHandlers(theScene);
		
		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					if(active == true) {
						// update position
						update();
						// draw frame
						render(gc);
					}
				}
			);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
	
}