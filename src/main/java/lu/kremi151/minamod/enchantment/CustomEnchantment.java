package lu.kremi151.minamod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class CustomEnchantment extends Enchantment{
	
	private int minLevel = 1;
	private int maxLevel = 1;
	private final boolean treasure;

	public CustomEnchantment(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots, boolean treasure){
		super(rarityIn, typeIn, slots);
		this.treasure = treasure;
	}

	public CustomEnchantment(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots){
		this(rarityIn, typeIn, slots, false);
	}
	
	/**
     * Returns the minimum level that the enchantment can have.
     */
	@Override
    public int getMinLevel()
    {
        return minLevel;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
	@Override
    public int getMaxLevel()
    {
        return maxLevel;
    }

	@Override
    public boolean isTreasureEnchantment()
    {
        return treasure;
    }
	
	public CustomEnchantment setLevelRange(int min, int max){ // NO_UCD (unused code)
		if(min > max)throw new IllegalArgumentException("minLevel value higher than maxLevel value for custom enchantment \"" + this.getName() + "\"");
		this.minLevel = min;
		this.maxLevel = max;
		return this;
	}

}
