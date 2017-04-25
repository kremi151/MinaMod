package lu.kremi151.minamod.enums;

import java.util.Random;

public enum EnumFrostTempleType {
	BLACK(0),
	WHITE(1);
	
	private int type;
	
	EnumFrostTempleType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public static EnumFrostTempleType getByType(int type){
		for(EnumFrostTempleType t : values()){
			if(t.getType() == type){
				return t;
			}
		}
		return null;
	}
	
	public static EnumFrostTempleType getRandom(Random r){ // NO_UCD (unused code)
		return getByType(r.nextInt(values().length));
	}
}
