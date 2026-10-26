package com.example.craftanything;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import java.util.*;

public final class RecipeStore {
    private final CraftAnythingPlugin plugin; private final Map<String, RecipeData> recipes = new LinkedHashMap<>();
    RecipeStore(CraftAnythingPlugin p) { plugin=p; }
    public Collection<RecipeData> all() { return recipes.values(); }
    public RecipeData get(String n) { return recipes.get(n.toLowerCase()); }
    public void load() {
        recipes.clear(); ConfigurationSection root=plugin.getConfig().getConfigurationSection("recipes"); if(root==null)return;
        for(String n:root.getKeys(false)) { ItemStack[] in=new ItemStack[9]; for(int i=0;i<9;i++) in[i]=root.getItemStack(n+".ingredients."+i); ItemStack out=root.getItemStack(n+".result"); if(out!=null)recipes.put(n.toLowerCase(),new RecipeData(n,in,out)); }
    }
    public void save(RecipeData d) { String p="recipes."+d.name().toLowerCase(); plugin.getConfig().set(p+".result",d.result()); for(int i=0;i<9;i++)plugin.getConfig().set(p+".ingredients."+i,d.ingredients()[i]); plugin.saveConfig(); recipes.put(d.name().toLowerCase(),d); }
    public void delete(String n) { recipes.remove(n.toLowerCase()); plugin.getConfig().set("recipes."+n.toLowerCase(),null); plugin.saveConfig(); registerBukkitRecipes(); }
    public void registerBukkitRecipes() {
        for (RecipeData d : recipes.values()) { try {
            if (d.result() == null || d.result().getType().isAir()) continue;
            NamespacedKey key = plugin.key(d.name());
            Bukkit.removeRecipe(key);
            int minRow=3,minCol=3,maxRow=-1,maxCol=-1;
            for(int i=0;i<9;i++) if(filled(d.ingredients()[i])) { int row=i/3,col=i%3; minRow=Math.min(minRow,row);minCol=Math.min(minCol,col);maxRow=Math.max(maxRow,row);maxCol=Math.max(maxCol,col); }
            if(maxRow<0) continue;
            ShapedRecipe recipe=new ShapedRecipe(key,d.result().clone());
            char next='A'; char[][] shape=new char[maxRow-minRow+1][maxCol-minCol+1]; Map<Material,Character> symbols=new HashMap<>();
            for(int row=minRow;row<=maxRow;row++) for(int col=minCol;col<=maxCol;col++) { ItemStack item=d.ingredients()[row*3+col]; if(filled(item)){ Character symbol=symbols.get(item.getType()); if(symbol==null){symbol=next++;symbols.put(item.getType(),symbol);} shape[row-minRow][col-minCol]=symbol; } else shape[row-minRow][col-minCol]=' '; }
            String[] rows=new String[shape.length]; for(int row=0;row<shape.length;row++) rows[row]=new String(shape[row]); recipe.shape(rows); for(Map.Entry<Material,Character> entry:symbols.entrySet()) recipe.setIngredient(entry.getValue(),entry.getKey()); Bukkit.addRecipe(recipe);
            } catch (IllegalArgumentException ex) { plugin.getLogger().warning("Skipped invalid recipe '"+d.name()+"': "+ex.getMessage()); }
        }
    }
    private boolean filled(ItemStack item){return item!=null&&!item.getType().isAir();}
}
