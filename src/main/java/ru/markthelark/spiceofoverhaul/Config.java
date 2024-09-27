package ru.markthelark.spiceofoverhaul;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


//implement configs
@Mod.EventBusSubscriber(modid = SpiceOfOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SOL_MODULE = BUILDER
            .comment("Spice of Life system of lowering food stats when it is being eaten too often")
            .define("enableSOLModule", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> HISTORY_LENGTH = BUILDER
            .comment("Number of eaten foods that will affect food values of next item eaten")
            .defineInRange("historyLength", 20, 1, 1000);
    private static final ForgeConfigSpec.ConfigValue<Boolean> IDLE_EXHAUSTION = BUILDER
            .comment("Configured number of exhaustion will be applied constantly")
            .define("idleExhaustion", true);
    private static final ForgeConfigSpec.ConfigValue<Double> IDLE_EXHAUSTION_QUANTITY = BUILDER
            .comment("The number which will be added to exhaustion each tick")
            .defineInRange("idleExhaustionQuantity", 0.01f, 0.0f, 4.0f);
    private static final ForgeConfigSpec.ConfigValue<Boolean> GUI_TEXT = BUILDER
            .comment("adds text to GUI describing health and hunger status !HAVE A LOT OF PROBLEMS IMPLEMENTING THIS, WRITE ME ON GITHUB IF YOU KNOW HOW TO DEAL WITH MINECRAFT GUI!")
            .define("addGuiText", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HEALTH_STRIKES = BUILDER
            .comment("Strikes for having low hp")
            .define("lowHealthStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Double> HEALTH_LEVEL = BUILDER
            .comment("Strikes for having low hp start at this health level")
            .defineInRange("healthLevel", 0.25f, 0.05f, 1f);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HUNGER_STRIKES = BUILDER
            .comment("Strikes for having low hunger")
            .define("lowHungerStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Integer> HUNGER_LEVEL = BUILDER
            .comment("Strikes for having low hunger start at this hunger level")
            .defineInRange("hungerLevel", 5, 1, 20);
    private static final ForgeConfigSpec.ConfigValue<Boolean> HIGH_SATURATION_STRIKES = BUILDER
            .comment("Strikes for having high saturation")
            .define("highSaturationStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Double> SATURATION_LEVEL = BUILDER
            .comment("Strikes for having high saturation start at this saturation level")
            .defineInRange("saturationLevel", 5f, 0f, 10f);
    private static final ForgeConfigSpec.ConfigValue<Boolean> STRIKES_JUMP = BUILDER
            .comment("You cannot jump when having extreme strikes")
            .define("strikesJump", true);
    private static final ForgeConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_BASE = BUILDER
            .comment("This value is equal to highest effect amplifier when you have 1 hp/hunger")
            .defineInRange("strikesDifficultyBase", 1, 0, 10);
    private static final ForgeConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_SCALE = BUILDER
            .comment("This value will be added to effect amplifier for each difficulty level past PEACEFUL")
            .defineInRange("strikesDifficultyScale", 1, 0, 10);

    private static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_REWORK = BUILDER
            .comment("Regeneration only requires hunger, not saturation")
            .define("regenRework", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> CAKE_EATABLE_ANYWAY = BUILDER
            .comment("Makes cake eatable even if it does not give any hunger")
            .define("cakeEatableAnyway", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableSOLModule;
    public static boolean idleExhaustion;
    public static float idleExhaustionQuantity;
    public static boolean addGuiText;
    public static boolean lowHealthStrikes;
    public static float healthLevel;
    public static boolean lowHungerStrikes;
    public static int hungerLevel;
    public static boolean highSaturationStrikes;
    public static float saturationLevel;
    public static boolean strikesJump;
    public static boolean regenRework;
    public static boolean cakeEatableAnyway;
    public static int historyLength;
    public static int strikesDifficultyBase;
    public static int StrikesDifficultyScale;
//    public static String magicExpression;

//    private static boolean validateItemName(final Object obj)
//    {
//        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
//    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        enableSOLModule = ENABLE_SOL_MODULE.get();
        historyLength = HISTORY_LENGTH.get();

        idleExhaustion = IDLE_EXHAUSTION.get();
        idleExhaustionQuantity = IDLE_EXHAUSTION_QUANTITY.get().floatValue();

        addGuiText =GUI_TEXT.get();

        lowHungerStrikes =LOW_HUNGER_STRIKES.get();
        hungerLevel = HUNGER_LEVEL.get();
        lowHealthStrikes = LOW_HEALTH_STRIKES.get();
        healthLevel = HEALTH_LEVEL.get().floatValue();
        highSaturationStrikes = HIGH_SATURATION_STRIKES.get();
        saturationLevel = SATURATION_LEVEL.get().floatValue();

        strikesJump = STRIKES_JUMP.get();
        strikesDifficultyBase = STRIKES_DIFFICULTY_BASE.get();
        StrikesDifficultyScale = STRIKES_DIFFICULTY_SCALE.get();

        regenRework = REGEN_REWORK.get();
        cakeEatableAnyway = CAKE_EATABLE_ANYWAY.get();

    }
}
