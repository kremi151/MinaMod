package lu.kremi151.minamod.client.fx;

import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFXSpore extends ParticleSuspendedTown
{
	
    public EntityFXSpore(World parWorld,
            double parX, double parY, double parZ, double xSpeedIn, double ySpeedIn, double zSpeedIn, float red, float green, float blue) 
    {
        super(parWorld, parX, parY, parZ, xSpeedIn, ySpeedIn, zSpeedIn);
        setParticleTextureIndex((int)(Math.random() * 8.0D));
        particleScale = 0.5F;
        setRBGColorF(red, green, blue);
    }
    
    @Override
    public void onUpdate(){
    	this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }else{
        	this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        }
    }

}
