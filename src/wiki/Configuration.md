# Configuration

The **Flexible Commands Mod** uses JSON files for configuration, making it easy for server admins to create and manage dynamic commands and info commands.

For quick configuration, feel free to use my [**WebEditor**](https://fce.sethi.tech/)!

---

## **Commands Configuration**

Commands are stored in `config/pebbles-flexible-commands`. Each command is defined in its own JSON file. Below is the structure and example for creating commands.

### **Command Structure**

| Field          | Description                                                                 |
|----------------|-----------------------------------------------------------------------------|
| `alias`        | The main alias for the command.                                            |
| `baseCommand`  | The base command to execute.                                               |
| `permission`   | The permission required to use the command.                                |
| `runAs`        | Specifies if the command runs as the player (`player`) or console (`console`). |
| `cooldownSeconds` | The cooldown period in seconds before a player can use the command again (default: 0). |
| `cooldownMessage` | The message shown when a command is on cooldown. Supports `{remaining}` placeholder for seconds left. |
| `template`     | The template string for the command. `{}` placeholders are replaced dynamically. Supports `{executor}` to refer to the player executing the command. |
| `arguments`    | List of arguments for the command.                                         |
| `customLogic`  | Map of keys to custom logic definitions (optional).                        |

### **Command Argument Structure**

| Field     | Description                                      |
|-----------|--------------------------------------------------|
| `name`    | The name of the argument (e.g., `player`).       |
| `type`    | The type of the argument (`string`, `player`, `choice`, `int`). |
| `choices` | List of choices for `choice` type arguments (optional). |

---

### **Example Command Configuration**

#### **Give Pokémon Command**
```json
{
  "alias": "givepoke",
  "baseCommand": "pokegiveother",
  "permission": "flexiblecommands.command.givepoke",
  "runAs": "console",
  "cooldownSeconds": 300,
  "cooldownMessage": "<red>You must wait {remaining} seconds before using this command again!",
  "template": "{baseCommand} {player} {pokemon} shiny={shiny} galarian={galarian} speed_iv={custom:ivs:0} attack_iv={custom:ivs:1}",
  "arguments": [
    { "name": "player", "type": "player" },
    { "name": "pokemon", "type": "string" },
    { "name": "shiny", "type": "choice", "choices": ["true", "false"] },
    { "name": "galarian", "type": "choice", "choices": ["true", "false"] }
  ],
  "customLogic": {
    "ivs": {
      "type": "guaranteedMaxIvs",
      "params": {
        "numMaxIvs": 2,
        "maxValue": 31,
        "randomRange": [0, 31]
      }
    }
  }
}
```

### Give Random Diamonds Command
```json
{
  "alias": "giverandomdiamonds",
  "baseCommand": "give",
  "permission": "flexiblecommands.command.giverandomdiamonds",
  "runAs": "console",
  "template": "{baseCommand} {player} minecraft:diamond {custom:randomamount}",
  "arguments": [
    { "name": "player", "type": "player" }
  ],
  "customLogic": {
    "randomamount": {
      "type": "randomnumberrange",
      "params": {
        "min": 1,
        "max": 64
      }
    }
  }
}
```

## **Info Commands Configuration**

Info commands are simple text commands that display a predefined message. These are stored in `config/pebbles-flexible-commands/info-commands`.

### **Info Command Structure**

| Field       | Description                                                         |
|-------------|---------------------------------------------------------------------|
| `aliases`   | List of aliases for the info command.                               |
| `permission`| Permission required to use the command.                             |
| `message`   | The message displayed when the command is executed.                 |

### **Example Info Command**

#### **Discord Command**
```json
{
  "aliases": ["discord", "discordlink", "dc", "disc"],
  "permission": "flexiblecommands.command.discord",
  "message": "<green>Join our [<yellow><click:open_url:'https://discord.gg/kzpyuB57Gu'>Discord</click></yellow>] server!"
}
```

## **Custom Logic Reference**

Custom logic dynamically generates data for commands based on logic types and parameters.

### **Supported Logic Types**

#### **1. `guaranteedMaxIvs`**
Ensures that a specified number of IVs have their maximum value, while others are randomized.

- **Parameters**:
  - `numMaxIvs`: Number of IVs to set to max (default: `3`).
  - `maxValue`: Maximum IV value (default: `31`).
  - `randomRange`: Range for random IVs (default: `[0, 31]`).

- **Template Example**:
  {
    "template": "{baseCommand} {player} {pokemon} attack_iv={custom:ivs:0} speed_iv={custom:ivs:1}"
  }

#### **2. `randomNumberRange`**
Generates a random number within a specified range.

- **Parameters**:
  - `min`: Minimum value.
  - `max`: Maximum value.

- **Template Example**:
  {
    "template": "{baseCommand} {player} minecraft:diamond {custom:randomamount}"
  }

#### **3. `randomPlayer`**
Selects a random online player.

- **Template Example**:
  {
    "template": "{baseCommand} {custom:randomplayer} minecraft:diamond 10"
  }

#### **4. `randomStringList`**
Selects a random string from a predefined list.

- **Parameters**:
  - `list`: A list of strings.

- **Template Example**:
  {
    "template": "{baseCommand} {player} {custom:randompokemon}"
  }

---

## **FAQs**
### Q: How do I execute command with the player name it in?
A: You can use {executor} placeholder for it.

### Q: How do I add new commands?
A: Create a new JSON file in the respective folder (`alias-commands` or `info-commands`). Use the provided examples as templates.

### Q: How do I set up command cooldowns?
A: Use the `cooldownSeconds` property to set a cooldown period in seconds, and `cooldownMessage` to customize the message shown when a command is on cooldown. The `{remaining}` placeholder will be replaced with the seconds left.

### Q: What happens if the JSON is invalid?
A: The mod will skip invalid files and log an error in the console.
---
