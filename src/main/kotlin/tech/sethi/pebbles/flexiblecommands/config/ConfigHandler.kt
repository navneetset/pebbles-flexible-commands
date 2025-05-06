package tech.sethi.pebbles.flexiblecommands.config

import com.google.gson.GsonBuilder
import java.io.File

object ConfigHandler {

    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()



    fun loadAllCommandAliases(): List<CommandConfig> {
        val commandFolder = File("config/pebbles-flexible-commands/alias-commands")
        if (!commandFolder.exists()) {
            commandFolder.mkdirs()
            // Create 5 example commands
            File(commandFolder, "givepoke.json").writeText(gson.toJson(givePokeCommand))
            File(commandFolder, "example.json").writeText(gson.toJson(exampleCommand))
            File(commandFolder, "giverandomdiamonds.json").writeText(gson.toJson(giveRandomDiamondsCommand))
            File(commandFolder, "giverandomdiamondsrandom.json").writeText(gson.toJson(giveRandomDiamondsRandomCommand))
            File(commandFolder, "givepokerandom.json").writeText(gson.toJson(givePokeRandomCommand))
        }

        val files = commandFolder.listFiles { file -> file.extension == "json" } ?: emptyArray()

        return files.flatMap { file ->
            try {
                val content = file.readText()
                val command = gson.fromJson(content, CommandConfig::class.java)
                listOf(command)
            } catch (e: Exception) {
                println("Failed to load command from file: ${file.name} - ${e.message}")
                emptyList()
            }
        }
    }

    fun loadAllInfoCommands(): List<InfoCommandConfig> {
        val infoFolder = File("config/pebbles-flexible-commands/info-commands")
        if (!infoFolder.exists()) {
            infoFolder.mkdirs()
            // Create example info commands
            File(infoFolder, "discord.json").writeText(gson.toJson(defaultDiscordCommand))
        }

        val files = infoFolder.listFiles { file -> file.extension == "json" } ?: emptyArray()

        return files.flatMap { file ->
            try {
                val content = file.readText()
                val command = gson.fromJson(content, InfoCommandConfig::class.java)
                listOf(command)
            } catch (e: Exception) {
                println("Failed to load info command from file: ${file.name} - ${e.message}")
                emptyList()
            }
        }
    }


    data class CommandConfig(
        val alias: String,                          // Command alias (e.g., "givepoke")
        val runAs: String = "console",              // Command to run as ("console", "player")
//        val commandCooldownSeconds: Int? = 0,       // Cooldown in seconds for player commands
        val baseCommand: String,                    // The base command to execute (e.g., "pokegiveother")
        val permission: String,                     // Permission string (e.g., "flexiblecommands.command.givepoke")
        val template: String,                       // Template string for the final command
        val arguments: List<CommandArgument>,       // List of arguments for the command
        val customLogic: Map<String, LogicConfig> = emptyMap()   // Custom logic definitions
    )

    data class CommandArgument(
        val name: String,                           // Argument name (e.g., "player", "pokemon")
        val type: String,                           // Argument type (e.g., "string", "int", "choice")
        val choices: List<String>? = null           // Choices for "choice" type arguments
    )

    data class LogicConfig(
        val type: String,                           // Type of custom logic (e.g., "guaranteedMaxIvs")
        val params: Map<String, Any>                // Parameters for the logic
    )

    data class InfoCommandConfig(
        val aliases: List<String> = listOf("discord", "discordlink", "dc", "disc"),
        val permission: String = "flexiblecommands.command.discord",
        val message: String = "<green>Join our [<yellow><click:open_url:'https://discord.gg/kzpyuB57Gu'>Discord</click></yellow>] server!"
    )

}
