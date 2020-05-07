package com.mystudio.gamename;

import org.mini2Dx.core.graphics.*; //Sprite
import org.mini2Dx.core.engine.geom.CollisionPoint;
import org.mini2Dx.core.game.*; //GameContainer

public class Player {
  private CollisionPoint point;
  private Sprite sprite;

  float prevXPos = 0;
  float prevYPos = 0;

  public Player(Sprite sprite, CollisionPoint point) {
    this.sprite = sprite;
    this.point = point;
  }

  void update() {
    point.preUpdate();
  }

  void interpolate(GameContainer gc, float alpha) {
    point.interpolate(gc, alpha);
  }

  void setPrevPosFromCurrent() {
    prevXPos = point.getX();
    prevYPos = point.getY();
  }

  void setPos(float x, float y) {
    point.set(x, y);
  }

  void setX(float x) {
    point.setX(x);
  }

  void setY(float y) {
    point.setY(y);
  }

  float getX() {
    return point.getX();
  }

  float getY() {
    return point.getY();
  }

  int getRenderX() {
    return point.getRenderX();
  }

  int getRenderY() {
    return point.getRenderY();
  }
}
