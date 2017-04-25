package lu.kremi151.minamod.client.render.model;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGiraffe extends ModelBase // NO_UCD (unused code)
{
  //fields
    ModelRenderer Body;
    ModelRenderer LegBR;
    ModelRenderer LegFR;
    ModelRenderer LegBL;
    ModelRenderer LegFL;
    ModelRenderer Neck;
    ModelRenderer Head1;
    ModelRenderer Head2;
    ModelRenderer EarL;
    ModelRenderer EarR;
    ModelRenderer HornR;
    ModelRenderer HornL;
    ModelRenderer Queue;
  
  public ModelGiraffe()
  {
    textureWidth = 256;
    textureHeight = 256;
    
      Body = new ModelRenderer(this, 0, 0);
      Body.addBox(-5F, -7F, -14F, 10, 14, 28);
      Body.setRotationPoint(0F, 1F, 0F);
      Body.setTextureSize(64, 32);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      LegBR = new ModelRenderer(this, 0, 0);
      LegBR.addBox(-3F, -1F, -1F, 3, 24, 3);
      LegBR.setRotationPoint(-5F, 1F, 11F);
      LegBR.setTextureSize(64, 32);
      LegBR.mirror = true;
      setRotation(LegBR, 0F, 0F, 0F);
      LegFR = new ModelRenderer(this, 0, 0);
      LegFR.addBox(-3F, -1F, -1F, 3, 24, 3);
      LegFR.setRotationPoint(-5F, 1F, -12F);
      LegFR.setTextureSize(64, 32);
      LegFR.mirror = true;
      setRotation(LegFR, 0F, 0F, 0F);
      LegBL = new ModelRenderer(this, 0, 0);
      LegBL.addBox(0F, -1F, -1F, 3, 24, 3);
      LegBL.setRotationPoint(5F, 1F, 11F);
      LegBL.setTextureSize(64, 32);
      LegBL.mirror = true;
      setRotation(LegBL, 0F, 0F, 0F);
      LegFL = new ModelRenderer(this, 0, 0);
      LegFL.addBox(0F, -1F, -1F, 3, 24, 3);
      LegFL.setRotationPoint(5F, 1F, -12F);
      LegFL.setTextureSize(64, 32);
      LegFL.mirror = true;
      setRotation(LegFL, 0F, 0F, 0F);
      Neck = new ModelRenderer(this, 0, 0);
      Neck.addBox(-1F, -22F, -2F, 2, 24, 4);
      Neck.setRotationPoint(0F, -4F, -12F);
      Neck.setTextureSize(64, 32);
      Neck.mirror = true;
      setRotation(Neck, 0.4712389F, 0F, 0F);
      Head1 = new ModelRenderer(this, 0, 0);
      Head1.addBox(-2F, -27F, -3F, 4, 5, 6);
      Head1.setRotationPoint(0F, -4F, -12F);
      Head1.setTextureSize(64, 32);
      Head1.mirror = true;
      setRotation(Head1, 0.4712389F, 0F, 0F);
      Head2 = new ModelRenderer(this, 0, 0);
      Head2.addBox(-1.5F, -26F, -5F, 3, 3, 2);
      Head2.setRotationPoint(0F, -4F, -12F);
      Head2.setTextureSize(64, 32);
      Head2.mirror = true;
      setRotation(Head2, 0.4712389F, 0F, 0F);
      EarL = new ModelRenderer(this, 0, 0);
      EarL.addBox(2F, -27F, 1F, 3, 2, 1);
      EarL.setRotationPoint(0F, -4F, -12F);
      EarL.setTextureSize(64, 32);
      EarL.mirror = true;
      setRotation(EarL, 0.4712389F, 0F, -0.0523599F);
      EarR = new ModelRenderer(this, 0, 0);
      EarR.addBox(-5F, -27F, 1F, 3, 2, 1);
      EarR.setRotationPoint(0F, -4F, -12F);
      EarR.setTextureSize(64, 32);
      EarR.mirror = true;
      setRotation(EarR, 0.4712389F, 0F, 0.0523599F);
      HornR = new ModelRenderer(this, 0, 0);
      HornR.addBox(-1.5F, -30F, 1.5F, 1, 3, 1);
      HornR.setRotationPoint(0F, -4F, -12F);
      HornR.setTextureSize(64, 32);
      HornR.mirror = true;
      setRotation(HornR, 0.4712389F, 0F, 0F);
      HornL = new ModelRenderer(this, 0, 0);
      HornL.addBox(0.5F, -30F, 1.5F, 1, 3, 1);
      HornL.setRotationPoint(0F, -4F, -12F);
      HornL.setTextureSize(64, 32);
      HornL.mirror = true;
      setRotation(HornL, 0.4712389F, 0F, 0F);
      Queue = new ModelRenderer(this, 0, 0);
      Queue.addBox(-0.5F, -0.5F, 0F, 1, 14, 1);
      Queue.setRotationPoint(0F, -2F, 14F);
      Queue.setTextureSize(64, 32);
      Queue.mirror = true;
      setRotation(Queue, 0.0349066F, 0F, 0F);
  }
  
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Body.render(f5);
    LegBR.render(f5);
    LegFR.render(f5);
    LegBL.render(f5);
    LegFL.render(f5);
    Neck.render(f5);
    Head1.render(f5);
    Head2.render(f5);
    EarL.render(f5);
    EarR.render(f5);
    HornR.render(f5);
    HornL.render(f5);
    Queue.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }


  @Override
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    setHeadRotation(f4 / (180F / (float)Math.PI), f3 / (180F / (float)Math.PI));
    setFeetRotation(f, f1);
  }
  
  private void setHeadRotation(float pangleX, float pangleY){
	  float angleX = (27f * (float)MinaUtils.CONST_M) + (pangleX * 0.1f);
	  float angleY = pangleY * 0.75f;
	  this.Neck.rotateAngleX = angleX;
	  this.Neck.rotateAngleY = angleY;
	  this.Head1.rotateAngleX = angleX;
	  this.Head1.rotateAngleY = angleY;
	  this.Head2.rotateAngleX = angleX;
	  this.Head2.rotateAngleY = angleY;
	  this.EarR.rotateAngleX = angleX;
	  this.EarR.rotateAngleY = angleY;
	  this.EarL.rotateAngleX = angleX;
	  this.EarL.rotateAngleY = angleY;
	  this.HornR.rotateAngleX = angleX;
	  this.HornR.rotateAngleY = angleY;
	  this.HornL.rotateAngleX = angleX;
	  this.HornL.rotateAngleY = angleY;
  }
  
  private void setFeetRotation(float par1, float par2){
	  float f = 0.7f;
	  this.LegFR.rotateAngleX = (MathHelper.cos(par1 * 0.6662F) * 1.4F * par2) * f;
	  this.LegFL.rotateAngleX = (MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2) * f;
	  this.LegBR.rotateAngleX = (MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2) * f;
	  this.LegBL.rotateAngleX = (MathHelper.cos(par1 * 0.6662F) * 1.4F * par2) * f;
  }

}
