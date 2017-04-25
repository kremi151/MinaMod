package lu.kremi151.minamod.client.render;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.entity.EntityIceSentinel;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderIceSentinel extends RenderBiped<EntityIceSentinel>{ // NO_UCD (unused code)
	
	private static final ResourceLocation tradesmanTextures = new ResourceLocation(MinaMod.MODID, "textures/entity/icesentinel/default.png");

	public RenderIceSentinel(RenderManager rm) {
		super(rm, new ModelBiped(), 0.8f);
		
	}

	@Override
    protected ResourceLocation getEntityTexture(EntityIceSentinel entityIn)
    {
        return tradesmanTextures;
    }

}
