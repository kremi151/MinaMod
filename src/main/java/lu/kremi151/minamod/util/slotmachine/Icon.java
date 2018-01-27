package lu.kremi151.minamod.util.slotmachine;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Icon{
	public final Item icon;
	public final int weight;
	public final boolean cherry;
	
	public Icon(Item icon, int weight, boolean cherry) {
		this.icon = icon;
		this.weight = weight;
		this.cherry = cherry;
	}
	
	public Icon(NBTTagCompound nbt) throws NBTException {
		if(nbt.hasKey("Item", 8) && nbt.hasKey("Weight", 99)) {
			ResourceLocation iconRes = new ResourceLocation(nbt.getString("Item"));
			Item item = Item.REGISTRY.getObject(iconRes);
			if(item != null) {
				this.icon = item;
				this.weight = nbt.getInteger("Weight");
				this.cherry = nbt.getBoolean("Cherry");
			}else {
				throw makeException(nbt, "Item", "Unknown item: " + iconRes.toString());
			}
		}else {
			throw new NBTException("The NBT data for this icon is missing either an Item or Weight attribute, or both", nbt.toString(), 0);
		}
	}
	
	private NBTException makeException(NBTTagCompound source, String element, String message) {
		String json = source.toString();
		int index = json.indexOf(element);
		return new NBTException(message, json, index);
	}
	
	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Item", this.icon.getRegistryName().toString());
		nbt.setInteger("Weight", this.weight);
		if(this.cherry)nbt.setBoolean("Cherry", true);//To reduce memory usage
		return nbt;
	}
}