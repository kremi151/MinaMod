package lu.kremi151.minamod.client.render;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelPenguin;
import lu.kremi151.minamod.entity.EntityPenguin;
import lu.kremi151.minamod.entity.EntityPenguin.RenderData;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderPenguin extends RenderLiving<EntityPenguin>{ // NO_UCD (unused code)

	private final ResourceLocation res_default = new ResourceLocation(MinaMod.MODID,"textures/entity/penguin/default.png");
	private final ResourceLocation res_child = new ResourceLocation(MinaMod.MODID,"textures/entity/penguin/child.png");

	public RenderPenguin(RenderManager rm) {
		super(rm, new ModelPenguin(), 0.25f);
	}

	public void renderPenguin(EntityPenguin entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderPenguin((EntityPenguin)par1EntityLiving, par2, par4, par6, par8, par9);
    }
 
	@Override
	public void doRender(EntityPenguin par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderPenguin((EntityPenguin)par1Entity, par2, par4, par6, par8, par9);
    }
	
	@Override
    protected void preRenderCallback(EntityPenguin entity, float par) {
		super.preRenderCallback(entity, par);
		//GL11.glScalef(0.7F, 0.7F, 0.7F);
		RenderData rd = entity.getRenderData();
		if(entity.isInWater() && (entity.motionX != 0.0 || entity.motionZ != 0.0)){
			if(!rd.was_in_water){
				rd.was_in_water = true;
				rd.cooldown = RenderData.max_cooldown;
			}
			float f = 1f;
			if(rd.cooldown > 0){
				f -= ((float)(rd.cooldown--)/(float)RenderData.max_cooldown);
			}
			GL11.glRotatef(f * 90f, 1f, 0f, 0f);
		}else{
			if(rd.was_in_water){
				rd.was_in_water = false;
				rd.cooldown = RenderData.max_cooldown;
			}
			float f = 0f;
			if(rd.cooldown > 0){
				f = ((float)(rd.cooldown--)/(float)RenderData.max_cooldown);
			}
			GL11.glRotatef(f * 90f, 1f, 0f, 0f);
		}
		if(entity.isChild()){
			GL11.glScalef(0.4f, 0.4f, 0.4f);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPenguin entity) {
		return entity.isChild()?res_child:res_default;
	}

}
