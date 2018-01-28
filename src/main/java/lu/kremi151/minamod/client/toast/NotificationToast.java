package lu.kremi151.minamod.client.toast;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotificationToast implements IToast{

	private final ItemStack icon;
    private final String title;
    private final String subtitle;
    private final long duration;
    private long end = 0;

    public NotificationToast(ItemStack iconIn, ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent, long duration)
    {
        this(iconIn, titleComponent.getFormattedText(), subtitleComponent == null ? null : subtitleComponent.getFormattedText(), duration);
    }
    
    public NotificationToast(ItemStack iconIn, String titleIn, String subtitleIn, long duration) {
    	this.icon = iconIn;
        this.title = titleIn;
        this.subtitle = subtitleIn;
        this.duration = duration;
    }

    @Override
    public IToast.Visibility draw(GuiToast toastGui, long delta)
    {
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
        drawItemStack(icon, 6, 6, "");

        if (this.subtitle == null)
        {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 30, 12, -11534256);
        }
        else
        {
            toastGui.getMinecraft().fontRenderer.drawString(this.title, 30, 7, -11534256);
            toastGui.getMinecraft().fontRenderer.drawString(this.subtitle, 30, 18, -16777216);
        }

        if(end == 0) {
        	end = System.currentTimeMillis() + duration;
        }
        
        return System.currentTimeMillis() <= end ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }
    
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if(font == null)font = Minecraft.getMinecraft().fontRenderer;
        final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, stack, x, y/* - (this.draggedStack.isEmpty() ? 0 : 8)*/, altText);
    }

}
