package ru.markthelark.spiceofoverhaul;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import ru.markthelark.spiceofoverhaul.util.FormulaProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


//implement configs
@Mod.EventBusSubscriber(modid = SpiceOfOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SOL_MODULE = BUILDER
            .comment("Spice of Life system of lowering food stats when it is being eaten too often")
            .define("enableSOLModule", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> IDLE_EXHAUSTION = BUILDER
            .comment("Small number of exhaustion will be applied constantly")
            .define("idleExhaustion", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HEALTH_STRIKES = BUILDER
            .comment("Strikes for having low hp")
            .define("lowHealthStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HUNGER_STRIKES = BUILDER
            .comment("Strikes for having low hunger")
            .define("lowHungerStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> STRIKES_JUMP = BUILDER
            .comment("You cannot jump when having extreme strikes")
            .define("strikesJump", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> HIGH_SATURATION_STRIKES = BUILDER
            .comment("Strikes for having high saturation")
            .define("highSaturationStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_REWORK = BUILDER
            .comment("Regeneration only requires hunger, not saturation")
            .define("regenRework", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> CAKE_EATABLE_ANYWAY = BUILDER
            .comment("Makes cake eatable even if it does not give any hunger")
            .define("cakeEatableAnyway", false);
//    private static final ForgeConfigSpec.IntValue JUMP_HUNGER = BUILDER
//            .comment("A minimal required hunger to be able to jump")
//            .defineInRange("jumpHunger", 10, 0, 20);
//
//    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_EXPRESSION = BUILDER
//            .comment("NOT IMPLEMENTED YET formula for dependence of hunger on quantity of eaten, use HUNGER, SATURATION, EATEN and operations +,-,*,/,^")
//            .define("magicExpression", "HUNGER*(EATEN)^0.7");
//
//    // a list of strings that are treated as resource locations for items
//    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
//            .comment("A list of items to log on common setup.")
//            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableSOLModule;
    public static boolean idleExhaustion;
    public static boolean lowHealthStrikes;
    public static boolean lowHungerStrikes;
    public static boolean strikesJump;
    public static boolean highSaturationStrikes;

    public static boolean regenRework;
    public static boolean cakeEatableAnyway;
//    public static float idleExhaustion;
    public static int jumpHunger;
    public static String magicExpression;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        enableSOLModule = ENABLE_SOL_MODULE.get();
        idleExhaustion = IDLE_EXHAUSTION.get();
        lowHungerStrikes =LOW_HUNGER_STRIKES.get();
        lowHealthStrikes = LOW_HEALTH_STRIKES.get();
        strikesJump = STRIKES_JUMP.get();
        highSaturationStrikes = HIGH_SATURATION_STRIKES.get();
        regenRework = REGEN_REWORK.get();
        cakeEatableAnyway = CAKE_EATABLE_ANYWAY.get();
//        jumpHunger = JUMP_HUNGER.get();
//        magicExpression = MAGIC_EXPRESSION.get();
//
//        // convert the list of strings into a set of items
//        items = ITEM_STRINGS.get().stream()
//                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
//                .collect(Collectors.toSet());
    }
}
