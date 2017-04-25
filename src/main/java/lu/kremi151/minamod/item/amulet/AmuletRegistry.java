package lu.kremi151.minamod.item.amulet;

import java.util.Optional;

public class AmuletRegistry {
	
	private static final AmuletBase AMULETS[];
	
	static{
		AMULETS = new AmuletBase[]{
				new AmuletExperience(),
				new AmuletEnder(),
				new AmuletReturn()
		};
		for(int i = 0 ; i < AMULETS.length ; i++)AMULETS[i].allocateId(i);
	}
	
	public static String[] getVariantNames(){
		String res[] = new String[AMULETS.length];
		for(int i = 0 ; i < AMULETS.length ; i++){
			res[i] = "amulet_of_" + AMULETS[i].getUnlocalizedName().toLowerCase();
		}
		return res;
	}
	
	public static int amuletCount(){
		return AMULETS.length;
	}
	
	public static AmuletBase getById(int id){
		return AMULETS[id];
	}
	
	public static Optional<AmuletBase> getByIdSafe(int id){
		if(id >= 0 && id < AMULETS.length){
			return Optional.of(getById(id));
		}else{
			return Optional.empty();
		}
	}

}
