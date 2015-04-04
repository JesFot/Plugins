package fr.mpp.mpp;

import java.lang.reflect.Constructor;

import fr.mpp.mpp.classes.*;

public enum Classes
{
	Piocheur("Piocheur", CPiocheur.class),
	Miner("Mineur", CMiner.class),
	Farmer("Fermier", CFarmer.class),
	Defensor("Defenseur", CDefensor.class),
	Chevalier("Chevalier", CChevalier.class),
	Assassin("Assassin", CAssassin.class),
	Killer("Meurtrier",  CKiller.class),
	Cavaler("Cavalier",  CCavaler.class),
	Maire("Maire",  CMaire.class),
	Prince("Prince",  CPrince.class),
	Princess("Princesse",  CPrincess.class),
	Vendor("Marchand",  CVendor.class),
	Architect("Architecte", CArchitect.class),
	Bucheron("Bucheron",  CBucheron.class),
	Farmer_chinois("Farmer chinois",  CCheFarmer.class),
	Noobie("bie",  CNoobie.class),
	Invite("Invité",  CInvite.class),
	Regular("Habitué",  CRegular.class),
	Kami("Kami-sama",  CKami.class),
	Cheater("Cheater",  CCheater.class),
	Notch("Notch",  CNotch.class),
	Redstoner("Redstonneur",  CRedstoner.class),
	Null(),
	Default(Noobie);
	
	private String name = "";
	private Class<? extends CClasses> classe;
	
	Classes(String name, Class<? extends CClasses> classe)
	{
		this.name = name;
		this.classe = classe;
	}
	
	Classes(Classes classe)
	{
		this.name = classe.getName();
		this.classe = classe.getClasse();
	}
	
	Classes()
	{
		this.name = "null";
		this.classe = null;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Class<? extends CClasses> getClasse()
	{
		return this.classe;
	}
	public Constructor<? extends CClasses> getNClasse()
	{
		Class<? extends CClasses> cl = this.classe;
		try {
			return cl.getConstructor();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setClasse(Class<? extends CClasses> p_class)
	{
		this.classe = p_class;
	}
}