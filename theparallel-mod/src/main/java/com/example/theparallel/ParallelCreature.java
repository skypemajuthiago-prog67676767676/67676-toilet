package com.example.theparallel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ParallelCreature extends HostileEntity {
    public ParallelCreature(EntityType<? extends ParallelCreature> type, World world) { super(type, world); }

    public static DefaultAttributeContainer.Builder attrs(double speed, double hp, double dmg, double follow) {
        return HostileEntity.createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, speed)
            .add(EntityAttributes.GENERIC_MAX_HEALTH, hp)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, dmg)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, follow);
    }

    @Override protected void initGoals() {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new MeleeAttackGoal(this, 1.2, false));
        goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16f));
        goalSelector.add(4, new LookAroundGoal(this));
        targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // El Creador oscurece la visión de quien tenga cerca.
    @Override public void tick() {
        super.tick();
        if (!getWorld().isClient && getType() == ModEntities.CREATOR && age % 40 == 0) {
            for (PlayerEntity p : getWorld().getEntitiesByClass(PlayerEntity.class, getBoundingBox().expand(14), x -> true))
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0, false, false));
        }
    }

    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_ENDERMAN_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource s) { return SoundEvents.ENTITY_ENDERMAN_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_ENDERMAN_DEATH; }
}
