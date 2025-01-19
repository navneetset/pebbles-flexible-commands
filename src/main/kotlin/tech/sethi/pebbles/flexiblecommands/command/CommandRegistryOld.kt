package tech.sethi.pebbles.flexiblecommands.command


//object CommandRegistryOld {
//    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
//        // Load configuration for dynamic commands
//        ConfigHandler.loadConfig()
//
//        // Register each command dynamically
//        ConfigHandler.commands.forEach { command ->
//            val rootCommand = buildCommand(command)
//            dispatcher.register(rootCommand)
//        }
//
//    }
//
//    private fun buildCommand(command: ConfigHandler.CommandAlias): LiteralArgumentBuilder<ServerCommandSource> {
//        // Start with the root literal command
//        val rootBuilder =
//            literal(command.alias).requires { source -> source.hasPermissionLevel(2) } // Permission level check
//
//        // Build nested arguments
//        val argumentChain = buildArgumentChain(command)
//
//        // Attach the argument chain to the root
//        rootBuilder.then(argumentChain)
//
//        return rootBuilder
//    }
//
//    private fun buildArgumentChain(command: ConfigHandler.CommandAlias): ArgumentBuilder<ServerCommandSource, *> {
//        // Start from the last argument and build backward
//        var currentArgument: ArgumentBuilder<ServerCommandSource, *>? = null
//
//        for (arg in command.arguments.reversed()) {
//            val argumentBuilder = createArgument(arg)
//
//            if (currentArgument == null) {
//                // Attach the `executes` block to the last argument
//                argumentBuilder.executes { context ->
//                    executeCommand(context, command)
//                }
//            } else {
//                // Chain the current argument to the next
//                argumentBuilder.then(currentArgument)
//            }
//
//            currentArgument = argumentBuilder
//        }
//
//        return currentArgument!!
//    }
//
//    private fun createArgument(arg: ConfigHandler.CommandArgument): RequiredArgumentBuilder<ServerCommandSource, *> {
//        return when (arg.type.lowercase()) {
//            "string" -> argument(arg.name, StringArgumentType.string())
//            "boolean" -> argument(arg.name, BoolArgumentType.bool())
//            "int" -> argument(arg.name, IntegerArgumentType.integer())
//            else -> throw IllegalArgumentException("Unsupported argument type: ${arg.type}")
//        }
//    }
//
//    private fun executeCommand(
//        context: CommandContext<ServerCommandSource>, command: ConfigHandler.CommandAlias
//    ): Int {
//        val source = context.source
//        val inputArgs = mutableMapOf<String, String>()
//
//        // Collect arguments from the context
//        command.arguments.forEach { arg ->
//            val value = try {
//                context.getArgument(arg.name, String::class.java)
//            } catch (e: IllegalArgumentException) {
//                arg.default
//            }
//            if (value != null) {
//                inputArgs[arg.name] = value
//            }
//        }
//
//        // Construct and execute the final command string
//        val constructedCommand = constructCommand(command, inputArgs)
//        PM.runCommand(constructedCommand)
//
//        // Send feedback
//        source.sendFeedback({ Text.literal("Executed: $constructedCommand") }, false)
//        return 1
//    }
//
//
////    fun exampleCommand(dispatcher: CommandDispatcher<ServerCommandSource>) {
////        val givepoke = literal("givepoke").requires { it.hasPermissionLevel(2) }
////
////        val player = argument("player", StringArgumentType.string())
////        val pokemon = argument("pokemon", StringArgumentType.string()).executes {
////            val source = it.source
////            val playerArg = StringArgumentType.getString(it, "player")
////            val pokemonArg = StringArgumentType.getString(it, "pokemon")
////            source.sendFeedback(
////                { PM.returnStyledText("<blue>Executing givepoke command for $playerArg with $pokemonArg") }, false
////            )
////            1
////        }
////
////        player.then(pokemon)
////
////        givepoke.then(player)
////
////        dispatcher.register(givepoke)
////    }
//
//}
