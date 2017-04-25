package lu.kremi151.minamod.client.render;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelBee;
import lu.kremi151.minamod.entity.EntityBee;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBee extends RenderLiving<EntityBee>{ // NO_UCD (unused code)
	
	private final ResourceLocation res_default = new ResourceLocation(MinaMod.MODID, "textures/entity/bee/default.png");

	public RenderBee(RenderManager rm) {
		super(rm, new ModelBee(), 0.05f);
	}

	public void renderBee(EntityBee entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderBee((EntityBee)par1EntityLiving, par2, par4, par6, par8, par9);
    }
 
	public void doRender(EntityBee par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderBee((EntityBee)par1Entity, par2, par4, par6, par8, par9);
    }
	
	@Override
    protected void preRenderCallback(EntityBee entity, float par) {
		super.preRenderCallback(entity, par);
		float scale = 0.0625f;
		if(entity.isQueen())scale *= 2f;
	    GlStateManager.scale(scale, scale, scale);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBee entity) {
		return res_default;
	}

}
