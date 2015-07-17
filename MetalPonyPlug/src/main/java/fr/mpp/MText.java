package fr.mpp;

public class MText
{
	public static enum MLang
	{
		en_EN(0, "english", "en_EN.yml"),
		fr_FR(2, "francais", "fr_FR.yml"),
		en_US(1, "english_USA", "en_US.yml"),
		DEFAULT(-1, "default-lang", "en_US.yml", "en");
		
		protected int id;
		protected String name;
		protected String genericLang;
		protected String file;
		
		MLang(int id, String name, String file)
		{
			this.name = name;
			this.file = file;
			this.genericLang = this.toString().split("_")[0].toLowerCase();
			this.id = id;
		}
		
		MLang(int id, String name, String file, String gen)
		{
			this.name = name;
			this.file = file;
			this.genericLang = gen;
			this.id = id;
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