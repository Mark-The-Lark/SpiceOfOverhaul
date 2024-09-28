package ru.markthelark.spiceofoverhaul;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.markthelark.spiceofoverhaul.effect.WellFedEffect;
import ru.markthelark.spiceofoverhaul.network.SyncHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.markthelark.spiceofoverhaul.util.FoodEventHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpiceOfOverhaul.MODID)
public class SpiceOfOverhaul
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "spiceofoverhaul";
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final RegistryObject<MobEffect> WELLFED = EFFECTS.register("wellfed", WellFedEffect::new);


    public SpiceOfOverhaul()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        MinecraftForge.EVENT_BUS.register(FoodEventHandler.class);
        MinecraftForge.EVENT_BUS.register(this);
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );
    }

    private void preInit(final FMLCommonSetupEvent event)
    {
        if (ModList.get().isLoaded("appleskin") && Config.enableSOLModule) {
            SyncHandler.init();
        }
    }
}
