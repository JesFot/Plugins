package fr.jesfot.gbp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils
{
	public static void copy(InputStream is, File dest) throws IOException, NullPointerException, FileNotFoundException, SecurityException
	{
		OutputStream os = new FileOutputStream(dest);
		byte buffer[] = new byte[12];
		int readed;
		while((readed = is.read(buffer, 0, 12)) > 0)
		{
			os.write(buffer, 0, readed);
		}
		os.flush();
		os.close();
	}
}
