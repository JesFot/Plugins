package fr.jesfot.gbp.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

public class GSecurityLogger
{
	private LinedFile log;
	private File logFile;
	private File logFolder;
	private String backupFile;
	
	//private boolean autoSave = false;
	
	public GSecurityLogger(File folder, String logFileName)
	{
		this.logFolder = folder;
		this.logFile = new File(folder, logFileName);
		/*try
		{
			this.backupFile = FileUtils.readFileToString(this.logFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
		//this.log = new LinedFile(this.logFile);
		//this.prepare();
	}
	
	public String getDate()
	{
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return df.format(date);
	}
	
	protected void prepare()
	{
		if(!this.logFolder.exists())
		{
			this.logFolder.mkdirs();
		}
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
		Date date = new Date();
		String dateText = df.format(date);
		if(this.logFile.exists())
		{
			this.log.readFile();
			try
			{
				String exdate = this.log.getLine(0);
				this.logFile.renameTo(new File(this.logFolder, this.logFile.getName() + "." + exdate));
				this.logFile.createNewFile();
				this.log.readFile().addLine(dateText).addLine("Starting new log file...").writeFile();
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				this.logFile.createNewFile();
				this.log.readFile().addLine(dateText).addLine("Starting new log file...").writeFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasChanged()
	{
		try
		{
			String state = FileUtils.readFileToString(this.logFile);
			if(state.contentEquals(this.backupFile))
			{
				return true;
			}
			return false;
		}
		catch(IOException e)
		{
			return true;
		}
	}
	
	public void save()
	{
		this.log.writeFile();
		try
		{
			this.backupFile = FileUtils.readFileToString(this.logFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setAutoSave(boolean state)
	{
		//this.autoSave = state;
	}
	
	public void reload()
	{
		this.log.readFile();
		try
		{
			this.backupFile = FileUtils.readFileToString(this.logFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void info(final String message)
	{
		this.log(Level.INFO, message);
	}
	
	public void warn(final String message)
	{
		this.log(Level.WARNING, message);
	}
	
	public void severe(final String message)
	{
		this.log(Level.SEVERE, message);
	}
	
	public void config(final String message)
	{
		this.log(Level.CONFIG, message);
	}
	
	public void fine(final String message)
	{
		this.log(Level.FINE, message);
	}
	
	public void all(final String message)
	{
		this.log(Level.ALL, message);
	}
	
	/*public void log(Level level, final String message)
	{
		if(this.autoSave)
		{
			this.reload();
		}
		this.log.addLine("[" + this.getDate() + "] [" + level.getName() + "]: " + message);
		if(this.autoSave)
		{
			this.save();
		}
	}*/
	
	public void log(Level lvl, final String message)
	{
		String f = "[" + this.getDate() + "] [" + lvl.getName() + "]: " + message;
		try
		{
			if(!this.logFolder.exists())
			{
				this.logFolder.mkdirs();
			}
			if(!this.logFile.exists())
			{
				this.logFile.createNewFile();
			}
			
			FileWriter fw = new FileWriter(this.logFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(f);
			pw.flush();
			pw.close();
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}