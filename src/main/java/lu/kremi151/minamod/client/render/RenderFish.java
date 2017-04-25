package lu.kremi151.minamod.client.render;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelFish;
import lu.kremi151.minamod.entity.EntityFish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFish extends RenderLiving<EntityFish>{ // NO_UCD (unused code)

	public RenderFish(RenderManager rm) {
		super(rm, new ModelFish(), 0.25f);
	}

	public void renderFish(EntityFish entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderFish((EntityFish)par1EntityLiving, par2, par4, par6, par8, par9);
    }
 
	@Override
	public void doRender(EntityFish par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderFish((EntityFish)par1Entity, par2, par4, par6, par8, par9);
    }
	
	@Override
    protected void preRenderCallback(EntityFish entity, float par) {
		super.preRenderCallback(entity, par);
		//GL11.glScalef(0.7F, 0.7F, 0.7F);
		
		float scale = entity.getScale();
		GL11.glScalef(scale, scale, scale);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFish entity) {
		return entity.getFishType().getTexture(entity);
	}

}
