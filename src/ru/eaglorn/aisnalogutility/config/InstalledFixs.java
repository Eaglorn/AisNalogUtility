package ru.eaglorn.aisnalogutility.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ru.eaglorn.aisnalogutility.AisNalogUtility;

public class InstalledFixs {
	public String VERSION = "";
	public ArrayList<String> FIXS = new ArrayList<String>();
	
	public static void getInstalledFixs() {
		try {
            File file = new File("c:\\AisNalogUtility\\config\\installedfixs.json");
            if (file.createNewFile()) {
            	Data.INSTALLED_FIXS.VERSION = AisNalogUtility.AIS_VERSION;
            	FileWriter writer = new FileWriter("c:\\AisNalogUtility\\config\\installedfixs.json");
                writer.write(new Gson().toJson(Data.INSTALLED_FIXS, InstalledFixs.class));
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
        }
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\installedfixs.json"));
			Data.INSTALLED_FIXS = new Gson().fromJson(reader, InstalledFixs.class);
			if(!Data.INSTALLED_FIXS.VERSION.equals(AisNalogUtility.AIS_VERSION)) {
				Data.INSTALLED_FIXS.FIXS = new ArrayList<String>();
				FileWriter writer = new FileWriter("c:\\AisNalogUtility\\config\\installedfixs.json");
                writer.write(new Gson().toJson(Data.INSTALLED_FIXS, InstalledFixs.class));
                writer.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
}
