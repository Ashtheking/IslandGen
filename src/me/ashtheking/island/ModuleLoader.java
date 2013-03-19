package me.ashtheking.island;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * ModuleLoader is responsible for loading and keeping track of all modules that
 * are specified in "mods.config".
 * 
 * @author Ashwin
 * 
 */
public class ModuleLoader
{
	/**
	 * The list of loaded modules.
	 * 
	 */
	public static ArrayList<Module> modules = new ArrayList<Module>();
	/**
	 * A hashmap which is a link between the JButtons and the modules they load
	 * 
	 */
	public static HashMap<JButton, Module> hash = new HashMap<JButton, Module>();

	/**
	 * Launches the JFrame which lists the modules that it has loaded
	 * 
	 */

	public static void jFrame() {
		JFrame frame = new JFrame("Modules");
		JPanel panel = new JPanel();
		for (Module m : modules) {
			JButton button = new JButton(m.getClass().getSimpleName());
			hash.put(button, m);
			panel.add(button);
		}
		for (final JButton b : hash.keySet())
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					IslandGen.getInstance().loadedModule(hash.get(b));
				}
			});
		frame.setContentPane(panel);
		frame.setSize(modules.size() * 40 + 20, modules.size() * 40 + 20);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Loads all the modules listed inside "mods.config", and adds a
	 * ModuleHeight if one is not present already.
	 * 
	 */

	public static void loadModules() {
		try {
			File f = new File("mods.config");
			if (!f.exists())
				f.createNewFile();
			Scanner scan = new Scanner(f);
			while (scan.hasNext()) {
				String s = scan.nextLine();
				try {
					Object ob = Class.forName(s).newInstance();
					modules.add((Module) ob);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			boolean b = false;
			for (Module m : modules)
				if (m instanceof ModuleHeight)
					b = true;
			if (!b)
				modules.add(new ModuleHeight());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}