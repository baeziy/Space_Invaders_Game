// Importing AWT (Window) GUI's functions
import java.awt.Canvas; 				// For Canvas (Board)
import java.awt.Color; 					// Colors :v
import java.awt.Dimension; 				// Specifying Dimensions of objects
import java.awt.Graphics; 				// Graphics (Drawing)
import java.awt.Graphics2D; 			// 2-D Graphics (Entities)
import java.awt.GraphicsConfiguration; 	// Configure Graphics 
import java.awt.GraphicsEnvironment; 	// Show Graphics
import java.awt.event.KeyAdapter; 		// Inputs from Keyboard
import java.awt.event.KeyEvent; 		// Events through keyboard inputs
import java.awt.Image; 					// Image Display
import java.awt.image.BufferStrategy; 	// Allow multiple images on same window
import java.awt.image.BufferedImage; 	// Display multiple images on same window
import java.awt.Transparency; 			// Using given Images with 'tranparency', not opaque
import java.awt.Rectangle; 				// For 'Hit-Boxes' of entities
import java.awt.Toolkit;				// Tools to use images

import java.util.*; 					// Buint-in Java utilities

import javax.swing.JFrame; 				// Swing GUI for Frames
import javax.swing.JPanel; 				// Swing GUI for Panels
import javax.imageio.ImageIO; 			// To Locally allow images

import java.io.FileWriter;				// Write to files
import java.io.FileReader;				// Read from files
import java.io.BufferedReader;			// To read multiple lines from files
import java.io.IOException; 			// For Error handling

import java.net.URL; 					// To get proper animations

// Extending Pre-build canvas to make our board
public class Space_Invaders extends Canvas {
	BufferStrategy strategy; 				// to change movement speed
	boolean gameRunning = true; 			// Checking if game running
	ArrayList<Entity> entities = new ArrayList<Entity>(); 	// List of all the entities that exist in our game
	ArrayList<Entity> removeList = new ArrayList<Entity>();	// List of entities that need to be removed from the game this loop */
	Entity player; 							// The entity representing the player 
	double moveSpeed = 300;					// Player movement sleed
	long lastFire = 0; 						// The time at which last fired a bullet
	long firingInterval = 500;				// The interval between our players bullet (ms)
	int alienCount; 						// The number of aliens left on the screen
	int score; 								// Current Score
	int highscore;							// High Score from logs
	int level = 1;							// Level
	String message = "";					// The message to display which waiting for a key press
	boolean waitingForKeyPress = true;		// True if waiting for any key to be pressed
	boolean leftPressed = false;			// True if left key is pressed 
	boolean rightPressed = false;			// True if right key is pressed
	boolean firePressed = false;			// True if we are firing (spacebar)
	// True if game logic needs to be applied this loop, normally as a result of a game event
	boolean logicRequiredThisLoop = false; // Since in normal loops, entities are only moving

	// The main method which defines our: Menuframe, Panels, Initiates entities,
	public Space_Invaders()throws Exception { 
		JFrame container = new JFrame("Space Invaders"); // Create a frame to contain our game
		container.setSize(new Dimension(800, 635));
		// Setting an Icon and injecting to frame directly
		container.setIconImage(ImageIO.read(Space_Invaders.class.getResourceAsStream("images/logo.png")));
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setLocationRelativeTo(null);
		// Creating a Panel and setting up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setSize(new Dimension(800,635));
		//panel.setLayout(null);
		
		// Setting up board size, and adding it to the panel -> frame
		setBounds(0,0,800,635);
		panel.add(this);

		// Setting up so it doesn't clear the frame every loop (Ignoring re-paint)
		setIgnoreRepaint(true);
		
		// Display the Window
		//container.pack();
		container.setResizable(false); // Resizing of window disabled
		container.setVisible(true);
		
		// If User closes the GUI, stop the program (else it will keep running in background)
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addKeyListener(new KeyInputHandler()); // Defining an input handler 
		requestFocus(); // Focus on the latest key that is pressed
		createBufferStrategy(2); // To allow multiple movements 
		strategy = getBufferStrategy(); 
		
		initEntities(); // Initiallizing Entities for the start
		//this.gameLoop();
	}
	
	void startGame() { // Start game :V
		// clear out any existing entities and intialize a new game or level 
		entities.clear();
		initEntities();

		// Blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	void initEntities() { // Setting up where the entities will be displayed initially

		// Create the player player and place it roughly in the center of the screen
		player = new PlayerShip(this,"images/player.gif",370,550);
		entities.add(player); // Adding to entities as player
		
		// Create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		alienCount = 0;
		for (int row=0; row<5; row++) {
			for (int x=0; x<12; x++) {
				Entity alien = new Invaders(this,"images/alien.gif",100+(x*50),(50)+row*30);
				entities.add(alien);
				alienCount++;
			}
		}
	}

