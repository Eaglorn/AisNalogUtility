package ru.eaglorn.aisnalogutility;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import ru.eaglorn.aisnalogutility.config.ConfigAdmin;
import ru.eaglorn.aisnalogutility.config.ConfigApp;
import ru.eaglorn.aisnalogutility.config.Data;
import ru.eaglorn.aisnalogutility.fixs.Fix;
import ru.eaglorn.aisnalogutility.fixs.FixCheckboxListCellRenderer;
import ru.eaglorn.aisnalogutility.fixs.FixListSelectionDocument;
import ru.eaglorn.aisnalogutility.fixs.FixThread;
import ru.eaglorn.aisnalogutility.utils.AdvApi32;

public class AisNalogUtility {

	public static String AIS_PATH = "";
	public static String AIS_VERSION = "";

	public static JFrame APP = null;

	public final static String APP_VERSION = "5";

	public static JButton BUTTON_INSTALL_ALL_FIX = null;
	public static JButton BUTTON_INSTALL_CHECKED_FIX = null;

	public static LoadingThread LOAD_THREAD = null;

	public static Logger LOGGER = null;

	static JSplitPane SPLIT_PANE = null;
	
	public static JButton buttonLastFix() {
		BUTTON_INSTALL_ALL_FIX = new JButton("Установить новые фиксы АИС Налог-3 ПРОМ");

		BUTTON_INSTALL_ALL_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				FixThread.isLastFix = true;
				Thread fix_thread = new FixThread();
				fix_thread.start();
			}
		});
		return BUTTON_INSTALL_ALL_FIX;
	}

	public static JButton buttonAllFix() {
		BUTTON_INSTALL_ALL_FIX = new JButton("Установить все фиксы для АИС Налог-3 ПРОМ");

		BUTTON_INSTALL_ALL_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				FixThread.isAllFix = true;
				Thread fix_thread = new FixThread();
				fix_thread.start();
			}
		});
		return BUTTON_INSTALL_ALL_FIX;
	}

	public static JButton buttonCheckedFix() {
		BUTTON_INSTALL_CHECKED_FIX = new JButton("Установить выбранные фиксы для АИС Налог-3 ПРОМ");

		BUTTON_INSTALL_CHECKED_FIX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!((JButton) e.getSource()).isEnabled())
					return;

				LoadingThread.IS_RUN = true;
				LOAD_THREAD = new LoadingThread();
				LOAD_THREAD.start();
				Thread fix_thread = new FixThread();
				fix_thread.start();
			}
		});
		return BUTTON_INSTALL_CHECKED_FIX;
	}

	private static boolean checkPrivileges() {
		File testPriv = new File("c:\\Windows\\");
		if (!testPriv.canWrite())
			return false;
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return false;
		} finally {
			if (fileTest != null)
				fileTest.delete();
		}
		return true;
	}

	public static JList<String> getFixList() {
		DefaultListModel<String> fix_list_init = new DefaultListModel<>();

		FixListSelectionDocument listSelectionDocument = new FixListSelectionDocument();
		File dir = new File(Data.CONFIG_APP.NET_PATH + "\\promfix");
		File[] arrFiles = dir.listFiles();
		List<File> lst = Arrays.asList(arrFiles);

		int i = 1;
		for (File file : lst) {
			fix_list_init.addElement(file.getName());
			Data.FIXS.add(new Fix(i, file.getName()));
			i++;
		}

		JList<String> fix_list = new JList<>(fix_list_init);
		fix_list.setCellRenderer(new FixCheckboxListCellRenderer<String>());
		fix_list.addListSelectionListener(listSelectionDocument);
		fix_list.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					if (index0 >= 0)
						Data.FIXS.get(index0).CHECKED = false;
					super.removeSelectionInterval(index0, index1);
				} else {
					if (index0 >= 0)
						Data.FIXS.get(index0).CHECKED = true;
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		fix_list.setLayoutOrientation(JList.VERTICAL);
		return fix_list;
	}

	public static Logger getLogger() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("C:/AisNalogUtility/log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.print("");
		writer.close();

		Logger logger = Logger.getLogger("AisNalogUtilityLog");
		FileHandler fh;

		try {

			fh = new FileHandler("C:/AisNalogUtility/log.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return logger;
	}

	public static JPanel getPanelFix() {
		JScrollPane scroll_pane_fix = new JScrollPane();
		scroll_pane_fix.setViewportView(getFixList());

		JPanel panel_fix = new JPanel(new BorderLayout());

		panel_fix.add(scroll_pane_fix);

		return panel_fix;
	}

	public static JPanel getPanelInstall() {

		JPanel panel_install = new JPanel();
		panel_install.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(4, 0, 0, 0);
		panel_install.setLayout(layout);

		String path = "";
		String version = "";
		if (new File("c:\\Program Files (x86)\\Ais3Prom\\Client\\version.txt").exists()) {
			AIS_PATH = "c:\\Program Files (x86)\\Ais3Prom\\";
			path = AIS_PATH + "Client\\version.txt";
		} else if (new File("c:\\Program Files\\Ais3Prom\\Client\\version.txt").exists()) {
			AIS_PATH = "c:\\Program Files\\Ais3Prom\\";
			path = AIS_PATH + "Client\\version.txt";
		} else {
			version = "АИС Налог-3 ПРОМ не установлен";
		}

		File file = new File(path);

		if (file.exists()) {
			try {
				version = String.join("", FileUtils.readLines(file, "UTF-8"));
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
				e.printStackTrace();
			}
		}
		
		File dir = new File("ftp:////fap.regions.tax.nalog.ru//Aisnalog3//AisNalog3PROM//");
		File[] arrFiles = dir.listFiles();
		List<File> lst = Arrays.asList(arrFiles);
		
		File ais_dir = null;
		
		for (File f : lst) {
			if (ais_dir != null) {
				if (f.lastModified() > ais_dir.lastModified()) ais_dir = f;
			} else ais_dir = f;
		}
		String ais_dir_name = "";
		try {
			ais_dir_name = ais_dir.getName();
			ais_dir_name = ais_dir_name.replace("_", ".");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}

		AIS_VERSION = version;

		JLabel old_version = new JLabel("Установленная версия АИС Налог-3 ПРОМ: " + AIS_VERSION, SwingConstants.CENTER);
		JLabel new_version = new JLabel("Новая версия АИС Налог-3 ПРОМ: " + Data.CONFIG_APP.VERSION,
				SwingConstants.CENTER);

		panel_install.add(old_version);
		panel_install.add(buttonAllFix());
		panel_install.add(buttonCheckedFix());
		panel_install.add(new_version);

		return panel_install;
	}

	public static void main(String[] args) {
		LOGGER = getLogger();

		String arg = args[0];

		if (arg.equals("-run"))
			runAppRun();
		if (arg.equals("-app"))
			runAppMain();
		if (arg.equals("-auth"))
			runAppAuth();
	}

	public static void processBuilderStart(String path, String[] commands) throws InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("\"" + path + commands[0] + "\"");
		pb.redirectError();
		try {
			Process process = pb.start();
			process.waitFor();
			LoadingThread.IS_RUN = false;
			APP.setVisible(false);
			APP.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	private static void runApp() {
		ConfigApp.getConfig();

		APP = new JFrame();
		APP.setTitle("Аис Налог-3 ПРОМ Утилита для установки фиксов (v" + APP_VERSION + ")");
		APP.setSize(650, 550);
		APP.setVisible(true);
		APP.setLocationRelativeTo(null);
		APP.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		SPLIT_PANE = new JSplitPane();
		SPLIT_PANE.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		SPLIT_PANE.setLeftComponent(getPanelInstall());
		SPLIT_PANE.setRightComponent(getPanelFix());

		APP.add(SPLIT_PANE);
	}

	public static void runAppAuth() {
		Console terminal = System.console();
		String login = terminal.readLine("Input local admin login: ");
		char[] password = terminal.readPassword("Input local admin password: ");

		Data.CONFIG_ADMIN = new ConfigAdmin(login, password);

		String crypt = new Gson().toJson(Data.CONFIG_ADMIN, ConfigAdmin.class);
		crypt = Crypt.encrypt(crypt);
		try (FileWriter file = new FileWriter("c:\\AisNalogUtility\\config\\auth")) {
			file.write(crypt);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	public static void runAppMain() {
		if (!checkPrivileges()) {
			Elevator.executeAsAdministrator("c:\\AisNalogUtility\\java\\bin\\javaw.exe ",
					"-jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app");
		} else {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					runApp();
				}
			});
		}
	}

	public static void runAppRun() {
		try {
			FileReader file = new FileReader(new File("c:\\AisNalogUtility\\config\\auth"));
			try (Scanner scan = new Scanner(file)) {
				String gson = "";
				while (scan.hasNextLine()) {
					gson += scan.nextLine();
				}
				gson = Crypt.decrypt(gson);
				Data.CONFIG_ADMIN = new Gson().fromJson(gson, ConfigAdmin.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				LOGGER.log(Level.WARNING, e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, e.getMessage());
		}

		WString nullW = null;
		PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
		STARTUPINFO startupInfo = new STARTUPINFO();
		boolean result = false;
		result = AdvApi32.INSTANCE.CreateProcessWithLogonW(new WString(Data.CONFIG_ADMIN.LOGIN), nullW,
				new WString(Data.CONFIG_ADMIN.PASSWORD), AdvApi32.LOGON_WITH_PROFILE, nullW,
				new WString(
						"c:\\AisNalogUtility\\java\\bin\\javaw.exe -jar c:\\AisNalogUtility\\app\\AisNalogUtility.jar -app"),
				AdvApi32.CREATE_NEW_CONSOLE, null, nullW, startupInfo, processInformation);
		if (!result) {
			int error = Kernel32.INSTANCE.GetLastError();
			System.out.println("OS error #" + error);
			LOGGER.log(Level.WARNING, "OS error #" + error);
			System.out.println(Kernel32Util.formatMessageFromLastErrorCode(error));
			LOGGER.log(Level.WARNING, Kernel32Util.formatMessageFromLastErrorCode(error));
		}
	}

	public static void sendMessage(String text) {
		JOptionPane.showMessageDialog(APP, text, "Важно!", JOptionPane.WARNING_MESSAGE);
	}
}
