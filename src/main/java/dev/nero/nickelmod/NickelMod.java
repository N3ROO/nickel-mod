package dev.nero.nickelmod;

import dev.nero.nickelmod.lists.BlockList;
import dev.nero.nickelmod.lists.ItemList;
import dev.nero.nickelmod.lists.ToolMaterialList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// This is the mod id. It must be the same as the one in mods.toml.
@Mod("nickelmod")
public class NickelMod {

    public static NickelMod INSTANCE;
    public static final String MODID = "nickelmod";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public NickelMod() {
        INSTANCE = this;

        // We need to call these two functions to make sure that setup and clientRegistries are called
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        // It tells to Forge that this mod exists!
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        for(Biome biome : ForgeRegistries.BIOMES) {
            // Here we say that the nickel ore should appear:
            // - between layers 50 and 100
            // - with its default state
            // - 4 attached at most
            // - 10 max per chunk
            biome.addFeature(
                    GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(
                            Feature.ORE,
                            new OreFeatureConfig(
                                    OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                    BlockList.nickel_ore.getDefaultState(),
                                    4           // vein size (how many ores are attached at most?)
                            ),
                            Placement.COUNT_RANGE,
                            new CountRangeConfig(
                                    10,         // max number of block in this layer -> probability
                                    50,    // minimum height
                                    0,        // height base
                                    100      // max height
                            )
                    )
            );
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Here goes the entities registration for example
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                ItemList.nickel = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(location("nickel")),
                ItemList.nickel_sword = new SwordItem(ToolMaterialList.nickel_sword, 4, - 3, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(location("nickel_sword")),
                ItemList.nickel_ore = new BlockItem(BlockList.nickel_ore, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(BlockList.nickel_ore.getRegistryName())
            );
        }

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                BlockList.nickel_ore = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(50f, 3.0f).harvestLevel(3).sound(SoundType.METAL)).setRegistryName(location("nickel_ore"))
            );
        }

        private static ResourceLocation location(String name) {
            return new ResourceLocation(MODID, name);
        }
    }
}
