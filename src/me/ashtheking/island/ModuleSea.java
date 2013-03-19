package me.ashtheking.island;

import java.awt.Color;

/**
 * ModuleSea generates a basic colored heightmap, and takes into account water
 * and land. The highest elevation is white, while the deepest oceans are blue.
 * 
 * @author Ashwin
 * 
 */
public class ModuleSea extends Module
{
	/**
	 * Provides a basic color map based off elevation. If the tile is marked as
	 * water, assign it as dark blue or blue based off it's height. Otherwise,
	 * assign the elevation map from light green to dark green to white.
	 */

	@Override
	public Color[][] getColors(double[][] heightmap, boolean[][] water) {
		Color[][] m = new Color[heightmap.length][heightmap.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap.length; y++)
				if (water[x][y]) {
					if (heightmap[x][y] < 0.25)
						m[x][y] = Color.blue.darker();
					else if (heightmap[x][y] >= 0.25 && heightmap[x][y] < 0.5)
						m[x][y] = Color.blue;
				}
				else if (heightmap[x][y] >= 0.5 && heightmap[x][y] < 1)
					m[x][y] = Color.green;
				else if (heightmap[x][y] >= 1 && heightmap[x][y] < 1.2)
					m[x][y] = Color.green.darker();
				else if (heightmap[x][y] >= 1.2)
					m[x][y] = Color.white;
				else
					m[x][y] = Color.green;
		return m;
	}
}
