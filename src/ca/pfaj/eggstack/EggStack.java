package ca.pfaj.eggstack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class EggStack extends JavaPlugin implements Listener {
	
	static boolean DEBUG = false;
	
	public static final String BIG_EGG_NAME = "Eggus";
	public static final String BIG_EGG_LORE_STRING = "Egg × 9";
	ItemStack egg;
	ItemStack nineEggs;
	ItemStack spawnEgg;
	PluginLogger logger;
	
	void debug(String msg) {
		if (DEBUG) logger.log(Level.INFO, msg);
	}
	
	@Override
	public void onEnable() {
		logger = new PluginLogger(this);
		
		// register a method to check if the correct spawn egg is being crafted
		getServer().getPluginManager().registerEvents(this, this);
		
		// create the big egg
		spawnEgg = new ItemStack(Material.MONSTER_EGG);
		ItemMeta spawnEggMeta = spawnEgg.getItemMeta();
		spawnEggMeta.setDisplayName(EggStack.BIG_EGG_NAME); // set name of the big egg to "Eggus"
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(EggStack.BIG_EGG_LORE_STRING);
		spawnEggMeta.setLore(lore); // set the lore to "Egg × 9"
		spawnEgg.setItemMeta(spawnEggMeta);
		
		// create the recipe for 9 eggs -> big egg
		ShapelessRecipe spawnEggRecipe = new ShapelessRecipe(spawnEgg);
		spawnEggRecipe.addIngredient(9, Material.EGG);
		getServer().addRecipe(spawnEggRecipe);
		
		// create the recipe for big egg -> 9 eggs
		nineEggs = new ItemStack(Material.EGG, 9);
		ShapelessRecipe eggRecipe = new ShapelessRecipe(nineEggs);
		eggRecipe.addIngredient(Material.MONSTER_EGG);
		getServer().addRecipe(eggRecipe);
		
		egg = new ItemStack(Material.EGG);
	}

	@Override
	public void onDisable() {}
	
	@EventHandler
	public void onNineEggsCrafted(PrepareItemCraftEvent event) {
		/**
		 * Ensure that the spawn egg in the recipe is an Eggus.
		 */
		debug("Crafting started...");
		CraftingInventory ci = event.getInventory();
		if (ci.getResult().getType() == Material.EGG && ci.getResult().getAmount() == 9) { // check that the item being crafted is 9 eggs
			debug("Crafting some eggs...");
		    // make sure the Eggus is an ingredient
			  boolean found = false;
			  for (ItemStack item : ci.getMatrix()) {
				  if (item != null && item.hasItemMeta()) {
					  // check if the name is correct - legacy, may be removed in the future
					  if (EggStack.BIG_EGG_NAME.equals(item.getItemMeta().getDisplayName())) {
						  found = true;
					  }
					  // check if the lore is correct
					  List<String> lore = item.getItemMeta().getLore();
					  if (lore != null && lore.size() > 0) {
						  if (EggStack.BIG_EGG_LORE_STRING.equals(lore.get(0))) {
							  found = true;
						  }
					  }
				  }
			  }
			  if (!found) {
				  debug("Wrong ingredient!");
				  ci.setResult(null);
			  }
			  else {
				  debug("Correct ingredient!");
			  }
		}
	}
}
