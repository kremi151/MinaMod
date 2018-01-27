package lu.kremi151.minamod;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * These achievements will be replaced by advancements in 1.12
 * 
 * @author michm
 *
 */
public class MinaAchievements {

	public static final Achievement MODIFY_STATS = new Achievement("achievement.minamod.modify_stats",
			"minamod.modify_stats", 2, 2, MinaItems.MIXTURE, OPEN_STATS).registerStat();

	public static final Achievement TRAIN_STATS = new Achievement("achievement.minamod.train_stats",
			"minamod.train_stats", 2, 4, MinaItems.KATANA, MODIFY_STATS).registerStat();

	static void register() {
		AchievementPage.registerAchievementPage(new AchievementPage(MinaMod.MODNAME,
				new Achievement[] { 
						CITRINICIOUS,
						OPEN_STATS, 
						MODIFY_STATS, 
						TRAIN_STATS, 
						OPEN_AMULET_INV 
						}));
		
	}
}
