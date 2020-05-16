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

  // drawTexture(com.badlogic.gdx.graphics.Texture texture, float x, float y)
  // drawTexture(com.badlogic.gdx.graphics.Texture texture, float x, float y, float width, float height, boolean flipY)
}
