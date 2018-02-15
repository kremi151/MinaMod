package lu.kremi151.minamod.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum GiftColorHandler implements IBlockColor, IItemColor{
	INSTANCE;
	
	private static final int bandColors[] = new int[EnumDyeColor.values().length];
	private static final ResourceLocation giftBandDefinitionJson = new ResourceLocation(MinaMod.MODID, "misc/gift_bands.json");
	
	private GiftColorHandler(){
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(rm -> loadColorDefinitions());
	}
	
	private void loadColorDefinitions(){
		try {
			IResource ir = Minecraft.getMinecraft().getResourceManager().getResource(giftBandDefinitionJson);
			InputStream in = ir.getInputStream();
			JsonObject jo = new Gson().fromJson(new JsonReader(new InputStreamReader(in)), JsonObject.class);
			in.close();
			
			for(EnumDyeColor color : EnumDyeColor.values()){
				int col = getDefaultBandColorForDye(color);
				boolean invert = false;
				if(jo.has(color.getName())){
					JsonObject jcolor = jo.getAsJsonObject(color.getName());
					if(jcolor.has("color")){
						JsonElement jcol = jcolor.get("color");
						if(jcol.isJsonPrimitive()){
							try{
								String colorName = jcol.getAsString();
								EnumDyeColor color2 = null;
								for(EnumDyeColor color3 : EnumDyeColor.values()){
									if(color3.getName().equals(colorName)){
										color2 = color3;
										break;
									}
								}
								if(color2 != null){
									col = color2.getColorValue();
								}
							}catch(ClassCastException e){
								col = jcol.getAsInt();
							}
						}else if(jcol.isJsonArray()){
							JsonArray jacol = jcol.getAsJsonArray();
							if(jacol.size() >= 3){
								int r = jacol.get(0).getAsInt() % 256;
								int g = jacol.get(1).getAsInt() % 256;
								int b = jacol.get(2).getAsInt() % 256;
								col = MinaUtils.convertRGBToDecimal(r, g, b);
							}
						}
					}
					if(jcolor.has("invert")){
						invert = jcolor.get("invert").getAsBoolean();
					}
					
				}
				if(invert){
					bandColors[color.ordinal()] = MinaUtils.invertColor(col);
				}else{
					bandColors[color.ordinal()] = col;
				}
			}
			
		} catch (IOException e) {
			MinaMod.errorln("Error loading file %s", giftBandDefinitionJson.toString());
			e.printStackTrace();
		}
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		return getBandColorForDye(EnumDyeColor.byMetadata(stack.getMetadata()));
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		return getBandColorForDye(state.getValue(BlockColored.COLOR));
	}
	
	private int getBandColorForDye(EnumDyeColor color){
		return bandColors[color.ordinal()];
	}

	private int getDefaultBandColorForDye(EnumDyeColor color){
		switch(color){
		case WHITE:
			return MapColor.RED.colorValue;
		default:
			return MinaUtils.invertColor(color.getColorValue());
		}
	}

}
