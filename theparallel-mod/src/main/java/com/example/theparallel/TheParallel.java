package com.example.theparallel;
import net.fabricmc.api.ModInitializer;
public class TheParallel implements ModInitializer {
    public static final String MODID = "theparallel";
    @Override public void onInitialize() { ModEntities.register(); }
}
