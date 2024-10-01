package ru.markthelark.spiceofoverhaul;


import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.markthelark.spiceofoverhaul.effect.WellFedEffect;
import ru.markthelark.spiceofoverhaul.items.FoodBag;
import ru.markthelark.spiceofoverhaul.network.SyncHandler;
import ru.markthelark.spiceofoverhaul.util.AppleSkinEvent;
import ru.markthelark.spiceofoverhaul.util.FoodEventHandler;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpiceOfOverhaul.MODID)
public class SpiceOfOverhaul
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "spiceofoverhaul";
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, MobEffect> WELLFED = EFFECTS.register("wellfed", WellFedEffect::new);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final Supplier<Item> LUNCH_BOX = ITEMS.register("lunch_box", () -> new FoodBag(new Item.Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));
    public static final Supplier<Item> PAPER_BAG = ITEMS.register("paper_bag", () -> new FoodBag(new Item.Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));


    public SpiceOfOverhaul(IEventBus modEventBus, ModContainer modContainer)
    {

        modEventBus.addListener(this::onRegisterPayloadHandler);
        NeoForge.EVENT_BUS.register(FoodEventHandler.class);
        if (ModList.get().isLoaded("appleskin")) {
            NeoForge.EVENT_BUS.register(AppleSkinEvent.class);
        }
//        NeoForge.EVENT_BUS.register(this);
            EFFECTS.register(modEventBus);
            ITEMS.register(modEventBus);
        modContainer.registerConfig(
                net.neoforged.fml.config.ModConfig.Type.COMMON,
                Config.SPEC
        );
    }

    @SubscribeEvent
    private void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event)
    {
        SyncHandler.register(event);
    }
}
