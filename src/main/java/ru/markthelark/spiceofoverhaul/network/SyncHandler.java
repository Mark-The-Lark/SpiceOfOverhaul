package ru.markthelark.spiceofoverhaul.network;

import ru.markthelark.spiceofoverhaul.SpiceOfOverhaul;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SyncHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SpiceOfOverhaul.MODID, "sync"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void init()
    {
        CHANNEL.registerMessage(1, MessageQueueSync.class, MessageQueueSync::encode, MessageQueueSync::decode, MessageQueueSync::handle);

        MinecraftForge.EVENT_BUS.register(new SyncHandler());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer))
            return;
        ServerPlayer _player = (ServerPlayer) event.getEntity();
        if (!(_player.getFoodData() instanceof FoodHashAccessor))
            return;
        Object msg = new MessageQueueSync(((FoodHashAccessor)(_player.getFoodData())).getFoodQueueString());
        CHANNEL.sendTo(msg, _player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

