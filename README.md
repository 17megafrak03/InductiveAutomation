InductiveAutomation is a Minecraft mod that adds various technical content to Minecraft. 
It's mainly focused on providing automation systems for large otherwise extremely time consuming or annoying tasks.
Another goal is to rather provide a few multifunctional machines instead of many specialized machines for only very specific tasks.

Links:
- [InductiveAutomation on Curse](http://minecraft.curseforge.com/projects/inductive-automation)

##Wiki / Documentation
- [InductiveAutomation Manual](https://github.com/CD4017BE/InductiveAutomation/blob/master-1.8.9/doc/manual/InductiveAutomationManual.pdf)

#Download & Installation

Choose a [release version](https://github.com/CD4017BE/InductiveAutomation/releases)
- For normal playing with Forge-Modloader download the file that just ends with `.jar`
- For use in deobfuscated ForgeGradle environment download the `-deobf.jar` file.

**Important:** Also download the required version of CD4017BE_lib that is linked in the release description.
These files then just go into the `mods` folder of your Minecraft-installation, Modpack or Gradle-project.

## For Mod Developers
All APIs of this mod are located in the [CD4017BE_lib](https://github.com/CD4017BE/CD4017BE_lib) project, so you probably only need the deobf.jar and sources of that one.

## Project setup from source

If you want to program with the mod itself you need an empty ForgeGradle project first. Then either use Git or download the zipped source-code to import it into your project.
As this project depends on my CD4017BE_lib you also need to download the `-deobf.jar` for it and add it to your build path. Or alternatively setup another ForgeGradle project for it and add that as dependency. It's recommended to have the library project folder located next to InductiveAutomation project folder, so build.gradle doesn't need to changed much.
In order to compile the JEI-plugin of this mod you also need to add the deobfuscated version of JEI to build path and gradle dependency.

#Modpack permissions

You are allowed to use this mod in any public Modpack you like as long as you provide source credits.