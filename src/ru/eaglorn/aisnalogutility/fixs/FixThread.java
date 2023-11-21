package ru.eaglorn.aisnalogutility.fixs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import ru.eaglorn.aisnalogutility.AisNalogUtility;
import ru.eaglorn.aisnalogutility.LoadingThread;
import ru.eaglorn.aisnalogutility.config.Data;

public class FixThread extends Thread {

	public static boolean isAllFix = false;

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
		
		for (Fix fix : Data.FIXS) {
			if (fix.CHECKED || isAllFix) {
				String pathFix = Data.CONFIG_APP.NET_PATH + "\\promfix\\" + fix.NAME;
				try {
					LoadingThread.LOAD_PROCESS_TEXT = "Статус выполнения: распаковка  фикса " + fix.NAME;
					decompress7ZipEmbedded(new File(pathFix), new File(AisNalogUtility.AIS_PATH));
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
				} 
			}
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
