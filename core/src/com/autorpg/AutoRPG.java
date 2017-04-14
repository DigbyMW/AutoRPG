package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TextureRegion region;
	private TextureRegion test_r;
	private TextureRegion test_g;
	private TextureRegion test_b;
	private TileSystem tile_system;
	
	@Override
	public void create () {
		// Initializes tile system and region.
		tile_system = new TileSystem();
		region = tile_system.get_tile("placeholder");
		test_r = tile_system.get_tile("test r");
		test_g = tile_system.get_tile("test g");
		test_b = tile_system.get_tile("test b");
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Draw tile at 1, 1
		tile_system.batch_begin();
		tile_system.draw_tile(region, 1, 1);
		tile_system.draw_tile(region, 2, 1);
		tile_system.draw_tile(test_r, 3, 1);
		tile_system.draw_tile(test_g, 4, 2);
		tile_system.draw_tile(test_b, 5, 3);
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
	}
}
