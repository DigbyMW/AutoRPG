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
	private TextureRegion player_tile;
	
	@Override
	public void create () {
		tile_system = new TileSystem();
		terrain = new Terrain(9, 0.3);
		player_tile = tile_system.get_tile("player");
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Input
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT) && !terrain.checkCollision(player_x + 1, player_y)) {
			player_x ++;
		} if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT) && !terrain.checkCollision(player_x - 1, player_y)) {
			player_x --;
		} if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP) && !terrain.checkCollision(player_x, player_y + 1)) {
			player_y ++;
		} if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN) && !terrain.checkCollision(player_x, player_y - 1)) {
			player_y --;
		}
		
		// Drawing Terrain
		terrain.tile_system.batch_begin();
		terrain.tile_map.draw(0, 0, player_x - 20, player_y - 15, 40, 30);
		terrain.tile_system.batch_end();
		
		// Drawing other tiles
		tile_system.batch_begin();
		tile_system.draw_tile(player_tile, 20, 15);
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
		terrain.dispose();
	}
}
