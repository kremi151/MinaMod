package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerAutoCrafter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAutoCrafter extends GuiContainer{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/auto_crafter.png");

	public GuiAutoCrafter(ContainerAutoCrafter inventorySlotsIn) {
		super(inventorySlotsIn);
		this.xSize = 238;
		this.ySize = 196;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal(((ContainerAutoCrafter)this.inventorySlots).getAutoCrafter().getName()), 8, 10,
				4210752);
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
