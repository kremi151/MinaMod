package lu.kremi151.minamod.block.tileentity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

public class TileEntityCampfire extends TileEntity implements ITickable{

	private final Set<CookingItem> cooking = new HashSet<>();
	
	public boolean trackItem(EntityItem item){
		if(!cooking.contains(item)){
			ItemStack res = FurnaceRecipes.instance().getSmeltingResult(item.getEntityItem());
			int ticks = 200 * item.getEntityItem().getCount();
			return cooking.add(new CookingItem(item, res, ticks));
		}else{
			return false;
		}
	}

	@Override
	public void update() {
		if(!world.isRemote && cooking.size() > 0){
			Iterator<CookingItem> it = cooking.iterator();
			while(it.hasNext()){
				CookingItem citem = it.next();
				if(!citem.itemRef.isDead){
					EntityItem itemRef = citem.itemRef;
					//itemRef.setNoDespawn();
					itemRef.setAgeToCreativeDespawnTime();
					if(System.currentTimeMillis() % 5 == 0){
						MinaMod.getProxy().spawnParticleEffectToAllAround(EnumParticleEffect.SMOKE, world, itemRef.posX, itemRef.posY + 0.35d, itemRef.posZ, 1.0f, 1.0f, 1.0f);
					}
					if(--citem.ticksLeft <= 0){
						if(!citem.nextStage.isEmpty()){
							ItemStack res = citem.nextStage.copy();
							res.setCount(itemRef.getEntityItem().getCount());
							itemRef.setEntityItemStack(res);
							it.remove();
						}else{
							itemRef.setFire(5);
						}
					}
				}else{
					it.remove();
				}
			}
		}
	}
	
	private static class CookingItem{
		private final EntityItem itemRef;
		private int ticksLeft = 200;
		private final ItemStack nextStage;
		
		private CookingItem(EntityItem itemRef, ItemStack nextStage, int ticksLeft){
			this.itemRef = itemRef;
			this.ticksLeft = ticksLeft;
			this.nextStage = nextStage;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj == this){
				return true;
			}else if(obj instanceof CookingItem){
				return ((CookingItem)obj).itemRef == this.itemRef;
			}else if(obj instanceof EntityItem){
				return obj == this.itemRef;
			}else{
				return false;
			}
		}
		
		@Override
		public int hashCode(){
			return itemRef.hashCode();
		}
	}
}
