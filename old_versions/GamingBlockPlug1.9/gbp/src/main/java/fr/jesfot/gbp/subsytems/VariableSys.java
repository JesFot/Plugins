package fr.jesfot.gbp.subsytems;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.jesfot.gbp.configuration.NBTSubConfig;
import fr.jesfot.gbp.utils.Utils;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;

public class VariableSys
{
	private NBTSubConfig varConfig;
	
	public VariableSys(NBTSubConfig p_varConfig)
	{
		this.varConfig = p_varConfig;
	}
	
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
			return ((Boolean)this.getObject(key).obj).booleanValue();
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
			return ((Integer)this.getObject(key).obj).intValue();
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
			return ((Float)this.getObject(key).obj).floatValue();
		}
		throw new IllegalClassFormatException();
	}
	
	public double getDouble(String key) throws IllegalClassFormatException
	{
		if(!this.store.containsKey(key))
		{
			throw new IllegalClassFormatException();
		}
		if(this.getType(key).equals(ObjType.Double))
		{
			return ((Double)this.getObject(key).obj).doubleValue();
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
		NBTTagList list = new NBTTagList();
		for(Entry<String, ObjData> entry : this.store.entrySet())
		{
			NBTTagCompound e = new NBTTagCompound();
			e.setString("Key", entry.getKey());
			if(!entry.getKey().equalsIgnoreCase("plugin"))
			{
				e.setString("Value", this.getToString(entry.getKey()));
				e.setString("Type", this.getType(entry.getKey()).toString());
			}
			else
			{
				e.setString("Value", "false");
				e.setString("Type", ObjType.Boolean.toString());
			}
			list.add(e);
		}
		this.varConfig.readNBTFromFile().setTag("VariableList", list).writeNBTToFile();
	}
	
	public void getFromFile()
	{
		NBTTagCompound tmp = new NBTTagCompound();
		NBTTagList list = this.varConfig.readNBTFromFile().getCopy().getList("VariableList", tmp.getTypeId());
		if(!list.isEmpty())
		{
			for(int i = 0; i < list.size(); i++)
			{
				NBTTagCompound comp = list.get(i);
				String key = comp.getString("Key");
				String obj = comp.getString("Value");
				String type = comp.getString("Type");
				int t = this.getIdForType(type);
				switch(t)
				{
				case 0:
					this.storeString(key, obj);
					break;
				case 1:
					this.storeInt(key, Utils.toInt(obj, 0));
					break;
				case 2:
					this.storeBool(key, Boolean.getBoolean(obj));
					break;
				case 3:
					this.storeFloat(key, Utils.toFloat(obj, 0));
					break;
				case 4:
					this.storeDouble(key, Utils.toDouble(obj, 0));
					break;
				default:
					this.storeObject(key, new ObjData((Object)obj, ObjType.Object));
				}
			}
		}
	}

	public void remove(String name)
	{
		this.store.remove(name);
		this.storeToFile();
	}
	
	private int getIdForType(String tyoe)
	{
		if(tyoe.contentEquals("string"))
		{
			return 0;
		}
		else if(tyoe.contentEquals("integer"))
		{
			return 1;
		}
		else if(tyoe.contentEquals("boolean"))
		{
			return 2;
		}
		else if(tyoe.contentEquals("float"))
		{
			return 3;
		}
		else if(tyoe.contentEquals("double"))
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
		if(tyoe.equalsIgnoreCase("string") || tyoe.equalsIgnoreCase("str"))
		{
			return 0;
		}
		else if(tyoe.equalsIgnoreCase("integer") || tyoe.equalsIgnoreCase("int"))
		{
			return 1;
		}
		else if(tyoe.equalsIgnoreCase("boolean") || tyoe.equalsIgnoreCase("bool"))
		{
			return 2;
		}
		else if(tyoe.equalsIgnoreCase("float"))
		{
			return 3;
		}
		else if(tyoe.equalsIgnoreCase("double"))
		{
			return 4;
		}
		else
		{
			return -1;
		}
	}
}