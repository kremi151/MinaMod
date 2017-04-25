package lu.kremi151.minamod.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper {

	public static void renderLeash(double x1, double y1, double z1, double x2, double y2, double z2, float partialTicks)
    {
		//y2 = y2 - (1.6D - (double)entityLivingIn.height) * 0.5D;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        double d0 = 0;//interpolateValue((double)entity.prevRotationYaw, (double)entity.rotationYaw, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
        double d1 = 0;//interpolateValue((double)entity.prevRotationPitch, (double)entity.rotationPitch, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
        double d2 = Math.cos(d0);
        double d3 = Math.sin(d0);
        double d4 = Math.sin(d1);

        double d5 = Math.cos(d1);
        double d6 = x1 - d2 * 0.7D - d3 * 0.5D * d5;
        double d7 = y1 - d4 * 0.5D - 0.25D;
        double d8 = z1 - d3 * 0.7D + d2 * 0.5D * d5;
        double d9 = /*interpolateValue((double)entityLivingIn.prevRenderYawOffset, (double)entityLivingIn.renderYawOffset, (double)partialTicks) * 0.01745329238474369D +*/ (Math.PI / 2D);
        d2 = 0;//Math.cos(d9) * (double)entityLivingIn.width * 0.4D;
        d3 = 0;//Math.sin(d9) * (double)entityLivingIn.width * 0.4D;
        double d10 = x1 + d2;
        double d11 = y1;
        double d12 = z2 + d3;
        x2 = x2 + d2;
        z2 = z2 + d3;
        double d13 = (double)((float)(d6 - d10));
        double d14 = (double)((float)(d7 - d11));
        double d15 = (double)((float)(d8 - d12));
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        int i = 24;
        double d16 = 0.025D;
        vertexbuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

        for (int j = 0; j <= 24; ++j)
        {
            float f = 0.5F;
            float f1 = 0.4F;
            float f2 = 0.3F;

            if (j % 2 == 0)
            {
                f *= 0.7F;
                f1 *= 0.7F;
                f2 *= 0.7F;
            }

            float f3 = (float)j / 24.0F;
            vertexbuffer.pos(x2 + d13 * (double)f3 + 0.0D, y2 + d14 * (double)(f3 * f3 + f3) * 0.5D + (double)((24.0F - (float)j) / 18.0F + 0.125F), z2 + d15 * (double)f3).color(f, f1, f2, 1.0F).endVertex();
            vertexbuffer.pos(x2 + d13 * (double)f3 + 0.025D, y2 + d14 * (double)(f3 * f3 + f3) * 0.5D + (double)((24.0F - (float)j) / 18.0F + 0.125F) + 0.025D, z2 + d15 * (double)f3).color(f, f1, f2, 1.0F).endVertex();
        }

        tessellator.draw();
        vertexbuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

        for (int k = 0; k <= 24; ++k)
        {
            float f4 = 0.5F;
            float f5 = 0.4F;
            float f6 = 0.3F;

            if (k % 2 == 0)
            {
                f4 *= 0.7F;
                f5 *= 0.7F;
                f6 *= 0.7F;
            }

            float f7 = (float)k / 24.0F;
            vertexbuffer.pos(x2 + d13 * (double)f7 + 0.0D, y2 + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F) + 0.025D, z2 + d15 * (double)f7).color(f4, f5, f6, 1.0F).endVertex();
            vertexbuffer.pos(x2 + d13 * (double)f7 + 0.025D, y2 + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F), z2 + d15 * (double)f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }
	
	/**
     * Gets the value between start and end according to pct
     */
    private static double interpolateValue(double start, double end, double pct)
    {
        return start + (end - start) * pct;
    }
	
}
