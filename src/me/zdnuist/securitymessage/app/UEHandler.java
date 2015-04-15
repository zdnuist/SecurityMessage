package me.zdnuist.securitymessage.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import android.util.Log;


public class UEHandler implements Thread.UncaughtExceptionHandler {
	private MyApp softApp;

	public UEHandler(MyApp app) {
		softApp = app;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// print
		long threadId = thread.getId();
		Log.w("ANDROID_ZD", "Thread.getName()=" + thread.getName() + " id=" + threadId + " state=" + thread.getState());
		Log.w("ANDROID_ZD", "Error[" + info + "]");
		if (threadId != 1) {
			// 此处示例跳转到汇报异常界面。
		} else {
			// 此处示例发生异常后，重新启动应用
//			// kill App Progress
//			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private void write2ErrorLog(File file, String content) {
		FileOutputStream fos = null;
		try {
			if (file.exists()) {
				// 清空之前的记录
				file.delete();
			} else {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}