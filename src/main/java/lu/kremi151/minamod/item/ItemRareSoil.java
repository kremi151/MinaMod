package lu.kremi151.minamod.item;

import java.util.Random;

import lu.kremi151.minamod.exceptions.InvalidIdException;
import lu.kremi151.minamod.util.weightedlist.MutableWeightedList;
import lu.kremi151.minamod.util.weightedlist.WeightedList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemRareSoil extends Item{
	
	private final WeightedList<Type> weightedTypes;

	public ItemRareSoil() {
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MATERIALS);
		
		MutableWeightedList<Type> wlist = WeightedList.create();
		for(Type type : Type.values()) {
			wlist.add(type, (double)type.weight);
		}
		this.weightedTypes = wlist.immutable();
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(Type type : Type.values()) {
			subItems.add(new ItemStack(itemIn, 1, type.meta));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + Type.getByMeta(stack.getMetadata(), Type.LANTHAN).unlocalizedName;
	}
	
	public static String[] getVariantNames() {
		String res[] = new String[Type.values().length];
		for(Type type : Type.values()) {
			res[type.ordinal()] = type.unlocalizedName;
		}
		return res;
	}
	
	public Type getRandomType(Random rand) {
		return weightedTypes.randomElement(rand);
	}
	
	public static enum Type{
		LANTHAN(0, "lanthan", 20), //For accus
		NEODYM(1, "neodym", 20), //Permanent magnets, electro motors
		COLTAN(2, "coltan", 10); //Condensators
		
		private final int meta, weight;
		private final String unlocalizedName;
		
		private Type(int meta, String unlocalizedName, int weight) {
			this.meta = meta;
			this.weight = weight;
			this.unlocalizedName = unlocalizedName;
		}
		
		public int getMeta() {
			return meta;
		}
		
		public static Type getByMeta(int meta, Type def) {
			for(Type type : values()) {
				if(type.meta == meta) {
					return type;
				}
			}
			return def;
		}
		
		public static Type getByMeta(int meta) throws InvalidIdException {
			Type res = getByMeta(meta, null);
			if(res == null) {
				throw new InvalidIdException("Unknown meta");
			}else {
				return res;
			}
		}
	}
}
