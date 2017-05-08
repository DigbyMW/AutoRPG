package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TileSystem tile_system;
	private Terrain terrain;
	
	// Player coordinates
	private int player_x;
	private int player_y;
	private int swords;
	private int monsters;
	private TextureRegion player_tile;
	private int mode;
	private Menu menu_main;
	private Menu menu_game;
	private Menu menu_win;
	private Menu menu_lose;
	
	@Override
	public void create () {
		mode = 0;
		tile_system = new TileSystem();
		
		String[] menu_main_text = new String[2];
		menu_main_text[0] = "Play";
		menu_main_text[1] = "Quit";
		menu_main = new Menu(menu_main_text, tile_system);
		
		String[] menu_game_text = new String[2];
		menu_game_text[0] = "Continue";
		menu_game_text[1] = "Exit";
		menu_game = new Menu(menu_game_text, tile_system);
		
		String[] menu_win_text = new String[1];
		menu_win_text[0] = "You Win!";
		menu_win = new Menu(menu_win_text, tile_system);
		
		String[] menu_lose_text = new String[1];
		menu_lose_text[0] = "You Lose!";
		menu_lose = new Menu(menu_lose_text, tile_system);
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		switch (mode) {
			case 0:
				// Main menu
				menu();
				break;
			case 1:
				// Gameplay
				game_loop();
				game_draw();
				break;
			case 2:
				// In game menu
				game_draw();
				game_menu();
				break;
			case 3:
				// Game over screen
				end_game();
				break;
			case - 1:
				// Game exit
				Gdx.app.exit();
		}
	}
	
	private void game_loop() {
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
			mode = 2;
			return;
		}
		
		int old_x = player_x;
		int old_y = player_y;
		
		// Input
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
			player_x ++;
		} if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
			player_x --;
		} if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP)) {
			player_y ++;
		} if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
			player_y --;
		}
		
		if (terrain.edgeCollision(player_x, player_y)) {
			player_x = old_x;
			player_y = old_y;
		}
		
		// Interaction
		if (old_x != player_x || old_y != player_y) {
			if (terrain.waterCollision(player_x, player_y)) {
				swords --;
				player_tile = tile_system.get_tile("ship");
			} else {
				player_tile = tile_system.get_tile("player");
				if (terrain.swordCollision(player_x, player_y)) {
					swords ++;
				} if (terrain.monsterCollision(player_x, player_y)) {
					swords -= 2;
					monsters --;
				}
			}
		}
		if (swords < 0 || monsters == 0) {
			mode = 3;
		}
	}
	
	// In game menu activated with escape
	private void game_menu() {
		String input = menu_game.update();
		if (input == "Continue") {
			mode = 1;
			return;
		}
		if (input == "Exit") {
			mode = 0;
			menu_game.selected = 0;
			return;
		}
		
		tile_system.batch_begin();
		menu_game.draw();
		tile_system.batch_end();
	}
	
	// Main menu
	private void menu() {
		String input = menu_main.update();
		if (input == "Play") {
			start_game();
			mode = 1;
			return;
		}
		if (input == "Quit") {
			mode = - 1;
		}
		
		tile_system.batch_begin();
		menu_main.draw();
		tile_system.batch_end();
	}
	
	private void game_draw() {
		// Drawing Terrain
		terrain.tile_system.batch_begin();
		terrain.tilemap_terrain.draw(0, 0, player_x - 20, player_y - 15, 40, 30);
		terrain.tilemap_swordsAndMonsters.draw(0, 0, player_x - 20, player_y - 15, 40, 30);
		terrain.tile_system.batch_end();
		
		// Drawing other tiles
		tile_system.batch_begin();
		tile_system.string_to_tilemap("Swords " + swords + "\nMonsters " + monsters).draw(0, 0, 0, 0, 11, 2);
		tile_system.draw_tile(player_tile, 20, 15);
		tile_system.batch_end();
	}
	
	// Reset game values
	private void start_game() {
		terrain = new Terrain(5, 0.3, 50, 12);
		player_tile = tile_system.get_tile("player");
		
		player_x = (int) (Math.random() * terrain.get_size());
		player_y = (int) (Math.random() * terrain.get_size());
		swords = 0;
		monsters = 12;
		
		while (terrain.waterCollision(player_x, player_y)) {
			player_x = (int) (Math.random() * terrain.get_size());
			player_y = (int) (Math.random() * terrain.get_size());
		}
	}
	
	private void end_game() {
		if (monsters == 0) {
			// Display win message
			tile_system.batch_begin();
			menu_win.draw();
			tile_system.batch_end();
			if (menu_win.update() != null) {
				mode = 0;
			}
		} else {
			// Display lose message
			tile_system.batch_begin();
			menu_lose.draw();
			tile_system.batch_end();
			if (menu_lose.update() != null) {
				mode = 0;
			}
		}
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
		terrain.dispose();
	}
}
