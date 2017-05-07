package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TileSystem tile_system;
	private Terrain terrain;
	
	@Override
	public void create () {
		tile_system = new TileSystem();
		terrain = new Terrain(9, 0.2);
	}

	@Override
	public void render () {
		
		// Regenerate terrain when space is pressed
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
			terrain = new Terrain(9, 0.2);
		}
		
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Drawing tiles
		tile_system.batch_begin();
		tile_system.batch_end();
		
		terrain.tile_system.batch_begin();
		terrain.tile_map.draw(0, 0, 0, 0, 40, 30);
		terrain.tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
		terrain.dispose();
	}
}
