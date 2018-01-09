package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerCollector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCollector extends GuiContainer {

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/collector/default.png");

	private final ContainerCollector ct;

	public GuiCollector(ContainerCollector container) {
		super(container);
		this.ct = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		
		fontRenderer.drawString(
				I18n.translateToLocal(ct.getCollector().getName()), 8, 10,
				4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		int t = Math.min(1 + ((7 * ct.getTicksLeft()) / ct.getMaxTicks()), 7);
		int it = 7 - t;

		this.drawTexturedModalRect(x + 138, y + 36 + it, 4, 168 + it, 10, t);
		this.drawTexturedModalRect(x + 138, y + 43 + t, 4, 175 + t, 10, it);
		
//		this.drawTexturedModalRect(80, 80, 4, 141, 10, 14);

	}
	
}
