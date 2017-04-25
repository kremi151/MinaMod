package lu.kremi151.minamod.client.render;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.render.model.ModelIceGolhem;
import lu.kremi151.minamod.entity.EntityIceGolhem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderIceGolhem extends RenderLiving<EntityIceGolhem>{ // NO_UCD (unused code)
	
	private final ResourceLocation res_passive = new ResourceLocation(MinaMod.MODID,"textures/entity/icegolhem/passive.png");
	private final ResourceLocation res_tamed = new ResourceLocation(MinaMod.MODID,"textures/entity/icegolhem/tamed.png");
	private final ResourceLocation res_zombie = new ResourceLocation(MinaMod.MODID,"textures/entity/icegolhem/zombie.png");
	private final ResourceLocation res_disabled = new ResourceLocation(MinaMod.MODID,"textures/entity/icegolhem/disabled.png");

	public RenderIceGolhem(RenderManager rm) {
		super(rm, new ModelIceGolhem(), 0.3f);
	}

	public void renderIceGolhem(EntityIceGolhem entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
    }
 
	public void doRenderLiving(EntityIceGolhem par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
		renderIceGolhem((EntityIceGolhem)par1EntityLiving, par2, par4, par6, par8, par9);
    }

	@Override
	public void doRender(EntityIceGolhem par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
		renderIceGolhem((EntityIceGolhem)par1Entity, par2, par4, par6, par8, par9);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityIceGolhem entity) {
		EntityIceGolhem e = (EntityIceGolhem) entity;
		if(e.isDisabled()){
			return res_disabled;
		}else if(e.isTamed()){
			return res_tamed;
		}else if(e.isZombie()){
			return res_zombie;
		}else{
			return res_passive;
		}
	}

}
