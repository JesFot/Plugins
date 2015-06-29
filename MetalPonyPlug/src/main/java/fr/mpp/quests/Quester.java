package fr.mpp.quests;

import java.util.LinkedHashMap;
import java.util.UUID;

public class Quester
{
	UUID id;
	
	public LinkedHashMap<Quest, Integer> currentQuests = new LinkedHashMap<Quest, Integer>();
}