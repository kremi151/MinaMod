package lu.kremi151.minamod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWookie extends ModelBase
{
  //fields
    ModelRenderer BodyCenter;
    ModelRenderer BodyTop;
    ModelRenderer BodyRight;
    ModelRenderer BodyLeft;
    ModelRenderer BodyFront;
    ModelRenderer BodyBack;
    ModelRenderer BodyBottom;
    ModelRenderer FootRight;
    ModelRenderer FootLeft;
    ModelRenderer LegRight;
    ModelRenderer LegLeft;
    ModelRenderer ArmRight;
    ModelRenderer ArmLeft;
    ModelRenderer Nose;
    ModelRenderer Beard1;
    ModelRenderer Beard2;
  
  public ModelWookie()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      BodyCenter = new ModelRenderer(this, 0, 0);
      BodyCenter.addBox(-4F, -9F, -4F, 8, 10, 8);
      BodyCenter.setRotationPoint(0F, 18F, 0F);
      BodyCenter.setTextureSize(64, 32);
      BodyCenter.mirror = true;
      setRotation(BodyCenter, 0F, 0F, 0F);
      BodyTop = new ModelRenderer(this, 32, 0);
      BodyTop.addBox(-3F, -10F, -3F, 6, 1, 6);
      BodyTop.setRotationPoint(0F, 18F, 0F);
      BodyTop.setTextureSize(64, 32);
      BodyTop.mirror = true;
      setRotation(BodyTop, 0F, 0F, 0F);
      BodyRight = new ModelRenderer(this, 32, 7);
      BodyRight.addBox(-5F, -8F, -3F, 1, 8, 6);
      BodyRight.setRotationPoint(0F, 18F, 0F);
      BodyRight.setTextureSize(64, 32);
      BodyRight.mirror = true;
      setRotation(BodyRight, 0F, 0F, 0F);
      BodyLeft = new ModelRenderer(this, 32, 7);
      BodyLeft.addBox(4F, -8F, -3F, 1, 8, 6);
      BodyLeft.setRotationPoint(0F, 18F, 0F);
      BodyLeft.setTextureSize(64, 32);
      BodyLeft.mirror = true;
      setRotation(BodyLeft, 0F, 0F, 0F);
      BodyFront = new ModelRenderer(this, 46, 7);
      BodyFront.addBox(-3F, -8F, -5F, 6, 8, 1);
      BodyFront.setRotationPoint(0F, 18F, 0F);
      BodyFront.setTextureSize(64, 32);
      BodyFront.mirror = true;
      setRotation(BodyFront, 0F, 0F, 0F);
      BodyBack = new ModelRenderer(this, 46, 7);
      BodyBack.addBox(-3F, -8F, 4F, 6, 8, 1);
      BodyBack.setRotationPoint(0F, 18F, 0F);
      BodyBack.setTextureSize(64, 32);
      BodyBack.mirror = true;
      setRotation(BodyBack, 0F, 0F, 0F);
      BodyBottom = new ModelRenderer(this, 32, 0);
      BodyBottom.addBox(-3F, 1F, -3F, 6, 1, 6);
      BodyBottom.setRotationPoint(0F, 18F, 0F);
      BodyBottom.setTextureSize(64, 32);
      BodyBottom.mirror = true;
      setRotation(BodyBottom, 0F, 0F, 0F);
      FootRight = new ModelRenderer(this, 8, 18);
      FootRight.addBox(-1F, 3F, -2F, 2, 1, 3);
      FootRight.setRotationPoint(-2F, 20F, 0F);
      FootRight.setTextureSize(64, 32);
      FootRight.mirror = true;
      setRotation(FootRight, 0F, 0F, 0F);
      FootLeft = new ModelRenderer(this, 8, 18);
      FootLeft.addBox(-1F, 3F, -2F, 2, 1, 3);
      FootLeft.setRotationPoint(2F, 20F, 0F);
      FootLeft.setTextureSize(64, 32);
      FootLeft.mirror = true;
      setRotation(FootLeft, 0F, 0F, 0F);
      LegRight = new ModelRenderer(this, 0, 18);
      LegRight.addBox(-1F, 0F, -1F, 2, 3, 2);
      LegRight.setRotationPoint(-2F, 20F, 0F);
      LegRight.setTextureSize(64, 32);
      LegRight.mirror = true;
      setRotation(LegRight, 0F, 0F, 0F);
      LegLeft = new ModelRenderer(this, 0, 18);
      LegLeft.addBox(-1F, 0F, -1F, 2, 3, 2);
      LegLeft.setRotationPoint(2F, 20F, 0F);
      LegLeft.setTextureSize(64, 32);
      LegLeft.mirror = true;
      setRotation(LegLeft, 0F, 0F, 0F);
      ArmRight = new ModelRenderer(this, 0, 23);
      ArmRight.addBox(-2F, -0.5F, -5F, 2, 1, 6);
      ArmRight.setRotationPoint(-5F, 14F, 0F);
      ArmRight.setTextureSize(64, 32);
      ArmRight.mirror = true;
      setRotation(ArmRight, 0.418879F, 0.2443461F, 0F);
      ArmLeft = new ModelRenderer(this, 0, 23);
      ArmLeft.addBox(0F, -0.5F, -5F, 2, 1, 6);
      ArmLeft.setRotationPoint(5F, 14F, 0F);
      ArmLeft.setTextureSize(64, 32);
      ArmLeft.mirror = true;
      setRotation(ArmLeft, 0.418879F, -0.2443461F, 0F);
      Nose = new ModelRenderer(this, 0, 0);
      Nose.addBox(-0.5F, -4.5F, -6F, 1, 1, 1);
      Nose.setRotationPoint(0F, 18F, 0F);
      Nose.setTextureSize(64, 32);
      Nose.mirror = true;
      setRotation(Nose, 0F, 0F, 0F);
      Beard1 = new ModelRenderer(this, 18, 18);
      Beard1.addBox(-1.4F, -4F, -5.5F, 1, 3, 1);
      Beard1.setRotationPoint(0F, 18F, 0F);
      Beard1.setTextureSize(64, 32);
      Beard1.mirror = true;
      setRotation(Beard1, 0F, 0F, 0F);
      Beard2 = new ModelRenderer(this, 18, 18);
      Beard2.addBox(0.4F, -4F, -5.5F, 1, 3, 1);
      Beard2.setRotationPoint(0F, 18F, 0F);
      Beard2.setTextureSize(64, 32);
      Beard2.mirror = true;
      setRotation(Beard2, 0F, 0F, 0F);
  }
  
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    BodyCenter.render(f5);
    BodyTop.render(f5);
    BodyRight.render(f5);
    BodyLeft.render(f5);
    BodyFront.render(f5);
    BodyBack.render(f5);
    BodyBottom.render(f5);
    FootRight.render(f5);
    FootLeft.render(f5);
    LegRight.render(f5);
    LegLeft.render(f5);
    ArmRight.render(f5);
    ArmLeft.render(f5);
    Nose.render(f5);
    Beard1.render(f5);
    Beard2.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    this.LegRight.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    this.LegLeft.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
    this.FootRight.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    this.FootLeft.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
    this.ArmRight.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
    this.ArmLeft.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
  }

}
