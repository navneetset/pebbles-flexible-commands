package tech.sethi.pebbles.flexiblecommands.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import tech.sethi.pebbles.flexiblecommands.config.ConfigHandler
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import tech.sethi.pebbles.flexiblecommands.FlexibleCommands.server
import tech.sethi.pebbles.flexiblecommands.util.PM
import tech.sethi.pebbles.flexiblecommands.util.PermUtil

object CommandRegistry {

    fun registerAliases(dispatcher: CommandDispatcher<ServerCommandSource>) {
        // Load configuration for dynamic commands
        val commands = ConfigHandler.loadAllCommandAliases()

        // Register each command dynamically
        commands.forEach { command ->
            val rootCommand = buildCommand(command)
            dispatcher.register(rootCommand)
        }
    }

    fun registerInfoCommands(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val infoCommands = ConfigHandler.loadAllInfoCommands()

        infoCommands.forEach { infoCommand ->
            infoCommand.aliases.forEach { alias ->
                val command = literal(alias)
                    .requires { source -> PermUtil.commandRequiresPermission(source, infoCommand.permission) }
                    .executes {
                        val source = it.source
                        source.sendFeedback({ PM.returnStyledText(infoCommand.message) }, false)
                        1
                    }
                dispatcher.register(command)
            }
        }
    }

    private fun buildCommand(command: ConfigHandler.CommandConfig): LiteralArgumentBuilder<ServerCommandSource> {
        val rootBuilder = literal(command.alias)
            .requires { source -> PermUtil.commandRequiresPermission(source, command.permission) }

        val argumentChain = buildArgumentChain(command)
        if (argumentChain != null) {
            rootBuilder.then(argumentChain)
        } else {
            rootBuilder.executes { context ->
                executeCommand(context, command)
            }
        }

        return rootBuilder
    }

    private fun buildArgumentChain(command: ConfigHandler.CommandConfig): ArgumentBuilder<ServerCommandSource, *>? {
        if (command.arguments.isEmpty()) {
            return null
        }

        var currentArgument: ArgumentBuilder<ServerCommandSource, *>? = null

        for (arg in command.arguments.reversed()) {
            val argumentBuilder = createArgument(arg)

            if (currentArgument == null) {
                argumentBuilder.executes { context ->
                    executeCommand(context, command)
                }
            } else {
                argumentBuilder.then(currentArgument)
            }

            currentArgument = argumentBuilder
        }

        return currentArgument
    }


    private fun createArgument(arg: ConfigHandler.CommandArgument): RequiredArgumentBuilder<ServerCommandSource, *> {
        return when (arg.type.lowercase()) {
            "string" -> argument(arg.name, StringArgumentType.string())
            "player" -> argument(arg.name, EntityArgumentType.player())
            "choice" -> {
                val choices = arg.choices ?: error("Choices required for 'choice' type in argument '${arg.name}'")
                argument(
                    arg.name, StringArgumentType.word()
                ).suggests { _, builder -> choices.forEach { builder.suggest(it) }; builder.buildFuture() }
            }

            "int" -> argument(arg.name, IntegerArgumentType.integer())
            else -> throw IllegalArgumentException("Unsupported argument type: ${arg.type}")
        }
    }

    private fun executeCommand(
        context: CommandContext<ServerCommandSource>,
        command: ConfigHandler.CommandConfig
    ): Int {
        val source = context.source
        val inputArgs = mutableMapOf<String, String>()

        command.arguments.forEach { arg ->
            val value = try {
                when (arg.type.lowercase()) {
                    "player" -> EntityArgumentType.getPlayer(context, arg.name).name.string // Get player name
                    else -> context.getArgument(arg.name, String::class.java)
                }
            } catch (e: IllegalArgumentException) {
                null
            }
            if (value != null) {
                inputArgs[arg.name] = value
            }
        }

        val customValues = command.customLogic.mapValues { (key, logic) ->
            processCustomLogic(logic.type, logic.params)
        }

        val finalCommand = command.template.replace(Regex("\\{(.+?)\\}")) { match ->
            val key = match.groupValues[1]

            when {
                key == "baseCommand" -> command.baseCommand
                key == "executor" -> {
                    val player = source.player
                    if (player != null) player.name.string else ""
                }
                key.startsWith("custom:") -> {
                    val parts = key.removePrefix("custom:").split(":")
                    val logicKey = parts[0]
                    val index = parts.getOrNull(1)?.toIntOrNull()
                    val logicValue = customValues[logicKey]

                    when {
                        index != null && logicValue is List<*> -> logicValue[index].toString() // Handle indexed list
                        index == null && logicValue != null -> logicValue.toString() // Handle non-indexed logic
                        else -> throw IllegalStateException("Invalid custom logic key or index: $key")
                    }
                }
                inputArgs.containsKey(key) -> inputArgs[key]!!
                else -> throw IllegalStateException("Missing argument: $key")
            }
        }

        when (command.runAs) {
            "console" -> PM.runCommand(finalCommand)
            "player" -> {
                val player = source.player ?: throw IllegalStateException("Player required for 'player' runAs type")
                PM.runCommandAsPlayer(player, finalCommand)
            }
            else -> throw IllegalArgumentException("Unsupported runAs type: ${command.runAs}")
        }

        println("Pebble's Flexible Command Executed: $finalCommand")
        return 1
    }




    private fun processCustomLogic(type: String, params: Map<String, Any>): Any {
        return when (type.lowercase()) {
            "guaranteedmaxivs" -> {
                val numMaxIvs = (params["numMaxIvs"] as? Double)?.toInt() ?: 3
                val maxValue = (params["maxValue"] as? Double)?.toInt() ?: 31
                val randomRangeList = params["randomRange"] as? List<*>
                val randomRange =
                    randomRangeList?.mapNotNull { (it as? Double)?.toInt() } ?: listOf(0, 31) // Default range

                // Validate random range
                if (randomRange.size != 2 || randomRange[0] > randomRange[1]) {
                    throw IllegalArgumentException("Invalid randomRange: $randomRange")
                }

                // Generate initial IVs
                val ivsArray = MutableList(6) { (randomRange[0]..randomRange[1]).random() }

                // Assign guaranteed max IVs
                val maxIndices = mutableSetOf<Int>()
                while (maxIndices.size < numMaxIvs) {
                    maxIndices.add((0 until 6).random())
                }
                maxIndices.forEach { index -> ivsArray[index] = maxValue }

                ivsArray
            }

            "randomnumberrange" -> {
                val min = (params["min"] as? Double)?.toInt() ?: 1
                val max = (params["max"] as? Double)?.toInt() ?: 64

                if (min > max) throw IllegalArgumentException("Invalid range: min ($min) is greater than max ($max)")

                (min..max).random()
            }

            "randomplayer" -> {
                val onlinePlayers = server!!.playerManager.playerList
                if (onlinePlayers.isEmpty()) throw IllegalStateException("No players online to pick a random player")
                onlinePlayers.random().name.string
            }

            "randomstringlist" -> {
                val list = params["list"] as? List<*> ?: throw IllegalArgumentException("Missing or invalid list parameter")
                if (list.isEmpty()) throw IllegalArgumentException("List parameter for randomstringlist cannot be empty")
                list.random().toString()
            }

            else -> throw IllegalArgumentException("Unsupported custom logic type: $type")
        }
    }






}
