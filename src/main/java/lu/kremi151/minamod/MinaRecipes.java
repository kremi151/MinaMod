package lu.kremi151.minamod;

import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinaRecipes {

	static void initFurnaceRecipes(MinaMod mod) {
		GameRegistry.addSmelting(MinaItems.CHESTNUT, new ItemStack(MinaItems.CHESTNUT_COOKED, 1), 0f);
		GameRegistry.addSmelting(MinaBlocks.PLATINUM_ORE, new ItemStack(MinaItems.PLATINUM_INGOT), 0.8f);
		GameRegistry.addSmelting(new ItemStack(MinaItems.RARE_EARTH), new ItemStack(MinaItems.RARE_EARTH, 1, 1), 1f);
		GameRegistry.addSmelting(MinaBlocks.COPPER_ORE, new ItemStack(MinaItems.COPPER_INGOT), 0.8f);
		GameRegistry.addSmelting(MinaItems.RUBBER_TREE_BRANCH, new ItemStack(MinaItems.RUBBER), 0.1f);

		for(BlockMinaPlanks.EnumType type : BlockMinaPlanks.EnumType.values()){
			GameRegistry.addSmelting(new ItemStack(BlockStandaloneLog.getBlockFor(type), 1), new ItemStack(Items.COAL, 1, 1), 0.2f);
		}
	}

	static void initBrewingRecipes(MinaMod mod) {
		//None present at the moment
	}
}
