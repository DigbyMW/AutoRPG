package com.autorpg;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain {
	double[][] map; // Contains heightmap
	public TileSystem.TileMap tilemap_terrain; // Contains tilemap for drawing terrain
	public TileSystem.TileMap tilemap_swordsAndMonsters;
	public TileSystem tile_system; // Used for making tilemap_terrain
	private boolean[][] swords;
	private boolean[][] monsters;
	
	TextureRegion water;
	TextureRegion sand;
	TextureRegion grass;
	TextureRegion sword;
	
	TextureRegion[] monster_tiles;
	
	// Constructor generates the heightmap
	Terrain(int size, double bumpiness, int sword_count, int monster_count) {
		// Setting up the array. map size has to be 2^n + 1.
		int map_size = (int) (Math.pow(2, size) + 1);
		map = new double[map_size][map_size];
		swords = new boolean[map_size][map_size];
		monsters = new boolean[map_size][map_size];
		tile_system = new TileSystem();
		tilemap_terrain = tile_system.new TileMap(map_size, map_size);
		tilemap_swordsAndMonsters = tile_system.new TileMap(map_size, map_size);
		
		// Prepare tiles
		water = tile_system.get_tile("water");
		sand = tile_system.get_tile("sand");
		grass = tile_system.get_tile("grass");
		sword = tile_system.get_tile("sword");
		
		monster_tiles = new TextureRegion[6];
		for (int i = 0; i < monster_tiles.length; i ++) {
			String label = "monster" + (i + 1);
			monster_tiles[i] = tile_system.get_tile(label);
		}
		
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
		
		// Generate swords
		for (int i = 0; i < sword_count; i ++) {
			int x = (int) (Math.random() * map_size);
			int y = (int) (Math.random() * map_size);
			
			while(swords[x][y] || waterCollision(x, y)) {
				x = (int) (Math.random() * map_size);
				y = (int) (Math.random() * map_size);
			}
			
			tilemap_swordsAndMonsters.set(sword, x, y);
			swords[x][y] = true;
		}
		
		// Generate monsters
		for (int i = 0; i < monster_count; i ++) {
			int x = (int) (Math.random() * map_size);
			int y = (int) (Math.random() * map_size);
			
			while(monsters[x][y] || waterCollision(x, y) || swords[x][y]) {
				x = (int) (Math.random() * map_size);
				y = (int) (Math.random() * map_size);
			}
			
			tilemap_swordsAndMonsters.set(monster_tiles[(int)(Math.random() * 6)], x, y);
			monsters[x][y] = true;
		}
	}
	
	public int get_size() {
		return map.length;
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
	
	// Set tile in tilemap_terrain according to the height map
	private void set_tile(int x, int y) {
		if (map[x][y] < 0.5) {
			tilemap_terrain.set(water, x, y);
		} else if (map[x][y] < 0.6) {
			tilemap_terrain.set(sand, x, y);
		} else {
			tilemap_terrain.set(grass, x, y);
		}
	}
	
	public boolean waterCollision(int x, int y) {
		if (!edgeCollision(x, y)) {
			return map[x][y] < 0.5;
		}
		return false;
	}
	
	public boolean swordCollision(int x, int y) {
		if (!edgeCollision(x, y)) {
			boolean result = swords[x][y];
			if (result) {
				swords[x][y] = false;
				tilemap_swordsAndMonsters.set(null, x, y);
			}
			return result;
		}
		return false;
	}
	
	public boolean monsterCollision(int x, int y) {
		if (!edgeCollision(x, y)) {
			boolean result = monsters[x][y];
			if (result) {
				monsters[x][y] = false;
				tilemap_swordsAndMonsters.set(null, x, y);
			}
			return result;
		}
		return false;
	}
	
	public boolean edgeCollision(int x, int y) {
		return x < 0 || y < 0 || x > map.length - 1 || y > map.length - 1;
	}
	
	public void dispose() {
		tile_system.dispose();
	}
}
