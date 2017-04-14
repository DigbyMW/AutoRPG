package com.autorpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AutoRPG extends ApplicationAdapter {
	TextureRegion region;
	private TileSystem tile_system;
	
	@Override
	public void create () {
		// Initializes tile system and region.
		tile_system = new TileSystem();
		region = tile_system.get_tile("placeholder");
	}

	@Override
	public void render () {
		// Clear screen black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Draw tile at 1, 1
		tile_system.batch_begin();
		tile_system.draw_tile(region, 1, 1);
		tile_system.batch_end();
	}
	
	@Override
	public void dispose () {
		// Dispose of tile_system's sprite batch.
		tile_system.dispose();
	}
}
