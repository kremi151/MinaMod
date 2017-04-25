package lu.kremi151.minamod.client.render;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelWookie;
import lu.kremi151.minamod.entity.EntityWookie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderWookie extends RenderLiving<EntityWookie>{ // NO_UCD (unused code)

	private final ResourceLocation res_default = new ResourceLocation(MinaMod.MODID,"textures/entity/wookie/default.png");

	public RenderWookie(RenderManager rm) {
		super(rm, new ModelWookie(), 0.25f);
	}

	public void renderPenguin(EntityWookie entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderPenguin((EntityWookie)par1EntityLiving, par2, par4, par6, par8, par9);
    }
 
	@Override
	public void doRender(EntityWookie par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderPenguin((EntityWookie)par1Entity, par2, par4, par6, par8, par9);
    }
	
	@Override
    protected void preRenderCallback(EntityWookie entity, float par) {
		super.preRenderCallback(entity, par);
		//GL11.glScalef(0.7F, 0.7F, 0.7F);
		if(entity.isChild()){
			GL11.glScalef(0.4f, 0.4f, 0.4f);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWookie entity) {
		return res_default;
	}

}
