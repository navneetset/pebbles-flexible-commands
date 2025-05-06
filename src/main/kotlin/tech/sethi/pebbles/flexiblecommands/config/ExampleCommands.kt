package tech.sethi.pebbles.flexiblecommands.config

import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.CommandArgument
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.CommandConfig
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.InfoCommandConfig
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.LogicConfig

val givePokeCommand = CommandConfig(
    alias = "givepoke",
    runAs = "console",
    cooldownSeconds = 60, // 1 minute cooldown
    cooldownMessage = "<red>You must wait {remaining} seconds before using this command again!",
    baseCommand = "pokegiveother",
    permission = "flexiblecommands.command.givepoke",
    template = "{baseCommand} {player} {pokemon}",
    arguments = listOf(
        CommandArgument(
            name = "player",
            type = "player"
        ),
        CommandArgument(
            name = "pokemon",
            type = "choice",
            choices = listOf("pikachu", "eevee", "charizard", "bulbasaur", "squirtle")
        )
    ),
    customLogic = emptyMap()
)

val exampleCommand = CommandConfig(
    alias = "example",
    runAs = "console",
    baseCommand = "give",
    permission = "flexiblecommands.command.example",
    template = "{baseCommand} {player} minecraft:diamond {count}",
    arguments = listOf(
        CommandArgument(
            name = "player",
            type = "player"
        ),
        CommandArgument(
            name = "count",
            type = "int"
        )
    ),
    customLogic = emptyMap()
)

val giveRandomDiamondsCommand = CommandConfig(
    alias = "randomdiamonds",
    runAs = "console",
    baseCommand = "give",
    permission = "flexiblecommands.command.randomdiamonds",
    template = "{baseCommand} {player} minecraft:diamond {custom:randomnumber}",
    arguments = listOf(
        CommandArgument(
            name = "player",
            type = "player"
        )
    ),
    customLogic = mapOf(
        "randomnumber" to LogicConfig(
            type = "randomnumberrange",
            params = mapOf(
                "min" to 1.0,
                "max" to 64.0
            )
        )
    )
)

val giveRandomDiamondsRandomCommand = CommandConfig(
    alias = "randomdiamondsrandom",
    runAs = "console",
    baseCommand = "give",
    permission = "flexiblecommands.command.randomdiamondsrandom",
    template = "{baseCommand} {custom:randomplayername} minecraft:diamond {custom:randomnumber}",
    arguments = listOf(),
    customLogic = mapOf(
        "randomnumber" to LogicConfig(
            type = "randomnumberrange",
            params = mapOf(
                "min" to 1.0,
                "max" to 64.0
            )
        ),
        "randomplayername" to LogicConfig(
            type = "randomplayer",
            params = mapOf()
        )
    )
)

val givePokeRandomCommand = CommandConfig(
    alias = "giverandom",
    runAs = "console",
    cooldownSeconds = 300, // 5 minute cooldown
    cooldownMessage = "<red>This command is on cooldown! Try again in {remaining} seconds.",
    baseCommand = "pokegiveother",
    permission = "flexiblecommands.command.giverandom",
    template = "{baseCommand} {player} {custom:randompokemon}",
    arguments = listOf(
        CommandArgument(
            name = "player",
            type = "player"
        )
    ),
    customLogic = mapOf(
        "randompokemon" to LogicConfig(
            type = "randomstringlist",
            params = mapOf(
                "list" to listOf("pikachu", "eevee", "charizard", "bulbasaur", "squirtle")
            )
        )
    )
)

val defaultDiscordCommand = InfoCommandConfig(
    aliases = listOf("discord", "discordlink", "dc", "disc"),
    permission = "flexiblecommands.command.discord",
    message = "<green>Join our [<yellow><click:open_url:'https://discord.gg/kzpyuB57Gu'>Discord</click></yellow>] server!"
)