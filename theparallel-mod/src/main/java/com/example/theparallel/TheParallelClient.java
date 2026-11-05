package com.example.theparallel;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class TheParallelClient implements ClientModInitializer {
    static final Identifier HALL = Identifier.of(TheParallel.MODID, "textures/gui/hallucination.png");
    static final Identifier DOOR = Identifier.of(TheParallel.MODID, "textures/gui/door.png");
    static int hallTicks, doorTicks, nextHall = 600;

    @Override public void onInitializeClient() {
        reg(ModEntities.CREATOR, "creator", 0.9157f);
        reg(ModEntities.RED, "red", 0.4912f);
        reg(ModEntities.BLACK, "black", 1.0546f);
        reg(ModEntities.WHITE, "tall_white", 0.4032f);
        reg(ModEntities.SPIDER, "spider", 1.1372f);
        HudRenderCallback.EVENT.register(TheParallelClient::hud);
        ClientTickEvents.END_CLIENT_TICK.register(TheParallelClient::tick);
        // La criatura de la puerta aparece al abrir puertas (50%).
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            var st = world.getBlockState(hit.getBlockPos());
            if (world.isClient && hand == Hand.MAIN_HAND && st.getBlock() instanceof DoorBlock && st.getBlock() != Blocks.IRON_DOOR
                && !st.get(DoorBlock.OPEN) && world.random.nextFloat() < 0.5f) {
                doorTicks = 14;
                play(0.6f, 0.9f);
            }
            return ActionResult.PASS;
        });
    }

    private static void reg(EntityType<ParallelCreature> t, String id, float aspect) {
        EntityRendererRegistry.register(t, ctx -> new SpriteRenderer(ctx, Identifier.of(TheParallel.MODID, "textures/entity/" + id + ".png"), aspect));
    }
    private static void play(float pitch, float vol) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_GHAST_SCREAM, pitch, vol));
    }

    // Alucinaciones: aparecen solo en tu pantalla, más seguido en la oscuridad.
    static void tick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null || mc.isPaused()) return;
        if (hallTicks > 0) hallTicks--;
        if (doorTicks > 0) doorTicks--;
        if (--nextHall <= 0) {
            int light = mc.world.getLightLevel(mc.player.getBlockPos());
            nextHall = (light < 7 ? 300 : 900) + mc.world.random.nextInt(1200);
            hallTicks = 16;
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_ENDERMAN_SCREAM, 0.5f, 0.5f));
        }
    }

    static void hud(DrawContext ctx, RenderTickCounter tc) {
        if (hallTicks > 0) draw(ctx, HALL, 492, 561, Math.min(1f, hallTicks / 6f) * 0.85f);
        if (doorTicks > 0) draw(ctx, DOOR, 565, 335, Math.min(1f, doorTicks / 6f));
    }
    static void draw(DrawContext ctx, Identifier t, int tw, int th, float a) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int sw = mc.getWindow().getScaledWidth(), sh = mc.getWindow().getScaledHeight(), w = sh * tw / th;
        int x = (sw - w) / 2 + (int) ((Math.random() - .5) * 6);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc(); RenderSystem.setShaderColor(1, 1, 1, a);
        ctx.drawTexture(t, x, 0, w, sh, 0f, 0f, tw, th, tw, th);
        RenderSystem.setShaderColor(1, 1, 1, 1); RenderSystem.disableBlend();
    }
}
