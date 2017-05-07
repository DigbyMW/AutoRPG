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
	private TextureRegion player_tile;
	
	@Override
	public void create () {
		tile_system = new TileSystem();
		terrain = new Terrain(5, 0.3, 50);
		player_tile = tile_system.get_tile("player");
		
		player_x = (int) (Math.random() * terrain.get_size());
		player_y = (int) (Math.random() * terrain.get_size());
		swords = 0;
		
		while (terrain.waterCollision(player_x, player_y)) {
			player_x = (int) (Math.random() * terrain.get_size());
			player_y = (int) (Math.random() * terrain.get_size());
		}
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
				}
			}
		}
		if (swords < 0) {
			end_game();
		}
		
		// Drawing Terrain
		terrain.tile_system.batch_begin();
		terrain.tilemap_terrain.draw(0, 0, player_x - 20, player_y - 15, 40, 30);
		terrain.tilemap_swords.draw(0, 0, player_x - 20, player_y - 15, 40, 30);
		terrain.tile_system.batch_end();
		
		// Drawing other tiles
		tile_system.batch_begin();
		tile_system.string_to_tilemap("Swords " + swords).draw(0, 0, 0, 0, 10, 1);
		tile_system.draw_tile(player_tile, 20, 15);
		tile_system.batch_end();
	}
	
	private void end_game() {
		terrain = new Terrain(5, 0.3, 50);
		player_tile = tile_system.get_tile("player");
		
		player_x = (int) (Math.random() * terrain.get_size());
		player_y = (int) (Math.random() * terrain.get_size());
		swords = 0;
		
		while (terrain.waterCollision(player_x, player_y)) {
			player_x = (int) (Math.random() * terrain.get_size());
			player_y = (int) (Math.random() * terrain.get_size());
		}
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
		terrain.dispose();
	}
}
