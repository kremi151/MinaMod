package lu.kremi151.minamod.client.blockmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import lu.kremi151.minamod.block.BlockWallCable;
import lu.kremi151.minamod.util.IntPair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelWallCable implements IPerspectiveAwareModel, IResourceManagerReloadListener {

	private final IModel model;
	private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
	private final TextureAtlasSprite particle, up, down, north, east, south, west;
	private final VertexFormat format;

	public BakedModelWallCable(IModel model, TextureAtlasSprite particle, TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite north, TextureAtlasSprite east, TextureAtlasSprite south, TextureAtlasSprite west, VertexFormat fmt,
			ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
		this.model = model;
		this.particle = particle;
		this.up = up;
		this.down = down;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		this.format = fmt;
		this.transforms = transforms;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		this.cache.clear();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState xtate = (IExtendedBlockState) state;
			if (xtate.getValue(BlockWallCable.MODEL_ID) != null && xtate.getValue(BlockWallCable.MODEL_META) != null) {
				int blockID = xtate.getValue(BlockWallCable.MODEL_ID);
				int blockMeta = xtate.getValue(BlockWallCable.MODEL_META);
				return this.getCachedModel(blockID, blockMeta).getQuads(state, side, rand);
			}
		}
		return ImmutableList.of();
	}

	private final Map<IntPair, IBakedModel> cache = new HashMap<>();
	
	public IBakedModel getCachedModel(int blockID, int blockMeta) {
		IntPair key = new IntPair(blockID, blockMeta);
		IBakedModel model = cache.get(key);
		if(model == null) {
			Block block = Block.getBlockById(blockID);
			if(block == null)block = Blocks.AIR;
			model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
					.getModelForState(block.getStateFromMeta(blockMeta));
			this.cache.put(key, model);
		}
		
		return model;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return particle;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}
	
	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Lists.<ItemOverride>newArrayList());
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return MapWrapper.handlePerspective(this, transforms, cameraTransformType);
	}

}
