package lu.kremi151.minamod.client.render.tileentity;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.block.tileentity.TileEntityPlate;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderPlate extends TileEntitySpecialRenderer{
	
	final float a = 1f / 16f;

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f, int p_180535_9_) {
		 // locationBlocksTexture is a "ResourceLocation" that points to a texture made of many block "icons".
	    // It will look very ugly, but creating our own ResourceLocation is beyond the scope of this tutorial.
	    //this.bindTexture(TextureMap.locationBlocksTexture);
		if (!(te instanceof TileEntityPlate))
			return; // should never happen
		
		TileEntityPlate tep = (TileEntityPlate) te;
		
		IBlockState ibs = tep.getWorld().getBlockState(tep.getPos());
		EnumFacing facing = (EnumFacing) ibs.getValue(BlockHorizontal.FACING);
		
		float rot = 0f;
		
		switch(facing){
		case NORTH:
			rot = 0f;
			break;
		case EAST:
			rot = 270;
			break;
		case SOUTH:
			rot = 180f;
			break;
		case WEST:
			rot = 90f;
			break;
		default:
			throw new RuntimeException("Invalid facing:" + facing);
		}
		
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

	    Tessellator tessellator = Tessellator.getInstance();
	    VertexBuffer wr = tessellator.getBuffer();
	    GL11.glPushMatrix();
	    GL11.glTranslated(x + 0.5d, y, z + 0.5d); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
	    GL11.glRotatef(rot, 0f, 1f, 0f);
	    //wr.begin(GL11.GL_QUADS, wr.getVertexFormat());
	    
//	    int brightness = ibs.getBlock().getMixedBrightnessForBlock(te.getWorld(), te.getPos());
//	    wr.setBrightness(brightness);
//	    wr.setColorOpaque_F(1.0F, 1.0F, 1.0F);
	    
	    preparePlateTexture(tep);
	    
	    //bottom
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 2*a, 0, 2*a,	 0, 0);
	    addVertexWithUVPlate(wr, 14*a, 0, 2*a, 1, 0);
	    addVertexWithUVPlate(wr, 14*a, 0, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 2*a, 0, 14*a, 0, 1);
	    tessellator.draw();

	    //front
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 2*a, 0, 2*a, 0, 0);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 2*a, 0, 1);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 2*a, 1, 1);
	    addVertexWithUVPlate(wr, 14*a, 0, 2*a, 1, 0);
	    tessellator.draw();

	    //back
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 2*a, 0, 14*a, 0, 0);
	    addVertexWithUVPlate(wr, 14*a, 0, 14*a, 1, 0);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 14*a, 0, 1);
	    tessellator.draw();

	    //left
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 2*a, 0, 14*a, 0, 0);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 14*a, 0, 1);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 2*a, 1, 1);
	    addVertexWithUVPlate(wr, 2*a, 0, 2*a, 1, 0);
	    tessellator.draw();

	    //right
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 14*a, 0, 2*a, 0, 0);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 2*a, 0, 1);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 14*a, 0, 14*a, 1, 0);
	    tessellator.draw();

	    //top 1st border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 2*a, 0, 0);
	    addVertexWithUVPlate(wr, 2*a, 2*a, 14*a, 0, 1);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 2*a, 1, 0);
	    tessellator.draw();

	    //top 2nd border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 2*a, 0, 0);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 14*a, 0, 1);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 14*a, 2*a, 2*a, 1, 0);
	    tessellator.draw();

	    //top 3rd border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 2*a, 0, 0);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 4*a, 0, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 4*a, 1, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 2*a, 1, 0);
	    tessellator.draw();

	    //top 4th border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 12*a, 0, 0);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 14*a, 0, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 14*a, 1, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 12*a, 1, 0);
	    tessellator.draw();

	    //base
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 4*a, 0, 0);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 12*a, 0, 1);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 12*a, 1, 1);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 4*a, 1, 0);
	    tessellator.draw();

	    //inner 1st border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 4*a, 0, 0);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 4*a, 0, 1);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 12*a, 1, 1);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 12*a, 1, 0);
	    tessellator.draw();

	    //inner 2nd border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 4*a, 0, 0);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 12*a, 1, 0);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 12*a, 1, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 4*a, 0, 1);
	    tessellator.draw();

	    //inner 3rd border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 4*a, 0, 0);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 4*a, 0, 1);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 4*a, 1, 1);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 4*a, 1, 0);
	    tessellator.draw();

	    //inner 4th border
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	    addVertexWithUVPlate(wr, 12*a, 1*a, 12*a, 0, 0);
	    addVertexWithUVPlate(wr, 4*a, 1*a, 12*a, 1, 0);
	    addVertexWithUVPlate(wr, 4*a, 2*a, 12*a, 1, 1);
	    addVertexWithUVPlate(wr, 12*a, 2*a, 12*a, 0, 1);
	    tessellator.draw();
	    
	    GL11.glPopMatrix();
	    
	    //food:

	    GL11.glPushMatrix();
	    GL11.glTranslated(x + 0.5d, y, z + 0.5d); // +1 so that our "drawing" appears 1 block over our block (to get a better view)
	    GL11.glRotatef(rot, 0f, 1f, 0f);

	    if(!tep.getItem().isEmpty()){
	    	renderFood(wr, tessellator, tep);
	    }

	    GL11.glPopMatrix();
	    
//TODO:	    GL11.glRotatef(90f, 1f, 0f, 1f);
	    
	}
	
	private void renderFood(VertexBuffer wr, Tessellator t, TileEntityPlate te){
		
		prepareFoodTexture(te);
		
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		addVertexWithUVFood(wr, 5*a, 1.1*a, 11*a, 1d, 0d);
		addVertexWithUVFood(wr, 11*a, 1.1*a, 11*a, 0d, 0d);
		addVertexWithUVFood(wr, 11*a, 1.1*a, 5*a, 0d, 1d);
		addVertexWithUVFood(wr, 5*a, 1.1*a, 5*a, 1d, 1d);
	    t.draw();
		
//		Minecraft.getMinecraft().getRenderManager().renderEntitySimple(te.getRepresentativeItem(), 0.5f);
	}
	
	private TextureAtlasSprite tas;
	
	private void prepareFoodTexture(TileEntityPlate te){
		this.tas = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(te.getItem().getItem(),te.getItem().getMetadata());
	}
	
	private void preparePlateTexture(TileEntityPlate te){
		this.tas = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK));
	}
	
	private void addVertexWithUVFood(VertexBuffer wr, double x, double y, double z, double u, double v){

		double udif = tas.getMaxU() - tas.getMinU();
		double vdif = tas.getMaxV() - tas.getMinV();
		//TODO: Render
//		wr.addVertexWithUV(x - 0.5d, y, z - 0.5d, tas.getMinU() + (u * udif), tas.getMinV() + (v * vdif));
		
		wr.pos(x - 0.5d, y, z - 0.5d).tex(tas.getMinU() + (u * udif), tas.getMinV() + (v * vdif)).endVertex();;
	}
	
	private void addVertexWithUVPlate(VertexBuffer wr, double x, double y, double z, double u, double v){

		double udif = tas.getMaxU() - tas.getMinU();
		double vdif = tas.getMaxV() - tas.getMinV();
		//TODO: Render
//		wr.addVertexWithUV(x - 0.5d, y, z - 0.5d, tas.getMinU() + (u * udif), tas.getMinV() + (v * vdif));
		
		wr.pos(x - 0.5d, y, z - 0.5d).tex(tas.getMinU() + (u * udif), tas.getMinV() + (v * vdif)).endVertex();;
	}

}
