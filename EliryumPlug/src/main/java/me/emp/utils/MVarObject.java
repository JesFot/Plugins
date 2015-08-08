package me.emp.utils;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.emp.config.EVarConfig;

public class MVarObject
{
	private EVarConfig varConfig;
	
	public MVarObject(EVarConfig p_varConfig)
	{
		this.varConfig = p_varConfig;
	}
	
	public enum ObjType
	{
		Object("object"),
		String("string"),
		Boolean("boolean"),
		Int("integer"),
		Float("float");
		
		private String name;
		
		private ObjType(String p_name)
		{
			this.name = p_name;
		}
		
		/**
		 * @return type in a string
		 */
		@Override
		public String toString()
		{
			return name;
		}
	}
	public class ObjData
	{
		protected Object obj;
		protected ObjType type;
		
		public ObjData()
		{
			this.type = ObjType.Object;
		}
		
		public ObjData(Object object)
		{
			this.obj = object;
			this.type = ObjType.Object;
		}
		
		public ObjData(Object object, ObjType p_type)
		{
			this.obj = object;
			this.type = p_type;
		}
	}
	protected Map<String, ObjData> store = new HashMap<String, ObjData>();
	
	public void storeObject(String key, ObjData object)
	{
		this.store.put(key, object);
	}
	
	public ObjData getObject(String key)
	{
		if(!this.store.containsKey(key))
		{
			return null;
		}
		return this.store.get(key);
	}
	
	public ObjType getType(String key)
	{
		if(!this.store.containsKey(key))
		{
			return null;
		}
		return this.getObject(key).type;
	}
	
	public void storeString(String key, String value)
	{
		this.storeObject(key, new ObjData(value, ObjType.String));
	}
	
	public void storeBool(String key, boolean value)
	{
		this.storeObject(key, new ObjData(value, ObjType.Boolean));
	}
	
	public void storeInt(String key, int value)
	{
		this.storeObject(key, new ObjData(value, ObjType.Int));
	}
	
	public void storeFloat(String key, float value)
	{
		this.storeObject(key, new ObjData(value, ObjType.Float));
	}
	
	public String getString(String key) throws IllegalClassFormatException
	{
		if(!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if(this.getType(key).equals(ObjType.String))
		{
			return (String)this.getObject(key).obj;
		}
		throw new IllegalClassFormatException();
	}
	
	public boolean getBool(String key) throws IllegalClassFormatException
	{
		if(!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if(this.getType(key).equals(ObjType.Boolean))
		{
			return (boolean)this.getObject(key).obj;
		}
		throw new IllegalClassFormatException();
	}
	
	public int getInt(String key) throws IllegalClassFormatException
	{
		if(!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if(this.getType(key).equals(ObjType.Int))
		{
			return (int)this.getObject(key).obj;
		}
		throw new IllegalClassFormatException();
	}
	
	public float getFloat(String key) throws IllegalClassFormatException
	{
		if(!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if(this.getType(key).equals(ObjType.Float))
		{
			return (float)this.getObject(key).obj;
		}
		throw new IllegalClassFormatException();
	}
	
	public String getToString(String key)
	{
		if(!this.store.containsKey(key))
		{
			return null;
		}
		switch(this.getType(key))
		{
		case String:
			try
			{
				return this.getString(key);
			}
			catch (IllegalClassFormatException e)
			{
				return "";
			}
		case Boolean:
			try
			{
				return (this.getBool(key) ? "true" : "false");
			}
			catch (IllegalClassFormatException e)
			{
				return "";
			}
		case Int:
			try
			{
				return Integer.toString(this.getInt(key));
			}
			catch (IllegalClassFormatException e)
			{
				return "";
			}
		case Float:
			try
			{
				return Float.toString(this.getFloat(key));
			}
			catch (IllegalClassFormatException e)
			{
				return "";
			}
		case Object:
			return this.getObject(key).obj.toString();
		default:
			return null;
		}
	}
	
	public void storeToFile()
	{
		Set<String> values = this.varConfig.getVarConfig().getKeys(true);
		for(String key : values)
		{
			this.varConfig.getVarConfig().set(key, null);
		}
		this.varConfig.getVarConfig().set("plugin", "false;boolean");
		for(String n : this.store.keySet())
		{
			if(!n.equalsIgnoreCase("plugin"))
			{
				this.varConfig.getVarConfig().set(n, (this.getToString(n)+";"+this.getType(n).toString()).toString());
			}
		}
		this.varConfig.saveVarConfig();
	}
	
	public void getFromFile()
	{
		this.varConfig.reloadVarConfig();
		this.store.clear();
		Set<String> values = this.varConfig.getVarConfig().getKeys(false);
		for(String key : values)
		{
			String obj = this.varConfig.getVarConfig().getString(key).split(";")[0];
			String type = this.varConfig.getVarConfig().getString(key).split(";")[1];
			switch(type)
			{
			case "string":
				this.storeString(key, obj);
				break;
			case "integer":
				this.storeInt(key, Integer.parseInt(obj));
				break;
			case "boolean":
				this.storeBool(key, Boolean.getBoolean(obj.toLowerCase()));
				break;
			case "float":
				this.storeFloat(key, Float.parseFloat(obj));
				break;
			default:
				this.storeObject(key, new ObjData((Object)obj, ObjType.Object));
				break;
			}
		}
	}

	public void remove(String name)
	{
		this.store.remove(name);
		this.storeToFile();
	}
}