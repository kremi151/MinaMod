package lu.kremi151.minamod.network;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.network.abstracts.AbstractClientMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
	
	public static class Handler extends AbstractClientMessageHandler<MessageSpawnParticleEffect>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, MessageSpawnParticleEffect msg, MessageContext ctx) {
			MinaMod.getProxy().spawnParticleEffect(msg.getEffect(), player.world, msg.x(), msg.y(), msg.z(), msg.red(), msg.green(), msg.blue());
			return null;
		}

	}

}
