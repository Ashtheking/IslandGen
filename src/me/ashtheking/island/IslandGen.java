package me.ashtheking.island;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * This is the main class of the project. IslandGen not only generates the
 * elevation map, but also renders and draws all modules.
 * 
 * @author Ashwin
 * 
 */
public class IslandGen extends JPanel implements ActionListener
{

	/**
	 * 
	 * @author Ashwin
	 * 
	 *         This thread is used to generate the world independent of the main
	 *         thread, speeding up things significantly.
	 */

	private static class GenThread implements Runnable
	{
		private static boolean running;

		@Override
		public void run() {
			if (running)
				return;
			running = true;
			if (instance != null)
				try {
					instance.generate1();
				} catch (Exception e) {
					e.printStackTrace();
				}
			running = false;
		}
	}

	/**
	 * 
	 * @author Ashwin
	 * 
	 *         This class listens for the ENTER key, and calls the save() method
	 */

	private class Key extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_ENTER)
				save();
		}
	}

	/**
	 * 
	 * @author Ashwin
	 * 
	 *         This class calls generate every time the mous is clicked on the
	 *         JPanel.
	 */

	private class Mouse extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event) {
			generate();
			// loadedModule(loadedModule);
		}
	}

	public static double displace(double[][] array, float num) {
		double max = num / (double) (array.length * 2) * 3;
		return (Math.random() - 0.5f) * max;
	}

	/**
	 * This method returns the currently running instance of the class
	 * 
	 * @return current instance of IslandGen
	 */

	public static IslandGen getInstance() {
		return instance;
	}

	/**
	 * JFrame is created and world loaded.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		JFrame frame = new JFrame("Island Generator");
		IslandGen isle = new IslandGen();
		instance = isle;
		JScrollPane pane = new JScrollPane(isle);
		frame.setContentPane(pane);
		frame.setSize(isle.io.getWidth() * 2 + 25, isle.io.getHeight() * 2 + 50);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Midpoint takes the array and runs the midpoint-displacement algorithim
	 * upon it to generate the elevation map.
	 * 
	 * @param array
	 *            The array to be run on.
	 * @param x
	 *            The starting X value
	 * @param y
	 *            The starting Y value
	 * @param width
	 *            The width of the generation to run
	 * @param height
	 *            The height of the generation to run
	 * @param c1
	 *            A corner of the array
	 * @param c2
	 *            A corner of the array
	 * @param c3
	 *            A corner of the array
	 * @param c4
	 *            A corner of the array
	 */
	public static void midpoint(double[][] array, int x, int y, int width, int height, double c1, double c2, double c3,
			double c4) {
		double Edge1, Edge2, Edge3, Edge4, Middle;
		int newWidth = width / 2;
		int newHeight = height / 2;
		if (width > 2 || height > 2) {
			Middle = (c1 + c2 + c3 + c4) / 4 + displace(array, newWidth + newHeight);
			Edge1 = (c1 + c2) / 2;
			Edge2 = (c2 + c3) / 2;
			Edge3 = (c3 + c4) / 2;
			Edge4 = (c4 + c1) / 2;
			midpoint(array, x, y, newWidth, newHeight, c1, Edge1, Middle, Edge4);
			midpoint(array, x + newWidth, y, newWidth, newHeight, Edge1, c2, Edge2, Middle);
			midpoint(array, x + newWidth, y + newHeight, newWidth, newHeight, Middle, Edge2, c3, Edge3);
			midpoint(array, x, y + newHeight, newWidth, newHeight, Edge4, Middle, Edge3, c4);
		}
		else {
			double k = Math.abs((c1 + c2 + c3 + c4) / 4.0);
			k *= 3.5;
			if (k > 4)
				k = 4;
			if (k < 0)
				k = 0;
			array[x][y] = k;
		}
	}

	/**
	 * Power (base 2) of the array
	 */
	public int power = 9;

	/**
	 * Size of the array
	 */
	public int size = (int) Math.pow(2, power);

	/**
	 * Elevation map, most used array in the entire program.
	 */
	public double[][] array = new double[size][size];
	/**
	 * Water map, used in ModuleMoisture
	 */

	public boolean[][] water = new boolean[size][size];

	/**
	 * Random, used by many things!
	 */

	Random rand = new Random();
	/**
	 * A buffered image for drawing and for saving.
	 * 
	 */
	public BufferedImage io = new BufferedImage(array.length, array.length, BufferedImage.TYPE_INT_RGB);

	private static final long serialVersionUID = 8195468829591202726L;

	private static IslandGen instance;

	/**
	 * The currently loaded Module in the system
	 * 
	 */
	Module loadedModule;

	/**
	 * The loaded Module's color map for drawing the array
	 * 
	 */

	Color[][] moduleColor = new Color[size][size];

	/**
	 * Starts everything, loads modules, etc.
	 */

	public IslandGen()
	{
		ModuleLoader.loadModules();
		loadedModule = ModuleLoader.modules.get(0);
		generate();
		addMouseListener(new Mouse());
		addKeyListener(new Key());
		setFocusable(true);
		Timer t = new Timer(10, this);
		t.start();
		ModuleLoader.jFrame();
	}

	/**
	 * Used to update the image every tick.
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (getGraphics() != null)
			paint(getGraphics());
	}

	/**
	 * Method called to create the generation thread.
	 * 
	 */

	public void generate() {
		new Thread(new GenThread()).start();
	}

	/**
	 * Actual generation method.
	 * 
	 * First seeds the array with random values. Then it seeds the array so it
	 * will generate an island. It then runs the midpoint-displacement algorithm
	 * to generate the array. Models erosion while calculating values of the
	 * loaded moisture.
	 * 
	 */

	public void generate1() throws InterruptedException {
		System.out.println("Starting Generation...");
		long t = System.nanoTime();
		long start = t;

		array = new double[size][size];
		water = new boolean[size][size];
		moduleColor = new Color[size][size];

		System.out.print("\t Filling with values...");
		for (int x = 0; x < array.length; x += 2)
			for (int y = 0; y < array[x].length; y += 2)
				array[x][y] = rand.nextDouble() * 20 + 1;
		long m = System.nanoTime();
		System.out.println("OK!" + "\t\t\t Time: " + (m - t));
		t = m;

		System.out.print("\t Seeding to form Island(s)...");
		for (int x = 0; x < array.length; x += array.length - 1)
			for (int y = 0; y < array[x].length; y++)
				array[x][y] = 0;
		for (int x = 0; x < array.length; x++)
			for (int y = 0; y < array[x].length; y += array.length - 1)
				array[x][y] = 0;
		m = System.nanoTime();
		System.out.println("OK!" + "\t\t Time: " + (m - t));
		t = m;

		System.out.print("\t Running Generation Algorithm....");
		midpoint(array, 0, 0, array.length, array.length, array[0][0], array[array.length - 1][0],
				array[0][array.length - 1], array[array.length - 1][array.length - 1]);
		m = System.nanoTime();
		System.out.println("OK!" + "\t\t Time: " + (m - t));
		t = m;

		System.out.print("\t Generating Lakes....");
		for (int k = 0; k < 5; k++) {
			int x = 0, y = 0;
			while (water[x][y]) {
				x = rand.nextInt(array.length);
				y = rand.nextInt(array.length);
			}
			for (int nX = x - 2; nX < x + 2; nX++)
				for (int nY = y - 2; nY < y + 2; nY++)
					if (nX >= 0 && nX < array.length && nY >= 0 && nY < array.length)
						array[nX][nY] *= 0.75;
		}
		m = System.nanoTime();
		System.out.println("OK!" + "\t\t\t Time: " + (m - t));
		t = m;

		System.out.print("\t Calculating Water....");
		for (int x = 0; x < array.length; x++)
			for (int y = 0; y < array.length; y++)
				water[x][y] = array[x][y] < 0.5;
		m = System.nanoTime();
		System.out.println("OK!" + "\t\t\t Time: " + (m - t));
		t = m;

		loadedModule(loadedModule);

		// System.out.print("\t Generating Rivers....");
		// for (int x = 0; x < 10; x++)
		// if(!riverGen())
		// x--;
		// m = System.nanoTime();
		// System.out.println("OK!" + "\t\t\t Time: " + (m - t ));
		// t = m;

		System.out.print("\t Modeling Erosion....");
		for (int k = 0; k < rand.nextInt(10) + 2; k++) {
			double erosion = (rand.nextInt(10) + 90) / 100.0;
			double werode = (rand.nextInt(5) + 95) / 100.0;
			for (int x = 0; x < array.length; x++)
				for (int y = 0; y < array.length; y++) {
					if (water[x][y])
						array[x][y] *= werode;
					else
						array[x][y] *= erosion;
					water[x][y] = array[x][y] < 0.5;
				}
			loadedModule.calculate(array, water);
			moduleColor = loadedModule.getColors(array, water);
			Thread.sleep(1000);
		}
		m = System.nanoTime();
		System.out.println("OK!" + "\t\t\t Time: " + (m - t));
		t = m;

		System.out.println("Done with Generation! \t\t\t\t\t Time:" + (System.nanoTime() - start));
	}

	/**
	 * Loads the module specified in the parameter.
	 * 
	 * @param m
	 *            The module to be loaded.
	 */

	public void loadedModule(Module m) {
		long mT = System.nanoTime();
		loadedModule = m;
		System.out.print("\t Running <" + loadedModule.getClass().getSimpleName() + "> Calculation....");
		loadedModule.calculate(array, water);
		moduleColor = loadedModule.getColors(array, water);
		long mL = System.nanoTime();
		System.out.println("OK!" + "\t Time: " + (mL - mT));
	}

	/**
	 * Paints the array based off the module's Color Map
	 * 
	 */

	@Override
	public void paint(Graphics g) {
		Graphics graph = io.getGraphics();
		for (int x = 0; x < array.length; x++)
			for (int y = 0; y < array[x].length; y++) {
				if (array[x][y] == 0.0)
					continue;
				// graph.setColor(array[x][y].getColor());
				Color c = moduleColor[x][y];
				if (c == null) {
					int n = (int) (array[x][y] * 50);
					if (n > 255)
						n = 255;
					if (n < 0)
						n = 0;
					c = new Color(n, n, n);
				}
				graph.setColor(c);
				graph.fillRect(x * 1, y * 1, 2, 2);
			}
		g.drawImage(io, 0, 0, io.getWidth() * 2, io.getHeight() * 2, null);
	}

	// public boolean riverGen() {
	// int x, y;
	// x = y = rand.nextInt(array.length);
	// boolean b = false;
	// if (array[x][y] > 1 && rand.nextInt(10) == 0) {
	// RiverNode node = new RiverNode(x, y, this, null, null);
	// RiverNode parent = node;
	// int z = 0;
	// while (!water[node.getX()][node.getY()] || z < 100) {
	// RiverNode n = node.calculateNext();
	// node.setNext(n);
	// node = node.getNext();
	// z++;
	// }
	// while (parent.getNext() != null) {
	// int mX = parent.getX(), mY = parent.getY();
	// array[mX][mY] *= 0.75;
	// parent = parent.getNext();
	// }
	// b = true;
	// }
	// return b;
	// }

	/**
	 * Saves the Buffered Image to a .png file.
	 * 
	 */

	public void save() {
		try {
			ImageIO.write(io, "png", new File("" + System.nanoTime() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
