package nl.mistermel.quickcraft.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class ItemUtils {
	
	public static ItemStack[] getIngredients(Material mat) {
		Recipe recipe = Bukkit.getRecipesFor(new ItemStack(mat)).get(0);
		if(recipe instanceof ShapedRecipe) {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			return convertItems(shapedRecipe.getIngredientMap().values());
		}
		if(recipe instanceof ShapelessRecipe) {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			return convertItems(shapelessRecipe.getIngredientList());
		}
		return null;
	}
	
	public static ItemStack[] convertItems(Collection<ItemStack> items) {
		List<ItemStack> newItems = new ArrayList<ItemStack>();
		for(ItemStack item : items) {
			if(item.getType() == Material.IRON_INGOT) {
				newItems.add(new ItemStack(Material.IRON_ORE));
			} else if(item.getType() == Material.GOLD_INGOT) {
				newItems.add(new ItemStack(Material.GOLD_ORE));
			} else if(item.getType() == Material.DIAMOND) {
				newItems.add(new ItemStack(Material.DIAMOND_ORE));
			} else if(item.getType() == Material.STICK) {
				newItems.add(new ItemStack(Material.LOG));
			} else if(item.getType() == Material.WOOD) {
				newItems.add(new ItemStack(Material.LOG));
			} else {
				newItems.add(item);
			}
		}
		ItemStack[] array = (ItemStack[]) newItems.toArray();
		return array;
	}
	
}
