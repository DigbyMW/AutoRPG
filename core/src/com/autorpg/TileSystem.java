package com.autorpg;

import com.badlogic.gdx.Gdx; // Used to load images.
import com.badlogic.gdx.graphics.Texture; // Used for creating textures from images.
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Used to split a texture into tiles.
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Used to draw texture regions.


public class TileSystem {
	Tiles tiles;
	SpriteBatch sprite_batch;
	
	TileSystem () {
		// Initialize sprite_batch and tiles.
		sprite_batch = new SpriteBatch();
		tiles = new Tiles(1);
		
		// Add content to tiles.
		tiles.add("placeholder", new Texture(Gdx.files.internal("placeholder.png")), 0);
	}
	
	// Returns a tile from tiles.
	public TextureRegion get_tile(String name) {
		return tiles.get(name);
	}
	
	// Draws a tile using the sprite batch.
	public void draw_tile(TextureRegion region, int x, int y) {
		sprite_batch.draw(region, x * 16, y * 16);
	}
	
	// Starts the sprite batch for drawing.
	public void batch_begin() {
		sprite_batch.begin();
	}
	
	// Ends the sprite batch.
	public void batch_end() {
		sprite_batch.end();
	}
	
	// Disposes of the sprite batch
	public void dispose() {
		sprite_batch.dispose();
	}
	
	private class Tiles { // Used to store tiles and match them to names.
		TextureRegion placeholder; // A placeholder tile to return when an invalid tile is requested
		TextureRegion[] tiles; // Array of tiles
		String[] tile_names; // Labels for the tiles
		int tiles_index; // The next empty index in 'tiles'
		
		Tiles (int size) {
			// Load placeholder and initialize arrays.
			placeholder = new TextureRegion(new Texture(Gdx.files.internal("placeholder.png")), 0, 0, 16, 16);
			tiles = new TextureRegion[size];
			tile_names = new String[size];
		}
		
		public void add(String name, Texture texture, int tile) {
			// Add new tile to the next index and increment the index.
			tiles[tiles_index] = new TextureRegion(texture, 0 + (tile * 16), 0, 16, 16 + (tile * 16));
			tile_names[tiles_index] = name;
			tiles_index ++;
		}
		
		public TextureRegion get(String name) {
			// Returns one of the stored tiles.
			for (int i = 0; i < tiles_index; i ++) {
				// Iterate thorugh tiles and return the one that
				// matches name.
				if (tile_names[i] == name) {
					return tiles[i];
				}
			}
			// If no tile with that name is found return
			// the placeholder tile.
			return placeholder;
		}
		
	}
}