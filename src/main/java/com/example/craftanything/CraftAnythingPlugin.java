package com.example.craftanything;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftAnythingPlugin extends JavaPlugin {
    private RecipeStore store;
    @Override public void onEnable() {
        saveDefaultConfig();
        store = new RecipeStore(this); store.load();
        RecipeEditor editor = new RecipeEditor(this, store);
        var command = getCommand("craftanything");
        if (command != null) { var executor = new CraftAnythingCommand(this, store, editor); command.setExecutor(executor); command.setTabCompleter(executor); }
        getServer().getPluginManager().registerEvents(editor, this);
        getServer().getPluginManager().registerEvents(new CraftingListener(store), this);
        store.registerBukkitRecipes();
    }
    public void reloadPlugin() { reloadConfig(); store.load(); store.registerBukkitRecipes(); }
    public NamespacedKey key(String name) { return new NamespacedKey(this, "custom_" + name.toLowerCase()); }
    public RecipeStore store() { return store; }
}
