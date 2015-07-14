package fr.mpp;


public class MDebug
{
	protected MetalPonyPlug mpp;
	protected boolean debugLog, debugPlayer;
	public int level = 20;
	
	public MDebug(MetalPonyPlug mppl)
	{
		this.mpp = mppl;
	}
	
	public void setDebugLog(boolean neo)
	{
		this.debugLog = neo;
	}
	
	public void setPlayerDebugLog(boolean neo)
	{
		this.debugPlayer = neo;
	}
	
	public void broad(String message)
	{
		if(this.debugPlayer)
		{
			this.mpp.broad("[Mpp-DEBUG] "+message);
		}
		if(this.debugLog)
		{
			this.mpp.getLogger().info("[Mpp - DEBUG] "+message);
		}
	}

	public void broad(String message, int im)
	{
		if(!(im <= level))
		{
			return;
		}
		this.mpp.broad("[DEBUG]"+message);
	}
}