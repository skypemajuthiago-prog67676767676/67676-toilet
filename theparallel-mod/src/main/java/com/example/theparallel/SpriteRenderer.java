package com.example.theparallel;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

/** Dibuja la imagen como un cartel vertical que siempre mira al jugador. */
public class SpriteRenderer extends EntityRenderer<ParallelCreature> {
    private final Identifier tex; private final float aspect;
    public SpriteRenderer(EntityRendererFactory.Context ctx, Identifier tex, float aspect) { super(ctx); this.tex = tex; this.aspect = aspect; }
    @Override public Identifier getTexture(ParallelCreature e) { return tex; }

    @Override public void render(ParallelCreature e, float yaw, float tickDelta, MatrixStack m, VertexConsumerProvider vp, int light) {
        m.push();
        m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - this.dispatcher.camera.getYaw()));
        float h = e.getHeight(), w = h * aspect;
        VertexConsumer vc = vp.getBuffer(RenderLayer.getEntityCutoutNoCull(tex));
        MatrixStack.Entry en = m.peek();
        v(vc, en, -w / 2, 0, 0, 1, light); v(vc, en, w / 2, 0, 1, 1, light);
        v(vc, en, w / 2, h, 1, 0, light);  v(vc, en, -w / 2, h, 0, 0, light);
        m.pop();
        super.render(e, yaw, tickDelta, m, vp, light);
    }
    private static void v(VertexConsumer vc, MatrixStack.Entry e, float x, float y, float u, float t, int light) {
        vc.vertex(e, x, y, 0f).color(255, 255, 255, 255).texture(u, t).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(e, 0f, 0f, 1f);
    }
}