	// Updating after an event
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	// If alein is killed, remove its entity
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	// If player is killed
	public void notifyDeath() {
		message = "Game Over, you lost. try again?";
		waitingForKeyPress = true;
	}

	// If all enemies are slain (player wins)
	public void notifyWin() {
		message = "Well done! " + level++ + " passed!";
		waitingForKeyPress = true;

	}
	/* If alein is killed: I
	Increase score (and manage highscore if needed) 
	Decrease the amount of aliens
	Speed up rest of the aliens by 2%
	If all Aliens are slain, proceed to next level
	*/
	public void notifyAlienKilled() {
		// reduce the alient count
		alienCount--; // Decrease Aliens
		score++; // Increase Score
		// Managing High Score
		FileWriter w;
		try {
			FileReader file = new FileReader("High_Score.txt"); // Read file
			BufferedReader readerr = new BufferedReader(file); // Read String
			highscore =	Integer.parseInt(readerr.readLine()); // Convert String to Int
			readerr.close(); // Close the reader
			if(highscore < score){ // If highscore is beaten
				highscore = score; // Setting highscore as score
				w = new FileWriter("High_Score.txt"); // Open the logs for highscore
				w.write(Integer.toString(score)); // Converting Int score to string and writing
				w.flush(); // Cleaning the writer
				w.close(); // Closing the writer
			}
		} catch (IOException e) {} // In case some error while file handling
	
		if (alienCount == 0) {
			notifyWin(); // If there's no Alien left, proceed win (next level)
		}
		// Speed up the remaining aliens
		for (int i=0;i<entities.size();i++) { // Looping for amount of remaining aliens
			Entity entity = (Entity) entities.get(i); // Getting objects of all aliens
			if (entity instanceof Invaders) { // If its an alien
				double speed_incr = 1.02 + (level * 0.01); // speed up by 2% per kill and 1% overall per level
				entity.setHorizontalMovement(entity.getHorizontalMovement() * speed_incr); // Setting speed
			}
		}
	}

