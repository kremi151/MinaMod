package lu.kremi151.minamod.client.blockmodel;

import java.util.Collection;
import java.util.function.Function;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelGravestone implements IModel
{
	private static final ResourceLocation font = new ResourceLocation("minecraft", "textures/font/ascii.png");
    public static final ResourceLocation font2 = new ResourceLocation("minecraft", "font/ascii");
    private static final LoadingCache<VertexFormat, SimpleModelFontRenderer> fontCache = CacheBuilder.newBuilder().maximumSize(3).build(new CacheLoader<VertexFormat, SimpleModelFontRenderer>()
    {
        @Override
        public SimpleModelFontRenderer load(VertexFormat format) throws Exception
        {
            Matrix4f m = new Matrix4f();
            m.m20 = 1f / 128f;
            m.m01 = m.m12 = -m.m20;
            m.m33 = 1;
            m.setTranslation(new Vector3f(1 + 1f / 0x100, 1f, 0));
            return new SimpleModelFontRenderer(
                Minecraft.getMinecraft().gameSettings,
                font,
                Minecraft.getMinecraft().getTextureManager(),
                false,
                m,
                format
            ) {
                @Override
                protected float renderUnicodeChar(char c, boolean italic)
                {
                    return super.renderDefaultChar(126, italic);
                }
            };
        }
    });

    private final IBakedModel wrapped;

    public ModelGravestone(IBakedModel wrapped)
    {
        this.wrapped = wrapped;
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return ImmutableList.of(font2);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        return new BakedModelGravestone(wrapped, fontCache.getUnchecked(format), bakedTextureGetter.apply(font2));
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

}
