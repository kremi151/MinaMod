package lu.kremi151.minamod.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockCustomAxis extends BlockRotatedPillar{

	public BlockCustomAxis(Material materialIn) {
		super(materialIn);
	}

	public BlockCustomAxis(Material materialIn, MapColor color) {
		super(materialIn, color);
	}

}
