package lu.kremi151.minamod.client;

import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.overlay.Overlayable;
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

	private LinkedList<Overlayable> overlayables = new LinkedList<Overlayable>();
	
	private ScreenLayer layer = null;

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
			if(!Minecraft.getMinecraft().gameSettings.showDebugInfo)drawOverlayable(event);

			if(layer != null){
				layer.draw(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
			}
		}
	}

	private void drawOverlayable(RenderGameOverlayEvent event) {
		if(MinaMod.getMinaConfig().isOverlayEnabled()){
			//
			// // Get our extended player properties and assign it locally so we can
			// easily access it
			// MinaPlayer props = MinaPlayer.get(this.mc.thePlayer);
			// if(props == null)return;
			// int displayWidth = event.resolution.getScaledWidth();
			// int displayHeight = event.resolution.getScaledHeight();
			//
			//
			int x = MinaMod.getMinaConfig().getOverlayX();
			int y = MinaMod.getMinaConfig().getOverlayY();

			Iterator<Overlayable> oi = overlayables.iterator();
			while (oi.hasNext()) {
				Overlayable ol = oi.next();

				if (ol.isVisible()) {
					ol.draw(this, x, y);
					// // setting all color values to 1.0F will render the texture
					// as it
					// looks in your texture file
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					//
					// // Somewhere in Minecraft vanilla code it says to do this
					// because
					// of a lighting bug
					GL11.glDisable(GL11.GL_LIGHTING);

					y += ol.getHeight();
				}

				if ((!ol.canStay()) || (!ol.tick())) {
					oi.remove();
				}
			}

			//
			// // setting all color values to 1.0F will render the texture as it
			// looks in your texture file
			// GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			//
			// // Somewhere in Minecraft vanilla code it says to do this because of
			// a lighting bug
			// GL11.glDisable(GL11.GL_LIGHTING);
			//
			// if (props.getMaxSympathy() != 0 ||
			// (MinaMod.getProxy().canShowMinaPropertiesOverlay()) &!
			// mc.ingameGUI.getChatGUI().getChatOpen())
			// {
			// int y = (displayHeight - 50) / 2;
			// drawTile(4, y, 0, 26, props.getSympathy(), props.getMaxSympathy());
			// }
		}
	}
	
	public boolean addOverlayable(Overlayable o){
		
		return this.overlayables.add(o);
	}
	
	public boolean isLayerActive(Class<? extends ScreenLayer> clazz){
		return layer != null && layer.getClass() == clazz;
	}
	
	public void setLayer(ScreenLayer layer){
		this.layer = layer;
	}
	
	public void resetLayer(){
		this.layer = null;
	}
	
	public int clearOverlayables(){
		int count = 0;
		Iterator<Overlayable> it = this.overlayables.iterator();
		while(it.hasNext()){
			if(!it.next().willStayForever()){
				it.remove();
				count++;
			}
		}
		return count;
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
