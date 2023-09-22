package org.example;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A 2d shooting game where you have to
 * survive multiple wave of enemies.
 *
 * @author Gareth Ng
 * @version 1.0
 */
public class Window extends PApplet {

 private PImage bulletSprite;

 private PImage enemySprite;

 private PImage playerSprite;

 private PImage background;

 private Enemy enemy;

 private Player player;

 private int enemySize = 20;

 private int playerSize = 15;

 private float bulletSpeed = 6f;

 private int bulletSize = 30;

 private int numberEnemy = 1000;

 private PVector center;

 private PFont font;

 private int playerScore = 0;

 private int gameStatus = 0;

 private boolean scoreSend;

 private boolean mouseClicked;

 private String typing = "";

 private String saved = "";

 private final ArrayList<Enemy> enemyArr = new ArrayList<>();

 private final ArrayList<Bullet> bullets = new ArrayList<Bullet>();

 private final ArrayList<ICollidable> collidables = new ArrayList<>();

 private final ArrayList<Enemy> dead = new ArrayList<>();

 private UserData user;


 /**
  * Initialize all relevant variables.
  */
 public void setup() {
  background = loadImage("org.bcit.comp2522.labs.projectGJ\\img\\space.jpg");
  background.resize(900, 900);
  enemySprite = loadImage("org.bcit.comp2522.labs.projectGJ\\img\\invader1.png");
  playerSprite = loadImage("org.bcit.comp2522.labs.projectGJ\\img\\player.png");
  bulletSprite = loadImage("org.bcit.comp2522.labs.projectGJ\\img\\bullet01.png");
  font = createFont("Arial", 36, true);
  center = new PVector(width/2, height/2);
  player = Player.getInstance(playerSprite, center, 3f, playerSize, playerSize, this);
  player.setPosition(center);
  spawnEnemy();

  if(user == null) {
   user = new UserData(this);
  }
  mouseClicked = false;
 }

 public void settings() {
  size(900, 900);
 }

 /**
  * The draw function that run on fps.
  */
 public void draw() {
  if (gameStatus == 0) {

   startScreen();
  } else if (gameStatus == 1) {
   gameScreen();
  } else if (gameStatus == 2) {
   gameEnd();
  }

  if (gameStatus == 3) {
   resetWindow();
   gameBuffer();
  }

 }

 /**
  * This function assists in spawning enemy
  * on the map.
  */
 public void spawnEnemy() {
  for (int i = 0; i < numberEnemy; i++) {
   PVector enemyDirection = new PVector(random(-1f, 1f), random(-1f, 1f)).normalize();
   PVector enemyPosition = new PVector(0, 0);
   enemy = new Enemy(enemySprite, enemyPosition, enemyDirection, 2f, enemySize, enemySize, this);
   addEnemy(enemy);
  }
 }

 /**
  * Allows players to shoot bullets based on
  * mouse pressed.
  */
 public void mousePressed() {
  PVector playerPos = player.position;
  PVector mouse = new PVector(mouseX, mouseY);
  PVector towardsMouse = PVector.sub(mouse, playerPos);
  bullets.add(new Bullet(bulletSprite, playerPos.copy(),
   towardsMouse.normalize(),
   bulletSpeed, bulletSize,
   bulletSize, this));
 }

 /**
  * Check for mouse click and change the game screen
  * based on certain criteria.
  */
 public void mouseClicked() {
  if(gameStatus == 2) {
//   mouseClicked = true;
   gameStatus = 3;
  } else if (gameStatus == 3) {
   gameStatus = 1;
  }
 }

 /**
  * The first screen with game title,
  * and request user's input for username.
  */
 private void startScreen () {
  int indent = 100;
  background(background);
  textAlign(CENTER);
  textSize(70);
  text("Till Death Do Us Part", width/2, height/2);
  textSize(40);
  text("Your name: " + typing, width/2, height - 150);
  textSize(20);
  text("Press Enter Key", width/2, height - 100);
  scoreSend = false;
 }

 /**
  * Show a screen between the death screen
  * and a new game. (Leaderboard)
  */
 private void gameBuffer() {
  background(background);
  textAlign(CENTER);
  text("Click to Try Again", width/2, height/2);
  resetWindow();
  scoreSend = false;
 }

