package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockStool;
import lu.kremi151.minamod.block.BlockTable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.registries.IForgeRegistry;

public class VillagerHelper {
	
	private static VillagerHelper instance = null;

	public final VillagerProfession professionBaker;
	public final VillagerProfession professionCarpenter;
	public final VillagerProfession professionJewelier;
	
	public final VillagerCareer careerBaker;
	public final VillagerCareer careerCarpenter;
	public final VillagerCareer careerJewelier;
	
	private boolean initialized = false;

	private VillagerHelper(){
		if(instance != null){
			throw new RuntimeException("There can be only one");
		}
		//System.out.println("creating villagers...");
		professionBaker = new VillagerProfession(MinaMod.MODID + ":baker",
				MinaMod.MODID + ":textures/entity/villager/baker.png",
				MinaMod.MODID + ":textures/entity/zombie_villager/baker.png");
		professionCarpenter = new VillagerProfession(MinaMod.MODID + ":carpenter",
				MinaMod.MODID + ":textures/entity/villager/carpenter.png",
				MinaMod.MODID + ":textures/entity/zombie_villager/carpenter.png");
		professionJewelier = new VillagerProfession(MinaMod.MODID + ":jewelier",
				MinaMod.MODID + ":textures/entity/villager/jewelier.png",
				MinaMod.MODID + ":textures/entity/zombie_villager/jewelier.png");

		careerBaker = new VillagerCareer(professionBaker, MinaMod.MODID + ".baker");
		careerCarpenter = new VillagerCareer(professionCarpenter, MinaMod.MODID + ".carpenter");
		careerJewelier = new VillagerCareer(professionJewelier, MinaMod.MODID + ".jewelier");
		//System.out.println("created villagers");
	}

	private void initVillagers() {
		if(initialized) {
			return;
		}else {
			initialized = true;
		}
		careerBaker.addTrade(1, new ListItemForEmeralds(Items.CAKE, new PriceInfo(3, 5)),
				new ListItemForEmeralds(Item.getItemFromBlock(MinaBlocks.CHOCOLATE_CAKE), new PriceInfo(5, 8)),
				new ListItemForEmeralds(Item.getItemFromBlock(MinaBlocks.STRAWBERRY_CAKE), new PriceInfo(5, 8)));

		careerBaker.addTrade(2,
				new ListItemForEmeralds(Item.getItemFromBlock(MinaBlocks.RHUBARB_PIE), new PriceInfo(6, 10)));

		careerBaker.addTrade(3,
				new ListItemForEmeralds(Item.getItemFromBlock(MinaBlocks.HONEY_CAKE), new PriceInfo(7, 11)),
				new ListItemForEmeralds(Item.getItemFromBlock(MinaBlocks.CREEPER_CAKE), new PriceInfo(10, 15)));

		Iterator<BlockStool> it = BlockStool.getStools();
		while (it.hasNext()) {
			BlockStool s = it.next();
			ITradeList tl[] = new ITradeList[16];
			for (int i = 0; i < 16; i++) {
				tl[i] = new ListItemForEmeralds(new ItemStack(s, 1, i), new PriceInfo(8, 14));
			}
			careerCarpenter.addTrade(1, new SelectionOfTrades(2, tl));
		}

		Iterator<BlockTable> it2 = BlockTable.getTableBlocks();
		ArrayList<BlockTable> commonTables = new ArrayList<BlockTable>();
		ArrayList<BlockTable> uncommonTables = new ArrayList<BlockTable>();
		while (it2.hasNext()) {
			BlockTable t = it2.next();
			if (t.getType().hasCraftingRecipe()) {
				commonTables.add(t);
			} else {
				uncommonTables.add(t);
			}
		}

		ITradeList tl[] = new ITradeList[2 * commonTables.size()];
		int l = tl.length / 2;
		for (int i = 0; i < l; i++) {
			tl[(2 * i)] = new ListItemForEmeralds(new ItemStack(commonTables.get(i), 1, 0), new PriceInfo(10, 16));
			tl[(2 * i) + 1] = new ListItemForEmeralds(new ItemStack(commonTables.get(i), 1, 1), new PriceInfo(10, 16));
		}
		careerCarpenter.addTrade(2, new SelectionOfTrades(3, tl));

		tl = new ITradeList[2 * uncommonTables.size()];
		l = tl.length / 2;
		for (int i = 0; i < l; i++) {
			tl[(2 * i)] = new ListItemForEmeralds(new ItemStack(uncommonTables.get(i), 1, 0), new PriceInfo(16, 20));
			tl[(2 * i) + 1] = new ListItemForEmeralds(new ItemStack(uncommonTables.get(i), 1, 1),
					new PriceInfo(16, 20));
		}
		careerCarpenter.addTrade(3, new SelectionOfTrades(3, tl));

		tl = new ITradeList[] { new ListItemForEmeralds(Items.GOLD_INGOT, new PriceInfo(4, 6)),
				new ListItemForEmeralds(Items.IRON_INGOT, new PriceInfo(4, 6)),
				new ListItemForEmeralds(MinaItems.PLATINUM_INGOT, new PriceInfo(13, 17)),
				new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(8, 9)),
				new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(13, 16)),
				new EmeraldForItems(MinaItems.PLATINUM_INGOT, new PriceInfo(6, 7)) };

		careerJewelier.addTrade(1, new SelectionOfTrades(3, tl));

		tl = new ITradeList[] { new ListItemForEmeralds(Items.DIAMOND, new PriceInfo(8, 11)),
				new ListItemForEmeralds(MinaItems.RUBY, new PriceInfo(8, 11)),
				new ListItemForEmeralds(MinaItems.SAPPHIRE, new PriceInfo(8, 11)),
				new EmeraldForItems(Items.DIAMOND, new PriceInfo(8, 13)),
				new EmeraldForItems(MinaItems.RUBY, new PriceInfo(9, 13)),
				new EmeraldForItems(MinaItems.SAPPHIRE, new PriceInfo(9, 13)) };

		careerJewelier.addTrade(2, new SelectionOfTrades(3, tl));

		tl = new ITradeList[] { new ListItemForEmeralds(MinaItems.CITRIN, new PriceInfo(15, 20)),
				new EmeraldForItems(MinaItems.CITRIN, new PriceInfo(16, 21)) };

		careerJewelier.addTrade(3, new SelectionOfTrades(1, tl));
	}
	
	public void register(IForgeRegistry<VillagerProfession> registry){
		initVillagers();
		registry.register(professionBaker);
		registry.register(professionCarpenter);
		registry.register(professionJewelier);
	}
	
	public static VillagerHelper instance(){
		if(instance == null){
			instance = new VillagerHelper();
		}
		return instance;
	}

	private static class SelectionOfTrades implements ITradeList {

		private ITradeList[] trades;
		private int max;

		SelectionOfTrades(int max, ITradeList... trades) {
			this.trades = trades;
			this.max = max;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			LinkedList<ITradeList> tradz = new LinkedList<ITradeList>();
			tradz.addAll(Arrays.asList(trades));
			int l = trades.length;
			if (l == 0)
				return;
			for (int i = 0; i < Math.min(l, max); i++) {
				int idx = random.nextInt(l);
				ITradeList tl = tradz.get(idx);
				tradz.remove(idx);
				l--;
				tl.addMerchantRecipe(merchant, recipeList, random);
				if (l == 0)
					return;
			}
		}

	}

}
