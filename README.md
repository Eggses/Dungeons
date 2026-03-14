**Dungeons**

> **Note:** This plugin was built for a specific private server and is not currently available as a public release.

A Minecraft plugin that brings instanced dungeon experiences to your server. Each dungeon run clones a template world, loads it with a unique name, and cleans itself up automatically when the run ends.

## Features

- **Instanced worlds** — Each dungeon run is a fresh clone of a template world, keeping runs isolated and replayable.
- **Portal-based entry** — Players can only enter while the dungeon's portal is open. Once closed, the instance is locked.
- **Custom gameplay rules** — Each dungeon can define its own rules and events to create unique experiences.
- **Interactive triggers** — Execute custom code in response to players pressing buttons, pressure plates, or levers.
- **Automatic cleanup** — When all players leave (via portal or logout), the dungeon world is automatically deleted.
- **Crash recovery** — If the server stops mid-run, any leftover worlds are cleaned up the next time the plugin loads.

## How It Works

1. A dungeon is created by cloning a pre-configured template world.
2. A portal opens, allowing players to enter the instance.
3. Once the portal closes, the run is locked — no new players can join.
4. Players complete the dungeon (or leave early via the portal).
5. When the last player exits, the world is deleted automatically.

## Commands

All commands are under `/dungeons`. Most subcommands require operator permissions.

| Command | Permission | Description |
|---|---|---|
| `/dungeons` | everyone | Base command |
| `/dungeons reload <all\|messages\|menus\|dungeon_templates>` | op | Reload config files without restarting the server |
| `/dungeons give <type> <key> [player]` | op | Give a dungeon item to yourself or another player |
| `/dungeons destroy <all\|instance>` | op | Force-destroy one or all active dungeon instances |
| `/dungeons trigger` | op | Manually fire a dungeon trigger |
| `/dungeons stats [player]` | op | View dungeon stats for yourself or another player |

## Configuration

| File | Description |
|---|---|
| `<dungeon>.yml` | One file per dungeon. Defines the template world, portal, spawn positions, area triggers, mob spawns, and boss. |
| `dungeon_tools.yml` | Custom items giveable via `/dungeons give`, including material, name, and lore. |
| `messages.yml` | All player-facing text. Supports MiniMessage formatting and placeholders like `%dungeon_name%`. |
| `menus.yml` | GUI layouts for the keystone menu, stats screen, and dungeon shop. |
| `stats.yml` | Auto-managed. Stores per-player stats — best time and completion count per dungeon. |
| `log.yml` | Auto-managed. Tracks active dungeon world names and runtime errors for crash recovery. |

### Dungeon template (`<dungeon_name>.yml`)

Each dungeon is defined in its own YAML file.

**Top-level settings**
```yaml
dungeon_name: "<gradient:...>Malignant Marsh</gradient>"  # Display name (supports MiniMessage)
dungeon_template_name: "malignant_marsh"                  # Folder name of the template world to clone
dungeon_portal_world: "world"                             # World where the portal room exists
```

**Portal**
```yaml
dungeon_portal:
  open_duration_seconds: "60"   # How long the portal stays open after a keystone is used
  on_open:                      # Runs when the portal opens
    - "SOUND sound=block.end_portal.spawn"
    - "MESSAGE <gold>Portal open for %open_duration% seconds.</gold>"
  on_close:                     # Runs when the portal closes
    - "MESSAGE <red>Portal has collapsed.</red>"
```

**On start**

Runs once when the dungeon instance begins.
```yaml
on_start:
  - "GRAVEYARD pos=X,Y,Z rotation=45"      # Places a respawn graveyard
  - "WEATHER weather=rain"                  # Sets the weather
  - "TIME time=7am"                         # Sets the time
  - "EVENT events=poisonwater,poisonvine"   # Activates named dungeon events
  - "SHOP pos=X,Y,Z rotation=-120"          # Spawns a shop
```

**Areas & mob spawning**

The dungeon is split into named areas. When a player walks into an area's `entry_bounds`, `on_entry` fires and spawns mobs. Once all mobs are defeated, `on_clear` fires — typically removing a barrier wall to open the next area.
```yaml
dungeon_areas:
  my_area:
    entry_bounds: "pos1=X,Y,Z pos2=X,Y,Z"
    on_entry:
      - "MOB type=ZOMBIE pos=X,Y,Z level=6 count=2"
      - "MOB type=ZOMBIE pos=X,Y,Z level=8 count=1 preset=BRUISER armour=CHAINMAIL_SENTRY_IRON_2 weapon=IRON_AXE"
    on_clear:
      - "FILL pos1=X,Y,Z pos2=X,Y,Z block=AIR"
```

**MOB syntax**

| Parameter | Required | Description |
|---|---|---|
| `type` | ✅ | Minecraft mob type (e.g. `ZOMBIE`, `SKELETON`, `EVOKER`) |
| `pos` | ✅ | Spawn position (`X,Y,Z`) |
| `level` | ✅ | Mob level — scales stats |
| `count` | ✅ | Number of mobs to spawn |
| `preset` | ❌ | Behaviour/combat preset (see below) |
| `armour` | ❌ | Armour in `MATERIAL_TRIM_TRIMMAT_LEVEL` format (e.g. `CHAINMAIL_SENTRY_IRON_2`) |
| `weapon` | ❌ | Main hand weapon (e.g. `IRON_AXE`, `NETHERITE_SWORD`) |
| `offhand` | ❌ | Offhand item (e.g. `SHIELD`) |

**Presets**

| Preset | Description |
|---|---|
| `BRUISER` | Heavy melee fighter |
| `KNIGHT` | Armoured melee combatant |
| `FIEND` | Aggressive melee attacker |
| `NOXIOUS_CULTIVATOR` | Mooshroom with toxic abilities |
| `ENCHANTER` | Illusioner that buffs nearby mobs |
| `BEEHIVE_CREEPER` | Creeper with a bee-swarm explosion |

**Boss area**
```yaml
boss_area:
  boss_preset: "swamp_boss_rotbloom"     # Named boss to spawn
  boss_room_entry_bounds: "..."          # Region that triggers boss room entry
  boss_room_entry_position: "..."        # Where the player is teleported on entry
  on_boss_defeat:
    - "FILL pos1=... block=END_GATEWAY"  # Opens the exit portal on defeat
    - "PORTAL pos1=... pos2=..."
```
