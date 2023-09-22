package org.example;

import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Player's class.
 */
public class Player extends AbstractCharacter implements IObservable {

 private static Player player;

 private final float range = 100f;

 private final ArrayList<Observer> observers;

 /**
  * Constructor.
  * @param image
  * @param position
  * @param speed
  * @param width
  * @param height
  * @param window
  */
 public Player(PImage image, PVector position, float speed,
               float width, float height, Window window) {
  super(image, position, speed, width, height, window);
  this.observers = new ArrayList<>();
 }

 /**
  * Singleton.
  * @param image
  * @param position
  * @param speed
  * @param width
  * @param height
  * @param window
  * @return
  */
 public static Player getInstance(PImage image, PVector position,
                                  float speed,
                                  float width,
                                  float height,
                                  Window window) {
  if (player == null) {
   player = new Player(image, position, speed, width, height, window);
  }
  return player;
 }

 /**
  * Function to move player to the right.
  */
 public void goRight() {
  PVector right = new PVector(1,0);
  this.position = this.position.add(right.mult(10));
 }

 /**
  * Function to move player to the left.
  */
 public void goLeft() {
  PVector right = new PVector(-1,0);
  this.position = this.position.add(right.mult(10));
 }

 /**
  * Function to move player up.
  */
 public void goUp() {
  PVector right = new PVector(0,-1);
  this.position = this.position.add(right.mult(10));
 }

 /**
  * Function to move player down.
  */
 public void goBot() {
  PVector right = new PVector(0,1);
  this.position = this.position.add(right.mult(10));
 }

 /**
  * Stub.
  */
 @Override
 public void update() {

 }

 /**
  * The effect when player collide with enemy.
  * @param c
  */
 @Override
 public void collideEffect(ICollidable c) {
  if (c instanceof Enemy) {
   window.resetStatus();
  }
 }

 /**
  * If player's current score is more than half
  * of enemy's population at the beginning, and they
  * enemy are within a certain radius, they will be registered
  * into the observer arraylist.
  * @param currentScore
  */
 public void scoreOver(int currentScore) {
  for (Enemy e: window.getEnemies()) {
   if (currentScore > window.getEnemySize()/2 && (this.position.dist(e.getPosition()) <= range + this.width / 2f)) {
    registerObserver(e);
   } else {
    unregisterObserver(e);
   }

  }

 }

 /**
  * Getters for observer arraylist.
  * @return
  */
 public ArrayList<Observer> getObservers() {
  return observers;
 }

 /**
  * Add into observers arraylist.
  * @param o
  */
 @Override
 public void registerObserver(Observer o) {
  observers.add(o);

 }

 /**
  * Remove from observers arraylist.
  * @param o
  */
 @Override
 public void unregisterObserver(Observer o) {
  observers.remove(o);

 }

 /**
  * Function that send player's position to
  * the observers.
  */
 @Override
 public void notifyObservers() {
  for (Observer o : observers) {
   o.update(this.position.copy());
  }

 }
}
