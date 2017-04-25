package lu.kremi151.minamod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelIceGolhem extends ModelBase
{
  //fields
    ModelRenderer Leg1;
    ModelRenderer Leg2;
    ModelRenderer Body;
    ModelRenderer Head;
    ModelRenderer Arm1;
    ModelRenderer Arm2;
  
  public ModelIceGolhem()
  {
    textureWidth = 64;
    textureHeight = 32;
    
    Leg1 = new ModelRenderer(this, 0, 0);
    Leg1.addBox(-1F, 0F, -1F, 2, 4, 2);
    Leg1.setRotationPoint(-2F, 20F, 0F);
    Leg1.setTextureSize(64, 32);
    Leg1.mirror = true;
    setRotation(Leg1, 0F, 0F, 0F);
    Leg2 = new ModelRenderer(this, 0, 0);
    Leg2.addBox(-1F, 0F, -1F, 2, 4, 2);
    Leg2.setRotationPoint(2F, 20F, 0F);
    Leg2.setTextureSize(64, 32);
    Leg2.mirror = true;
    setRotation(Leg2, 0F, 0F, 0F);
    Body = new ModelRenderer(this, 0, 6);
    Body.addBox(-3F, -3F, -2F, 6, 6, 4);
    Body.setRotationPoint(0F, 17F, 0F);
    Body.setTextureSize(64, 32);
    Body.mirror = true;
    setRotation(Body, 0F, 0F, 0F);
    Arm1 = new ModelRenderer(this, 20, 0);
    Arm1.addBox(-2F, -1F, -1F, 2, 5, 2);
    Arm1.setRotationPoint(-3F, 15F, 0F);
    Arm1.setTextureSize(64, 32);
    Arm1.mirror = true;
    setRotation(Arm1, 0F, 0F, 0F);
    Arm2 = new ModelRenderer(this, 20, 0);
    Arm2.addBox(0F, -1F, -1F, 2, 5, 2);
    Arm2.setRotationPoint(3F, 15F, 0F);
    Arm2.setTextureSize(64, 32);
    Arm2.mirror = true;
    setRotation(Arm2, 0F, 0F, 0F);
    Head = new ModelRenderer(this, 0, 16);
    Head.addBox(-4F, -4F, -4F, 8, 4, 8);
    Head.setRotationPoint(0F, 14F, 0F);
    Head.setTextureSize(64, 32);
    Head.mirror = true;
    setRotation(Head, 0F, 0F, 0F);

  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Leg1.render(f5);
    Leg2.render(f5);
    Body.render(f5);
    Head.render(f5);
    Arm1.render(f5);
    Arm2.render(f5);
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
    
	    this.Head.rotateAngleX = f4 / (180F / (float)Math.PI);
	    this.Head.rotateAngleY = f3 / (180F / (float)Math.PI);
	    this.Leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
	    this.Leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
	    this.Arm2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
	    this.Arm1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    
  }
  
  @Override
  public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
	  
  }
  

}
