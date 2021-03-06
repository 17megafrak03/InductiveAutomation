package cd4017be.automation.TileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cd4017be.api.Capabilities;
import cd4017be.api.automation.PipeEnergy;
import cd4017be.automation.Config;
import cd4017be.automation.Objects;
import cd4017be.automation.shaft.ShaftComponent;
import cd4017be.automation.shaft.ShaftPhysics;
import cd4017be.lib.BlockItemRegistry;
import cd4017be.lib.templates.IPipe;
import cd4017be.lib.templates.MultiblockTile;
import cd4017be.lib.util.Utils;

public class Shaft extends MultiblockTile<ShaftComponent, ShaftPhysics> implements IPipe {

	private Cover cover;
	public PipeEnergy energy;

	public Shaft() {
		this.comp = new ShaftComponent(this, 1000F);
	}

	@Override
	public void update() {
		super.update();
		if (!worldObj.isRemote && energy != null) energy.Ucap *= comp.getCoilLoss();
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing dir, float X, float Y, float Z) {
		if (worldObj.isRemote) return true;
		if (cover != null) {
			if (player.isSneaking() && item == null) {
				this.dropStack(cover.item);
				cover = null;
				this.markUpdate();
				return true;
			}
			return false;
		} 
		if (onClicked(player, hand, item)) return true;
		else if (item != null && !player.isSneaking() && (cover = Cover.create(item)) != null) {
			if (--item.stackSize <= 0) item = null;
			player.setHeldItem(hand, item);
			this.markUpdate();
			return true;
		} else return false;
	}

	public boolean onClicked(EntityPlayer player, EnumHand hand, ItemStack item) {
		float mass = comp.m;
		if (player == null || (player.isSneaking() && item == null)) {
			switch (comp.type) {
			case 1: dropStack(BlockItemRegistry.stack("m.magnet", 1)); break;
			case 2: dropStack(new ItemStack(Objects.electricCoilC)); break;
			case 3: dropStack(new ItemStack(Objects.electricCoilA)); break;
			case 4: dropStack(new ItemStack(Objects.electricCoilH)); break;
			case 5: dropStack(new ItemStack(Blocks.IRON_BARS)); break;
			default: return false;
			}
			comp.type = 0;
			mass = 1000F;
		} else if (comp.type == 0 && !player.isSneaking() && item != null) {
			Item i = item.getItem();
			if (item.isItemEqual(BlockItemRegistry.stack("m.magnet", 1))) {comp.type = 1; mass = 2000F;}
			else if (i == Item.getItemFromBlock(Objects.electricCoilC)) {comp.type = 2; mass = 2000F;}
			else if (i == Item.getItemFromBlock(Objects.electricCoilA)) {comp.type = 3; mass = 2000F;}
			else if (i == Item.getItemFromBlock(Objects.electricCoilH)) {comp.type = 4; mass = 2000F;}
			else if (i == Item.getItemFromBlock(Blocks.IRON_BARS)) {comp.type = 5; mass = 16000F;}
			else return false;
			player.setHeldItem(hand, --item.stackSize <= 0 ? null : item);
		} else return false;
		if (comp.type >= 2 && comp.type < 5 && energy == null) energy = new PipeEnergy(Config.Umax[comp.type - 2], Config.Rcond[comp.type - 2]);
		else if (comp.type == 0 && energy != null) energy = null;
		if (mass != comp.m) comp.network.changeMass(comp, mass);
		markUpdate();
		return true;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		comp.writeToNBT(nbt);
		nbt.setByte("type", comp.type);
		if (energy != null) energy.writeToNBT(nbt, "energy");
		if (cover != null) cover.write(nbt, "cover");
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		comp = ShaftComponent.readFromNBT(this, nbt);
		comp.type = nbt.getByte("type");
		if (comp.type >= 2 && comp.type < 5) {
			energy = new PipeEnergy(Config.Umax[comp.type - 2], Config.Rcond[comp.type - 2]);
			energy.readFromNBT(nbt, "energy");
		}
		cover = Cover.read(nbt, "cover");
	}

	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		cover = Cover.read(nbt, "cover");
		comp.type = nbt.getByte("type");
		if (comp.type >= 2 && comp.type < 5 && energy == null) energy = new PipeEnergy(Config.Umax[comp.type - 2], Config.Rcond[comp.type - 2]);
		else if (comp.type == 0 && energy != null) energy = null;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
		if (nbt.hasKey("RotVel")) {
			comp.network.v = nbt.getFloat("RotVel");
		}
		if (nbt.hasKey("type")) {
			cover = Cover.read(nbt, "cover");
			//byte con = nbt.getByte("con");
			//if (con != shaft.con) {
			//	shaft.con = con;
			//	shaft.updateCon = true;
			//}
			comp.type = nbt.getByte("type");
			if (comp.type >= 2 && comp.type < 5 && energy == null) energy = new PipeEnergy(Config.Umax[comp.type - 2], Config.Rcond[comp.type - 2]);
			else if (comp.type == 0 && energy != null) energy = null;
			this.markUpdate();
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (cover != null) cover.write(nbt, "cover");
		nbt.setByte("type", comp.type);
		//nbt.setByte("con", shaft.con);
		return new SPacketUpdateTileEntity(getPos(), -1, nbt);
	}

	@Override
	public void breakBlock() {
		super.breakBlock();
		if (cover != null) this.dropStack(cover.item);
		onClicked(null, null, null);
	}

	@Override
	public int textureForSide(byte s) {
		if (s == -1 && worldObj.isRemote) this.checkRenderUpdate();
		if (s < 0 || s / 2 != this.getBlockMetadata()) return -1;
		TileEntity te = Utils.getTileOnSide(this, s);
		return te != null && te instanceof Shaft ? -2 : 0;
	}

	private void checkRenderUpdate() {
		int l = worldObj.getCombinedLight(pos, 0);
		if (l != lastLight) {
			lastLight = l;
			comp.network.model = null;
		}
	}

	@Override
	public Cover getCover() {
		return cover;
	}

	public ShaftPhysics physics() {
		return comp.network;
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing s) {
		if (cap == Capabilities.ELECTRIC_CAPABILITY) return energy != null;
		else return super.hasCapability(cap, s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing s) {
		if (cap == Capabilities.ELECTRIC_CAPABILITY) return (T)energy;
		else return super.getCapability(cap, s);
	}

	@SideOnly(Side.CLIENT)
	private int lastLight = 0;

}
