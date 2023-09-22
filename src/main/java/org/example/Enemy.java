package org.example;

import processing.core.PImage;
import processing.core.PVector;

/**
 * The enemy class.
 */
public class Enemy extends AbstractCharacter implements Observer {

 private PVector direction;

 private float distanceFromSource;

 /**
  * Constructor.
  * @param image
  * @param position
  * @param direction
  * @param speed
  * @param width
  * @param height
  * @param window
  */
 public Enemy(PImage image, PVector position, PVector direction, float speed, float width, float height, Window window) {
  super(image, position, speed, width, height, window);
  this.direction = direction;
 }

 /**
  * Function that make enemy seek the player.
  * @param position
  */
 public void seekPlayer(PVector position) {
  PVector enemyPos = this.position.copy();
  this.direction = position.add(enemyPos.mult(-1f)).normalize();
 }

 /**
  * Getters for distance with another object.
  * @return
  */
 public float getDistance() {
  return distanceFromSource;

 }

 /**
  * Update the distance between the target.
  * @param c
  */
 public void updateDistance(AbstractCharacter c) {
  PVector seekPos = c.getPosition().copy();
  this.distanceFromSource = seekPos.dist(this.position) - (this.width / 2f) - (c.getWidth() / 2f);
 }

 /**
  * The move function.
  */
 @Override
 public void update() {
  PVector multDir = this.direction.copy();
  this.position = this.position.add(multDir.mult(speed));
 }

 /**
  * Getters for direction variable.
  * @return
  */
 public PVector getDirection() {
  return direction;
 }

 /**
  * Setters for direction variable.
  * @param direction
  */
 public void setDirection(PVector direction) {
  this.direction = direction;
 }

 /**
  * The effect when enemy collide with
  * bullet objects.
  * @param c
  */
 @Override
 public void collideEffect(ICollidable c) {
  if (c instanceof Bullet) {
   window.addDeadEnemyQueue(this);
  }
 }

 /**
  * Set it so if enemy move out of the screen,
  * it will warp back from the opposite site.
  */
 public void borders() {
  if (position.x < -width) {
   position.x = window.width + width;
  }
  if (position.y < -width) {
   position.y = window.height + width;
  }
  if (position.x > window.width + width) {
   position.x = -width;
  }
  if (position.y > window.height + width) {
   position.y = -width;
  }
 }

 /**
  * Observer pattern function, received player's
  * current position and set the enemy to seek.
  *
  * @param msg
  */
 @Override
 public void update(Object msg) {
  PVector playerPos = (PVector) msg;
  seekPlayer(playerPos);
 }
}
