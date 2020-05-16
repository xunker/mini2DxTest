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
  boolean followMouse = false;

  float xDir = 0;
  float yDir = 0;

  float rot = 0f;

  final float xDirMax = 25;
  final float yDirMax = 25;
  final float xDirIncrement = xDirMax/5;
  final float yDirIncrement = yDirMax/5;
  final float xDirDecay = 0.1f;
  final float yDirDecay = 0.1f;

  private Texture dirtCenter;

  final int mapRows = 8;
  final int mapColumns = 10;
  char[][] mapData = new char[mapRows][mapColumns];

	@Override
    public void initialise() {
      /* This is run when the game starts and is only called once. Here you'll
      create any required objects and load any resources needed for your game.
      After the initialise method is finished, the update, interpolate and
      render methods are called continuously until the game ends. */

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
              colNumber++;
            }
          }

          lineNumber++;
        }
        reader.close();
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }


      dirtCenter = Tile.fromFile("Tiles/dirtCenter.png");

      // MyInputProcessor inputProcessor = new MyInputProcessor();
      Gdx.input.setInputProcessor(new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
          System.out.print("keyDown: ");
          System.out.println(keycode);

          followMouse = false;
          switch (keycode) {
            case Keys.ESCAPE:
              System.exit(0);
              // Gdx.app.exit();
              break;
            case (Keys.SPACE):
              System.out.println("space");
              xDir = 0;
              yDir = 0;
              break;

            case (Keys.UP):
              System.out.println("up");
              yDir = yDir - yDirIncrement;
              break;

            case (Keys.DOWN):
              System.out.println("down");
              yDir = yDir + yDirIncrement;
              break;

            case (Keys.LEFT):
              System.out.println("left");
              xDir = xDir - xDirIncrement;
              break;

            case (Keys.RIGHT):
              System.out.println("right");
              xDir = xDir + xDirIncrement;
              break;
          }

          return true; // return true to indicate the event was handled
        }

        @Override
        public boolean mouseMoved(int x, int y) {
          if (!followMouse)
            return false;

          double deltaX = x - player.getX();
          double deltaY = y - player.getY();
          double rad = Math.atan2(deltaY, deltaX); // In radians

          double deg = rad * (180 / Math.PI);
          sprite.setRotation((float) deg);

          // renderingRequested = true;

          return true; // return true to indicate the event was handled
        }

        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
          if (button == Buttons.LEFT) {
            xDir = x - player.getX();
            yDir = y - player.getY();

            if (Math.abs(xDir) > xDirMax) {
              float percentDrop = xDirMax / Math.abs(xDir);
              xDir = xDirMax;
              yDir = yDir * percentDrop;
            }

            if (Math.abs(yDir) > yDirMax) {
              float percentDrop = yDirMax / Math.abs(yDir);
              yDir = yDirMax;
              xDir = xDir * percentDrop;
            }

            System.out.print("xPos: ");
            System.out.print(player.getX());
            System.out.print(", yPos: ");
            System.out.print(player.getY());
            System.out.print(", x: ");
            System.out.print(x);
            System.out.print(", y: ");
            System.out.print(y);
            System.out.print(", xDir: ");
            System.out.print(xDir);
            System.out.print(",  yDir: ");
            System.out.println(yDir);

            return true; // return true to indicate the event was handled
          } else if (button == Buttons.RIGHT) {
            xDir = 0;
            yDir = 0;

            player.setPos(x, y);

            return true;
          }

          return false;
        }

      });

      texture = new Texture("arrow.png");
      sprite = new Sprite(texture);
      point = new CollisionPoint();

      player = new Player(sprite, point);

      player.setPos(width / 2, height / 2);
      player.setPrevPosFromCurrent();

      // Gdx.graphics.setContinuousRendering(false);
      // Gdx.graphics.requestRendering();

    }

    @Override
    public void update(final float delta) {
      /* This is where your game will apply game logic, update animations,
      change background music, etc. The variable called 'delta' is provided and
      represents the amount of time in seconds to advance game logic. By default
      delta is 0.1 seconds. */

      // decay
      if (xDir > 0) {
        xDir = xDir - xDirDecay;
        if (xDir < 0)
          xDir = 0;
      }
      if (yDir > 0) {
        yDir = yDir - yDirDecay;
        if (yDir < 0)
          yDir = 0;
      }

      if (xDir < 0) {
        xDir = xDir + xDirDecay;
        if (xDir > 0)
          xDir = 0;
      }

      if (yDir < 0) {
        yDir = yDir + yDirDecay;
        if (yDir > 0)
          yDir = 0;
      }

    }

    @Override
    public void interpolate(final float alpha) {
      /* This is where your game calculates the render coordinates of your
      sprites. The variable 'alpha' will be between 0.0 and 1.0, representing
      how much of an update to simulate, i.e. 0.5 means it is halfway through
      an update.*/


      // if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
      //   // Gdx.app.exit();
      //   System.exit(0);
      // }

      player.update();
      player.setPos(player.getX() + xDir, player.getY() + yDir);

      player.prevXPos = player.getX();
      player.prevYPos = player.getY();

      player.setX(player.getX() + xDir);
      player.setY(player.getY() + yDir);

      if (player.getX() >= (width - texture.getWidth())) {
        xDir = -xDir;
      } else if (player.getX() < 0) {
        xDir = Math.abs(xDir);
      }

      if (player.getY() >= (height - texture.getHeight())) {
        yDir = -yDir;
      } else if (player.getY() < 0) {
        yDir = Math.abs(yDir);
      }

      if (!followMouse) {
        // rotation
        // https://stackoverflow.com/questions/15994194/how-to-convert-x-y-coordinates-to-an-angle
        double deltaX = player.getX() - player.prevXPos;
        double deltaY = player.getY() - player.prevYPos;
        double rad = Math.atan2(deltaY, deltaX); // In radians

        double deg = rad * (180 / Math.PI);
        sprite.setRotation((float) deg);
      }

      player.interpolate(null, alpha);
    }

    @Override
    public void render(final Graphics g) {
      /* This is where you'll draw you game to the screen. */
      // g.drawTexture(texture, xPos, xPos);
      // g.drawSprite(sprite, xPos, yPos);

      if ((player.getX() != player.prevXPos) || (player.getY() != player.prevYPos)) {
        // renderingRequested = true;
        followMouse = false;
      } else {
        followMouse = true;
      }

      // if (renderingRequested) {
      //   Gdx.graphics.requestRendering();
      //   // System.out.print("R");
      // } else {
      //   // System.out.print(".");
      // }



      // for (int i = 0; i < (width / dirtCenter.getWidth()); i++) {
      //   for (int j = 0; j < (height / dirtCenter.getHeight()); j++) {
      //     g.drawTexture(dirtCenter, dirtCenter.getWidth() * j, dirtCenter.getHeight() * j);
      //   }
      // }
      for (int i = 0; i < mapRows; i++) {
        for (int j = 0; j < mapColumns; j++) {
          if (mapData[i][j] == '#')
            g.drawTexture(dirtCenter, dirtCenter.getWidth() * j, dirtCenter.getHeight() * i);
        }
      }

      g.drawTexture(dirtCenter, 0, 0);
      g.drawSprite(sprite, player.getRenderX(), player.getRenderY());
    }
}
