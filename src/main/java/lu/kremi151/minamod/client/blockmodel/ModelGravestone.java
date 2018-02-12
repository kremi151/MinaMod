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
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelGravestone implements IModel
{
	/**
	 * This rotation matrix is created by me, someone who does not have any clue about how these rotation matrices work
	 */
	private static final Matrix4f ROT_Z;
	
	static {
		ROT_Z = new Matrix4f();
		ROT_Z.m00 = ROT_Z.m22 = MathHelper.cos((float) (Math.PI * 0.5f));
		ROT_Z.m20 = MathHelper.sin((float) (Math.PI * 0.5f));
		ROT_Z.m02 = -ROT_Z.m01;
		ROT_Z.m11 = 1;
		ROT_Z.m33 = 1;
	}
	
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
            //m.mul(ModelRotation.X0_Y90.getMatrix());
        	//m.mul(ModelRotation.X90_Y0.getMatrix());
            //m.mul(ModelRotation.X90_Y180.getMatrix());
            //m.mul(ModelRotation.X90_Y90.getMatrix());
            //m.mul(ModelRotation.X270_Y270.getMatrix());
            //m.mul(ModelRotation.X0_Y270.getMatrix());
            m.mul(ModelRotation.X90_Y270.getMatrix());
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
    
    private SimpleModelFontRenderer cachedFR = null;
    
    private SimpleModelFontRenderer getFR(VertexFormat format) {
    	if(cachedFR == null) {
    		Matrix4f m = new Matrix4f();
            m.m20 = 1f / 128f;
            m.m01 = m.m12 = -m.m20;
            m.m33 = 1;
            
            m.mul(ModelRotation.X270_Y0.getMatrix());
            m.mul(ROT_Z);
            
            //m.setTranslation(new Vector3f(1 + 1f / 0x100, 1f, 0));
            m.setTranslation(new Vector3f(0.25f, 1f, 0.6251f));
            cachedFR = new SimpleModelFontRenderer(
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
    	return cachedFR;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        //return new BakedModelGravestone(wrapped, fontCache.getUnchecked(format), bakedTextureGetter.apply(font2));
    	return new BakedModelGravestone(wrapped, getFR(format), bakedTextureGetter.apply(font2));
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

}
