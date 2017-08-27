package lu.kremi151.minamod.client.element;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonItem extends GuiButton{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/widgets.png");
	
	private ItemStack icon;
	private final RenderItem itemRenderer;

	public GuiButtonItem(int buttonId, int x, int y, ItemStack icon) {
		super(buttonId, x, y, 20, 18, "");
		this.icon = icon;
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(guiTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 202, i * 18, 20, 18);
            this.mouseDragged(mc, mouseX, mouseY);
            
            this.itemRenderer.renderItemIntoGUI(icon, this.x + ((this.width - 16) / 2), this.y + ((this.height - 16) / 2));
        }
    }

}
