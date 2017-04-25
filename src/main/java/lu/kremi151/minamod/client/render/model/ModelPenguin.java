package lu.kremi151.minamod.client.render.model;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPenguin extends ModelBase {
	// fields
	ModelRenderer FootRight;
	ModelRenderer FootLeft;
	ModelRenderer Body;
	ModelRenderer LegRight;
	ModelRenderer LegLeft;
	ModelRenderer Head;
	ModelRenderer WingRight;
	ModelRenderer WingLeft;
	ModelRenderer Nose;

	public ModelPenguin() {
		textureWidth = 64;
		textureHeight = 32;

		FootRight = new ModelRenderer(this, 0, 25);
		FootRight.addBox(-1F, 1F, -3F, 2, 1, 4);
		FootRight.setRotationPoint(-2F, 22F, 0F);
		FootRight.setTextureSize(64, 32);
		FootRight.mirror = true;
		setRotation(FootRight, 0F, 0F, 0F);
		FootLeft = new ModelRenderer(this, 0, 25);
		FootLeft.addBox(-1F, 1F, -3F, 2, 1, 4);
		FootLeft.setRotationPoint(2F, 22F, 0F);
		FootLeft.setTextureSize(64, 32);
		FootLeft.mirror = true;
		setRotation(FootLeft, 0F, 0F, 0F);
		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(-3F, -5F, -2.5F, 6, 10, 5);
		Body.setRotationPoint(0F, 17F, 0F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		LegRight = new ModelRenderer(this, 12, 25);
		LegRight.addBox(-1F, 0F, -1F, 2, 1, 2);
		LegRight.setRotationPoint(-2F, 22F, 0F);
		LegRight.setTextureSize(64, 32);
		LegRight.mirror = true;
		setRotation(LegRight, 0F, 0F, 0F);
		LegLeft = new ModelRenderer(this, 12, 25);
		LegLeft.addBox(-1F, 0F, -1F, 2, 1, 2);
		LegLeft.setRotationPoint(2F, 22F, 0F);
		LegLeft.setTextureSize(64, 32);
		LegLeft.mirror = true;
		setRotation(LegLeft, 0F, 0F, 0F);
		Head = new ModelRenderer(this, 0, 15);
		Head.addBox(-3F, -5F, -2.5F, 6, 5, 5);
		Head.setRotationPoint(0F, 12F, 0F);
		Head.setTextureSize(64, 32);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);
		WingRight = new ModelRenderer(this, 22, 0);
		WingRight.addBox(-1F, 0F, -2.5F, 1, 9, 5);
		WingRight.setRotationPoint(-3F, 12F, 0F);
		WingRight.setTextureSize(64, 32);
		WingRight.mirror = true;
		setRotation(WingRight, 0F, 0F, 0F);
		WingLeft = new ModelRenderer(this, 22, 0);
		WingLeft.addBox(-1F, 0F, -2.5F, 1, 9, 5);
		WingLeft.setRotationPoint(3F, 12F, 0F);
		WingLeft.setTextureSize(64, 32);
		WingLeft.mirror = true;
		setRotation(WingLeft, 0F, 3.141593F, 0F);
		Nose = new ModelRenderer(this, 22, 14);
		Nose.addBox(-1F, -2F, -3.5F, 2, 1, 1);
		Nose.setRotationPoint(0F, 12F, 0F);
		Nose.setTextureSize(64, 32);
		Nose.mirror = true;
		setRotation(Nose, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		FootRight.render(f5);
		FootLeft.render(f5);
		Body.render(f5);
		LegRight.render(f5);
		LegLeft.render(f5);
		Head.render(f5);
		WingRight.render(f5);
		WingLeft.render(f5);
		Nose.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		setHeadRotation(f4 * (float) MinaUtils.CONST_M, f3
				* (float) MinaUtils.CONST_M);
		setWalkRotation(f, f1);
	}

	private void setHeadRotation(float angleX, float angleY) {
		this.Head.rotateAngleX = angleX;
		this.Head.rotateAngleY = angleY;
		this.Nose.rotateAngleX = angleX;
		this.Nose.rotateAngleY = angleY;
	}

	private void setWalkRotation(float par1, float par2) {
		this.LegRight.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F
				* par2;
		this.FootRight.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F
				* par2;
		this.LegLeft.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;
		this.FootLeft.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;

		this.WingRight.rotateAngleX = MathHelper.cos(par1 * 0.6662F)
				* 1.4F * par2;
		this.WingLeft.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
	}

}
