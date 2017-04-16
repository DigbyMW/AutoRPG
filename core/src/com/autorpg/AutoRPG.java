package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TextureRegion test_r;
	private TextureRegion test_g;
	private TextureRegion test_b;
	private TileSystem tile_system;
	private TileSystem.TileMap map;
	
	@Override
	public void create () {
		// Initializes tile system and regions.
		tile_system = new TileSystem();
		test_r = tile_system.get_tile("test r");
		test_g = tile_system.get_tile("test g");
		test_b = tile_system.get_tile("test b");
		
		// Initialize and populate map.
		map = tile_system.new TileMap(15, 15);
		for (int x = 0; x < 15; x ++) {
			for (int y = 0; y < 15; y ++) {
				if (x == y || x + y == 14) {
					// Red cross
					map.set(test_r, x, y);
				} else if ((y == 7 && x < 14 && x > 0)||
						   (x == 7 && y < 14 && y > 0)) {
					// Green Cross
					map.set(test_g, x, y);
				} else {
					// Blue Background
					map.set(test_b, x, y);
				}
			}
		}
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tile_system.batch_begin();
		
		// Original
		map.draw(0, 0, 0, 0, 15, 15);
		
		// Bottom Row
		map.draw(16, 0, 0, 0, 5, 5);
		map.draw(22, 0, 5, 0, 5, 5);
		map.draw(28, 0, 10, 0, 5, 5);
		
		// Middle Row
		map.draw(16, 6, 0, 5, 5, 5);
		map.draw(22, 6, 5, 5, 5, 5);
		map.draw(28, 6, 10, 5, 5, 5);
		
		// Top Row
		map.draw(16, 12, 0, 10, 5, 5);
		map.draw(22, 12, 5, 10, 5, 5);
		map.draw(28, 12, 10, 10, 5, 5);
		
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
	}
}
