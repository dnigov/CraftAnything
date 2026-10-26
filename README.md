# Craft Anything

Paper plugin skeleton for custom recipes: any 3x3 ingredient arrangement can produce any item.

## Build

Requires Java 21 and Gradle 8+. Run `gradlew build` on Windows or `./gradlew build` on Linux/macOS; copy `build/libs/Craft-Anything-1.0.0.jar` to `plugins/`.

The implementation uses stable Bukkit/Paper APIs and is intended to run across the 1.21.x line. The requested `26.1` is not a currently valid Minecraft/Paper release; change the `paper-api` version in `pom.xml` when that release exists.

## Commands

`/CraftAnything recipe create <name>` opens the editor. Put ingredients in the 3x3 grid, the result in the result slot, then click green concrete to save or red concrete to cancel.

`/CraftAnything recipe delete <name>`, `/CraftAnything recipe list`, and `/CraftAnything reload` are also supported.
