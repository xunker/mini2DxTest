package com.mystudio.gamename;

import org.mini2Dx.core.graphics.*; //Sprite
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.engine.geom.CollisionPoint;
import org.mini2Dx.core.game.*; //GameContainer

public class Enemy {
  private CollisionPoint point;
  public Sprite sprite;
  public Texture texture;

  float prevXPos = 0;
  float prevYPos = 0;

  int remainingXMovement = 0;
  int remainingYMovement = 0;
  int xMovement = 1;
  int yMovement = 1;

  public int mapXPos = 0;
  public int mapYPos = 0;

  public Enemy(Sprite sprite, Texture texture, CollisionPoint point) {
    this.texture = texture;
    this.sprite = sprite;
    this.point = point;

    xMovement = texture.getWidth() / 25;
    yMovement = texture.getHeight() / 25;
  }

  void update(final float delta) {
    if (remainingXMovement != 0) {
      if (remainingXMovement > 0) {
        setX(getX() + xMovement);
        remainingXMovement -= xMovement;
      } else if (remainingXMovement < 0) {
        setX(getX() - xMovement);
        remainingXMovement += xMovement;
      }
    }

    if (remainingYMovement != 0) {
      if (remainingYMovement > 0) {
        setY(getY() + yMovement);
        remainingYMovement -= yMovement;
      } else if (remainingYMovement < 0) {
        setY(getY() - yMovement);
        remainingYMovement += yMovement;
      }
    }

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

  void moveMapX(int mapUnits) {
    if (remainingXMovement != 0)
      return;

    mapXPos += mapUnits;
    remainingXMovement += mapUnits * texture.getWidth();
  }

  void moveMapY(int mapUnits) {
    if (remainingYMovement != 0)
      return;

    mapYPos += mapUnits;
    remainingYMovement += mapUnits * texture.getHeight();
    ;
  }

  int getRenderX() {
    return point.getRenderX();
  }

  int getRenderY() {
    return point.getRenderY();
  }
}
