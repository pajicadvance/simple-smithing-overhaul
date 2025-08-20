# Simple Smithing Overhaul

**_Fully-Featured, Vanilla Friendly._**

Smithing in Minecraft is incredibly stingy. The excessively high cost of managing gear deters players from ever interacting with the mechanic and steers them towards relying exclusively on Mending for item repair. This mod opens up new ways to upgrade and maintain your gear by **improving the usability of vanilla smithing mechanics, introducing new smithing templates, and rebalancing enchantment sources**. The goal is to ease up the harsh costs and restrictions of smithing mechanics and make those mechanics more important for obtaining enchanted gear instead of solely relying on the enchanting table, looted items and villager trades, in turn making them more rewarding.

All the features, changes and rebalances in the mod result in **generally faster enchanted item acquisition** and **higher enchanting flexibility**, all while making the entire process **feel more rewarding** at the same time.

The mod implements a **vanilla styled smithing advancement tree**, with 11 advancements to accomplish and 2 hidden advancements to discover.

![advancements](https://cdn.modrinth.com/data/cached_images/a69ad0ef63a3463dacfc9a487c99bfab99b1033e.png)

Many aspects of the mod are configurable. The mod description below reflects the default settings, so take a look at the configuration screen if you don't like something.

**Highly recommended mods**
- [**EMI**](https://modrinth.com/mod/emi): Simple Smithing Overhaul has full EMI integration and is recommended for seeing recipes and changes from the mod in-game.
- [**Tax Free Levels**](https://modrinth.com/mod/tax-free-levels): Fully compatible with Simple Smithing Overhaul and greatly complements its changes.

Fabric version requires [Fabric API](https://modrinth.com/mod/fabric-api) and [Fzzy Config](https://modrinth.com/mod/fzzy-config). Use [Mod Menu](https://modrinth.com/mod/modmenu) to change settings in-game.

NeoForge version requires [Fzzy Config](https://modrinth.com/mod/fzzy-config). Use the Mods screen to change settings in-game.

# Core Changes

## Streamlined Repairs

The amount of material required to repair an item from zero to full durability is now the same as the amount of material required to craft the item, instead of always being 4.

For example, a diamond pickaxe is crafted with 3 diamonds, which means that only 3 diamonds are required to fully repair it instead of 4. This applies to every repairable item in the game.

Additionally, repair recipes were added for all vanilla items which did not have one, and netherite gear is repaired with diamonds instead of netherite ingots.

These features are fully configurable for mod support. Modded items can be made repairable, and the amount of material required to fully repair them can be defined in the mod configuration. I've already added values for some mods I play with, so you can use them as examples.

## Anvil Improvements

- Repairing unenchanted items no longer costs any XP levels.
- Repairing items no longer increases prior work penalty. Combining enchanted items and upgrading enchantments using the new smithing template still increases prior work penalty.
- Renaming items no longer costs any XP levels.
- Anvils are now twice as less likely to get damaged during use.
- Removed "Too Expensive!" prompt, now allowing you to perform actions that cost more than 40 XP levels.

## Grindstone Improvements

- Combining an enchanted item with a piece of netherite scrap in the grindstone halves the repair cost of the item.
- Disenchanting items in the grindstone now grants more XP.

## Enchanting and Enchanted Loot Changes

In order to emphasize the importance of smithing mechanics, some limits have been imposed on how strong items obtained from the enchanting table and loot can be, while increasing the chances of finding enchanted books and experience bottles in loot, and increasing the amount of experience awarded from experience bottles.

The goal is to make the road to maxed out enchanted items more involved and rewarding instead of relying on villager trading or lucking out on the enchanting table or loot chests.

- The maximum amount of bookshelves the enchanting table can accept is reduced to 10 down from 15.
- Looted enchanted items can also only come with enchantments that the enchanting table allows at 10 bookshelves at most.
- Higher level enchanted books are way less common in loot compared to lower level books.
- Enchanted books sold by villagers will always be level 1.
- Enchanted book villager trades can only be used 3 times before having to restock, down from 12.

However:

- Enchanted books and experience bottles are way more common in loot chests, and can be found in more places.
- Experience bottles give way more experience (30-50, up from 3-11).

# New Mechanics

## Portable Item Repair

Gear can now be repaired on the go using flint or a new item, the whetstone, directly in the crafting menu. It's as easy as combining the tool you want to repair, the repair material, and flint or a whetstone. Any item that's repairable in the anvil can be repaired on the go, too.

Flint and regular whetstones can repair unenchanted items only. Enchanted items can only be repaired with enchanted whetstones. The enchanted whetstone has to contain all enchantments that are on the enchanted item you want to repair at levels equal or higher than the ones on the item. Enchantments can be added to whetstones by enchanting them in the enchanting table like books or in the anvil by combining them with enchanted books.

Flint is good for one repair and is consumed on use. Whetstones have a fixed durability of 12, with each repair taking off 1 durability. Whetstones themselves can only be repaired in the anvil with quartz.

![whetstone](https://cdn.modrinth.com/data/cached_images/281069961f02641377ef6b08c5134a0c4d55007b.png)

## Item Destruction Prevention

Items now won't be destroyed when they break. Instead, they will remain at 0 durability but will be non-functional until repaired. To indicate that an item is broken, its name will change to red and a "Broken" prefix will be applied to the name.

![broken_item](https://cdn.modrinth.com/data/cached_images/68cc4149afb7874c5f0420a059a1b510097f96a0.png)

## Enchantment Upgrade Smithing Template

Found in End City chests as rare loot, this smithing template can upgrade any enchantment on your enchanted item by one level, for an XP cost. Any enchanted item, including items which have stored enchantments like enchanted books, can be upgraded.

If the item has multiple enchantments, the enchantment to upgrade can be selected by adding more lapis (2 lapis to upgrade the second enchantment, 3 lapis to upgrade the third enchantment, and so on).

![enchantment_upgrade](https://cdn.modrinth.com/data/cached_images/dceb3c6b9fbee52dca364e02f44d737d94cb49ec.gif) ![enchantment_upgrade_duplication](https://cdn.modrinth.com/data/cached_images/64ee229915a0dc76f8072ab6f297ebe3cd75f0df.png)

## Pinnacle Enchantment Smithing Template

Found in Ancient City chests as rare loot, this smithing template will turn your "maxed out" enchanted item into a **pinnacle item**, upgrading a random enchantment on it by one level **above the max level limit**, for a hefty XP cost. This means you can get enchantments such as Fortune IV, Looting IV, and so on.

Items are considered "maxed out" when they are enchanted with all possible enchantments they support at max level, excluding curses. This accounts for exclusive sets, so for example, the following pickaxes are both considered "maxed out" and can be turned into pinnacle items:

- Fortune III, Efficiency V, Unbreaking III, Mending
- Silk Touch, Efficiency V, Unbreaking III, Mending

Items can be pinnacle upgraded multiple times in order to reroll the upgraded enchantment, however, the XP cost increases with each reroll. Enchantments that have a max level of 1 such as Mending aren't considered in the upgrade. Cursed items cannot be upgraded.

Pinnacle items can still be repaired with whetstones - the whetstone does not need to have the upgraded enchantment at the same level as the item, only at the regular max enchantment level.

![pinnacle_smithing](https://cdn.modrinth.com/data/cached_images/e61cee46b69c9a3a62505436fbb7ef56d9f27004.png) ![pinnacle_duplication](https://cdn.modrinth.com/data/cached_images/8a62c4ea281dd8829754a6ab8f3e2713b3148702.png)