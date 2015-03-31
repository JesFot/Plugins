package fr.mpp.mpp;

public class ClassesUtils
{
	public boolean isInZone(Localisation loc)
	{
		int mX = mpp.getConfig().getMaxX();
		int mY = mpp.getConfig().getMaxY();
		int mZ = mpp.getConfig().getMaxZ();

		int miX = mpp.getConfig().getMinX();
		int miY = mpp.getConfig().getMinY();
		int miZ = mpp.getConfig().getMinZ();

		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		if (x >= mX && x <= miX)
		{
			if (y >= mY && y <= miY)
			{
				if (z >= mZ && z <= miZ)
				{
					return true;
				}
			}
		}
		return false;
	}
}