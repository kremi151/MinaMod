package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityOven;
import lu.kremi151.minamod.container.ContainerOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOven extends GuiContainer{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/oven.png");
	private final ContainerOven container;

	public GuiOven(ContainerOven inventorySlotsIn) {
		super(inventorySlotsIn);
		this.container = inventorySlotsIn;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		final String title = I18n.translateToLocal("container.oven");
		final int titleWidth = this.fontRenderer.getStringWidth(title);
		fontRenderer.drawString(title, (this.xSize - titleWidth) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		float progr = (float)container.getCookProgress() / (float)TileEntityOven.MAX_BURNING_COUNTDOWN;
		int bdimen = Math.round(24 * progr);
		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, bdimen, 16);
		
		if(container.isCooking()) {
			this.drawTexturedModalRect(x + 57, y + 37, 176, 0, 14, 14);
		}
		
		progr = (float)container.getEnergy() / (float)TileEntityOven.MAX_ENERGY;
		bdimen = Math.round(16 * progr);
		int offset = 16 - bdimen;
		this.drawTexturedModalRect(x + 59, y + 56 + offset, 176, 31 + offset, 10, bdimen);
	}
}
