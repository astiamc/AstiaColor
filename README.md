# AstiaColor
*Namecolor plugin for AstiaMC*

## What is this?
This is a light-weight small little name-color plugin with integrated support for PlaceholderAPI and Essentials.
You can customise (and even create completely custom) GUIs with custom click event support!

> This projects was made for AstiaMC

## How To: Use this plugin

### Dependancies:
This plugin relies on the 1.8.8 Spigot API. Other version weren't tested but this plugin *might* work on newer versions as well.
You need [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) and [EssentialsX](https://essentialsx.net/downloads.html).

### GUI.yml
In this config you can create custom guis (Chest inventories).
Here is an example and afterwards I will explain the different components further: 
```yaml
default:
  slots: 9
  title: '&5&lColor GUI §7- %ac_name%'
  items:
    color-gui:
      material: CHEST
      slot: 5
      name: "&d&lView colors"
      lore:
        - "&7Cut yourself some slack with a custom §nname color!"
        - ""
        - "&5Information:"
        - " &5&l* &7Color type: &5Name"
        - " &5&l* &7Color selected: %ac_coloredcolor%"
        - ""
        - "&5&l<!> &dClick to view all name colors!"
      onclick:
        type: OPEN_INV
        value: colors-gui
        close: true
```

(Gonna finish it later)
