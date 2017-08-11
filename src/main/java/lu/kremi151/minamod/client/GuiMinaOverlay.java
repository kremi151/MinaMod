package lu.kremi151.minamod.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.util.ScreenLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinaOverlay extends Gui {
	private Minecraft mc;
	private static FontRenderer fontRenderer;

	private static ResourceLocation barsTexture;
	
	private final LinkedList<ScreenLayer> layers = new LinkedList<>();

	public GuiMinaOverlay(Minecraft mc) {
		super();
		// We need this to invoke the render engine.
		this.mc = mc;
		fontRenderer = mc.fontRenderer;
		barsTexture = new ResourceLocation(MinaMod.MODID,
				"textures/gui/overlay.png");
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event) { // NO_UCD (unused code)
		/*GlStateManager.pushMatrix();
		EntityPlayer p = Minecraft.getMinecraft().player;
		RenderHelper.renderLeash(p.posX - 5.0, p.posY + 1.0, p.posZ - 1.0, p.posX + 5.0, p.posY + 2.0, p.posZ - 1.5, event.getPartialTicks());
		GlStateManager.popMatrix();*/
		
		if (!event.isCancelable() && event.getType() == ElementType.CHAT) {
			for(ScreenLayer layer : layers) {
				layer.draw(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
			}
		}
	}
	
	public boolean isLayerActive(Class<? extends ScreenLayer> clazz){
		for(ScreenLayer layer : layers) {
			if(layer.getClass() == clazz) {
				return true;
			}
		}
		return false;
	}
	
	public void addLayer(ScreenLayer layer){
		this.layers.add(layer);
	}
	
	public void resetAllLayers(){
		resetLayers(layer -> true);
	}
	
	public void resetLayers(Class<? extends ScreenLayer> clazz){
		resetLayers(layer -> layer.getClass() == clazz);
	}
	
	public void resetLayers(Predicate<ScreenLayer> predicate){
		Iterator<ScreenLayer> it = this.layers.iterator();
		while(it.hasNext()) {
			if(predicate.test(it.next())) {
				it.remove();
			}
		}
	}

	/**
	 * Draws a value bar with a height of 11 pixels and a bg width of 128 pixels
	 * 
	 * @param caption
	 * @param u
	 * @param v
	 * @param x
	 * @param y
	 * @param percentage
	 */
	private void drawTile(int x, int y, int icon_u, int icon_v, int value,
			int max_value) {
		mc.getTextureManager().bindTexture(barsTexture);
		double q = ((double) value / (double) max_value);
		this.drawTexturedModalRect(x, y, 0, 0, 45, 22);
		this.drawTexturedModalRect(x + 3, y + 3, icon_u, icon_v, 16, 16);
		this.drawTexturedModalRect(x + 22, y + 13, 0, 22, (int) (q * 22d), 4);
		// this.drawString(fontRenderer, caption, x + 2, y + 2,
		// MinaUtils.convertRGBToDecimal(255, 255, 255));
	}

	// private IBlockState getBlockWithMetaLooked(MinaPlayer player){
	// if(mc.getRenderViewEntity().rayTrace(200, 1.0F) != null){
	// Vec3 hitVec = mc.getRenderViewEntity().rayTrace(200, 1.0F).hitVec;
	// BlockPos pos = new BlockPos(hitVec);
	// IBlockState bs = player.getPlayer().worldObj.getBlockState(pos);
	// return bs;
	// }
	// return null;
	// }
}
