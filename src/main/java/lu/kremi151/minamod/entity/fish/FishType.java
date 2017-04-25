package lu.kremi151.minamod.entity.fish;

import java.util.List;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.entity.EntityFish;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FishType extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<FishType>{
	
	private final String name;
	private final ResourceLocation texture;
	private float sizeMin = 0.3f, sizeMax = 1.0f, sizeChild = 0.2f;

	public FishType(String name, ResourceLocation texture){
		this.name = name;
		this.texture = texture;
	}
	
	public final String getName(){
		return name;
	}
	
	@SideOnly(Side.CLIENT)
	public final ResourceLocation getTexture(EntityFish entity){
		return texture;
	}
	
	public boolean isFavoriteFood(ItemStack stack){
		return stack.getItem() == MinaItems.NAMIE_SEEDS;
	}
	
	public boolean canStayOutOfWater(EntityFish entity){
		return false;
	}
	
	public boolean canDespawn(){
		return false;
	}
	
	public void addCustomAI(List<EntityAIBase> list){}
	
	public final float getMinSize(){
		return sizeMin;
	}
	
	public final float getMaxSize(){
		return sizeMax;
	}
	
	public final float getChildSize(){
		return sizeChild;
	}
	
	public final FishType setMinSize(float size){
		this.sizeMin = size;
		return this;
	}
	
	public final FishType setMaxSize(float size){
		this.sizeMax = size;
		return this;
	}
	
	public final FishType setChildSize(float size){
		this.sizeChild = size;
		return this;
	}
	
}
