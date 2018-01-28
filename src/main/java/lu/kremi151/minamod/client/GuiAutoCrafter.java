package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoCrafter;
import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.container.ContainerAutoCrafter;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.ReflectionLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAutoCrafter extends GuiCustomContainer.Inventory{

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/auto_crafter/default.png");
	private static final int FONT_COLOR_GREEN = MinaUtils.convertRGBToDecimal(0, 180, 60);
	private static final int FONT_COLOR_RED = MinaUtils.convertRGBToDecimal(255, 0, 0);

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
		final int energy = ((ContainerAutoCrafter)this.inventorySlots).getEnergy();
		fontRenderer.drawString(
				I18n.translateToLocalFormatted("gui.energy.display", energy), 8,
				ySize - 105, energy >= TileEntityAutoCrafter.ENERGY_TRESHOLD ? FONT_COLOR_GREEN : FONT_COLOR_RED);
		

		ItemStack sketch = ((ContainerAutoCrafter)this.inventorySlots).getAutoCrafter().sketchInv.getStackInSlot(0);
		if(!sketch.isEmpty() && sketch.hasCapability(ISketch.CAPABILITY, null)) {
			ISketch cap = sketch.getCapability(ISketch.CAPABILITY, null);
	        RenderHelper.enableGUIStandardItemLighting();
	        RenderHelper.enableStandardItemLighting();
			for(int i = 0 ; i < Math.min(9, cap.getOrder().size()) ; i++) {
		        GlStateManager.translate(0.0F, 0.0F, -32.0F);
				ReflectionLoader.GuiContainer_drawItemStack(this, cap.getOrder().get(i), 159 + ((i % 3) * 18), 14 + ((i / 3) * 18), "");
			}
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
