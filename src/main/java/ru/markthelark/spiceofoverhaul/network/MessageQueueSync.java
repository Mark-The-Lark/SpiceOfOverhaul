package ru.markthelark.spiceofoverhaul.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ru.markthelark.spiceofoverhaul.SpiceOfOverhaul;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;


public record MessageQueueSync(String foodQueue) implements CustomPacketPayload
{
    public static final Type<MessageQueueSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SpiceOfOverhaul.MODID, "foodQueue"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageQueueSync> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MessageQueueSync::foodQueue,
            MessageQueueSync::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static void handle(final MessageQueueSync message, final IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            ((FoodHashAccessor)ctx.player().getFoodData()).setFoodQueue(message.foodQueue);
        });
    }
}
