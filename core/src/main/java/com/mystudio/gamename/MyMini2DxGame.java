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

  Tile[] tiles = new Tile[255];

  final int mapRows = 15;
  final int mapColumns = 28;
  final int tileWidth = 70;
  final int tileHeight = 70;
  Tile[][] mapData = new Tile[mapRows][mapColumns];

  Enemy[] enemies = new Enemy[8];
  int enemyCounter = 0;

	@Override
    public void initialise() {
      /* This is run when the game starts and is only called once. Here you'll
      create any required objects and load any resources needed for your game.
      After the initialise method is finished, the update, interpolate and
      render methods are called continuously until the game ends. */

      tiles[0] = new Tile("bg.png", ' ', true, false, false);
      tiles[(char) '.'] = new Tile("Tiles/snowLedgeLeft.png", '.', true, false, false);
      tiles[(char) '#'] = new Tile("Tiles/castleMid.png", '#', false, false, false);
      tiles[(char) '-'] = new Tile("Tiles/ropeHorizontal.png", '-', true, false, true);
      tiles[(char) '='] = new Tile("Tiles/ladder_mid.png", '=', true, true, false);
      tiles[(char) 'o'] = new Tile("Tiles/box.png", 'o', true, false, false);
      tiles[(char) 'p'] = new Tile("Player/p1_front_resized.png", 'p', true, false, false);
      tiles[(char) 'e'] = new Tile("Enemies/pokerMadResized.png", 'e', true, false, false);

      texture = tiles[(char) 'p'].texture;
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
              // System.out.print(lineNumber);
              // System.out.print(" ");
              // System.out.print(colNumber);
              // System.out.print(" ");
              // System.out.println(ch);
              // mapData[lineNumber][colNumber] = ch;


              if (ch == 'p') {
                player.setPos(colNumber * tiles[(char) 'p'].texture.getWidth(), lineNumber * tiles[(char) 'p'].texture.getHeight());
                player.mapXPos = colNumber;
                player.mapYPos = lineNumber;
                player.setPrevPosFromCurrent();
              } else if (ch == 'e') {
                Enemy enemy = new Enemy(
                  new Sprite(tiles[(char) 'e'].texture), tiles[(char) 'e'].texture, new CollisionPoint()
                );
                enemy.setPos(colNumber * tiles[(char) 'e'].texture.getWidth(), lineNumber * tiles[(char) 'e'].texture.getHeight());
                enemy.mapXPos = colNumber;
                enemy.mapYPos = lineNumber;
                // enemy.setPrevPosFromCurrent();
                enemies[enemyCounter] = enemy;
                enemyCounter++;
              }

              mapData[lineNumber][colNumber] = tiles[(char)ch];

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
                if ((player.mapYPos > 0) && (
                  (mapData[player.mapYPos - 1][player.mapXPos] == null)
                  || (
                    (mapData[player.mapYPos - 1][player.mapXPos].isPassable) && (mapData[player.mapYPos][player.mapXPos].isClimbable)
                  )
                )) {
                  player.moveMapY(-1);
                }
              break;

            case (Keys.DOWN):
              System.out.println("down");
              if ((player.getY() + tileHeight) < height)
                if ((mapData[player.mapYPos + 1][player.mapXPos] == null)
                    || (mapData[player.mapYPos + 1][player.mapXPos].isPassable)) {
                  player.moveMapY(1);
                }
              break;

            case (Keys.LEFT):
              System.out.println("left");
              if ((player.getX() - tileWidth) >= 0)
                if ((mapData[player.mapYPos][player.mapXPos - 1] == null)
                    || (mapData[player.mapYPos][player.mapXPos - 1].isPassable)) {
                  player.moveMapX(-1);
                }
              break;

            case (Keys.RIGHT):
              System.out.println("right");
              if ((player.getX() + tileWidth) < width) {
                if ((mapData[player.mapYPos][player.mapXPos+1] == null)
                    || (mapData[player.mapYPos][player.mapXPos+1].isPassable)) {
                  player.moveMapX(1);
                }
              }
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

      /* falling: are you currently in a space that does not contain something
      that isClimable or isHoldable, and does not have a space below which is
      not isPassable? */

      if (mapData[player.mapYPos][player.mapXPos] != null) {
        if (!(mapData[player.mapYPos][player.mapXPos].isClimbable || mapData[player.mapYPos][player.mapXPos].isHoldable)) {
          if ((mapData[player.mapYPos + 1][player.mapXPos].isPassable) && ((!mapData[player.mapYPos
              + 1][player.mapXPos].isClimbable))) {
            System.out.println("Falling!");
            // we're going to fall!
            player.moveMapY(1);
          } else {
            // System.out.println(mapData[player.mapYPos + 1][player.mapXPos].character);
          }
        }
      }

      player.update(delta);
      for (int i = 0; i < enemyCounter; i++) {
        enemies[i].update(delta);
      }
    }

    @Override
    public void interpolate(final float alpha) {
      /* This is where your game calculates the render coordinates of your
      sprites. The variable 'alpha' will be between 0.0 and 1.0, representing
      how much of an update to simulate, i.e. 0.5 means it is halfway through
      an update.*/

      player.interpolate(null, alpha);
      for (int i = 0; i < enemyCounter; i++) {
        enemies[i].interpolate(null, alpha);
      }
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


      for (int i = 0; i <= height; i = i + tiles[0].texture.getHeight()) {
        for (int j = 0; j < width; j = j+ tiles[0].texture.getWidth()) {
          g.drawTexture(tiles[0].texture, j, i);
        }
      }

      for (int i = 0; i < mapRows; i++) {
        for (int j = 0; j < mapColumns; j++) {
          if (mapData[i][j] != null) {
            if (mapData[i][j].character == 'p')
              continue;
            if (mapData[i][j].character == 'e')
              continue;

            g.drawTexture(mapData[i][j].texture, mapData[i][j].texture.getWidth() * j, mapData[i][j].texture.getHeight() * i);
          }

        }
      }

      // draw enemies
      for(int i = 0; i < enemyCounter; i++) {
        g.drawSprite(enemies[i].sprite, enemies[i].getRenderX(), enemies[i].getRenderY());
      }

      g.drawSprite(sprite, player.getRenderX(), player.getRenderY());
    }
}
