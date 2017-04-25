package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSpawnParticleEffect implements IMessage{

	private EnumParticleEffect effect;
	private double posX, posY, posZ;
	private float red, green, blue;
	
	public MessageSpawnParticleEffect(){}
	
	public MessageSpawnParticleEffect(EnumParticleEffect effect, double posX, double posY, double posZ, float red, float green, float blue){
		this.effect = effect;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.red = red;
		this.green = green;
		this.red = red;
	}
	
	public MessageSpawnParticleEffect(EnumParticleEffect effect, double posX, double posY, double posZ){
		this(effect, posX, posY, posZ, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.effect = EnumParticleEffect.values()[buf.readInt()];
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
		this.red = buf.readFloat();
		this.green = buf.readFloat();
		this.blue = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(effect.ordinal());
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeFloat(red);
		buf.writeFloat(green);
		buf.writeFloat(blue);
	}
	
	public EnumParticleEffect getEffect(){ return effect; }
	public double x(){ return posX; }
	public double y(){ return posY; }
	public double z(){ return posZ; }
	public float red(){ return red; }
	public float green(){ return green; }
	public float blue(){ return blue; }

}
