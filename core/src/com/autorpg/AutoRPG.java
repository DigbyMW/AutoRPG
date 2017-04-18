package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TileSystem tile_system;
	private TileSystem.TileMap text; // Test map
	
	@Override
	public void create () {
		// Initializes tile system and text map.
		tile_system = new TileSystem();
		// Generate new map with the text "Hellow World"
		text = tile_system.string_to_tilemap("Hello\nWorld!");
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tile_system.batch_begin();
		// Draw map
		text.draw(1, 1, 0, 0, 6, 2);
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
	}
}
