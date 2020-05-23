package com.mystudio.gamename.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import com.mystudio.gamename.MyMini2DxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(MyMini2DxGame.GAME_IDENTIFIER);
		config.vSyncEnabled = true;
		config.title = "Basic Game Example";
		config.width = 70*28;
		config.height = 70*16;
		new DesktopMini2DxGame(new MyMini2DxGame(), config);
	}
}
