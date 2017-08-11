package lu.kremi151.minamod.proxy;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.entity.EntityPenguin;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.packet.message.MessageSpawnParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy{

	public void registerRenderers() {}
	public void registerBuildInBlocks() {}
	public void registerStateMappings() {}
	public void registerCustomMeshDefinitions() {}
	public void registerKeyBindings(){}
	public void registerVariantNames() {}
	public void registerFluidModels() {}
	public void registerItemAndBlockColors() {}
	public void executeClientSide(Runnable r){}
	public void openBook(ItemStack book){}
	public void initClientEvents(){}
	public void setAchievementsCustomStringFormatters() {}
	public void showAchievementOverlay(String title, String desc, long duration, ItemStack icon) {}
	
	public void addScreenLayer(int id, boolean force){}
	
	public <T> Optional<T> tryGetClientSideResult(Supplier<T> clientCode){
		return Optional.empty();
	}

	public void spawnParticleEffect(EnumParticleEffect effect, World worldObj, double posX, double posY, double posZ, float red, float green, float blue){}
	
	public void spawnParticleEffectToAllAround(EnumParticleEffect effect, World worldObj, double posX, double posY, double posZ, float red, float green, float blue){
		if(!worldObj.isRemote)MinaMod.getMinaMod().getPacketDispatcher().sendToAllAround(new MessageSpawnParticleEffect(effect, posX, posY, posZ, red, green, blue), new TargetPoint(worldObj.provider.getDimension(), posX, posY, posZ, 40.0));
	}

	private final HashMap<String, HashMap<UUID, Long>> cooldown_map = new HashMap<String, HashMap<UUID, Long>>();
	
	public void checkInitialPenguinState(EntityPenguin entity){}
	
	public void executeServerSide(Runnable r){
		r.run();
	}
	
	public boolean isServerSide(){
		return true;
	}
	
    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
    	return ctx.getServerHandler().player;
    }
    
    public void registerItemWithOre(Item item, String universalName) {
    	registerItemWithOre(item, universalName, universalName);
    }
    
    public void registerItemWithOre(Item item, String name, String oreName, String... variantNames){
    	registerItem(item, name, variantNames);
    	OreDictionary.registerOre(oreName, item);
    }
    
    public void registerItem(Item item, String name, String... variantNames){
    	if(item.getRegistryName() == null){
    		item.setRegistryName(new ResourceLocation(MinaMod.MODID, name));
    	}
    	GameRegistry.register(item);
    }
    
    public void registerBlockWithOre(Block block, String universalName){
    	registerBlockWithOre(block, universalName, universalName);
    }
    
    public void registerBlockWithOre(Block block, String name, String oreName, String... variantNames){
    	registerBlock(block, name, variantNames);
    	OreDictionary.registerOre(oreName, block);
    }
    
    public void registerBlock(Block block, String name, String... variantNames){
    	GameRegistry.register(block.setRegistryName(new ResourceLocation(MinaMod.MODID, name)));
    	GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    
    public void registerBlockOnly(Block block, String name){
    	GameRegistry.register(block.setRegistryName(new ResourceLocation(MinaMod.MODID, name)));
    }
	
	private HashMap<UUID, Long> getCooldownCategory(String category){
		HashMap<UUID, Long> res = cooldown_map.get(category);
		if(res != null){
			return res;
		}else{
			res = new HashMap<UUID, Long>();
			cooldown_map.put(category, res);
			return res;
		}
	}
	
	public long getCooldownDate(String category, UUID player){
		HashMap<UUID, Long> cm = getCooldownCategory(category);
		Long res = cm.get(player);
		if(res != null){
			return res;
		}else{
			return 0;
		}
	}
	
	public void setCooldownDate(String category, UUID player, long cooldownDate){
		getCooldownCategory(category).put(player, cooldownDate);
	}
	
	public void clearCooldowns(){
		cooldown_map.clear();
	}
    
}
