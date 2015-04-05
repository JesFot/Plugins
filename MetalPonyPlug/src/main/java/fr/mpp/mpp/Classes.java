package fr.mpp.mpp;

import fr.mpp.mpp.classes.*;

public enum Classes
{
	Piocheur("Piocheur", "Piocheur", new CPiocheur()),
	Miner("Mineur", "Mineur", new CMiner()),
	Farmer("Fermier", "Fermier", new CFarmer()),
	Defensor("Défenseur", "Defenseur", new CDefensor()),
	Chevalier("Chevalier", "Chevalier", new CChevalier()),
	Assassin("Assassin", "Assassin", new CAssassin()),
	Killer("Meurtrier", "Meutrier", new CKiller()),
	Cavaler("Cavalier", "Cavalier", new CCavaler()),
	Maire("Maire", "Maire", new CMaire()),
	Prince("Prince", "Prince", new CPrince()),
	Princess("Princesse", "Princesse", new CPrincess()),
	Vendor("Marchand", "Marchand", new CVendor()),
	Architect("Architecte", "Architecte", new CArchitect()),
	Bucheron("Bucheron", "Bucheron", new CBucheron()),
	Farmer_chinois("Farmer chinois", "Farmer_chinois", new CCheFarmer()),
	Noobie("Newbie", "Noobie", new CNoobie()),
	Invite("Invité", "Invite", new CInvite()),
	Regular("Habitué", "Regular", new CRegular()),
	Kami("Kami-sama", "Dieu", new CKami()),
	Cheater("Cheater", "Tricheur", new CCheater()),
	Notch("Notch", "Notch", new CNotch()),
	Redstoner("Redstonneur", "Redstonneur", new CRedstoner()),
	Null(),
	Default(Noobie);
	
	private String name = "";
	private String appel = "";
	private IClasses classe;
	
	Classes(String name, String app, IClasses classe)
	{
		this.appel = app;
		this.name = name;
		this.classe = classe;
		this.classe.setName(this.name);
	}
	
	Classes(Classes classe)
	{
		this.name = classe.getName();
		this.appel = classe.getAppel();
		this.classe = classe.getClasse();
	}
	
	Classes()
	{
		this.name = "null";
		this.appel = "null";
		this.classe = null;
	}
	
	public String getName()
	{
		return this.name;
	}
	public String getAppel()
	{
		return this.appel;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setAppel(String app)
	{
		this.appel = app;
	}
	public IClasses getClasse()
	{
		return this.classe;
	}
	public void setClasse(IClasses p_class)
	{
		this.classe = p_class;
	}
}