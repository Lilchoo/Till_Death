package org.example;

import processing.core.PImage;
import processing.core.PVector;
import java.util.Date;

public abstract class AbstractCharacter implements ICollidable, Comparable<AbstractCharacter>{

 protected PVector position;

 protected Date lastCollide;

 protected Window window;

 protected float speed;

 protected float width;

 protected float height;

 protected int collideDelay = 100;

 protected PImage image;

 public AbstractCharacter(PImage image, PVector position, float speed, float width, float height, Window window) {
  this.image = image;
  this.position = position;
  this.speed = speed;
  this.width = width;
  this.height = height;
  this.window = window;
  this.lastCollide = new Date();
 }

 public void setPosition(PVector position) {
  this.position = position;
 }

 public float getSpeed() {
  return speed;
 }

 public void setSpeed(float speed) {
  this.speed = speed;
 }

 @Override
 public float getWidth() {
  return width;
 }

 @Override
 public void setWidth(float width) {
  this.width = width;
 }

 public float getHeight() {
  return height;
 }

 public void setHeight(float height) {
  this.height = height;
 }

 @Override
 public boolean isCollided(ICollidable c) {
  if (c == this) {
   return false;
  }
  return c.getPosition().dist(this.position) <= (c.getWidth()/2f + (this.width-1));
 }

 @Override
 public void collideEffect(ICollidable c) {
 }

 @Override
 public PVector getPosition() {
  return this.position;
 }

 @Override
 public void draw(Window window) {
  window.image(image, this.position.x,
   this.position.y, this.width, this.height);
 }

 @Override
 abstract public void update();

 @Override
 public int compareTo(AbstractCharacter o) {
  return 0;
 }
}
