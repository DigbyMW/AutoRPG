package com.autorpg;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain {
	double[][] map; // Contains heightmap
	public TileSystem.TileMap tile_map; // Contains tilemap for drawing terrain
	public TileSystem tile_system; // Used for making tile_map
	
	TextureRegion water;
	TextureRegion sand;
	TextureRegion grass;
	
	// Constructor generates the heightmap
	Terrain(int size, double bumpiness) {
		// Setting up the array. map size has to be 2^n + 1.
		int map_size = (int) (Math.pow(2, size) + 1);
		map = new double[map_size][map_size];
		tile_system = new TileSystem();
		tile_map = tile_system.new TileMap(map_size, map_size);
		
		// Prepare tiles
		water = tile_system.get_tile("water");
		sand = tile_system.get_tile("sand");
		grass = tile_system.get_tile("grass");
		
		// Radomizing map corners
		map[0][0] = Math.random();
		map[0][map.length - 1] = Math.random();
		map[map.length - 1][0] = Math.random();
		map[map.length - 1][map.length - 1] = Math.random();
		// Set corner tiles
		set_tile(0, 0);
		set_tile(0, map.length - 1);
		set_tile(map.length - 1, 0);
		set_tile(map.length - 1, map.length - 1);
		
		
		// Main algorithm loop
		for (int distance = (int) Math.ceil(map.length / 2); distance >= 1; distance /= 2) {
			
			// distance * 2 is required a lot so it is pre-calculated here
			double distance_double = distance * 2;
			// Variation used to randomize each height
			double variation = (1 - (1 / distance)) * bumpiness;
			
			// Diamond Stage
			// Iterates through diagonal midpoints
			for (int y = distance; y < map.length; y += distance_double) {
				for (int x = distance; x < map.length; x += distance_double) {
					
					// Sets midpoint to randomized average of its diagonally adjacent heights
					map[x][y] = average(variation,
						map[x - distance][y - distance],
						map[x - distance][y + distance],
						map[x + distance][y - distance],
						map[x + distance][y + distance]);
					
					// Set tile for this midpoint
					set_tile(x, y);
				}
			}
			
			// Square Stage
			// Iterates through orthagonal midpoints
			
			// x_shift changes the offset of the midpoints on each row
			int x_shift = distance;
			for (int y = 0; y < map.length; y += distance) {
				for (int x = x_shift; x < map.length; x += distance_double) {
					
					// Sets midpoint to randomized average of its orthoganally adjacent heights
					
					// Special midpoint positions:
					if (x == 0) {
						// Left Edge
						map[x][y] = average(variation, map[x][y + distance], map[x][y - distance], map[x + distance][y]);
					} else if (x == map.length - 1) {
						// Right Edge
						map[x][y] = average(variation, map[x][y + distance], map[x][y - distance], map[x - distance][y]);
					} else if (y == 0) {
						// Top Edge
						map[x][y] = average(variation, map[x][y + distance], map[x + distance][y], map[x - distance][y]);
					} else if (y == map.length - 1) {
						// Bottom Edge
						map[x][y] = average(variation, map[x][y - distance], map[x + distance][y], map[x - distance][y]);
					}
					
					// Normal midpoint positions
					else {
						map[x][y] = average(variation,
							map[x][y + distance],
							map[x][y - distance],
							map[x + distance][y],
							map[x - distance][y]);
					}
					
					set_tile(x, y);
				}
				
				// Toggle x_shift between 0 and distance
				if (x_shift == 0) {
					x_shift = distance;
				} else {
					x_shift = 0;
				}
			}
		}
	}
	
	private double average(double variation, double... numbers) {
		double result = 0;
		
		// Get mean average
		for (int i = 0; i < numbers.length; i ++) {
			result += numbers[i];
		}
		result /= numbers.length;
		
		// Add variation
		result = result - (variation / 2) // Allows variation to go up or down
		// Randomization:
		+ (Math.random() * variation);
		
		// Keep variation between 0 and 1.
		if (result < 0) {
			result = 0;
		} else if (result > 1) {
			result = 1;
		}
		
		return result;
	}
	
	// Set tile in tile_map according to the height map
	private void set_tile(int x, int y) {
		if (map[x][y] < 0.5) {
			tile_map.set(water, x, y);
		} else if (map[x][y] < 0.6) {
			tile_map.set(sand, x, y);
		} else {
			tile_map.set(grass, x, y);
		}
	}
	
	public boolean checkCollision(int x, int y) {
		return x < 0 || y < 0 || x > map.length || y > map.length || map[x][y] < 0.5;
	}
	
	public void dispose() {
		tile_system.dispose();
	}
}
