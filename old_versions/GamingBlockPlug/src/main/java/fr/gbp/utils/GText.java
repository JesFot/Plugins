package fr.gbp.utils;

public class GText
{
	public static enum MLang
	{
		en_EN(0, "english", "en_EN.yml"),
		en_US(1, "english_USA", "en_US.yml"),
		fr_FR(2, "francais", "fr_FR.yml"),
		DEFAULT(-1, "default-lang", "en_US.yml", "en");
		
		protected int id;
		protected String name, genericLang, file;
		
		MLang(int p_id, String p_name, String p_file)
		{
			this.name = p_name;
			this.file = p_file;
			this.genericLang = this.toString().split("_")[0].toLowerCase();
			this.id = p_id;
		}
		
		MLang(int p_id, String p_name, String p_file, String gen)
		{
			this.name = p_name;
			this.file = p_file;
			this.genericLang = gen;
			this.id = p_id;
		}
		
		public String getFile()
		{
			return this.file;
		}
		
		public int getID()
		{
			return this.id;
		}
		
		public static MLang getByID(int id)
		{
			for(MLang l : MLang.values())
			{
				if(l.id == id)
				{
					return l;
				}
			}
			return MLang.DEFAULT;
		}
		
		public static MLang getByName(String name)
		{
			name = name.toLowerCase();
			for(MLang l : MLang.values())
			{
				if(name.contains(l.genericLang.toLowerCase()))
				{
					if(name.contains(l.toString().split("_")[1].toLowerCase()))
					{
						return l;
					}
				}
			}
			return MLang.DEFAULT;
		}
	}
}