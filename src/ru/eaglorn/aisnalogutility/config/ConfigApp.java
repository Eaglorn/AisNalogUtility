package ru.eaglorn.aisnalogutility.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ru.eaglorn.aisnalogutility.AisNalogUtility;

public class ConfigApp {
	public String NET_PATH = "";
	public String VERSION = "";
	
	public static void getConfig() {
		try {
			JsonReader reader = new JsonReader(new FileReader("c:\\AisNalogUtility\\config\\config.json"));
			Data.CONFIG_APP = new Gson().fromJson(reader, ConfigApp.class);
		} catch (IOException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}
}
