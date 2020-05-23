package com.mystudio.gamename;

// import org.mini2Dx.core.*; // exit()
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.*;
import com.badlogic.gdx.graphics.*;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.mini2Dx.core.engine.geom.CollisionPoint;
import com.badlogic.gdx.*; // InputProcessor

import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class MyMini2DxGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "com.mystudio.gamename";

  private Texture texture;
  private Sprite sprite;
  private CollisionPoint point;

  private Player player;

  // boolean renderingRequested = false;

  Texture[] textures = new Texture[255];

  final int mapRows = 15;
  final int mapColumns = 28;
  final int tileWidth = 70;
  final int tileHeight = 70;
  char[][] mapData = new char[mapRows][mapColumns];

	@Override
    public void initialise() {
      /* This is run when the game starts and is only called once. Here you'll
      create any required objects and load any resources needed for your game.
      After the initialise method is finished, the update, interpolate and
      render methods are called continuously until the game ends. */
      width = 100;

      textures[0] = Tile.fromFile("bg.png");
      // textures[(char)'.'] = Tile.fromFile("Tiles/liquidWater.png"); // empty space
      // textures[(char)'.'] = Tile.fromFile("Tiles/grassCenter.png");
      textures[(char)'#'] = Tile.fromFile("Tiles/castleMid.png");
      textures[(char)'-'] = Tile.fromFile("Tiles/ropeHorizontal.png");
      textures[(char)'='] = Tile.fromFile("Tiles/ladder_mid.png");
      textures[(char)'o'] = Tile.fromFile("Tiles/box.png");
      textures[(char)'p'] = Tile.fromFile("Player/p1_front_resized.png");
      textures[(char)'e'] = Tile.fromFile("Enemies/pokerMadResized.png");

      texture = textures[(char) 'p'];
      sprite = new Sprite(texture);
      point = new CollisionPoint();

      player = new Player(sprite, texture, point);

      player.setPos(width / 2, height / 2);
      player.setPrevPosFromCurrent();

      /* read map */
      try {
        BufferedReader reader = new BufferedReader(new FileReader("../map.txt"));

        String line = "";
        int lineNumber = 0;
        while (line != null) {
          line = reader.readLine();
          if ((line != null) && (line.length() > 0)) {
            int colNumber = 0;
            for (char ch : line.toCharArray()) {
              System.out.print(lineNumber);
              System.out.print(" ");
              System.out.print(colNumber);
              System.out.print(" ");
              System.out.println(ch);
              mapData[lineNumber][colNumber] = ch;


              if (ch == 'p') {
                player.setPos(colNumber * textures[(char)'p'].getWidth(), lineNumber * textures[(char) 'p'].getHeight());
                player.setPrevPosFromCurrent();
              }

              colNumber++;
            }
          }

          lineNumber++;
        }
        reader.close();
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }

      // MyInputProcessor inputProcessor = new MyInputProcessor();
      Gdx.input.setInputProcessor(new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
          System.out.print("keyDown: ");
          System.out.println(keycode);

          switch (keycode) {
            case Keys.ESCAPE:
              System.exit(0);
              // Gdx.app.exit();
              break;

            case (Keys.UP):
              System.out.println("up");
              if ((player.getY() - tileHeight) >- height)
                player.moveY(-tileHeight);
              break;

            case (Keys.DOWN):
              System.out.println("down");
              if ((player.getY() + tileHeight) < height)
                player.moveY(tileHeight);
              break;

            case (Keys.LEFT):
              System.out.println("left");
              if ((player.getX() - tileWidth) >= 0)
                player.moveX(-tileWidth);
              break;

            case (Keys.RIGHT):
              System.out.println("right");
              if ((player.getX() + tileWidth) < width)
                player.moveX(tileWidth);
              break;
          }

          return true; // return true to indicate the event was handled
        }
      });


      // Gdx.graphics.setContinuousRendering(false);
      // Gdx.graphics.requestRendering();

    }

    @Override
    public void update(final float delta) {
      /* This is where your game will apply game logic, update animations,
      change background music, etc. The variable called 'delta' is provided and
      represents the amount of time in seconds to advance game logic. By default
      delta is 0.1 seconds. */

      player.update(delta);
    }

    @Override
    public void interpolate(final float alpha) {
      /* This is where your game calculates the render coordinates of your
      sprites. The variable 'alpha' will be between 0.0 and 1.0, representing
      how much of an update to simulate, i.e. 0.5 means it is halfway through
      an update.*/


      player.interpolate(null, alpha);
    }

    @Override
    public void render(final Graphics g) {
      /* This is where you'll draw you game to the screen. */

      // if (renderingRequested) {
      //   Gdx.graphics.requestRendering();
      //   // System.out.print("R");
      // } else {
      //   // System.out.print(".");
      // }


      for (int i = 0; i <= height; i = i + textures[0].getHeight()) {
        for (int j = 0; j < width; j = j+ textures[0].getWidth()) {
          g.drawTexture(textures[0], j, i);
        }
      }

      for (int i = 0; i < mapRows; i++) {
        for (int j = 0; j < mapColumns; j++) {
          if (mapData[i][j] == 'p')
            continue;
          if (textures[(char)mapData[i][j]] != null) {
            g.drawTexture(textures[(char)mapData[i][j]], textures[(char)mapData[i][j]].getWidth() * j, textures[(char)mapData[i][j]].getHeight() * i);
          }
        }
      }

      g.drawSprite(sprite, player.getRenderX(), player.getRenderY());
    }
}
