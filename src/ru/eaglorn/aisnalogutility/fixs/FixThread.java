package ru.eaglorn.aisnalogutility.fixs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import ru.eaglorn.aisnalogutility.AisNalogUtility;
import ru.eaglorn.aisnalogutility.LoadingThread;
import ru.eaglorn.aisnalogutility.config.Data;
import ru.eaglorn.aisnalogutility.config.InstalledFixs;

public class FixThread extends Thread {

	public static boolean isAllFix = false;
	public static boolean isLastFix = false;

	public static void decompress7ZipEmbedded(File source, File destination) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder().inheritIO().command("c://AisNalogUtility//7zip/7z.exe", "x",
				source.getAbsolutePath(), "-o" + destination.getAbsolutePath(), "-aoa");
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			ProcessBuilder pb = new ProcessBuilder().inheritIO().command("taskkill", "/IM", "");
            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
        }
		
		Data.INSTALLED_FIXS.getInstalledFixs();
		
		boolean isContains = true;
		
		for (Fix fix : Data.FIXS) {
			if(!Data.INSTALLED_FIXS.FIXS.contains(fix.NAME)) {
				isContains = false;
			}
		}
		
		if(!isContains && isLastFix) {
			AisNalogUtility.sendMessage("Все доступные фиксы для текущей версии уже установлены!");
			LoadingThread.IS_RUN = false;
			AisNalogUtility.APP.setVisible(false);
			AisNalogUtility.APP.dispose();
		} else {
			for (Fix fix : Data.FIXS) {
				if (fix.CHECKED || isAllFix || (isLastFix && !Data.INSTALLED_FIXS.FIXS.contains(fix.NAME))) {
					String pathFix = Data.CONFIG_APP.NET_PATH + "\\promfix\\" + fix.NAME;
					try {
						LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: распаковка  фикса " + fix.NAME;
						decompress7ZipEmbedded(new File(pathFix), new File(AisNalogUtility.AIS_PATH));
						if(!Data.INSTALLED_FIXS.FIXS.contains(fix.NAME)) {
							Data.INSTALLED_FIXS.FIXS.add(fix.NAME);
						}
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
						AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
					} 
				}
			}
			
			try {
				FileWriter writer = new FileWriter("c:\\AisNalogUtility\\config\\installedfixs.json");
		        writer.write(new Gson().toJson(Data.INSTALLED_FIXS, InstalledFixs.class));
		        writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
			}
	
			try {
				LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: индексация распакованных фиксов.";
				String[] commands = { "CommonComponents.Catalog.IndexationUtility.exe" };
				AisNalogUtility.processBuilderStart(AisNalogUtility.AIS_PATH + "Client\\", commands);
			} catch (InterruptedException e) {
				e.printStackTrace();
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
			}
		}
	}
}
