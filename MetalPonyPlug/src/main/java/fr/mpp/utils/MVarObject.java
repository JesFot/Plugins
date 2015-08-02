package fr.mpp.utils;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.Map;

public class MVarObject
{
	public enum ObjType
	{
		Object,
		String,
		Boolean,
		Int,
		Float;
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
}