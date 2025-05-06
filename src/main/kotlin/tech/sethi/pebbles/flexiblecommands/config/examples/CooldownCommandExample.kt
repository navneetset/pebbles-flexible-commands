package tech.sethi.pebbles.flexiblecommands.config.examples

/**
 * Example JSON configuration for a command with cooldown
 *
 * Save this to: config/pebbles-flexible-commands/alias-commands/kitdaily.json
 */
val kitDailyExampleJson = """
{
  "alias": "kitdaily",
  "runAs": "console",
  "cooldownSeconds": 86400,
  "cooldownMessage": "<red>You can only use your daily kit once per day! Please wait <yellow>{remaining}</yellow> seconds.",
  "baseCommand": "give",
  "permission": "flexiblecommands.command.kitdaily",
  "template": "/{baseCommand} {player} diamond 5\n/{baseCommand} {player} golden_apple 2\n/{baseCommand} {player} iron_sword{custom:enchant}\n/tellraw {player} {\"text\":\"You received your daily kit!\",\"color\":\"green\"}",
  "arguments": [
    {
      "name": "player",
      "type": "player"
    }
  ],
  "customLogic": {
    "enchant": {
      "type": "loop",
      "params": {
        "count": 1,
        "command": "{\"id\":\"minecraft:sharpness\",\"lvl\":3}"
      }
    }
  }
}
""" 