package tech.sethi.pebbles.flexiblecommands.config

import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.CommandArgument
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.CommandConfig
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler.InfoCommandConfig

val givePokeCommand = CommandConfig(
    alias = "givepoke",
    baseCommand = "pokegiveother",
    permission = "flexiblecommands.command.givepoke",
    template = "{baseCommand} {player} {pokemon} shiny={shiny} galarian={galarian} speed_iv={custom:ivs:0} attack_iv={custom:ivs:1} special_attack_iv={custom:ivs:2} special_defence_iv={custom:ivs:3} defence_iv={custom:ivs:4} hp_iv={custom:ivs:5}",
    arguments = listOf(
        CommandArgument(name = "player", type = "player"),
        CommandArgument(name = "pokemon", type = "string"),
        CommandArgument(name = "shiny", type = "choice", choices = listOf("true", "false")),
        CommandArgument(name = "galarian", type = "choice", choices = listOf("true", "false"))
    ),
    customLogic = mapOf(
        "ivs" to ConfigHandler.LogicConfig(
            type = "guaranteedmaxivs",
            params = mapOf(
                "numMaxIvs" to 3,
                "maxValue" to 31,
                "randomRange" to listOf(0, 31)
            )
        )
    )
)


val exampleCommand = CommandConfig(
    alias = "example",
    baseCommand = "say",
    permission = "flexiblecommands.command.example",
    template = "{baseCommand} Hello {player}!",
    arguments = listOf(
        CommandArgument(name = "player", type = "player")
    )
)

val giveRandomDiamondsCommand = CommandConfig(
    alias = "giverandomdiamonds",
    baseCommand = "give",
    permission = "flexiblecommands.command.giverandomdiamonds",
    template = "{baseCommand} {player} minecraft:diamond {custom:randomamount}",
    arguments = listOf(
        CommandArgument(name = "player", type = "player")
    ),
    customLogic = mapOf(
        "randomamount" to ConfigHandler.LogicConfig(
            type = "randomnumberrange",
            params = mapOf(
                "min" to 1,
                "max" to 64
            )
        )
    )
)

val giveRandomDiamondsRandomCommand = CommandConfig(
    alias = "giverandomdiamondsrandom",
    baseCommand = "give",
    permission = "flexiblecommands.command.giverandomdiamondsrandom",
    template = "{baseCommand} {custom:randomplayer} minecraft:diamond {custom:randomamount}",
    arguments = listOf(), // No arguments needed for this command
    customLogic = mapOf(
        "randomplayer" to ConfigHandler.LogicConfig(
            type = "randomplayer",
            params = emptyMap() // No parameters needed for randomplayer
        ),
        "randomamount" to ConfigHandler.LogicConfig(
            type = "randomnumberrange",
            params = mapOf(
                "min" to 1,
                "max" to 64
            )
        )
    )
)

val givePokeRandomCommand = CommandConfig(
    alias = "givepokerandom",
    baseCommand = "pokegiveother",
    permission = "flexiblecommands.command.givepokerandom",
    template = "{baseCommand} {player} {custom:randompokemon}",
    arguments = listOf(
        CommandArgument(name = "player", type = "player")
    ),
    customLogic = mapOf(
        "randompokemon" to ConfigHandler.LogicConfig(
            type = "randomstringlist",
            params = mapOf(
                "list" to listOf("bulbasaur", "charmander", "squirtle", "pikachu", "jigglypuff", "meowth")
            )
        )
    )
)

val defaultDiscordCommand = InfoCommandConfig(
    aliases = listOf("discord", "discordlink", "dc", "disc"),
    permission = "flexiblecommands.command.discord",
    message = "<green>Join our [<yellow><click:open_url:'https://discord.gg/kzpyuB57Gu'>Discord</click></yellow>] server!"
)