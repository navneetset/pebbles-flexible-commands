# Pebble's Flexible Commands Mod

### Web Editor for configuration help: [Web Editor](https://fce.sethi.tech/)

Pebble's Flexible Commands Mod is a highly customizable Minecraft server mod designed to empower server administrators with the ability to create and manage dynamic and info-based commands. Whether you want to automate repetitive tasks, provide interactive player experiences, or simplify admin workflows, this mod gives you the flexibility to configure commands to suit your needs.

---

## **Features**

- **Dynamic Commands:** Define powerful commands with arguments, custom logic, and variable templates.
- **Info Commands:** Set up simple text-based commands to share important information like server links or tips.
- **Command Cooldowns:** Set time limits between command uses with persistent cooldowns that survive server restarts.
- **Custom Logic Support:**
  - Generate random values (e.g., numbers, items, players).
  - Create advanced logic such as guaranteed maximum IVs for Pokémon mods.
- **Easy JSON Configuration:** Configure commands through straightforward JSON files.
- **Supports Permissions:** Assign permissions to commands for precise access control.
- **Aliases:** Add multiple aliases for the same command for user convenience.
- **Player and Console Execution:** Specify if commands should run as the player or via the console.

---

## **Why Use Pebble's Flexible Commands Mod?**

- Fully customizable: No hardcoding required, everything is configured via JSON.
- Enhance player engagement with randomized rewards and dynamic logic.
- Reduce admin overhead by automating repetitive tasks.
- Prevent command abuse with configurable cooldowns.

---

## **Getting Started**

1. Install the mod on your server.
2. Navigate to `config/pebbles-flexible-commands` to configure commands.
3. Restart the server to apply changes.


Check out the [Wiki](https://github.com/navneetset/pebbles-flexible-commands/wiki) for detailed configuration guides, examples, and troubleshooting.

---

## **Command Cooldowns**

You can now set cooldowns on commands to limit how frequently they can be used:

```json
{
  "alias": "kitdaily",
  "cooldownSeconds": 86400,
  "cooldownMessage": "<red>You can only use this once per day! Wait <yellow>{remaining}</yellow> seconds."
  // other command properties...
}
```

- **cooldownSeconds**: Time in seconds before the command can be used again by the same player
- **cooldownMessage**: Message shown when on cooldown (use {remaining} to show seconds left)
- Cooldowns persist between server restarts

---

## **Feedback and Support**

Found a bug? Have a feature request? Let us know on our [GitHub Issues](https://github.com/navneetset/pebbles-flexible-commands/issues) page.
