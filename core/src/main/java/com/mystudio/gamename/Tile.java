package com.mystudio.gamename;

// import java.util.*; // hashtable
import com.badlogic.gdx.Gdx; // gdx.files
import com.badlogic.gdx.graphics.Texture;

public class Tile {
  final static String pathPrefix = "platformerGraphicsDeluxe_Updated/";

  static Texture fromFile(String filename) {
    Texture texture = new Texture(Gdx.files.internal(pathPrefix + filename));
    return texture;
  }

  public Texture texture;
  public boolean isPassable;
  public boolean isClimbable;
  public boolean isHoldable;

  public Tile(Texture texture, boolean isPassable, boolean isClimbable, boolean isHoldable) {
    this.texture = texture;
    this.isPassable = isPassable;
    this.isClimbable = isClimbable;
    this.isHoldable = isHoldable;
  }

  public Tile(String filename, boolean isPassable, boolean isClimbable, boolean isHoldable) {
    this.texture = new Texture(Gdx.files.internal(pathPrefix + filename));
    this.isPassable = isPassable;
    this.isClimbable = isClimbable;
    this.isHoldable = isHoldable;
  }

  // drawTexture(com.badlogic.gdx.graphics.Texture texture, float x, float y)
  // drawTexture(com.badlogic.gdx.graphics.Texture texture, float x, float y, float width, float height, boolean flipY)
}
