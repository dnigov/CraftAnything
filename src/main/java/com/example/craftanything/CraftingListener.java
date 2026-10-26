package com.example.craftanything;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;

final class CraftingListener implements Listener {
    private final RecipeStore store;
    CraftingListener(RecipeStore store){this.store=store;}

    @EventHandler public void prepare(PrepareItemCraftEvent event){
        RecipeData recipe=match(event.getInventory());
        if(recipe!=null && !hasAmounts(event.getInventory(),recipe)) event.getInventory().setResult(null);
    }
    @EventHandler public void craft(CraftItemEvent event){
        if(!(event.getWhoClicked() instanceof Player player)) return;
        RecipeData recipe=match(event.getInventory());
        if(recipe==null) return;
        event.setCancelled(true);
        if(!hasAmounts(event.getInventory(),recipe)) return;
        ItemStack[] matrix=event.getInventory().getMatrix();
        for(int i=0;i<9;i++){ItemStack required=recipe.ingredients()[i]; if(required==null||required.getType().isAir())continue; ItemStack current=matrix[i]; current.setAmount(current.getAmount()-required.getAmount()); if(current.getAmount()<=0)matrix[i]=null;}
        event.getInventory().setMatrix(matrix);
        player.getInventory().addItem(recipe.result().clone());
    }
    private RecipeData match(CraftingInventory inv){
        ItemStack[] matrix=inv.getMatrix();
        for(RecipeData recipe:store.all()){boolean same=true;for(int i=0;i<9;i++){ItemStack a=matrix[i],b=recipe.ingredients()[i];Material am=a==null?Material.AIR:a.getType(),bm=b==null?Material.AIR:b.getType();if(am!=bm){same=false;break;}}if(same)return recipe;}
        return null;
    }
    private boolean hasAmounts(CraftingInventory inv,RecipeData recipe){ItemStack[] matrix=inv.getMatrix();for(int i=0;i<9;i++){ItemStack need=recipe.ingredients()[i];if(need==null||need.getType().isAir())continue;ItemStack got=matrix[i];if(got==null||got.getType()!=need.getType()||got.getAmount()<need.getAmount())return false;}return true;}
}
