package fr.mpp.mpp;

import fr.mpp.mpp.classes.*;

public enum Classes
{
	Piocheur("Piocheur", new CPiocheur()),
	Miner("Mineur", new CMiner()),
	Farmer("Fermier", new CFarmer()),
	Defensor("Defenseur", new CDefensor()),
	Chevalier("Chevalier", new CChevalier()),
	Assassin("Assassin", new CAssassin()),
	Killer("Meurtrier", new CKiller()),
	Cavaler("Cavalier", new CCavaler()),
	Maire("Maire", new CMaire()),
	Prince("Prince", new CPrince()),
	Princess("Princesse", new CPrincess()),
	Vendor("Marchand", new CVendor()),
	Architect("Architecte", new CArchitect()),
	Bucheron("Bucheron", new CBucheron()),
	Farmer_chinois("Farmer chinois", new CCheFarmer()),
	Noobie("Newbie", new CNoobie()),
	Invite("Invité", new CInvite()),
	Regular("Habitué", new CRegular()),
	Kami("Kami-sama", new CKami()),
	Cheater("Cheater", new CCheater()),
	Notch("Notch", new CNotch()),
	Redstoner("Redstonneur", new CRedstoner()),
	Null(),
	Default(Noobie);
	
	private String name = "";
	private IClasses classe;
	
	Classes(String name, IClasses classe)
	{
		this.name = name;
		this.classe = classe;
		this.classe.setName(name);
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
	public IClasses getClasse()
	{
		return this.classe;
	}
	public void setClasse(IClasses p_class)
	{
		this.classe = p_class;
	}
}