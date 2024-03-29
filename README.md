# MobRepellent - a Minecraft Server Plugin

## What is MobRepellent?

MobRepellent is a plugin for third-party Minecraft server project PaperMC (other server support to come). MobRepellent allows players to construct "repellers" that will prevent hostile mobs from spawning within a certain distance.

## How do I use MobRepellent?

Simply add the MobRepellent jar file to your /plugins directory. MobRepellent will automatically create all the necessary files.

Once the plugin is loaded, to create a repeller you'll need 7 iron, gold, or diamond blocks. First, using five blocks, construct a shape that looks like a plus sign (+) parallel with the ground. Next, place the remaining two blocks directly above the block in the center of the plus sign.

After the repeller is constructed, no hostile mobs will spawn within a cuboid radius of the base of the repeller. For instance, if the radius is 50 blocks, it will repel in a 100x100x100 cube centered at the base of the repeller. Neutral mobs are not blocked by default, but this can be configured in the config.yml. MobRepellent does not affect mob spawners, hostile nor passive. To remove the repeller, simply remove one of the blocks that is part of the repeller.

Default repeller types and radii:
* Small - Iron block - 20 block radius
* Medium - Gold block - 30 block radius
* Large - Diamond block - 50 block radius

### Console/Admin Commands

All console commands default to OP-only.

    /mrlist - Lists all currently loaded repellers
    /mrreload - Reloads the configuration file
    /mrremove [repellerNumber] - Removes the selected number as found in the
                                 list given by /mrlist. Does not remove
                                 repeller structure.
    /mrremoveall - Removes all currently loaded repellers. Does not remove the
                   repeller structure.

### Permissions

    mobrepellent.* - Access to all permissions

    mobrepellent.list
      Default: OP-only
      Allows use of /mrlist command
    mobrepellent.reload
      Default: OP-only
      Allows use of /mrreload command
    mobrepellent.remove
      Default: OP-only
      Allows use of /mrremove command
    mobrepellent.removeall
      Default: OP-only
      Allows use of /mrremoveall command
    mobrepellent.create
      Default: all players
      Allows player to create a repeller
    mobrepellent.destroy
      Default: all players
      Allows player to destroy a repeller
    
### Configuring MobRepellent

You can configure MobRepellent to use almost any block type instead of the default blocks. You can also configure MobRepellent to use any radius to repel mobs, and select which mobs to repel and which to ignore. On the first run of the plugin, MobRepellent will automatically create a config.yml file with the default values. See the sample config.yml file for more information:

https://github.com/WillPall/MobRepellent/raw/master/docs/config.yml

## Todo list

* Update and test for Spigot and CraftBukkit
* Improve performance
* Further testing on newer Minecraft versions
* Ensure boss spawns work as expected with sane defaults and configuration options
* Custom repeller shapes (admin defined structures)
* Add visual cue that repeller is working. Possibly with the top block on fire (as suggested by darklust). This will be configurable and probably default to off.
* Options to use beacons instead of block structures

## Known Issues
* TNT and creeper explosions can destroy repeller structures without destroying the actual repeller functionality
* Permissions allow players to "construct" an inactive repeller, but not destroy it
      
## Changelog

#### Version 3.0.0
* Bump version for first release since 2017

#### Version 0.8.1
* Fixed an issue where the plugin would not function when first installed on a server without restarting the server

#### Version 0.8.0
* Updated basic functionality to work with 1.20.1 version of PaperMC
  * There are many edge cases and new mobs/blocks that this update does not take into account. Please submit an issue on GitHub with details if this is failing in a specific instance
* Updated to use MIT License
* Updated to use Gradle for package management

#### Version 0.7.2a (Gauthic)
* Fixed bug in running craftBukkit 1.4.4

#### Version 0.7.2
* Added configuration option to allow spawning below a repeller, regardless of its radius

#### Version 0.7.1
* Configuration files are now fully functional, although the format has changed (old format config files will automatically update to the new format). See the sample config.yml on GitHub for more info.
* Reloading config file while in-game is now fixed
* Damage values for blocks (e.g. orange wool) now works using '@' in the config file
* Many fixes to spawn-blocking (squid, villagers, golems, snowmen, spawner eggs, etc)

#### Version 0.7.0 (DEVELOPMENT - Use at your own risk)
* Basic functionality restored for CraftBukkit 1.2.3-R0.2 (#2060)
* Removed glowstone from restricted blocks, added mycelium, end stone
* Introduced small configuration file bugs (must stop server, edit file, restart server for changes to take effect)
* Introduced (re-introduced?) bug with squid being blocked from spawning, regardless of configuration options

#### Version 0.6
* Added configuration option to select which mobs to repel

#### Version 0.5.2
* Stopped repellers from blocking spawners. One, a repeller makes a dungeon seem like a cake-walk, and also many servers use passive mob spawners to provide resources for players.

#### Version 0.5.1
* Bugfix for mobs being mean and deciding to keep spawning no matter how many times I told them not to (thanks Jaiph!)

#### Version 0.5
* Added Permissions support!!
* Added two new commands for managing repellers
* Enabled configuration to select certain block types such as colored wool or cracked stone brick

#### Version 0.4.4
* Bugfix for repellers not loading correctly if the chunk becomes loaded and then unloaded
* Removed auto-checking for repeller existence because of Bukkit issue #656
* Bugfix for repellers just plain not working
* (Hopefully) final bugfix for egg spawning
* Bugfix for unnecessary 'block_id = -1' line in config.yml
* Bugfix for incorrect plugin version in plugin.yml

#### Version 0.4.3
* Added admin/console commands for listing repellers and reloading configuration files
* Bugfix for egg spawning
* Possible bugfixes for blocking entity spawning

#### Version 0.4.2
* Added delay to loading repellers until all other plugins have loaded. Hopefully this will solve any problems with the plugin looking for repellers in worlds that haven't been loaded.
      
#### Version 0.4.1
* Possible bug fix for loading/saving repellers
* Allow spawning chickens from eggs, even if neutral mobs are blocked

#### Version 0.4
* Added support for multiple repeller types with different radii.
* Added option to also repel neutral mobs (default: false)
* Added notification when creating or destroying a repeller

#### Version 0.3
* Added configuration file with support for selecting repel radius and repeller block type
* Lowered default radius from 100 blocks to 50 blocks

#### Version 0.2
* Added multi-world support (WOOHOO!)

#### Version 0.1
* Uploaded first version

## License and Reuse

Licensed under the MIT License. See LICENSE file for more information.
