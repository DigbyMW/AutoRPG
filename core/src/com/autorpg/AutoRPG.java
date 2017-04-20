package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter; // Main class
import com.badlogic.gdx.Gdx; // Used for clearing the screen with opengl
import com.badlogic.gdx.graphics.GL20; // Used for getting colours
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used for tiles.

public class AutoRPG extends ApplicationAdapter {
	private TileSystem tile_system;
	private Terrain test;
	
	@Override
	public void create () {
		tile_system = new TileSystem();
		test = new Terrain(9, 0.2);
	}

	@Override
	public void render () {
		
		// Regenerate terrain when space is pressed
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
			test = new Terrain(9, 0.2);
		}
		
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Visual output of terrain generator
		test.test();
		
		// Drawing tiles
		tile_system.batch_begin();
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
	}
}
