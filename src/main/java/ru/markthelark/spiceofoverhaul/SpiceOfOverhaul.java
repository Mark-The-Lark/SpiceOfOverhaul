package ru.markthelark.spiceofoverhaul;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
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


    public SpiceOfOverhaul()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        MinecraftForge.EVENT_BUS.register(FoodEventHandler.class);
        MinecraftForge.EVENT_BUS.register(this);

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
