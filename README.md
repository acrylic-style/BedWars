# BedWars

## Required plugins

- [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) (4.4.0)
- [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays) (2.4.0)
- [TomeitoLib](https://ci.acrylicstyle.xyz/job/TomeitoLib/) (0.1.1, Build #35)

## Config Tree

config.yml
```yaml
map: # <required> map name here, maps/<here>.yml [Default: none]
mininumPlayers: # <optional> specify minimum players for start game. [Default: 4]
maximumPlayers: # <optional> specify maximum players for join server. [Default: 16]
domain: # -1 score at the scoreboard. Shown for all players. [Default: www.acrylicstyle.xyz]
```

maps/\<map\>.yml
```yaml
world: # <required> world name [Default: none]
name: # <optional but strongly recommended> The name of this map. [Default: "???"]
divider: 0 # never set this to -1
teamSize: # <optional> size of a team [Default: 2]
teams:
  red:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  blue:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  yellow:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  green:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  aqua:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  pink:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  white:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  black:
    generator:
      x: # X pos for generator
      y: # Y pos for generator
      z: # Z pos for generator
    spawn:
      x: # X pos for spawn point
      y: # Y pos for spawn point
      z: # Z pos for spawn point
    itemShop:
      x: # X pos for Item Shop NPC
      y: # Y pos for Item Shop NPC
      z: # Z pos for Item Shop NPC
    upgrade:
      x: # X pos for Team Upgrade NPC
      y: # Y pos for Team Upgrade NPC
      z: # Z pos for Team Upgrade NPC
  semiMiddle: # Diamond Generator
    generators:
      - "X pos,Y pos,Z pos" # Type it without any whitespaces!
      - "X pos,Y pos,Z pos" # 2nd generator
  middle: # Emerald Generator
    generators:
      - "X pos,Y pos,Z pos" # Type it without any whitespaces!
      - "X pos,Y pos,Z pos" # 2nd generator
beds:
  locationTeam:
    ",,": RED # 1st part of bed 
    ",,": RED # 2nd part of bed
    ",,": BLUE
    ",,": BLUE
    # ...
```

## Features

- [x] Item Shop
- [x] Resource Generator(Iron, Gold)
- [x] Diamond Generator
- [x] Emerald Generator
- [x] Team Upgrades
- [x] (Almost) Configurable
- [x] Generators Hologram