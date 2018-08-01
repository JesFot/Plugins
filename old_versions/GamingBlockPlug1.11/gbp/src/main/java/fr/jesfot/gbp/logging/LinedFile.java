package fr.jesfot.gbp.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class LinedFile
{
	private File file;
	private List<String> lines;
	
	public LinedFile(final File f)
	{
		this.file = f;
		this.lines = new ArrayList<String>();
	}
	
	public LinedFile readFile()
	{
		if(this.lines == null)
		{
			this.lines = new ArrayList<String>();
		}
		try
		{
			FileInputStream fis = new FileInputStream(this.file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null;
			while((line = br.readLine()) != null)
			{
				this.lines.add(line);
			}
			
			br.close();
			fis.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return this;
	}
	
	public LinedFile writeFile()
	{
		if(this.lines == null || this.lines.isEmpty())
		{
			return this;
		}
		try
		{
			FileOutputStream fos = new FileOutputStream(this.file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(String line : this.lines)
			{
				bw.write(line);
				bw.newLine();
			}
			
			bw.close();
			fos.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return this;
	}
	
	public boolean inBounds(final int index)
	{
		if(index >= 0 && index < this.lines.size())
		{
			return true;
		}
		return false;
	}
	
	public String getLine(final int index)
	{
		if(!this.inBounds(index))
		{
			return null;
		}
		return this.lines.get(index);
	}
	
	public LinedFile setLine(final int index, final String line)
	{
		if(!this.inBounds(index))
		{
			return this;
		}
		this.lines.set(index, line);
		return this;
	}
	
	public LinedFile addLine(final String line)
	{
		this.lines.add(line);
		return this;
	}
	
	public LinedFile removeLine(final int index)
	{
		if(!this.inBounds(index))
		{
			return this;
		}
		List<String> newArray = new ArrayList<String>();
		for(int i = 0; i < this.lines.size(); i++)
		{
			if(i != index)
			{
				newArray.add(this.lines.get(i));
			}
		}
		this.lines = newArray;
		return this;
	}
	
	public LinedFile clearLine(final int index)
	{
		if(!this.inBounds(index))
		{
			return this;
		}
		this.lines.set(index, "");
		return this;
	}
	
	public LinedFile clearAll()
	{
		this.lines.clear();
		return this;
	}
	
	public int size()
	{
		return this.lines.size();
	}
}