package lu.kremi151.minamod.client.blockmodel;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.ForgeVersion;

public class MinaModelLoader implements ICustomModelLoader{

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(MinaMod.MODID) &&
                modelLocation.getResourcePath().equals("wall_cable");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new ModelWallCable();
	}

}
