package lu.kremi151.minamod.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBee extends ModelBase
{
  //fields
    ModelRenderer Body;
    ModelRenderer Front;
    ModelRenderer Back;
    ModelRenderer WingLeft;
    ModelRenderer WingRight;
  
  public ModelBee()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Body = new ModelRenderer(this, 0, 0);
      Body.addBox(-4F, -4F, -4F, 8, 8, 8);
      Body.setRotationPoint(0F, 20F, 0F);
      Body.setTextureSize(64, 32);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      Front = new ModelRenderer(this, 0, 16);
      Front.addBox(-3F, -3F, -5F, 6, 6, 1);
      Front.setRotationPoint(0F, 20F, 0F);
      Front.setTextureSize(64, 32);
      Front.mirror = true;
      setRotation(Front, 0F, 0F, 0F);
      Back = new ModelRenderer(this, 14, 16);
      Back.addBox(-3F, -3F, 4F, 6, 6, 1);
      Back.setRotationPoint(0F, 20F, 0F);
      Back.setTextureSize(64, 32);
      Back.mirror = true;
      setRotation(Back, 0F, 0F, 0F);
      WingLeft = new ModelRenderer(this, 0, 23);
      WingLeft.addBox(0F, -2.5F, 0F, 7, 5, 0);
      WingLeft.setRotationPoint(3F, 17F, 0F);
      WingLeft.setTextureSize(64, 32);
      WingLeft.mirror = true;
      setRotation(WingLeft, -0.296706F, -0.8901179F, -0.6981317F);
      WingLeft.mirror = false;
      WingRight = new ModelRenderer(this, 0, 23);
      WingRight.addBox(-7F, -2.5F, 0F, 7, 5, 0);
      WingRight.setRotationPoint(-3F, 17F, 0F);
      WingRight.setTextureSize(64, 32);
      WingRight.mirror = true;
      setRotation(WingRight, -0.296706F, 0.6806784F, 0.8726646F);
  }
  
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Body.render(f5);
    Front.render(f5);
    Back.render(f5);
    WingLeft.render(f5);
    WingRight.render(f5);
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
  }

}
