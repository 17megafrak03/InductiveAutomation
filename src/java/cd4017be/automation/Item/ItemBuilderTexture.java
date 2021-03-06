package cd4017be.automation.Item;

import java.util.List;

import cd4017be.automation.Automation;
import cd4017be.lib.DefaultItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 *
 * @author CD4017BE
 */
public class ItemBuilderTexture extends DefaultItem {

	public ItemBuilderTexture(String id) {
		super(id);
		this.setCreativeTab(Automation.tabAutomation);
	}

	@Override
	public void addInformation(ItemStack item, EntityPlayer player, List<String> list, boolean b) {
		if (item.getTagCompound() != null) list.add(item.getTagCompound().getString("name"));
		super.addInformation(item, player, list, b);
	}

}
