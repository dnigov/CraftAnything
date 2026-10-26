package com.example.craftanything;

import org.bukkit.inventory.ItemStack;

public record RecipeData(String name, ItemStack[] ingredients, ItemStack result) {}
