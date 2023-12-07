package ru.eaglorn.aisnalogutility.config;

import java.util.ArrayList;

import ru.eaglorn.aisnalogutility.fixs.Fix;

public class Data {
	public static ConfigAdmin CONFIG_ADMIN;
	public static ConfigApp CONFIG_APP = new ConfigApp();
	public static InstalledFixs INSTALLED_FIXS = new InstalledFixs();
	
	public static ArrayList<Fix> FIXS = new ArrayList<>();
}
