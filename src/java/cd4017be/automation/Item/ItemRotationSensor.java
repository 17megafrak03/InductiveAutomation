package cd4017be.automation.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import cd4017be.api.circuits.ItemBlockSensor;
import cd4017be.automation.Automation;
import cd4017be.automation.Objects;
import cd4017be.automation.shaft.ShaftComponent;

public class ItemRotationSensor extends ItemBlockSensor {

	public ItemRotationSensor(String id) {
		super(id, 20F);
		setCreativeTab(Automation.tabAutomation);
	}

	@Override
	protected float measure(ItemStack sensor, NBTTagCompound nbt, World world, BlockPos pos, EnumFacing side) {
		TileEntity te = world.getTileEntity(pos);
		ShaftComponent shaft = te != null ? te.getCapability(Objects.SHAFT_CAP, side) : null;
		return shaft != null ? shaft.network.v : 0F;
	}

}
