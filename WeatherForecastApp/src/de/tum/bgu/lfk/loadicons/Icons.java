package de.tum.bgu.lfk.loadicons;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * loads all files from a folder. the files needs to have a png, gif or jpg file extension.
 * the filename is set as the key but without the file extension
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 09.07.2015
 *
 */
public class Icons {
	
	private PApplet p;
	private HashMap<String, PImage> icons;
	
	/**
	 * constructor
	 * @param p PApplet
	 */
	public Icons(PApplet p){
		this.p = p;
		this.icons = new HashMap<>();
	}
	
	/**
	 * loads files with png, gif and jpg extension from the specified folder
	 * @param dir the directory from which to load images/icons
	 * @param ext which files to load png, gif or jpg
	 */
	public void loadIcons(String dir, FileExtensions ext){
		File[] files;
		File file = new File(dir);
		
		if (file.isDirectory()){
			files = file.listFiles();
			for(int i = 0; i < files.length; i++){
				
				switch(ext){
				case PNG:
					if(files[i].getName().substring(files[i].getName().lastIndexOf('.') + 1).equals("png")){
						String key = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
						PImage image = p.loadImage(files[i].getAbsolutePath(), "png");
						this.icons.put(key, image);
					}
					break;
					
				case GIF:
					if(files[i].getName().substring(files[i].getName().lastIndexOf('.') + 1).equals("gif")){
						String key = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
						PImage image = p.loadImage(files[i].getAbsolutePath(), "gif");
						this.icons.put(key, image);
					}
					break;
					
				case JPG:
					if(files[i].getName().substring(files[i].getName().lastIndexOf('.') + 1).equals("jpg")){
						String key = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
						PImage image = p.loadImage(files[i].getAbsolutePath(), "jpg");
						this.icons.put(key, image);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * returns the icon as PImage object.
	 * returns null if not such a key exists.
	 * @param key the key under which the icon is saved
	 * @return the PImage object, returns null if not such a key exists.
	 */
	public PImage getIcon(String key){
		if(icons.containsKey(key)){
			return icons.get(key);
		}else{
			return null;
		}		
	}
	
	/**
	 * return the keyset of the icons
	 * @return HashSet<String>
	 */
	public HashSet<String> getKeys(){
		return (HashSet<String>) icons.keySet();
	}
	
	/**
	 * removes all loaded icons
	 */
	public void clear(){
		icons.clear();
	}
	
	
	

}
