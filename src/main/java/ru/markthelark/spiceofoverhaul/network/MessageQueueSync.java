package ru.markthelark.spiceofoverhaul.network;

import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.network.NetworkEvent;
import squeek.appleskin.network.NetworkHelper;

import java.util.function.Supplier;

public class MessageQueueSync
{
    public String foodQueue;

    public MessageQueueSync(String foodQueue)
    {
        this.foodQueue = foodQueue;
    }

    public static void encode(MessageQueueSync pkt, FriendlyByteBuf buf)
    {
        buf.writeUtf(pkt.foodQueue);
    }

    public static MessageQueueSync decode(FriendlyByteBuf buf)
    {
        String msg = buf.readUtf();
        return new MessageQueueSync(msg);
    }

    public static void handle(final MessageQueueSync message, Supplier<NetworkEvent.Context> ctx)
    {
        Player player = NetworkHelper.getSidedPlayer(ctx.get());
        if (!(player instanceof LocalPlayer)){
            return;
        }
        FoodData foodData = player.getFoodData();
        if (foodData instanceof FoodHashAccessor) {
            ctx.get().enqueueWork(() -> {
                ((FoodHashAccessor)foodData).setFoodQueue(message.foodQueue);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
