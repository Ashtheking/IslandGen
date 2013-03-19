package me.ashtheking.island;

import java.awt.Color;
import java.util.ArrayList;

/**
 * ModuleTemp provides a temperature rendering the area. Currently, it doesn't
 * work very well, and needs far more testing and theory.
 * 
 * @author Ashwin
 * 
 */
public class ModuleTemp extends Module
{
	/**
	 * Overridden calculate() method for generating a temperature map.
	 * 
	 */
	@Override
	public void calculate(double[][] heightmap, boolean[][] watermap) {
		calc = new double[heightmap.length][heightmap.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap[x].length; y++)
				temp(heightmap, watermap, x, y);
	}

	/**
	 * Overriden getColors() method which provides a color based off the
	 * calculated map.
	 * 
	 */

	@Override
	public Color[][] getColors(double[][] heightmap, boolean[][] watermap) {
		Color[][] c = new Color[calc.length][calc.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap[x].length; y++)
				if (watermap[x][y])
					if (calc[x][y] <= 0.1)
						c[x][y] = Color.cyan;
					else if (heightmap[x][y] < 0.25)
						c[x][y] = Color.blue.darker();
					else
						c[x][y] = Color.blue;
				else
					c[x][y] = Color.green;
		return c;
	}

	/**
	 * Land is hot, and disperses heat throughout the water. This requires more
	 * calculation, as it doesn't take into effect elevation.
	 * 
	 * @param array
	 *            The array to run on, passed on from IslandGen
	 * @param water
	 *            The watermap to run on, passed on from IslandGen
	 * @param x
	 *            The starting X value
	 * @param y
	 *            The starting Y value
	 */

	private void temp(double[][] array, boolean[][] water, int x, int y) {
		if (!water[x][y])
			calc[x][y] = 4.0;
		for (int k = 1; k < 6; k++) {
			ArrayList<Object[]> a = getNearbyRegions(array, x, y, k);
			for (Object[] r : a)
				if (r != null) {
					double m = calc[x][y] * 0.7;
					if (m > calc[(Integer) r[0]][(Integer) r[1]])
						calc[(Integer) r[0]][(Integer) r[1]] = m;
				}
		}
	}

}
