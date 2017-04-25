package lu.kremi151.minamod.enums;

public enum EnumIceGolhemState {
	DISABLED((byte)0),
	PASSIVE((byte)1),
	ZOMBIE((byte)2);
	
	private byte id;
	private static final EnumIceGolhemState[] LOOKUP;
	
	static{
		LOOKUP = new EnumIceGolhemState[values().length];
		for(EnumIceGolhemState s : values()){
			LOOKUP[(int)s.getStateId()] = s;
		}
	}
	
	EnumIceGolhemState(byte id){
		this.id = id;
	}
	
	public byte getStateId(){
		return id;
	}
	
	public static EnumIceGolhemState getFromId(byte id){
		if(id < LOOKUP.length)return LOOKUP[(int)id];
		return ZOMBIE;
	}
}
