package lu.kremi151.minamod.enums;

import java.util.Random;

import javax.annotation.Nullable;

import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public enum EnumHerb implements IMixtureApplicator, IStringSerializable{

	BLACK(0, 		"black",		MinaUtils.convertRGBToDecimal(90, 90, 90),		false,	true,	0.35f,	0.01f,	-1,	-1,	-1),
	BORDEAUX(1, 	"bordeaux",		MinaUtils.convertRGBToDecimal(155, 49, 36),		false,	true,	0.35f,	0.05f,	0,	-1,	-1),
	RED(2, 			"red",			MinaUtils.convertRGBToDecimal(229, 38, 50),		true,	true,	0.35f,	0.03f,	1,	-1,	-1),
	FOREST_GREEN(3, "forest_green",	MinaUtils.convertRGBToDecimal(28, 154, 86),		false,	true,	0.35f,	0.05f,	-1,	0,	-1),
	OLIVE(4, 		"olive",		MinaUtils.convertRGBToDecimal(132, 130, 47),	false,	true,	0.35f,	0.05f,	0,	0,	-1),
	ORANGE(5, 		"orange",		MinaUtils.convertRGBToDecimal(237, 129, 66),	false,	true,	0.35f,	0.03f,	1,	0,	-1),
	GREEN(6, 		"green",		MinaUtils.convertRGBToDecimal(136, 200, 70),	true,	true,	0.35f,	0.05f,	-1,	1,	-1),
	SPRING_GREEN(7, "spring_green",	MinaUtils.convertRGBToDecimal(193, 222, 106),	false,	true,	0.35f,	0.05f,	0,	1,	-1),
	YELLOW(8, 		"yellow",		MinaUtils.convertRGBToDecimal(248, 244, 44),	false,	true,	0.35f,	0.03f,	1,	1,	-1),
	DARK_BLUE(9, 	"dark_blue",	MinaUtils.convertRGBToDecimal(7, 6, 134),		false,	true,	0.35f,	0.05f,	-1,	-1,	0),
	PURPLE(10, 		"purple",		MinaUtils.convertRGBToDecimal(102, 56, 150),	false,	true,	0.35f,	0.05f,	0,	-1,	0),
	MAGENTA(11, 	"magenta",		MinaUtils.convertRGBToDecimal(229, 0, 139),		false,	true,	0.35f,	0.03f,	1,	-1,	0),
	PINE_GREEN(12, 	"pine_green",	MinaUtils.convertRGBToDecimal(33, 136, 114),	false,	true,	0.35f,	0.05f,	-1,	0,	0),
	GRAY(13, 		"gray",			MinaUtils.convertRGBToDecimal(128, 128, 128),	true,	true,	0.35f,	0.25f,	0,	0,	0),
	SALMON(14, 		"salmon",		MinaUtils.convertRGBToDecimal(239, 145, 136),	false,	true,	0.35f,	0.03f,	1,	0,	0),
	LIGHT_GREEN(15, "light_green",	MinaUtils.convertRGBToDecimal(133, 242, 151),	false,	true,	0.35f,	0.05f,	-1,	1,	0),
	PALE_GREEN(16, 	"pale_green",	MinaUtils.convertRGBToDecimal(155, 247, 160),	false,	true,	0.35f,	0.05f,	0,	1,	0),
	KAKI(17, 		"kaki",			MinaUtils.convertRGBToDecimal(239, 230, 151),	false,	true,	0.35f,	0.03f,	1,	1,	0),
	BLUE(18, 		"blue",			MinaUtils.convertRGBToDecimal(21, 19, 240),		true,	true,	0.35f,	0.07f,	-1,	-1,	1),
	VIOLET(19, 		"violet",		MinaUtils.convertRGBToDecimal(93, 61, 152),		false,	true,	0.35f,	0.07f,	0,	-1,	1),
	PINK(20, 		"pink",			MinaUtils.convertRGBToDecimal(237, 125, 177),	false,	true,	0.35f,	0.05f,	1,	-1,	1),
	ROYAL_BLUE(21, 	"royal_blue",	MinaUtils.convertRGBToDecimal(47, 106, 183),	false,	true,	0.35f,	0.07f,	-1,	0,	1),
	PERIWINKLE(22, 	"periwinkle",	MinaUtils.convertRGBToDecimal(124, 114, 180),	false,	true,	0.35f,	0.07f,	0,	0,	1),
	LAVENDER(23, 	"lavender",		MinaUtils.convertRGBToDecimal(239, 154, 194),	false,	true,	0.35f,	0.05f,	1,	0,	1),
	CYAN(24, 		"cyan",			MinaUtils.convertRGBToDecimal(5, 253, 255),		false,	true,	0.35f,	0.07f,	-1,	1,	1),
	TURQUOISE(25, 	"turquoise",	MinaUtils.convertRGBToDecimal(170, 240, 242),	false,	true,	0.35f,	0.07f,	0,	1,	1),
	WHITE(26, 		"white",		MinaUtils.convertRGBToDecimal(255, 255, 255),	false,	true,	0.35f,	0.50f,	1,	1,	1),
	GOLD(27, 		"gold",			MinaUtils.convertRGBToDecimal(211, 175, 31),	false,	false,	0.00f,	0.00f,	0,	0,	0);
	
	private static final EnumHerb[] ID_ARRAY;
	public static final EnumHerb[] NATURAL_HERBS;
	public static final int MAX_ID;
	
	private final byte herb_id, atkEffect, defEffect, spdEffect;
	private final boolean natural, plantable;
	private final int tint;
	private final String variant_name;
	private final float probability, defMutability;
	
	private EnumHerb(int herb_id, String variant_name, int tint, boolean natural, boolean plantable, float probability, float defMutability, int atkEffect, int defEffect, int spdEffect){
		if(herb_id < 0)throw new IllegalArgumentException("Invalid id: " + herb_id);
		this.herb_id = (byte)herb_id;
		this.natural = natural;
		this.plantable = plantable;
		this.tint = tint;
		this.variant_name = variant_name;
		this.probability = probability;
		this.defMutability = defMutability;
		this.atkEffect = (byte) atkEffect;
		this.defEffect = (byte) defEffect;
		this.spdEffect = (byte) spdEffect;
	}
	
	public boolean isNatural(){
		return natural;
	}
	
	public boolean isPlantable(){
		return plantable;
	}
	
	public byte getHerbId(){
		return herb_id;
	}
	
	public int getTint(){
		return tint;
	}
	
	public float getProbability(){
		return probability;
	}
	
	public float getDefaultMutability(){
		return defMutability;
	}
	
	public boolean isOriginal(){
		return herb_id < 27;
	}
	
	@Override
	public int getStatEffect(EnumPlayerStat stat){
		switch(stat){
		case ATTACK:
			return atkEffect;
		case DEFENSE:
			return defEffect;
		case SPEED:
			return spdEffect;
		default:
			return 0;
		}
	}
	
	public static EnumHerb getByHerbId(byte herb_id){
		if(herb_id < 0 || herb_id >= values().length){
			throw new IndexOutOfBoundsException("Invalid id: " + herb_id);
		}
		return ID_ARRAY[herb_id];
	}
	
	@Nullable
	public static EnumHerb mutate(EnumHerb h1, EnumHerb h2, Random rand){
		return mutate(h1, h2, rand.nextInt(8));
	}
	
	/**
	 * Calculates a random mutation
	 * @param h1 The "receiving" herb whom the calculated attributes are based on
	 * @param h2 The "giving" herb whose attributes are randomly applicated based on the given mask
	 * @param mask A 3-bit integer. Can be bigger but only the first three bits are considered
	 * @return The mutation
	 */
	@Nullable
	public static EnumHerb mutate(EnumHerb h1, EnumHerb h2, int mask){
		if(h1.isOriginal() && h2.isOriginal()){//Is intended to work with MinaMod types, in case I will make an API...
			boolean b1 = (mask & 1) > 0;
			boolean b2 = (mask & 2) > 0;
			boolean b3 = (mask & 4) > 0;
			int a = MathHelper.clamp(b1?h2.getStatEffect(EnumPlayerStat.ATTACK):h1.getStatEffect(EnumPlayerStat.ATTACK), -1, 1) + 1;
			int d = MathHelper.clamp(b2?h2.getStatEffect(EnumPlayerStat.DEFENSE):h1.getStatEffect(EnumPlayerStat.DEFENSE), -1, 1) + 1;
			int s = MathHelper.clamp(b3?h2.getStatEffect(EnumPlayerStat.SPEED):h1.getStatEffect(EnumPlayerStat.SPEED), -1, 1) + 1;
			int id = (s * 9) + (d * 3) + a;
			return EnumHerb.getByHerbId((byte)id);
		}
		
		return null;
	}
	
	public static EnumHerb getRandomNatural(Random r){
		return NATURAL_HERBS[r.nextInt(NATURAL_HERBS.length)];
	}
	
	static{
		EnumHerb[] vv = values();
		ID_ARRAY = new EnumHerb[vv.length];
		int natCount = 0;
		int max_id = 0;
		for(int i = 0 ; i < vv.length ; i++){
			if(vv[i].herb_id > max_id){
				max_id = vv[i].herb_id;
			}
			ID_ARRAY[vv[i].herb_id]=vv[i];
			if(vv[i].isNatural())natCount++;
		}
		MAX_ID = max_id;
		NATURAL_HERBS = new EnumHerb[natCount];
		int natIdx = 0;
		for(int i = 0 ; i < vv.length ; i++){
			if(vv[i].isNatural()){
				NATURAL_HERBS[natIdx] = vv[i];
				natIdx++;
			}
		}
	}

	@Override
	public String getName() {
		return variant_name;
	}
}
