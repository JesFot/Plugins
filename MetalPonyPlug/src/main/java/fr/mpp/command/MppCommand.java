package fr.mpp.command;

public class MppCommand
{
	private final MetalPonyPlug mpp;

	public MppCommand(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}

	private void resetPlugin()
	{
		// Code ...
	}

	private void resetPlayer(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			mpp.setMeta(player, "MPP", null); // A completer
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (args >= 1)
		{
			if (args[0].equalsIgnoreCase("reset"))
			{
				if (args[1].equalsIgnoreCase("all"))
				{
					resetPlugin();
				}
				else
				{
					resetPlayer(sender);
				}
			}
			return true;
		}
		else
		{
			// Usage ...
			return false;
		}
	}
}
