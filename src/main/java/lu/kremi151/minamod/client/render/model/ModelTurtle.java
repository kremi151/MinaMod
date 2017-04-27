package lu.kremi151.minamod.client.render.model;

import lu.kremi151.minamod.entity.EntityTurtle;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTurtle extends ModelBase {
	// fields
	ModelRenderer Body;
	ModelRenderer LegBL;
	ModelRenderer LegFL;
	ModelRenderer LegBR;
	ModelRenderer LegFR;
	ModelRenderer Tail;
	ModelRenderer Neck;
	ModelRenderer Snout;

	public ModelTurtle() {
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(-5F, -2F, -5F, 10, 4, 10);
		Body.setRotationPoint(0F, 21F, 0F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		LegBL = new ModelRenderer(this, 0, 14);
		LegBL.addBox(-2F, -1F, -1F, 2, 4, 2);
		LegBL.setRotationPoint(-5F, 21F, 3F);
		LegBL.setTextureSize(64, 32);
		LegBL.mirror = true;
		setRotation(LegBL, 0F, 0F, 0F);
		LegFL = new ModelRenderer(this, 0, 14);
		LegFL.addBox(-2F, -1F, -1F, 2, 4, 2);
		LegFL.setRotationPoint(-5F, 21F, -3F);
		LegFL.setTextureSize(64, 32);
		LegFL.mirror = true;
		setRotation(LegFL, 0F, 0F, 0F);
		LegBR = new ModelRenderer(this, 0, 14);
		LegBR.addBox(0F, -1F, -1F, 2, 4, 2);
		LegBR.setRotationPoint(5F, 21F, 3F);
		LegBR.setTextureSize(64, 32);
		LegBR.mirror = true;
		setRotation(LegBR, 0F, 0F, 0F);
		LegFR = new ModelRenderer(this, 0, 14);
		LegFR.addBox(0F, -1F, -1F, 2, 4, 2);
		LegFR.setRotationPoint(5F, 21F, -3F);
		LegFR.setTextureSize(64, 32);
		LegFR.mirror = true;
		setRotation(LegFR, 0F, 0F, 0F);
		Tail = new ModelRenderer(this, 8, 14);
		Tail.addBox(-1F, -0.5F, 0F, 2, 1, 3);
		Tail.setRotationPoint(0F, 21F, 4F);
		Tail.setTextureSize(64, 32);
		Tail.mirror = true;
		setRotation(Tail, -0.2617994F, 0F, 0F);
		Neck = new ModelRenderer(this, 18, 14);
		Neck.addBox(-1F, -3F, -2F, 2, 4, 2);
		Neck.setRotationPoint(0F, 21F, -5F);
		Neck.setTextureSize(64, 32);
		Neck.mirror = true;
		setRotation(Neck, 0F, 0F, 0F);
		Snout = new ModelRenderer(this, 26, 14);
		Snout.addBox(-1F, -3F, -3F, 2, 2, 1);
		Snout.setRotationPoint(0F, 21F, -5F);
		Snout.setTextureSize(64, 32);
		Snout.mirror = true;
		setRotation(Snout, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		EntityTurtle turtle = (EntityTurtle) entity;
		if(!turtle.isInDefenseMode()){
			setBodyOffset(0F);
			LegBL.render(f5);
			LegFL.render(f5);
			LegBR.render(f5);
			LegFR.render(f5);
			Tail.render(f5);
			Neck.render(f5);
			Snout.render(f5);
		}else{
			setBodyOffset(0.0625F);
		}
		Body.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		// setHeadRotation(f4 / (180F / (float)Math.PI), f3 / (180F /
		// (float)Math.PI));
		setHeadRotation(f4 * (float) MinaUtils.CONST_M, f3
				* (float) MinaUtils.CONST_M);
		setFeetRotation(f, f1);

	}
	
	private void setBodyOffset(float offY){
		this.Body.offsetY = offY;
	}

	private void setHeadRotation(float angleX, float angleY) {
		this.Neck.rotateAngleX = angleX;
		this.Neck.rotateAngleY = angleY;
		this.Snout.rotateAngleX = angleX;
		this.Snout.rotateAngleY = angleY;
	}

	private void setFeetRotation(float par1, float par2) {
		this.LegFR.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.LegFL.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;
		this.LegBR.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;
		this.LegBL.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
	}

}
