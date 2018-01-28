package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.container.ContainerAutoFeeder;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAutoFeeder extends GuiCustomContainer.Inventory {

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/autofeeder/default.png");

	private static final int GUI_ENERGY_CAPTION_GREEN = MinaUtils.convertRGBToDecimal(20, 190, 40);
	private static final int GUI_ENERGY_CAPTION_RED = MinaUtils.convertRGBToDecimal(190, 20, 40);

	private final ContainerAutoFeeder ct;

	public GuiAutoFeeder(ContainerAutoFeeder container) {
		super(container);
		this.ySize = 149;
		this.ct = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal(ct.getAutoFeeder().getName()), 8, 10,
				4210752);
		fontRenderer.drawString(I18n.translateToLocalFormatted("gui.energy.display", ct.getEnergy()), 8, 20, (ct.getEnergy() >= TileEntityAutoFeeder.POWER_TO_EXTRACT)?GUI_ENERGY_CAPTION_GREEN:GUI_ENERGY_CAPTION_RED);
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if(!ct.getAutoFeeder().getFood().isEmpty() && ct.getAutoFeeder().hasEnoughPower()){
			int t = Math.min(1 + ((7 * ct.getTicksLeft()) / ct.getMaxTicks()), 7);
			int it = 7 - t;

			this.drawTexturedModalRect(x + 115, y + 33 + it, 4, 151 + it, 10, t);
			this.drawTexturedModalRect(x + 115, y + 40 + t, 4, 158 + t, 10, it);
		}else{
			this.drawTexturedModalRect(x + 115, y + 40, 4, 158, 10, 7);
		}
	}

}
