package lu.kremi151.minamod.packet.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageShowCustomAchievement implements IMessage{
	
	private String title, desc;
	private long duration;
	private ItemStack icon;
	
	public MessageShowCustomAchievement() {}
	
	public MessageShowCustomAchievement(String title, String desc, long duration, ItemStack icon){
		this.title = title;
		this.desc = desc;
		this.duration = duration;
		this.icon = icon;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		duration = buf.readLong();
		title = ByteBufUtils.readUTF8String(buf);
		desc = ByteBufUtils.readUTF8String(buf);
		icon = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(duration);
		ByteBufUtils.writeUTF8String(buf, title);
		ByteBufUtils.writeUTF8String(buf, desc);
		ByteBufUtils.writeItemStack(buf, icon);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public ItemStack getIcon() {
		return icon;
	}

}
