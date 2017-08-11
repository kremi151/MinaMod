package lu.kremi151.minamod;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * These achievements will be replaced by advancements in 1.12
 * @author michm
 *
 */
public class MinaAchievements {

	public static final Achievement CITRINICIOUS = 
		      new Achievement("achievement.minamod.citrinicious", "minamod.citrinicious", 
		      0, 0, MinaItems.CITRIN, (Achievement)null);

	public static final Achievement OPEN_STATS = 
		      new Achievement("achievement.minamod.stats", "minamod.stats", 
		      1, 0, MinaItems.BATTERY, (Achievement)null);

	public static final Achievement OPEN_AMULET_INV = 
		      new Achievement("achievement.minamod.amulets", "minamod.amulets", 
		      2, 0, MinaItems.AMULET, (Achievement)null);
	
	static void register() {
		AchievementPage.registerAchievementPage(
			      new AchievementPage("MinaMod Achievements", 
			            new Achievement[] {CITRINICIOUS, OPEN_STATS, OPEN_AMULET_INV}));
		
		CITRINICIOUS.registerStat();
		OPEN_STATS.registerStat();
		OPEN_AMULET_INV.registerStat();
	}
}
