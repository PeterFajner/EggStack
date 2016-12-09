package ca.pfaj.eggstack;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class EggStack extends JavaPlugin implements Listener {
	
	public static final String BIG_EGG_NAME = "Eggus";
	ItemStack nineEggs;
	ItemStack spawnEgg;
	
	@Override
	public void onEnable() {
		PluginLogger logger = new PluginLogger(this);
		logger.log(Level.INFO, "EggStack starting...");
		
		spawnEgg = new ItemStack(Material.MONSTER_EGG);
		ItemMeta spawnEggMeta = spawnEgg.getItemMeta();
		spawnEggMeta.setDisplayName(this.BIG_EGG_NAME);
		spawnEgg.setItemMeta(spawnEggMeta);
		
		ShapelessRecipe spawnEggRecipe = new ShapelessRecipe(spawnEgg);
		spawnEggRecipe.addIngredient(9, Material.EGG);
		getServer().addRecipe(spawnEggRecipe);
		
		nineEggs = new ItemStack(Material.EGG, 9);
		ShapelessRecipe eggRecipe = new ShapelessRecipe(nineEggs);
		eggRecipe.addIngredient(Material.MONSTER_EGG);
	}

	@Override
	public void onDisable() {}
	
	@EventHandler
	public void onNineEggsCrafted(PrepareItemCraftEvent event) {
		CraftingInventory ci = event.getInventory();
		if (ci.getResult().hasItemMeta() && this.nineEggs == ci.getResult()) {
		    // this is our custom item - make sure ingredient is found
			  boolean found = false;
			  for (ItemStack item : ci.getMatrix()) {
				  if (item != null && item.hasItemMeta()) {
					  if (EggStack.BIG_EGG_NAME.equals(item.getItemMeta().getDisplayName())) {
						  found = true;
					  }
				  }
			  }
			  if (!found) {
				  ci.setResult(null);
			  }
		}
	}
}
