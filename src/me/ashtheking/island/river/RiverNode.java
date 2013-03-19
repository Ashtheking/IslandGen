package me.ashtheking.island.river;

import java.util.ArrayList;

import me.ashtheking.island.IslandGen;

/**
 * RiverNode was my idea for generating rivers. However, I have now deprecated
 * the class, since it does not work with the generation algorithm I have
 * implemented.
 * 
 * @author Ashwin
 * 
 */

@Deprecated
public class RiverNode
{
	private int x;
	private int y;
	private IslandGen instance;
	private RiverNode next;
	private RiverNode parent;

	public RiverNode(int x, int y, IslandGen instance, RiverNode next, RiverNode parent)
	{
		this.x = x;
		this.y = y;
		this.instance = instance;
		this.next = next;
	}

	public RiverNode calculateNext() {
		double[][] array = instance.array;
		ArrayList<Integer[]> o = getNearbyRegions(array, x, y);
		double min = array[x][y];
		int mX = x, mY = y;
		for (Integer[] i : o)
			if (array[i[0]][i[1]] <= min) {
				min = array[i[0]][i[1]];
				mX = i[0];
				mY = i[1];
			}
		return new RiverNode(mX, mY, instance, null, this);
	}

	private ArrayList<Integer[]> getNearbyRegions(double[][] array, int x, int y) {
		ArrayList<Integer[]> a = new ArrayList<Integer[]>();
		if (x - 1 >= 0)
			a.add(new Integer[] { x - 1, y });
		if (x + 1 < array.length)
			a.add(new Integer[] { x + 1, y });
		if (y - 1 >= 0)
			a.add(new Integer[] { x, y - 1 });
		if (y + 1 < array.length)
			a.add(new Integer[] { x, y + 1 });
		return a;
	}

	public RiverNode getNext() {
		return next;
	}

	public RiverNode getParent() {
		return parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setNext(RiverNode next) {
		this.next = next;
	}

	public void setParent(RiverNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "X: " + x + "\tY: " + y + "\tA: " + instance.array[x][y];
	}

}
