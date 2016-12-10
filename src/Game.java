import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Game.java
 *
 *
 *
 * @author Brandon Nguyen
 *
 * @version December 09, 2016
 *
 */
public class Game extends Canvas {

    // Use accelerate page flipping
    private BufferStrategy strategy;

    // Game is running?
    private boolean gameRunning = true;

    // List of all the entities that exists in game
    private ArrayList entities = new ArrayList();

    // List of entities that needs to be removed from game
    private ArrayList removeList = new ArrayList();

    // Entity representing player
    private Entity ship;

    // The speed of player (pixels/sec)
    private double moveSpeed = 300;

    // Time at which last fired a shot
    private long lastFire = 0;

    // Interval between players shot (ms)
    private long firingInterval = 500;

    // Number of aliends left on screen
    private int alienCount;

    // Message display
    private String message = "";

    private boolean waitingForKeyPress = true;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean firePressed = false;
    private boolean logicRequiredThisLoop = false;

    String shipImage = "/Users/brandonnguyen/Desktop/Space Invaders/src/Sprites/ship.gif";
    String alienImage = "/Users/brandonnguyen/Desktop/Space Invaders/src/Sprites/alien.gif";
    String shotImage = "/Users/brandonnguyen/Desktop/Space Invaders/src/Sprites/shot.gif";

    public Game() {

        // create a frame to contain the game
        JFrame container = new JFrame("Space Invaders");

        // get hold the content of the frame and set up the resolution
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);

        // setup canvas size and put it into the content of the frame
        setBounds(0, 0, 800, 600);
        panel.add(this);

        // Tell AWT not to bother repainting canvas
        setIgnoreRepaint(true);

        // make window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // Exit game when user closes window
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Respond to keys presses
        addKeyListener(new KeyInputHandler());

        // Request the focus so key events
        requestFocus();

        // Manage accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // Initialize entities
        initEntities();

    }

    private void startGame() {
        entities.clear();
        initEntities();

        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }

    private void initEntities() {

        ship = new ShipEntity(this, shipImage, 370, 550);
        entities.add(ship);

        alienCount = 0;
        for (int row = 0; row < 5; row ++) {

            for (int x = 0; x < 12; x ++) {
                Entity alien = new AlienEntity(this, alienImage, 100 + (x * 50), (50) + row * 30);
                entities.add(alien);
                alienCount++;
            }

        }

    }

    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    public void removeEntity (Entity entity) {
        removeList.add(entity);
    }

    public void notifyDeath() {
        message = "Oh no! They got you, try again?";
        waitingForKeyPress = true;
    }

    public void notifyWin() {
        message = "Well done! You win!";
        waitingForKeyPress = true;
    }

    public void notifyAlienKilled() {
        alienCount--;

        if (alienCount == 0) {
            notifyWin();
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AlienEntity) {

                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);

            }
        }
    }

    public void tryToFire() {

        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity (this, shotImage, ship.getX() + 10, ship.getY() - 30);
        entities.add(shot);
    }

    public void gameLoop() {

        long lastLoopTime = System.currentTimeMillis();

        while (gameRunning) {

            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 600);

        }

    }

    public static void main(String[] args) {
        new Game();
    }

}