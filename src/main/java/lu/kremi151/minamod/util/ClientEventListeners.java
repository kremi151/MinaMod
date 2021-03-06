package lu.kremi151.minamod.util;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.GuiAmuletInventory;
import lu.kremi151.minamod.client.GuiPlayerStats;
import lu.kremi151.minamod.network.MessageJetpack;
import lu.kremi151.minamod.network.MessageOpenGui;
import lu.kremi151.minamod.network.MessageTriggerOpenStatsAchievement;
import lu.kremi151.minamod.network.MessageUseAmulet;
import lu.kremi151.minamod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
//@Mod.EventBusSubscriber
public class ClientEventListeners {
	
	private boolean was_pressed = false;

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if(FeatureList.enable_jetpack){
			if(!was_pressed && ClientProxy.KEY_JETPACK.isKeyDown()){
				MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageJetpack(true));
				was_pressed = true;
			}else if(was_pressed && !ClientProxy.KEY_JETPACK.isKeyDown()){
				MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageJetpack(false));
				was_pressed = false;
			}
		}
		if(ClientProxy.KEY_PLAYER_STATS.isKeyDown() && !FMLClientHandler.instance().isGUIOpen(GuiPlayerStats.class)){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageTriggerOpenStatsAchievement());
			FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().player, new GuiPlayerStats());
		}
		if(ClientProxy.KEY_AMULETS.isKeyDown() && !FMLClientHandler.instance().isGUIOpen(GuiAmuletInventory.class)){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageOpenGui(IDRegistry.guiIdAmulets));
		}
		if(ClientProxy.KEY_AMULET_1.isKeyDown()){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageUseAmulet(0));
		}
		if(ClientProxy.KEY_AMULET_2.isKeyDown()){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageUseAmulet(1));
		}
		if(ClientProxy.KEY_AMULET_3.isKeyDown()){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageUseAmulet(2));
		}
	}
	
//	@SubscribeEvent
//	public void onClientTick(ClientTickEvent event){
//		ClientProxy.using_jetpack = false;
//		if(MinaMod.getProxy().getClientInstance() != null){
//			ClientProxy p = MinaMod.getProxy().getClientInstance();
//			if(p.keyBindingJetpack.isKeyDown()){
//				EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
////				MinaMod.println("Jetpack pressed");
//				ItemStack chestplateArmor = ep.getCurrentArmor(2);
//				if(chestplateArmor != null){
//					if(chestplateArmor.getItem() == MinaItems.itemJetpack){
//						//ep.motionY = 0.24;
//						int c = ItemJetpack.getChargeLeft(chestplateArmor);
//						if(c > 0){
//							ep.motionY = 0.12;
//							ep.isAirBorne = true;
//							ep.capabilities.allowFlying = true;
//							ClientProxy.using_jetpack = true;
//							ItemJetpack.setCharge(chestplateArmor, c-1);
//						}
//					}
//				}
//			}else{
////				MinaMod.println("Jetpack no longer pressed");
//			}
//		}
//	}
	
	/*@SubscribeEvent
	public void onEntitySpawn(ClientDisconnectionFromServerEvent event) { // NO_UCD (unused code)
		MinaMod.getProxy().clearOverlays();
	}*/
	
	@SubscribeEvent
	public void onPreRenderEntity(RenderLivingEvent.Pre event){
		if(!(event.getEntity() instanceof EntityPlayer)){
			EntityLivingBase entity = event.getEntity();
			int lvl = MinaUtils.getSuperMobLevel(entity);
			if(lvl > 0) {
				if(lvl == 1){
					GlStateManager.color(0.35f, 0.5f, 0.35f);
				}else if(lvl == 2){
					GlStateManager.color(0.35f, 0.35f, 0.5f);
				}else if(lvl >= 3){
					GlStateManager.color(0.5f, 0.35f, 0.35f);
				}
				if(entity.getRNG().nextFloat() < 0.05f) {
					entity.world.spawnParticle(EnumParticleTypes.FLAME, entity.posX + entity.getRNG().nextDouble() - 0.5, entity.posY + (entity.getRNG().nextDouble() * entity.height), entity.posZ + entity.getRNG().nextDouble() - 0.5, 0.0, 0.02, 0.0);
				}
			}
		}
	}
}
