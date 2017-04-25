package lu.kremi151.minamod.client.render;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelTurtle;
import lu.kremi151.minamod.entity.EntityTurtle;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTurtle extends RenderLiving<EntityTurtle>{ // NO_UCD (unused code)

	private final ResourceLocation res_default = new ResourceLocation(MinaMod.MODID,"textures/entity/turtle/default.png");
	private final ResourceLocation res_mineturtle = new ResourceLocation(MinaMod.MODID,"textures/entity/turtle/mineturtle.png");

	public RenderTurtle(RenderManager rm) {
		super(rm, new ModelTurtle(), 0.3f);
	}

	public void renderTurtle(EntityTurtle entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityTurtle par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderTurtle((EntityTurtle)par1EntityLiving, par2, par4, par6, par8, par9);
    }
 
	@Override
	public void doRender(EntityTurtle par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderTurtle((EntityTurtle)par1Entity, par2, par4, par6, par8, par9);
    }
	
	@Override
    protected void preRenderCallback(EntityTurtle entity, float par) {
		super.preRenderCallback(entity, par);
		if(entity.isChild()){
			GL11.glScalef(0.4f, 0.4f, 0.4f);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTurtle entity) {
		return entity.isMineTurtle()?res_mineturtle:res_default;
	}

}
