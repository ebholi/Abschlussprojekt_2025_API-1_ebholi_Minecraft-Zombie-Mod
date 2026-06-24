# Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

A Minecraft Mod which adds new Zombie Variations made for a future Zombie Apocalypse Modpack.

Made for my final project at the Basic Learning Year in ZLI.

## Table of Contents

- [Features](#features)
  - [Base Zombie](#base-zombie)
  - [Rusher](#rusher)
  - [Tank](#tank)
- [Installation](#installation)
  - [Dependencies](#dependencies)
  - [Modrinth](#modrinth)
  - [Manual Install](#manual-install)
- [Running the Project directly from an IDE / CLI](#running-the-project-directly-from-an-ide--cli)
- [Scripts](#scripts)
- [License](#license)

## Features

The Mod currently features three new zombie types, the *Base Zombie*, *Rusher* and the *Tank*.

### Base Zombie

The Base Zombie is a slightly weaker version to the regular Zombie but spawns in larger Groups.

### Rusher

The Rusher has a special move. Upon first seeing you, it will charge towards you at a fast pace until taking or dealing damage. If he does catch you, he'll deal 1.5x the amount of damage he usually does.

### Tank

The Tank is a lot bulkier than the average zombie, which is both a benefit and drawback. With increased Damage, Health and knockback, he's a force to be reckoned with. He also has a very high chance to summon reinforcements to aid him in battle.

## Installation

### Dependencies

The Mod depends on the following projects to also be installed:

- [Fabric API](https://modrinth.com/mod/fabric-api)
- [GeckoLib](https://modrinth.com/mod/geckolib) version 5.4.5 or later
- Fabric Loader version 0.19.2 or later
- Minecraft version 1.21.11
- Java version 21 or later

### Modrinth

Download the project from [Modrinth](https://modrinth.com) and add it to any **Fabric 1.21.11** Modpack.

### Manual Install

1. Make sure you have a Minecraft Instance on the version `1.21.11` with Fabric installed.
2. Run these commands:

``` PowerShell
git clone https://github.com/ebholi/Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

cd Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

.\scripts\build.ps1
```

3. Navigate to this directory: `Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod\build\libs\`
4. Copy the `y_apocalypse_zombies-X.X.X.jar` file and paste it into your Minecraft Launcher's `mods/` folder.

## Running the Project directly from an IDE / CLI

In order to run the project from an IDE, use the following commands:

``` PowerShell
git clone https://github.com/ebholi/Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

cd Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

.\scripts\runClient.ps1
```

## Scripts

This project includes three scripts to aid development. If you want to install the scripts, you can download them from this [repository](https://github.com/Yooli8537/Minecraft-Fabric-1.21.11-Development-Scripts).

## License

This Project is licensed under the [MIT License](https://mit-license.org/).
