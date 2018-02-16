package lu.kremi151.minamod.util.registration;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public abstract class BlockRegistrationHandler extends CommonRegistrationHandler<Block> {
	
	protected boolean blockOnly;
	protected Function<Block, Item> itemBlockFactory = block -> new ItemBlock(obj).setRegistryName(obj.getRegistryName());

	public BlockRegistrationHandler(String name, Block obj) {
		super(name, obj);
	}
	
	public BlockRegistrationHandler blockOnly() {
		this.blockOnly = true;
		return this;
	}
	
	public BlockRegistrationHandler itemBlockFactory(Function<Block, Item> factory) {
		this.itemBlockFactory = factory;
		return this;
	}

	public BlockRegistrationHandler autoname() {
		this.obj.setUnlocalizedName(name);
		return this;
	}

}
