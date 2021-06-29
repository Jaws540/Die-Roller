package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Utils {
	
	public static void save(File file, RollsList savedRolls, Settings settings){
		try {
			FileOutputStream fs = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fs);
			
			ArrayList<Serializable> objects = new ArrayList<>();
			objects.add(savedRolls);
			objects.add(settings);
			
			oos.writeObject(objects);
			oos.flush();
			
			oos.close();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Serializable> load(File file){
		ArrayList<Serializable> save = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			save = (ArrayList<Serializable>) ois.readObject();
			
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(save != null)
			return save;
		else
			return null;
	}

}
