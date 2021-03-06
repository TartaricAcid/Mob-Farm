package shadows.mobfarm;

import java.util.*;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

public class BlockModCrop extends BlockCrops {
	public Item seed;
	public Item crop;
	public String regname;
	private String skullName;

	public BlockModCrop(String name, Item cropIn) {
		regname = name.toLowerCase();
		crop = cropIn;
		setUnlocalizedName(MobFarm.MODID + "." + regname);
		setRegistryName(regname);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());

	}

	public BlockModCrop(String name, Item cropIn, String skullName) {
		regname = name.toLowerCase();
		crop = cropIn;
		setUnlocalizedName(MobFarm.MODID + "." + regname);
		setRegistryName(regname);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		this.skullName = skullName;
	}

	@Override
	protected Item getSeed() {
		return Item.getByNameOrId("cancerplants:seed" + regname.substring(4));
	}

	@Override
	protected Item getCrop() {
		return crop;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		java.util.List<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(getSeed()));
		int age = getAge(state);

		if (age >= getMaxAge()) {
			if (getCrop() instanceof ItemSkull) {
				ItemStack itemstack = new ItemStack(Items.SKULL, 1, 3);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setTag("SkullOwner", new NBTTagString(skullName != "" ? skullName : "Notch"));
				itemstack.setTagCompound(nbt);
				ret.add(itemstack);
			}
			else {
				ret.add(new ItemStack(getCrop()));
			}
		}

		return ret;
	}
}