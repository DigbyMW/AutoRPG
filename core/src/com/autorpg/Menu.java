package com.autorpg;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

class Menu {
	String[] names;
	TileSystem.TileMap text_layer;
	TileSystem.TileMap background_layer;
	TileSystem tile_system;
	TextureRegion pointer;
	int selected;
	
	Menu (String[] names, TileSystem tile_system) {
		this.names = names;
		this.tile_system = tile_system;
		
		pointer = tile_system.get_tile("ui pointer");
		selected = 0;
		
		// Create string from menu items
		String fullText = names[0];
		for (int i = 1; i < names.length; i ++) {
			fullText += "\n" + names[i];
		}
		text_layer = tile_system.string_to_tilemap(fullText);
		
		// Generate border and background
		background_layer = tile_system.new TileMap(text_layer.get_width() + 4, text_layer.get_height() + 2);
		for (int x = 0; x < background_layer.get_width(); x ++) {
			for (int y = 0; y < background_layer.get_height(); y ++) {
				TextureRegion tile = tile_system.get_tile("ui border middle middle");
				if (x == 0) {
					if (y == 0) {
						tile = tile_system.get_tile("ui border bottom left");
					} else if (y == background_layer.get_height() - 1) {
						tile = tile_system.get_tile("ui border top left");
					} else {
						tile = tile_system.get_tile("ui border middle left");
					}
				} else if (x == background_layer.get_width() - 1) {
					if (y == 0) {
						tile = tile_system.get_tile("ui border bottom right");
					} else if (y == background_layer.get_height() - 1) {
						tile = tile_system.get_tile("ui border top right");
					} else {
						tile = tile_system.get_tile("ui border middle right");
					}
				} else {
					if (y == 0) {
						tile = tile_system.get_tile("ui border bottom middle");
					} else if (y == background_layer.get_height() - 1) {
						tile = tile_system.get_tile("ui border top middle");
					}
				}
				
				background_layer.set(tile, x, y);
			}
		}
	}
	
	public String update() {
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP)) {
			selected --;
			// Prevent selected from being negative
			if (selected < 0) {
				selected += names.length;
			}
		} if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
			selected ++;
			//Prevent selected from being larger than names.length
			selected %= names.length;
		}
		
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
			return names[selected];
		}
		return null;
	}
	
	public void draw() {
		draw_layer(background_layer);
		draw_layer(text_layer);
		
		// Draw pointer pointing at the selected menu item
		tile_system.draw_tile(pointer, 19 - (text_layer.get_width() / 2), 14 + (text_layer.get_height() / 2) + (text_layer.get_height() % 2) - selected);
	}
	
	// Draw layer centered in the window
	private void draw_layer(TileSystem.TileMap layer) {
		layer.draw(20 - (layer.get_width() / 2), 15 - (layer.get_height() / 2), 0, 0, layer.get_width(), layer.get_height());
	}
}
