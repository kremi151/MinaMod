package lu.kremi151.minamod.client.blockmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelWallCable implements IModel{
	
	private final HashMap<String, ResourceLocation> textures = new HashMap<>();
	
	public ModelWallCable() {}
	
	public ModelWallCable(HashMap<String, ResourceLocation> textures) {
		this.textures.putAll(textures);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return textures.values();
	}
	
	private TextureAtlasSprite safeResolveTexture(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, String name) {
		ResourceLocation res = textures.get(name);
		if(res != null) {
			return bakedTextureGetter.apply(res);
		}else {
			return null;
		}
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		
		return new BakedModelWallCable(this, 
				safeResolveTexture(bakedTextureGetter, "particle"),
				format, transformMap);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public IModel retexture(ImmutableMap<String, String> textures) {
		HashMap<String, ResourceLocation> resmap = new HashMap<>();
		for(Map.Entry<String, String> e : textures.entrySet()) {
			resmap.put(e.getKey(), new ResourceLocation(e.getValue()));
		}
		return new ModelWallCable(resmap);
	}

}
