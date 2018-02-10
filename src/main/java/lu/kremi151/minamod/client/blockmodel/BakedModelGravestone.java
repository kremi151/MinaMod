package lu.kremi151.minamod.client.blockmodel;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedModelGravestone implements IBakedModel
{
    private final SimpleModelFontRenderer fontRenderer;
    private final String message;
    private final TextureAtlasSprite fontTexture;
    private ImmutableList<BakedQuad> quads;

    public BakedModelGravestone(SimpleModelFontRenderer fontRenderer, String message, TextureAtlasSprite fontTexture)
    {
        this.fontRenderer = fontRenderer;
        this.message = message;
        this.fontTexture = fontTexture;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if (side == null)
        {
            if (quads == null)
            {
                fontRenderer.setSprite(fontTexture);
                fontRenderer.setFillBlanks(true);
                String[] lines = message.split("\\r?\\n");
                for (int y = 0; y < lines.length; y++)
                {
                    fontRenderer.drawString(lines[y], 0, y * fontRenderer.FONT_HEIGHT, 0xFFFF00FF);
                }
                quads = fontRenderer.build();
            }
            return quads;
        }
        return ImmutableList.of();
    }

    @Override
    public boolean isAmbientOcclusion() { return true; }

    @Override
    public boolean isGui3d() { return false; }

    @Override
    public boolean isBuiltInRenderer() { return false; }

    @Override
    public TextureAtlasSprite getParticleTexture() { return fontTexture; }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }

    @Override
    public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        TRSRTransformation transform = TRSRTransformation.identity();
        switch (cameraTransformType)
        {

            case THIRD_PERSON_LEFT_HAND:
                break;
            case THIRD_PERSON_RIGHT_HAND:
                break;
            case FIRST_PERSON_LEFT_HAND:
                transform = new TRSRTransformation(new Vector3f(-.12f, 1, -.5f), new Quat4f(1, -1, -1, 1), new Vector3f(.25f, .25f, .25f), null);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                transform = new TRSRTransformation(new Vector3f(-1, 1, -.5f), new Quat4f(1, 1, 1, 1), new Vector3f(.25f, .25f, .25f), null);
                break;
            case HEAD:
                break;
            case GUI:
                transform = new TRSRTransformation(null, new Quat4f(1, 1, 1, 1), new Vector3f(4, 4, 4), null);
                break;
            case FIXED:
                transform = new TRSRTransformation(null, new Quat4f(1, 1, 1, 1), null, null);
                break;
            default:
                break;
        }
        return Pair.of(this, transform.getMatrix());
    }

}
