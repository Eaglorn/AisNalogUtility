package ru.eaglorn.aisnalogutility.utils;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.win32.W32APIOptions;

public interface Shell32X extends Shell32 {
	public static class SHELLEXECUTEINFO extends Structure {
		public int cbSize = size();
		public int dwHotKey;
		public int fMask;
		public HINSTANCE hInstApp;
		public HKEY hKeyClass;
		public HANDLE hMonitor;
		public HANDLE hProcess;
		public HWND hwnd;
		public WString lpClass;
		public WString lpDirectory;
		public WString lpFile;
		public Pointer lpIDList;
		public WString lpParameters;

		public WString lpVerb;
		public int nShow;

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected List getFieldOrder() {
			return Arrays.asList(new String[] { "cbSize", "fMask", "hwnd", "lpVerb", "lpFile", "lpParameters",
					"lpDirectory", "nShow", "hInstApp", "lpIDList", "lpClass", "hKeyClass", "dwHotKey", "hMonitor",
					"hProcess", });
		}
	}

	Shell32X INSTANCE = Native.load("shell32", Shell32X.class, W32APIOptions.UNICODE_OPTIONS);
	int SE_ERR_ACCESSDENIED = 5;
	int SE_ERR_DLLNOTFOUND = 32;
	int SE_ERR_FNF = 2;
	int SE_ERR_OOM = 8;
	int SE_ERR_PNF = 3;
	int SE_ERR_SHARE = 26;
	int SEE_MASK_NOCLOSEPROCESS = 0x00000040;
	int SW_HIDE = 0;
	int SW_MAXIMIZE = 3;
	int SW_MINIMIZE = 6;
	int SW_RESTORE = 9;

	int SW_SHOW = 5;

	int SW_SHOWDEFAULT = 10;

	int SW_SHOWMAXIMIZED = 3;

	int SW_SHOWMINIMIZED = 2;

	int SW_SHOWMINNOACTIVE = 7;

	int SW_SHOWNA = 8;

	int SW_SHOWNOACTIVATE = 4;

	int SW_SHOWNORMAL = 1;

	int ShellExecute(int i, String lpVerb, String lpFile, String lpParameters, String lpDirectory, int nShow);

	boolean ShellExecuteEx(SHELLEXECUTEINFO lpExecInfo);

}
