package lu.kremi151.minamod.container;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerSelectItem extends BaseContainer{
	
	public static final int TITLE_PACK_GIFT = 1;
	
	private final static HashMap<UUID, Consumer<Result>> REQUEST_STORAGE = new HashMap<>();
	
	private final EntityPlayer player;
	private final int blockedSlot;
	private final String title;
	
	public ContainerSelectItem(EntityPlayer player, String title, int blockedSlot) {
		this.player = player;
		this.title = title;
		this.blockedSlot = blockedSlot + 27;
		bindPlayerInventory(player.inventory, 8, 20);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return ItemStack.EMPTY;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public int getBlockedSlot() {
		return blockedSlot;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static void selectItem(EntityPlayer player, Consumer<Result> listener) {
		selectItem(player, -1, listener);
	}
	
	public static void selectItem(EntityPlayer player, int lockSlot, Consumer<Result> listener) {
		selectItem(player, lockSlot, 0, listener);
	}
	
	public static void selectItem(EntityPlayer player, int lockSlot, int titleId, Consumer<Result> listener) {
		if(!player.world.isRemote) {
			if(player == null || listener == null)throw new NullPointerException();
			REQUEST_STORAGE.put(player.getUniqueID(), listener);
			player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdSelectItem, player.world, lockSlot, titleId, 0);
		}
	}
	
	public static void itemSelected(EntityPlayer player, int slot) {
		Consumer<Result> listener = REQUEST_STORAGE.remove(player.getUniqueID());
		if(listener == null) {
			throw new IllegalStateException();
		}else {
			listener.accept(new Result(player, slot));
		}
	}
	
	public static String titleForId(int id) {
		switch(id) {
			case TITLE_PACK_GIFT: return "item.unpacked_gift.select_item";
			default: return "gui.select_item.def_title";
		}
	}
	
	public static class Result{
		public final EntityPlayer player;
		public final int selected;
		
		private Result(EntityPlayer player, int selected) {
			this.player = player;
			this.selected = selected;
		}
	}

}
