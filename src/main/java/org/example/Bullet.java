package org.example;

import processing.core.PImage;
import processing.core.PVector;

/**
 * Player's bullet class.
 */
public class Bullet extends AbstractCharacter{

 private PVector velocity;

 /**
  * Constructor
  * @param image
  * @param position
  * @param velocity
  * @param speed
  * @param width
  * @param height
  * @param window
  */
 public Bullet(PImage image, PVector position, PVector velocity, float speed, float width, float height, Window window) {
  super(image, position, speed, width, height, window);
  this.velocity = velocity;

 }

 /**
  * Check if bullet is out of the screen,
  * remove it if true.
  * @param window
  * @return
  */
 public boolean outOfBounds(Window window) {
  if ((this.position.x > window.width
   || this.position.x < 0)
   || (this.position.y > window.height
   || this.position.y < 0)) {
   return true;
  } else {
   return false;
  }
 }

 /**
  * The move function.
  */
 @Override
 public void update() {
  velocity.setMag(3f);
  position.add(velocity);
 }
}