 /**
  * The main logic that runs the game.
  */
 private void gameScreen() {
  background(background);
  user.setUsername(saved);
  drawScore();
  player.draw(this);

  for (Enemy e : dead) {
   removeEnemy(e);
  }
  dead.clear();
  for (Enemy o : enemyArr) {
   o.draw(this);
   o.update();
   o.borders();
  }

  player.scoreOver(playerScore);
  player.notifyObservers();

  for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
   Bullet b = iterator.next();
   b.update();
   b.draw(this);

   if (b.outOfBounds(this)) {
    iterator.remove();
   }
  }

  for (ICollidable c: collidables) {


   if(player.isCollided(c)) {
    player.collideEffect(c);
   }
   for (Bullet b: bullets) {
    if (b.isCollided(c)) {
     c.collideEffect(b);
     playerScore++;
    }
   }
  }
  if (enemyArr.size() / numberEnemy < 0.4) {
   spawnEnemy();
  }
 }

 /**
  * A function that signify the end of the game.
  */
 public void gameEnd() {
  background(background);
  textAlign(CENTER);
  textSize(50);
  text("GAME OVER \n Do you even game bro?", width/2, height/2);
  textSize(30);
  text("Total Score: " + playerScore +
    "\n CLICK RESTART",
   width/2, height - 100);


  if (scoreSend == false) {
   user.setScore(playerScore);
   scoreSend = true;
  }
  drawHighscore(user.getHighScore());
 }


 /**
  * Function that draws player's current score.
  */
 public void drawScore() {
  textFont(font);
  text(user.getName(), 200, 50);
  text("Score: " + String.valueOf(playerScore), 500, 50);
 }

 /**
  * Function that draws player's highest ever
  * score.
  * @param score
  */
 public void drawHighscore(int score) {
  textFont(font);
  text("Highscore: " + String.valueOf(score), 300, 50);
 }

 /**
  * A function that change to the death screen when player dies.
  */
 public void resetStatus() {
  gameStatus = 2;
 }

 /**
  * Return the size of the enemy array.
  * @return
  */
 public int getEnemySize() {
  return enemyArr.size();
 }

 /**
  * Getters for enemy arraylist.
  * @return
  */
 public ArrayList<Enemy> getEnemies() {
  return enemyArr;
 }

 /**
  * Add enemy to relevant arrays.
  * @param enemy
  */
 public void addEnemy(Enemy enemy) {
  enemyArr.add(enemy);
  collidables.add(enemy);
 }

 /**
  * Remove enemy from relevant arrays.
  * @param enemy
  */
 public void removeEnemy(Enemy enemy) {
  enemyArr.remove(enemy);
  collidables.remove(enemy);

 }

 /**
  * Function to restart the game by resetting
  * majority values.
  */
 public void resetWindow() {
  enemyArr.clear();
  bullets.clear();
  collidables.clear();
  dead.clear();
  player.getObservers().clear();
  player = null;
  playerScore = 0;
  setup();
 }

 /**
  * An array to remove enemy.
  * @param enemy
  */

 public void addDeadEnemyQueue(Enemy enemy) { dead.add(enemy);}

 /**
  * This functions read the instruction given
  * from key pressed to move the player.
  * @param event
  */
 @Override
 public void keyPressed(KeyEvent event) {
  super.keyPressed(event);
  if (player == null) {
   return;
  }
  switch (event.getKeyCode()) {
   case RIGHT:
    player.goRight();
    break;
   case LEFT:
    player.goLeft();
    break;
   case UP:
    player.goUp();
    break;
   case DOWN:
    player.goBot();
    break;
   case ENTER:
    saved = typing;
    typing = "";
    gameStatus = 1;
   default:
    typing = typing + key;
    break;
  }
 }

 /**
  * The main driver that runs the program.
  * @param passedArgs
  */
 public static void main(String[] passedArgs) {
  String[] appletArgs = new String[]{"window"};
  Window window = new Window();
  PApplet.runSketch(appletArgs, window);
 }
}
