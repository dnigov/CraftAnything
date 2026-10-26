package com.example.craftanything;

import org.bukkit.ChatColor; import org.bukkit.command.*; import org.bukkit.entity.Player; import net.kyori.adventure.text.Component; import net.kyori.adventure.text.event.ClickEvent; import java.util.*;

public final class CraftAnythingCommand implements CommandExecutor, TabCompleter {
 private final CraftAnythingPlugin plugin; private final RecipeStore store; private final RecipeEditor editor; CraftAnythingCommand(CraftAnythingPlugin p,RecipeStore s,RecipeEditor e){plugin=p;store=s;editor=e;}
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(a.length>0&&a[0].equalsIgnoreCase("reload")){if(!s.hasPermission("craftanything.admin")){s.sendMessage("§cYou do not have permission.");return true;}plugin.reloadPlugin();s.sendMessage("§aCraft Anything reloaded.");return true;}
  if(!(s instanceof Player p)){s.sendMessage("Players only.");return true;} if(!p.hasPermission("craftanything.admin")){p.sendMessage("§cYou do not have permission.");return true;}
  if(a.length<2||!a[0].equalsIgnoreCase("recipe")){p.sendMessage("§e/CraftAnything recipe create|delete|list [name]");return true;}
  switch(a[1].toLowerCase()){case "create" -> {if(a.length<3){p.sendMessage("§cProvide a recipe name.");return true;}editor.open(p,a[2]);} case "delete" -> {if(a.length<3){p.sendMessage("§cProvide a recipe name.");return true;}store.delete(a[2]);p.sendMessage("§aRecipe deleted.");} case "list" -> {p.sendMessage("§eRecipes:");for(RecipeData r:store.all())p.sendMessage(Component.text("§7- §f"+r.name()+" §8[click to edit]").clickEvent(ClickEvent.runCommand("/craftanything recipe create "+r.name())));} default -> p.sendMessage("§e/CraftAnything recipe create|delete|list [name]");} return true;
 }
 public List<String> onTabComplete(CommandSender s,Command c,String l,String[] a){if(a.length==1)return List.of("recipe","reload");if(a.length==2)return List.of("create","delete","list");return List.of();}
}
