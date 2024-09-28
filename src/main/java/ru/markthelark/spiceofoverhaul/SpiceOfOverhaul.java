package ru.markthelark.spiceofoverhaul;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.markthelark.spiceofoverhaul.effect.WellFedEffect;
import ru.markthelark.spiceofoverhaul.network.SyncHandler;
import ru.markthelark.spiceofoverhaul.util.AppleSkinEvent;
import ru.markthelark.spiceofoverhaul.util.FoodEventHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpiceOfOverhaul.MODID)
public class SpiceOfOverhaul
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "spiceofoverhaul";
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, MobEffect> WELLFED = EFFECTS.register("wellfed", WellFedEffect::new);


    public SpiceOfOverhaul(IEventBus modEventBus, ModContainer modContainer)
    {
        //NeoForge.EVENT_BUS.addListener(this::onRegisterPayloadHandler);
        NeoForge.EVENT_BUS.register(FoodEventHandler.class);
        if (ModList.get().isLoaded("appleskin")) {
            NeoForge.EVENT_BUS.register(AppleSkinEvent.class);
        }
//        NeoForge.EVENT_BUS.register(this);
            EFFECTS.register(modEventBus);
        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );
    }

//    @SubscribeEvent
//    private void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event)
//    {
//        SyncHandler.register(event);
//    }
}
