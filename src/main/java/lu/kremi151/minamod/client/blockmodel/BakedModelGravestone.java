package lu.kremi151.minamod.client.blockmodel;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import lu.kremi151.minamod.block.BlockGravestone;
import lu.kremi151.minamod.util.ConcatenatedImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelGravestone implements IBakedModel
{
    private final SimpleModelFontRenderer fontRenderer;
    private final TextureAtlasSprite fontTexture;
    private final IBakedModel wrapped;

    public BakedModelGravestone(IBakedModel wrapped, SimpleModelFontRenderer fontRenderer, TextureAtlasSprite fontTexture)
    {
        this.wrapped = wrapped;
        this.fontRenderer = fontRenderer;
        this.fontTexture = fontTexture;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if (side == null)
        {
        	fontRenderer.setSprite(fontTexture);
            fontRenderer.setFillBlanks(true);
            String message = null;
            if(state instanceof IExtendedBlockState) {
            	message = ((IExtendedBlockState)state).getValue(BlockGravestone.CAPTION);
            }
            if(message == null) {
            	message = "???";
            }
            String[] lines = message.split("\\r?\\n");
            List<String> splitLines = Lists.newArrayList();
            for (int y = 0; y < lines.length; y++)
            {
            	// 128 = 1 block length => 64 = 1/2 block length
                splitLines.addAll(fontRenderer.listFormattedStringToWidth(lines[y], 64));
            }
            for (int y = 0; y < splitLines.size(); y++)
            {
                fontRenderer.drawString(splitLines.get(y), (64 - fontRenderer.getStringWidth(splitLines.get(y))) / 2, (int)((y - splitLines.size() / 2f) * fontRenderer.FONT_HEIGHT) + 0x40, 0xFF00FFFF);
            }
            /*ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            builder.addAll(fontRenderer.build());
            builder.addAll(wrapped.getQuads(state, side, rand));
            return builder.build();*/
            return new ConcatenatedImmutableList<>(fontRenderer.build(), wrapped.getQuads(state, side, rand));
        }
        return wrapped.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() { return true; }

    @Override
    public boolean isGui3d() { return false; }

    @Override
    public boolean isBuiltInRenderer() { return false; }

    @Override
    public TextureAtlasSprite getParticleTexture() { return wrapped.getParticleTexture(); }

    @Override
    public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        TRSRTransformation transform = TRSRTransformation.identity();
        boolean big = true;
        switch (cameraTransformType)
        {

            case THIRD_PERSON_LEFT_HAND:
                break;
            case THIRD_PERSON_RIGHT_HAND:
                break;
            case FIRST_PERSON_LEFT_HAND:
                transform = new TRSRTransformation(new Vector3f(-0.62f, 0.5f, -.5f), new Quat4f(1, -1, -1, 1), null, null);
                big = false;
                break;
            case FIRST_PERSON_RIGHT_HAND:
                transform = new TRSRTransformation(new Vector3f(-0.5f, 0.5f, -.5f), new Quat4f(1, 1, 1, 1), null, null);
                big = false;
                break;
            case HEAD:
                break;
            case GUI:
                if (ForgeModContainer.zoomInMissingModelTextInGui)
                {
                    transform = new TRSRTransformation(null, new Quat4f(1, 1, 1, 1), new Vector3f(4, 4, 4), null);
                    big = false;
                }
                else
                {
                    transform = new TRSRTransformation(null, new Quat4f(1, 1, 1, 1), null, null);
                    big = true;
                }
                break;
            case FIXED:
                transform = new TRSRTransformation(null, new Quat4f(-1, -1, 1, 1), null, null);
                break;
            default:
                break;
        }
        return Pair.of(this, transform.getMatrix());
    }

}