	public void tryToFire() { // Setting a proper duration between firing for player
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return; // Checking that we have waiting long enough to fire
		}		
		// Else: create the bullet entity, and record the time
		lastFire = System.currentTimeMillis(); // Recording time
		Bullett bullet = new Bullett(this,"images/bullet.gif",player.getX()+10,player.getY()-30); // Creating bullet entity 		
		entities.add(bullet); // Adding to list of entities
	}
	
	/*
	The main game loop. This loop is running during all game
	The things it does:
	1) Moving the game entities
	2) Drawing the screen contents (entities, text)
	3) Updating game events
	4) Checking Input
	*/
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis(); // Start loop time
		// keep looping round till the game ends
		while (gameRunning) {
			// Display background image
			Image img = Toolkit.getDefaultToolkit().getImage("images/game-space.jpg");

			long time_speed = System.currentTimeMillis() - lastLoopTime; // Calculating time and limiting it to keep entities on board
			lastLoopTime = System.currentTimeMillis(); 
			// Over-loading the GUI with graphic GUI (for games)
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black); // Setting color
			g.fillRect(0,0,800,635); // Fill the board with the graphics (black)
			
			// Looping through entities after game-start forcing them to move per commands
			if (!waitingForKeyPress) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);	
					entity.move(time_speed);
				}
			}
			g.drawImage(img, 0, 0, null); // Draw the background image
			g.setColor(Color.white); // Setting to white color for text
			g.drawString("Made by: ",645,585);
			g.drawString("- Abdullah Mehtab & Mustafa Kamal",585,595);
			g.drawString("High Score: " + highscore ,50,25);
			g.drawString("Current Score: " + score ,635,25);

			// Drawing all entities in the game depending on what they are (Player/Alien/Bullet)
			for (int i=0;i<entities.size();i++) {
				Entity entity = (Entity) entities.get(i);
				entity.draw(g);
			}
			
			/* Menu Collision detector
			Notify if every uniquely defined entity has 
			collided with the other
			Basic logic:
			If Bullet and Alien collide, both disappear (Alien dies)
			If Alien and Player collide, Player loses
			Other collisions are impossible
			*/
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
			
			// Remove any entity that has been marked
			entities.removeAll(removeList);
			removeList.clear(); // Clearing the remove-list

			// If Entity logic is requested, do it (for each)
			if (logicRequiredThisLoop) { // Used for collisions
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}
				logicRequiredThisLoop = false; // Set it to false for future loops unless called again
			}
			
			// If need to wait for any key to be pressed, display message
			if (waitingForKeyPress) {
				g.setColor(Color.white);
				g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
			}

			g.dispose(); // Clear the graphic history
			strategy.show(); // Show to the GUI
		
			// INPUTS FROM USER
			// Setting initial movement to 0
			player.setHorizontalMovement(0);
			
			// If left -> move left    If right -> move right
			if ((leftPressed) && (!rightPressed)) {
				player.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				player.setHorizontalMovement(moveSpeed);
			}
			
			// if spacebar, attempt to fire
			if (firePressed) {
				tryToFire();
			}
			
			// Adding 10 milli-second pause between every loop (else it'll be too fast)
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}

	class KeyInputHandler extends KeyAdapter {
		int pressCount = 1;	// To detect if a new game is started, or a new level
		
		public void keyPressed(KeyEvent e) { // Key is pressed (not clicked) [Can be held]
			// If 'Any key', Allow any key
			if (waitingForKeyPress) {
				return;
			}
			// If left key is pressed
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			// If right key is pressed
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			// If spacebar is pressed
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		} 

		public void keyReleased(KeyEvent e) { // If the pressed key is released (to stop actions)
			// If 'Any key', Allow any key
			if (waitingForKeyPress) {
				return;
			}
			// If left key is released
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			// If right key is released
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			// If spacebar is released
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		public void keyTyped(KeyEvent e) {
			// If 'Any key', Allow any key
			if (waitingForKeyPress) {
				if (pressCount == 1) { 
					// Start new game in case if first time
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else { // Increase amount and let it continue
					pressCount++;
				}
			}
			// If Escape, Quit game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	public static void main(String argv[]) throws Exception {
		Space_Invaders g = new Space_Invaders(); // Making object
		g.gameLoop(); // Run the game (object)
	}
}
// Defining a defaut class for different types of entities (Player/Alien/Bullet)
abstract class Entity {
	protected double x; // x-coordinate of entity
	protected double y; // y-coordinate of entity
	protected Animate animation; // Animation for the entity
	protected double dx; // x-axis speed for the enitity (pixels per second)
	protected double dy; // y-axis speed for the enitity (pixels per second)

	// Hitboxes (to detect collisions)
	Rectangle me = new Rectangle();	// The area used for this entity during collisions
	Rectangle him = new Rectangle(); // The area used for other entities during collision
	
	public Entity(String ref,int x,int y) { // ref = defining which type of entity, x-y are coordinates
		this.animation = Animation.get().getAnimate(ref); // Composition with Animate
		this.x = x;
		this.y = y;
	}
	
	public void move(long time_speed) { // time_speed is the movement speed
		// update the location of the entity based on move speeds
		x += (time_speed * dx) / 1000;
		y += (time_speed * dy) / 1000;
	}

	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}

	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}

	public double getHorizontalMovement() {
		return dx;
	}

	public double getVerticalMovement() {
		return dy;
	}

	public void draw(Graphics g) { // Draw the defined entity 
		animation.draw(g,(int) x,(int) y);
	}
	
	public void doLogic() { // Do logic provided on collisons
	}
	
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}
	
	// Check if this entity collised with 'other', 'other' represents the other entity
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x,(int) y,animation.getWidth(),animation.getHeight());
		him.setBounds((int) other.x,(int) other.y,other.animation.getWidth(),other.animation.getHeight());
		return me.intersects(him);
	}
	
	// Notification that this entity collided with other.
	public abstract void collidedWith(Entity other);
}
// Defining the Aliens / Invaders
class Invaders extends Entity {
	double moveSpeed = 75; // Horizontal movement speed
	Space_Invaders game; // The game where it is being used

	// Attributes already defined in abstract class
	public Invaders(Space_Invaders game,String ref,int x,int y) {
		super(ref,x,y);
		this.game = game;
		dx = -moveSpeed;
	}

	// Move the invader entity
	public void move(long time_speed) { // The time that has elapsed since last move = time_speed
		// If we have reached the left hand side of the screen and are moving left then request a logic update 
		if ((dx < 0) && (x < 10)) {
			game.updateLogic();
		}
		// and vice vesa, if we have reached the right hand side of the screen and are moving right, request a logic update
		if ((dx > 0) && (x > 750)) {
			game.updateLogic();
		}
		// proceed with normal move
		super.move(time_speed);
	}
	
