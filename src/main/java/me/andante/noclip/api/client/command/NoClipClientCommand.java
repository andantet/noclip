package me.andante.noclip.api.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.api.client.config.NoClipConfig;
import me.andante.noclip.api.client.keybinding.NoClipKeybindings;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public interface NoClipClientCommand {
    String CONFIG_RELOAD_KEY = "text.noclip.config_reload_successful";
    SimpleCommandExceptionType CONFIG_SYNTAX_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("text." + NoClip.MOD_ID + ".config_reload_syntax_error"));

    static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal(NoClip.MOD_ID)
            .executes(NoClipClientCommand::execute)
            .then(literal("config")
                .executes(NoClipClientCommand::executeConfig)
                .then(literal("reload")
                    .executes(NoClipClientCommand::executeConfigReload)
                )
            )
        );
    }

    static int execute(CommandContext<FabricClientCommandSource> context) {
        boolean pressed = !NoClipKeybindings.ACTIVATE_NOCLIP.isPressed();
        NoClipKeybindings.ACTIVATE_NOCLIP.forceSetPressed(pressed);
        return pressed ? 1 : 0;
    }

    static int executeConfig(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        MinecraftClient client = source.getClient();
        client.send(() -> client.setScreen(NoClipConfig.createScreen(client.currentScreen)));
        return 1;
    }

    static int executeConfigReload(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        if (!AutoConfig.getConfigHolder(NoClipConfig.class).load()) throw CONFIG_SYNTAX_EXCEPTION.create();
        context.getSource().sendFeedback(Text.translatable(CONFIG_RELOAD_KEY).setStyle(NoClipClient.getTextStyle()));
        return 1;
    }
}
