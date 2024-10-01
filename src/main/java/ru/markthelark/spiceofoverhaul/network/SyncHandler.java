package ru.markthelark.spiceofoverhaul.network;

import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ru.markthelark.spiceofoverhaul.SpiceOfOverhaul;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


public class SyncHandler
{
    private static final String PROTOCOL_VERSION = "1.0.0";

    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar(SpiceOfOverhaul.MODID)
                .versioned(PROTOCOL_VERSION)
                .optional();

        registrar.playToClient(MessageQueueSync.TYPE, MessageQueueSync.CODEC, MessageQueueSync::handle);

        NeoForge.EVENT_BUS.register(new SyncHandler());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer))
            return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        FoodData foodData = player.getFoodData();
        if (foodData instanceof FoodHashAccessor){
            var msg = new MessageQueueSync(((FoodHashAccessor)(foodData)).getFoodQueueString());
            PacketDistributor.sendToPlayer(player, msg);
        }
    }
}

