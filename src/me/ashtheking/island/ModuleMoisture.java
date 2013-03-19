package me.ashtheking.island;

import java.awt.Color;
import java.util.ArrayList;

/**
 * ModuleMoisture generates a moisture map. Water spreads moisture throughout
 * land, and then colors are calculated upon it.
 * 
 * @author Ashwin
 * 
 */
public class ModuleMoisture extends Module
{
	/**
	 * The overridden calculate() method for generating a moisture map.
	 * 
	 */

	@Override
	public void calculate(double[][] heightmap, boolean[][] watermap) {
		calc = new double[heightmap.length][heightmap.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap[x].length; y++)
				moisture(heightmap, watermap, x, y);
	}

	/**
	 * GetColor() method for returning a color based off elevation and moisture.
	 * 
	 * @param array
	 *            The elevation map.
	 * @param water
	 *            The water map.
	 * @param x
	 *            The x value in the maps.
	 * @param y
	 *            The y value in the maps.
	 * @return The Color, based of moisture.
	 */

	private Color getColor(double[][] array, boolean[][] water, int x, int y) {
		if (calc[x][y] == 0.0)
			moisture(array, water, x, y);
		if (calc[x][y] == 6)
			if (array[x][y] > 0.5)
				return Color.cyan;
			else if (array[x][y] < 0.5 && array[x][y] > 0.25)
				return Color.blue;
			else
				return Color.blue.darker();
		if (array[x][y] > 1) {
			if (calc[x][y] > 4)
				return new Color(0xF8F8F8); // SNOW
			if (calc[x][y] > 2)
				return new Color(0xDDDDBB); // TUNDRA
			if (calc[x][y] > 1)
				return new Color(0xBBBBBB); // BARE
			if (calc[x][y] <= 1)
				return new Color(0x999999); // SCORCHED

		}
		if (array[x][y] <= 1 && array[x][y] > 0.75) {
			if (calc[x][y] > 4)
				return new Color(0xaaD4BB); // TIAGA
			if (calc[x][y] > 2)
				return new Color(0xC4CFAA); // SHURBLAND
			if (calc[x][y] <= 2)
				return new Color(0xE4E8CA); // TEMPERATE DESERT

		}
		if (array[x][y] <= 0.75 && array[x][y] > 0.65) {
			if (calc[x][y] > 5)
				return new Color(0xA4C4A8); // TEMPERATE RAINFOREST
			if (calc[x][y] > 4)
				return new Color(0xB4C9A9); // TEMPERATE DECIDUOUS FOREST
			if (calc[x][y] > 1)
				return new Color(0xC4CCBB); // GRASSLAND
			if (calc[x][y] <= 1)
				return new Color(0xE4E8CA); // TEMPERATE DESERT

		}
		if (array[x][y] <= 0.65) {
			if (calc[x][y] > 4)
				return new Color(0x9CBBA9); // TROPICAL RAINFOREST
			if (calc[x][y] > 2)
				return new Color(0xA9CCA4); // TROPICAL SEASONAL FOREST
			if (calc[x][y] > 1)
				return new Color(0xC4CCBB); // GRASSLAND
			if (calc[x][y] <= 1)
				return new Color(0xE9DDC7); // SUBTROPICAL DESERT

		}
		return Color.blue;
	}

	/**
	 * Overridden getColors() method for returning the moisture map.
	 * 
	 */

	@Override
	public Color[][] getColors(double[][] heightmap, boolean[][] watermap) {
		Color[][] color = new Color[heightmap.length][heightmap.length];
		for (int x = 0; x < heightmap.length; x++)
			for (int y = 0; y < heightmap[x].length; y++)
				color[x][y] = getColor(heightmap, watermap, x, y);
		return color;
	}

	/**
	 * Spreads moisture from the water to the land. Some problems with this are
	 * that it doesn't calculate rainfall, and there are no rivers. This means
	 * that the higher the elevation, the generally drier the region. This isn't
	 * the most accurate.
	 * 
	 * @param array
	 *            The elevation map.
	 * @param water
	 *            The water amp.
	 * @param x
	 *            The Starting X value.
	 * @param y
	 *            The Starting Y value.
	 */

	private void moisture(double[][] array, boolean[][] water, int x, int y) {
		if (water[x][y]) {
			calc[x][y] = 6.0;
			for (int k = 1; k < 4; k++) {
				ArrayList<Object[]> a = getNearbyRegions(array, x, y, k);
				for (Object[] r : a)
					if (r != null && !(array[x][y] > 0.5)) {
						double m = calc[x][y] * (1.0 / 3.0);
						if (m > calc[(Integer) r[0]][(Integer) r[1]])
							calc[(Integer) r[0]][(Integer) r[1]] = m;
					}
			}
		}
	}
}
