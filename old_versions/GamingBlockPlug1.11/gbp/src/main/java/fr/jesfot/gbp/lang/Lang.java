package fr.jesfot.gbp.lang;

import java.util.HashMap;
import java.util.Map;

public class Lang
{
	private static Map<Integer, Lang> BY_ID = new HashMap<Integer, Lang>();
	private static Map<String, Lang> BY_NAME = new HashMap<String, Lang>();
	
	public static Lang DEFAULT = new Lang(-1, "default-lang", "en_US.lang", "en_US");
	public static Lang en_US = new Lang(0, "english_USA", "en_US.lang", "en_US");
	public static Lang fr_FR = new Lang(1, "francais", "fr_FR.lang", "fr_FR");
	
	protected int id;
	protected String name, genericLang, file;
	
	public Lang(int p_id, String p_name, String p_file, String pointer)
	{
		this.name = p_name;
		this.file = p_file;
		this.genericLang = pointer;
		this.id = p_id;
		BY_ID.put(new Integer(this.id), this);
		BY_NAME.put(pointer, this);
	}
	
	public String getFile()
	{
		return this.file;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public String toString()
	{
		return this.genericLang;
	}
	
	public static Lang getByID(int id)
	{
		Lang result = BY_ID.get(new Integer(id));
		if(result == null)
		{
			return DEFAULT;
		}
		return result;
	}
	
	public static Lang getByName(String name)
	{
		Lang result = BY_NAME.get(name);
		if(result == null)
		{
			return DEFAULT;
		}
		return result;
	}
}