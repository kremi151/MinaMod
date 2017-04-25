package lu.kremi151.minamod.item;

import java.util.UUID;

import com.google.common.collect.Multimap;

import lu.kremi151.minamod.MinaArmorMaterial;
import lu.kremi151.minamod.MinaMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ItemCustomArmor extends ItemArmor {

	protected static final UUID[] ARMOR_MODIFIERS;

	static {
		ARMOR_MODIFIERS = ObfuscationReflectionHelper.getPrivateValue(ItemArmor.class, null, "ARMOR_MODIFIERS",
				"field_185084_n");
	}

	public ItemCustomArmor(ArmorMaterial mat, int renderIndex, EntityEquipmentSlot slot) {
		super(mat, renderIndex, slot);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot,
			ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == this.armorType) {
			if (this.getArmorMaterial() == MinaArmorMaterial.OBSIDIAN) {
				multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
						new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Speed modifier",
								(double) -0.015, 0));
				multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(),
						new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Knockback modifier",
								(double) 0.1, 0));
			}else if (this.getArmorMaterial() == MinaArmorMaterial.PLATINUM) {
				multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
						new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Speed modifier",
								(double) -0.005, 0));
			}
		}

		return multimap;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		String pfx = getUnlocalizedName().substring(5).split("_")[0];
		switch (slot) {
		case HEAD:
		case CHEST:
		case FEET:
			return MinaMod.MODID + ":textures/models/armor/" + pfx + "_layer_1.png";
		case LEGS:
			return MinaMod.MODID + ":textures/models/armor/" + pfx + "_layer_2.png";
		default:
			return null;
		}

	}

}
