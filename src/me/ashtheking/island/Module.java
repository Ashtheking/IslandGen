package me.ashtheking.island;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The abstract class Module is key to IslandGen. Without Modules, all the
 * program would do is render an elevation map using a gradient.
 * 
 * @author Ashwin
 * 
 */
public abstract class Module
{
	/**
	 * A protected double[][] available for usage by modules.
	 * 
	 */
	protected double[][] calc;

	/**
	 * An overridable method for running calculations, usually placing values
	 * inside calc.
	 * 
	 * @param heightmap
	 *            The elevation map passed on from IslandGen
	 * @param watermap
	 *            The water map passed on from IslandGen
	 */

	public void calculate(double[][] heightmap, boolean[][] watermap) {
	}

	private double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/**
	 * The method to return the Color matrix for the world, based on the
	 * heightmap and watermap.
	 * 
	 * @param heightmap
	 *            The elevation map passed on from IslandGen
	 * @param watermap
	 *            The water map passed on from IslandGen
	 * @return
	 */
	public abstract Color[][] getColors(double[][] heightmap, boolean[][] watermap);

	/**
	 * A method returning an arraylist of arrays, which gets all the tiles a
	 * certain radius away.
	 * 
	 * @param array
	 *            The array to tile through
	 * @param x
	 *            The starting X value to find tiles away from
	 * @param y
	 *            The starting Y value to find tiles away from
	 * @param d
	 *            The number of tiles away to get blocks from
	 * @return An arraylist of arrays in the form Object[] { x-coordinate,
	 *         y-coordinate, value }
	 */

	protected ArrayList<Object[]> getNearbyRegions(double[][] array, int x, int y, int d) {
		ArrayList<Object[]> a = new ArrayList<Object[]>();
		for (int k = x - d; k < x + d; k++)
			for (int j = y - d; j < y + d; j++)
				if (k >= 0 && k < array.length && j >= 0 && j < array[k].length)
					if (distance(x, y, k, j) < d)
						a.add(new Object[] { k, j, array[k][j] });
		return a;
	}

	/**
	 * ToString method used in ModuleLoader's GUI.
	 * 
	 */

	@Override
	public String toString() {
		String s = this.getClass().getName();
		s = s.substring(s.indexOf("Module"));
		return s;
	}
}
