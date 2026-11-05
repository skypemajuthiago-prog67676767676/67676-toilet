package com.example.theparallel;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public class ModEntities {
    public static final EntityType<ParallelCreature> CREATOR = make("creator", 0.8f, 2.6f);
    public static final EntityType<ParallelCreature> RED = make("red", 0.7f, 3.0f);
    public static final EntityType<ParallelCreature> BLACK = make("black", 0.8f, 2.6f);
    public static final EntityType<ParallelCreature> WHITE = make("tall_white", 1.0f, 6.0f);
    public static final EntityType<ParallelCreature> SPIDER = make("spider", 1.4f, 1.5f);

    private static EntityType<ParallelCreature> make(String id, float w, float h) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(TheParallel.MODID, id),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ParallelCreature::new)
                .dimensions(EntityDimensions.fixed(w, h)).build());
    }

    private static Item egg(EntityType<ParallelCreature> t, String id) {
        return Registry.register(Registries.ITEM, Identifier.of(TheParallel.MODID, id + "_spawn_egg"),
            new SpawnEggItem(t, 0xEEEEEE, 0x111111, new Item.Settings()));
    }

    private static void setup(EntityType<ParallelCreature> t, String id, double speed, double hp, double dmg, double follow, int weight) {
        FabricDefaultAttributeRegistry.register(t, ParallelCreature.attrs(speed, hp, dmg, follow));
        SpawnRestriction.register(t, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER, t, weight, 1, 1);
        Item e = egg(t, id);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(g -> g.add(e));
    }

    public static void register() {
        setup(CREATOR, "creator", 0.30, 60, 8, 48, 3);     // antagonista principal
        setup(RED, "red", 0.25, 30, 5, 32, 6);
        setup(BLACK, "black", 0.27, 30, 5, 32, 6);
        setup(WHITE, "tall_white", 0.18, 80, 12, 48, 3);    // lento y enorme
        setup(SPIDER, "spider", 0.38, 20, 4, 32, 8);        // rápido
    }
}
