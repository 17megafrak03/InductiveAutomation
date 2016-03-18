package cd4017be.automation.jeiPlugin;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;

public class AssemblerRecipeCategory extends BlankRecipeCategory {

	private static final int SlotIn = 0, SlotOut = 4;
	
	private final ResourceLocation backgroundLocation;
	private final IDrawableAnimated power;
	private final IDrawableAnimated arrow;
	private final IDrawable background;
	private final String localizedName;
	private final ICraftingGridHelper craftingGridHelper;
	
	public AssemblerRecipeCategory(IGuiHelper guiHelper) {
		backgroundLocation = new ResourceLocation("automation", "textures/gui/recipesJEI/compressor.png");
		IDrawableStatic powerDrawable = guiHelper.createDrawable(backgroundLocation, 176, 0, 8, 52);
		power = guiHelper.createAnimatedDrawable(powerDrawable, 200, IDrawableAnimated.StartDirection.TOP, true);
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 184, 0, 32, 10);
		this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 40, IDrawableAnimated.StartDirection.LEFT, false);
		background = guiHelper.createDrawable(backgroundLocation, 25, 15, 126, 54);
		localizedName = Translator.translateToLocal("gui.cd4017be.electricCompressor.name");
		craftingGridHelper = guiHelper.createCraftingGridHelper(SlotIn, SlotOut);
	}

	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		power.draw(minecraft, 1, 1);
		arrow.draw(minecraft, 74, 22);
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public String getUid() {
		return "automation.assembler";
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 36, 9);
		guiItemStacks.init(1, true, 54, 9);
		guiItemStacks.init(2, true, 36, 27);
		guiItemStacks.init(3, true, 54, 27);
		guiItemStacks.init(4, false, 108, 18);

		if (recipeWrapper instanceof AssemblerRecipeWrapper) {
			craftingGridHelper.setInput(guiItemStacks, recipeWrapper.getInputs(), 3, 3);
			craftingGridHelper.setOutput(guiItemStacks, ((AssemblerRecipeWrapper)recipeWrapper).getOutputs());
		}
	}

}
