package ru.eaglorn.aisnalogutility;

import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LoadingThread extends Thread {

	static JPanel INSTALL_PANEL = null;

	public static boolean IS_RUN = true;
	public static String LOAD_PROCESS_TEXT = "";

	public static int LOAD_THREAD_INT = 0;
	public static JLabel LOAD_THREAD_JLABEL = null;
	public static boolean LOAD_THREAD_REVERSE = false;

	public static String LOAD_THREAD_TEXT = "";

	@Override
	public void run() {
		while (IS_RUN) {
			try {
				StringBuilder text = new StringBuilder();
				if (!LOAD_THREAD_REVERSE) {
					if (LOAD_THREAD_INT == 37)
						LOAD_THREAD_REVERSE = !LOAD_THREAD_REVERSE;
					for (int i = 0; i < 39; i++) {
						if (i == LOAD_THREAD_INT) {
							text.append("███");
						} else {
							text.append("_");
						}
					}
					LOAD_THREAD_INT++;
				} else {
					if (LOAD_THREAD_INT == 1)
						LOAD_THREAD_REVERSE = !LOAD_THREAD_REVERSE;
					for (int i = 0; i < 39; i++) {
						if (i == LOAD_THREAD_INT) {
							text.append("███");
						} else {
							text.append("_");
						}
					}
					LOAD_THREAD_INT--;
				}

				AisNalogUtility.APP.getContentPane().removeAll();
				AisNalogUtility.APP.setSize(565, 180);
				INSTALL_PANEL = new JPanel();
				StringBuilder labelJLabel = new StringBuilder();
				labelJLabel.append("<html><div style='text-align: center;'>Выполняется установка фиксов.<br>Во время установки не запускайте АИС Налог-3 ПРОМ!<br>После завершения установки программа закроется.<br><br>");
				labelJLabel.append(LOAD_PROCESS_TEXT).append("<br><br>").append(text.toString()).append("</div></html>");
				LOAD_THREAD_JLABEL = new JLabel(labelJLabel.toString(), SwingConstants.CENTER);
				INSTALL_PANEL.add(LOAD_THREAD_JLABEL);
				AisNalogUtility.APP.add(INSTALL_PANEL);
				AisNalogUtility.APP.revalidate();
				AisNalogUtility.APP.repaint();

				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
				AisNalogUtility.LOGGER.log(Level.WARNING, e.getMessage());
			}
		}
	}
}
