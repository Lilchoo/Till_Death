package org.example;

import processing.core.PVector;

/**
 * This interface set the based structure for
 * all collidable, and drawable object.
 */
public interface ICollidable {

 boolean isCollided(org.example.ICollidable c);

 void collideEffect(org.example.ICollidable c);

 PVector getPosition();

 void draw(Window window);

 void update(); // move

 float getWidth();

 void setWidth(float width);

}
