# Abschlussprojekt_2025_API-1_ebholi_Minecraft-Zombie-Mod

A Minecraft Mod which adds new Zombie Variations made for a future Zombie Apocalypse Modpack.

Made for my final project at the Basic Learning Year in ZLI.

## Features

The Mod currently features two new zombie types, the *Tank* and the *Rusher*

### Tank

The Tank is a lot bulkier than the average zombie, which is both a benefit and drawback.

- Health: 50 + 8 Armor
- Damage: 7
- Knockback: 2
- Speed: 0.17

### Rusher

The Rusher has a special move. Upon first seeing you, it will charge towards you at a fast pace.

- Health: 14
- Damage: 3
- Knockback: 0
- Speed: 0.26 (Excluding Charge Attack)

Charge:
- Speed: 0.39 (0.26 * 1.5)

## Installation

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

## Scripts

This project includes three scripts to aid development. If you want to install the scripts, you can download them in this [repository](https://github.com/Yooli8537/Minecraft-Fabric-1.21.11-Development-Scripts).
