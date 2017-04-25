package lu.kremi151.minamod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import lu.kremi151.minamod.MinaToolMaterials;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class ItemKatana extends ItemSword{
	
	private final float attackDamage;

	public ItemKatana() {
		super(MinaToolMaterials.KATANA);
        this.attackDamage = 3.0F + MinaToolMaterials.KATANA.getDamageVsEntity();
	}
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0.0, 0));
        }

        return multimap;
    }

}
