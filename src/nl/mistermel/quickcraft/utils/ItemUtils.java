package nl.mistermel.quickcraft.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class ItemUtils {

	private static Random r = new Random();

	public static Material getRandomMaterial() {
		Material mat = getPotentiallyUncraftableRandomMaterial();
		if (craftable(mat)) {
			if(mat.name().contains("TERRACOTTA")) {
				return getRandomMaterial();
			}
			return mat;
		} else
			return getRandomMaterial();
	}

	private static boolean craftable(Material mat) {
		List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(mat));
		return recipes.size() != 0;
	}

	private static Material getPotentiallyUncraftableRandomMaterial() {
		Material[] materials = Material.values();
		int random = r.nextInt(materials.length);
		return materials[random];
	}

	public static List<ItemStack> getIngredients(Material mat) {
		Recipe recipe = Bukkit.getRecipesFor(new ItemStack(mat)).get(0);
		if (recipe instanceof ShapedRecipe) {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			return convertItems(shapedRecipe.getIngredientMap().values());
		}
		if (recipe instanceof ShapelessRecipe) {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			return convertItems(shapelessRecipe.getIngredientList());
		}
		return null;
	}

	public static List<ItemStack> convertItems(Collection<ItemStack> items) {
		List<ItemStack> newItems = new ArrayList<ItemStack>();
		int plankAmount = 0;
		for (ItemStack item : items) {
			if (item != null) {
				/*
				 * if(item.getType() == Material.IRON_INGOT) { newItems.add(new
				 * ItemStack(Material.IRON_ORE)); } else if(item.getType() ==
				 * Material.GOLD_INGOT) { newItems.add(new
				 * ItemStack(Material.GOLD_ORE)); } else if(item.getType() ==
				 * Material.DIAMOND) { newItems.add(new
				 * ItemStack(Material.DIAMOND_ORE)); }
				 */
				if (item.getType() == Material.STICK) {
					newItems.add(new ItemStack(Material.LOG));
				} else if (item.getType() == Material.WOOD) {
					plankAmount++;
				} else {
					newItems.add(item);
				}
			}
		}
		if (plankAmount >= 4)
			newItems.add(new ItemStack(Material.LOG, plankAmount / 4));
		else if (plankAmount >= 1)
			newItems.add(new ItemStack(Material.LOG));
		return newItems;
	}

}
