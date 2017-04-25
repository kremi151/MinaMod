package lu.kremi151.minamod.enums;

import lu.kremi151.minamod.util.MinaUtils;

public enum EnumPlayerStat {
	ATTACK("attack", "gui.player_stats.attack", "gui.player_stats.atk_multiplicator", MinaUtils.convertRGBToDecimal(255, 128, 128)),
	DEFENSE("defense", "gui.player_stats.defense", "gui.player_stats.def_multiplicator", MinaUtils.convertRGBToDecimal(128, 255, 128)),
	SPEED("speed", "gui.player_stats.speed", "gui.player_stats.spd_multiplicator", MinaUtils.convertRGBToDecimal(128, 128, 255));
	
	private final int displayColor;
	private final float displayColorF[];
	private final String unlocalizedName, multplStr, id;
	
	private EnumPlayerStat(String id, String unlocalizedName, String multplStr, int displayColor){
		this.id = id;
		this.unlocalizedName = unlocalizedName;
		this.multplStr = multplStr;
		this.displayColor = displayColor;
		this.displayColorF = MinaUtils.convertDecimalToRGBFloat(displayColor);
	}
	
	public int getDisplayColor(){
		return displayColor;
	}
	
	public float[] getDisplayColorF(){
		return displayColorF;
	}
	
	public String getMultiplicatorString(){
		return multplStr;
	}
	
	public String getUnlocalizedName(){
		return unlocalizedName;
	}
	
	public String getId(){
		return id;
	}
}