	// Update the game logic related to aliens
	public void doLogic() {
		// Wwap over horizontal movement and move down the screen a bit
		dx = -dx;
		y += 10;
		// If Invaders reach the bottom of the screen then the player dies
		if (y > 570) {
			game.notifyDeath();
		}
	}
	
	// Notify if this alien has collided with another entity
	public void collidedWith(Entity other) {
		// Collisions with aliens are handled elsewhere
	}
}

// Define the player's ship
class PlayerShip extends Entity {
	Space_Invaders game; // The game where it is being used

	// Attributes already defined in abstract class
	public PlayerShip(Space_Invaders game,String ref,int x,int y) {
		super(ref,x,y);
		this.game = game;
	}
	
	// Move the player-ship entity
	public void move(long time_speed) { // Speed of the ship's movement
		// If moving left and have reached the left side of the screen, disable movement
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// If moving right and have reached the right side of the screen, disable movement
		if ((dx > 0) && (x > 750)) {
			return;
		}
		// Continue normally
		super.move(time_speed);
	}
	
	// Notify if the player has collided with another entity
	public void collidedWith(Entity other) {
		// If its an alien, notify that the player is dead
		if (other instanceof Invaders) {
			game.notifyDeath();
		}
	}
}

// Define Bullets
class Bullett extends Entity {
	double moveSpeed = -300; // Vertical speed of bullet (negative to move up)
	Space_Invaders game; // The game where it is being used
	boolean used = false; // True if this bullet has been "used", i.e. it hit something 

	// Attributes already defined in abstract class
	public Bullett(Space_Invaders testy,String animation,int x,int y) {
		super(animation,x,y);
		this.game = testy;
		dy = moveSpeed;
	}

	// Moving the bullet
	public void move(long time_speed) {
		// Move with normal speed
		super.move(time_speed);
		// If the bullet goes off the screen, remove it
		if (y < -100) {
			game.removeEntity(this);
		}
	}
	
	// Bullet's collision
	public void collidedWith(Entity other) {
		// Prevents double kills, if we've already hit something don't collide
		if (used) {
			return;
		}
		// If Alien is hit, remove it
		if (other instanceof Invaders) {
			// Remove the affected entities
			game.removeEntity(this); // Removing the bullet
			game.removeEntity(other); // Removing the alien
			
			// Notify the game that the alien has been killed
			game.notifyAlienKilled();
			used = true;
		}
	}
}

// Controls animations in graphics
class Animate {
	Image image; //The image that needs to be drawn/animated
	public Animate(Image image) { // Create a new animation based on an image
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth(null);
	}

	public int getHeight() {
		return image.getHeight(null);
	}

	public void draw(Graphics g,int x,int y) { // Graphic is the image need to be drawn for GUI, x-y are coordinates
		g.drawImage(image,x,y,null);
	}
}

// Animation for specific entity
class Animation {
	// Creating instance of this class
	static Animation single = new Animation();
	
	// Getting instance of this class
	public static Animation get() {
		return single;
	}
	
	// Creating hashmap for each entity and its name/state of animation
	HashMap<String,Animate> animations = new HashMap<String,Animate>();
	 
	public Animate getAnimate(String ref) {	// "ref" is the reference to the image to use for the animation
		// if we've already got the animation in the cache then just return and re-use the existing version
		if (animations.get(ref) != null) {
			return (Animate) animations.get(ref);
		}
		
		// otherwise, go away and grab the animation from the resource loader
		BufferedImage sourceImage = null;
		
		try {
			/* 
			Using ClassLoader.getResource() to do animation
			Note: I don't know the details of this, but it is required to use images as drawn graphics to be animated
				  since it holds the pixels together automatically
			*/
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				fail("Can't find ref: "+ref);
			}
			// use ImageIO to read the image in
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: "+ref);
		}
		
		// Create an Drawn Image Graphic of the right size to store our animation in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		// Create the Image using the given information
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		// Draw our given image into the animated image
		image.getGraphics().drawImage(sourceImage,0,0,null);
		// Create a animation, add it the cache then return it
		Animate animation = new Animate(image);
		animations.put(ref,animation);
		return animation;
	}
	
	void fail(String message) { // In case animations fails to happen
		// Almost never happens, just for sake of error_handling 
		// Since this is not a defined error, but a complex one
		// Simply print the message, and exit the game
		System.err.println(message);
		System.exit(0);
	}
}
