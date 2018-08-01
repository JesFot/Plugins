package me.jesfot.gamingblockplug.security;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.jesfot.gamingblockplug.plugin.GamingBlockPlug;
import me.jesfot.gamingblockplug.utils.DataUtils;
import me.jesfot.gamingblockplug.utils.NumberUtils;
import me.unei.configuration.api.IConfiguration;

public class VariablesSystem
{
	private IConfiguration storage;
	
	public VariablesSystem(GamingBlockPlug plugin)
	{
		this.storage = plugin.getNBT(null);
	}
	
	protected Map<String, ObjData> store = new HashMap<String, ObjData>();
	
	public enum ObjType
	{
		Object("object"),
		String("string"),
		Boolean("boolean"),
		Int("integer"),
		Float("float"),
		Double("double");
		
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
			return this.name;
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

	public void storeObject(String key, ObjData object)
	{
		this.store.put(key, object);
	}
	
	public ObjData getObject(String key)
	{
		if (!this.store.containsKey(key))
		{
			return null;
		}
		return this.store.get(key);
	}
	
	public ObjType getType(String key)
	{
		if (!this.store.containsKey(key))
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
		this.storeObject(key, new ObjData(new Boolean(value), ObjType.Boolean));
	}
	
	public void storeInt(String key, int value)
	{
		this.storeObject(key, new ObjData(new Integer(value), ObjType.Int));
	}
	
	public void storeFloat(String key, float value)
	{
		this.storeObject(key, new ObjData(new Float(value), ObjType.Float));
	}
	
	public void storeDouble(String key, double value)
	{
		this.storeObject(key, new ObjData(new Double(value), ObjType.Double));
	}
	
	public String getString(String key) throws IllegalClassFormatException
	{
		if (!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if (this.getType(key).equals(ObjType.String))
		{
			return (String) this.getObject(key).obj;
		}
		throw new IllegalClassFormatException();
	}
	
	public boolean getBool(String key) throws IllegalClassFormatException
	{
		if (!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if (this.getType(key).equals(ObjType.Boolean))
		{
			return ((Boolean) this.getObject(key).obj).booleanValue();
		}
		throw new IllegalClassFormatException();
	}
	
	public int getInt(String key) throws IllegalClassFormatException
	{
		if (!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if (this.getType(key).equals(ObjType.Int))
		{
			return ((Integer) this.getObject(key).obj).intValue();
		}
		throw new IllegalClassFormatException();
	}
	
	public float getFloat(String key) throws IllegalClassFormatException
	{
		if (!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if (this.getType(key).equals(ObjType.Float))
		{
			return ((Float) this.getObject(key).obj).floatValue();
		}
		throw new IllegalClassFormatException();
	}
	
	public double getDouble(String key) throws IllegalClassFormatException
	{
		if (!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if (this.getType(key).equals(ObjType.Double))
		{
			return ((Double) this.getObject(key).obj).doubleValue();
		}
		throw new IllegalClassFormatException();
	}
	
	public String getToString(String key)
	{
		if (!this.store.containsKey(key))
		{
			return null;
		}
		switch (this.getType(key))
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
			case Double:
				try
				{
					return Double.toString(this.getDouble(key));
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
		DataUtils.safeReload(this.storage);
		for (Entry<String, ObjData> entry : this.store.entrySet())
		{
			IConfiguration sub = this.storage.getSubSection("Variables." + entry.getKey());
			sub.setString("Key", entry.getKey());
			if (!entry.getKey().equalsIgnoreCase("plugin"))
			{
				sub.setString("Value", this.getToString(entry.getKey()));
				sub.setString("Type", this.getType(entry.getKey()).toString());
			}
			else
			{
				sub.setString("Value", "false");
				sub.setString("Type", ObjType.Boolean.toString());
			}
		}
		this.storage.save();
	}
	
	public void loadFromFile()
	{
		DataUtils.safeReload(this.storage);
		IConfiguration section = this.storage.getSubSection("Variables");
		for (String key : section.getKeys())
		{
			IConfiguration sub = this.storage.getSubSection(key);
			String obj = sub.getString("Value");
			String type = sub.getString("Type");
			int t = this.getIdForType(type);
			switch (t)
			{
				case 0:
					this.storeString(key, obj);
					break;
				case 1:
					this.storeInt(key, NumberUtils.toInteger(obj, 0));
					break;
				case 2:
					this.storeBool(key, Boolean.getBoolean(obj));
					break;
				case 3:
					this.storeFloat(key, NumberUtils.toFloat(obj, 0));
					break;
				case 4:
					this.storeDouble(key, NumberUtils.toDouble(obj, 0));
					break;
					
				default:
					this.storeObject(key, new ObjData(obj, ObjType.Object));
			}
		}
	}
	
	public void remove(String name)
	{
		this.store.remove(name);
	}
	
	private int getIdForType(String tyoe)
	{
		if (tyoe.contentEquals("string"))
		{
			return 0;
		}
		else if (tyoe.contentEquals("integer"))
		{
			return 1;
		}
		else if (tyoe.contentEquals("boolean"))
		{
			return 2;
		}
		else if (tyoe.contentEquals("float"))
		{
			return 3;
		}
		else if (tyoe.contentEquals("double"))
		{
			return 4;
		}
		else
		{
			return -1;
		}
	}
	
	public static int getIdForTypes(String tyoe)
	{
		if (tyoe.equalsIgnoreCase("string") || tyoe.equalsIgnoreCase("str"))
		{
			return 0;
		}
		else if (tyoe.equalsIgnoreCase("integer") || tyoe.equalsIgnoreCase("int"))
		{
			return 1;
		}
		else if (tyoe.equalsIgnoreCase("boolean") || tyoe.equalsIgnoreCase("bool"))
		{
			return 2;
		}
		else if (tyoe.equalsIgnoreCase("float"))
		{
			return 3;
		}
		else if (tyoe.equalsIgnoreCase("double"))
		{
			return 4;
		}
		else
		{
			return -1;
		}
	}
}
