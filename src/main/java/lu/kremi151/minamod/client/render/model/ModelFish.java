package lu.kremi151.minamod.client.render.model;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ModelFish - kremi151
 * Created using Tabula 5.1.0
 */
@SideOnly(Side.CLIENT)
public class ModelFish extends ModelBase {
	
	private final static float MAX_ANGLE = (float)(10.0 * MinaUtils.CONST_M);
	
    public ModelRenderer FinRight;
    public ModelRenderer FinLeft;
    public ModelRenderer Middle;
    public ModelRenderer Head;
    public ModelRenderer HeadBody;
    public ModelRenderer BackBody;
    public ModelRenderer TailBodyFront;
    public ModelRenderer TailBodyBack;
    public ModelRenderer Tail;

    public ModelFish() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Head = new ModelRenderer(this, 10, 0);
        this.Head.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.Head.addBox(-2.0F, -2.0F, -10.0F, 4, 4, 2, 0.0F);
        this.FinRight = new ModelRenderer(this, 0, 0);
        this.FinRight.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.FinRight.addBox(-4.3F, 0.0F, -3.0F, 2, 1, 3, 0.0F);
        this.HeadBody = new ModelRenderer(this, 0, 6);
        this.HeadBody.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.HeadBody.addBox(-2.5F, -2.5F, -8.0F, 5, 5, 8, 0.0F);
        this.TailBodyFront = new ModelRenderer(this, 18, 8);
        this.TailBodyFront.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.TailBodyFront.addBox(-2.0F, -2.0F, 4.0F, 4, 4, 2, 0.0F);
        this.TailBodyBack = new ModelRenderer(this, 18, 19);
        this.TailBodyBack.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.TailBodyBack.addBox(-1.5F, -1.5F, 6.0F, 3, 3, 2, 0.0F);
        this.Middle = new ModelRenderer(this, 0, 23);
        this.Middle.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.Middle.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
        this.FinLeft = new ModelRenderer(this, 0, 0);
        this.FinLeft.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.FinLeft.addBox(2.3F, 0.0F, -3.0F, 2, 1, 3, 0.0F);
        this.BackBody = new ModelRenderer(this, 0, 19);
        this.BackBody.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.BackBody.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 4, 0.0F);
        this.Tail = new ModelRenderer(this, 22, 0);
        this.Tail.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.Tail.addBox(-0.5F, -2.5F, 8.0F, 1, 5, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head.render(f5);
        this.FinRight.render(f5);
        this.HeadBody.render(f5);
        this.Middle.render(f5);
        this.TailBodyFront.render(f5);
        this.TailBodyBack.render(f5);
        this.FinLeft.render(f5);
        this.BackBody.render(f5);
        this.Tail.render(f5);
    }
    
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
		// setHeadRotation(f4 / (180F / (float)Math.PI), f3 / (180F /
		// (float)Math.PI));
		float angle = MAX_ANGLE * MathHelper.sin(3 * limbSwing);
		setFrontRotation(0, -angle);
		setBackRotation(0, angle);

	}

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    public void setPartAngle(ModelRenderer modelRenderer, float x, float y) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;//main axis
    }
    
    private void setFrontRotation(float angleX, float angleY) {
		setPartAngle(Head, angleX, angleY);
		setPartAngle(HeadBody, angleX, angleY);

		setPartAngle(FinRight, angleX, angleY);
		setPartAngle(FinLeft, angleX, angleY);
	}

	private void setBackRotation(float angleX, float angleY) {
		/*this.LegFR.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.LegFL.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;
		this.LegBR.rotateAngleX = MathHelper.cos(par1 * 0.6662F
				+ (float) Math.PI)
				* 1.4F * par2;
		this.LegBL.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;*/

		setPartAngle(BackBody, angleX, angleY);
		setPartAngle(TailBodyFront, angleX, angleY);
		setPartAngle(TailBodyBack, angleX, angleY);
		setPartAngle(Tail, angleX, angleY);
	}
}
