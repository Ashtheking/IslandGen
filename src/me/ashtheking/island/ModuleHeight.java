package me.ashtheking.island;

import java.awt.Color;

/**
 * The class ModuleHeight is the default module. It is always incldued with the
 * distribution, and is used so that there is never no module loaded. It
 * generates a gradient from black and white based off the elevation map.
 * 
 * @author Ashwin
 * 
 */
public class ModuleHeight extends Module
{
	private int g = 60;

	/**
	 * returns a gradient between black and white based on the hegith of the
	 * elevation map.
	 * 
	 */

	@Override
	public Color[][] getColors(double[][] heightmap, boolean[][] water) {
		Color[][] c = new Color[heightmap.length][heightmap.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap[x].length; y++) {
				int n = (int) (heightmap[x][y] * g);
				if (n > 255)
					n = 255;
				if (n < 0)
					n = 0;
				c[x][y] = new Color(n, n, n);
			}
		return c;
	}

}
