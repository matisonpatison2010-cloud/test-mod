package com.example.bossmusic;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BossMusicClient implements ClientModInitializer {

    private static boolean playingWither = false;
    private static boolean playingDragon = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            boolean witherNearby = !client.world.getEntitiesByClass(
                    WitherEntity.class,
                    client.player.getBoundingBox().expand(64),
                    entity -> entity.isAlive()
            ).isEmpty();

            boolean dragonNearby = !client.world.getEntitiesByClass(
                    EnderDragonEntity.class,
                    client.player.getBoundingBox().expand(128),
                    entity -> entity.isAlive()
            ).isEmpty();

            if (witherNearby && !playingWither) {
                playSound("wither_theme");
                playingWither = true;
            }

            if (!witherNearby) {
                playingWither = false;
            }

            if (dragonNearby && !playingDragon) {
                playSound("dragon_theme");
                playingDragon = true;
            }

            if (!dragonNearby) {
                playingDragon = false;
            }

        });
    }

    private void playSound(String name) {
        MinecraftClient client = MinecraftClient.getInstance();

        SoundEvent event = Registries.SOUND_EVENT.get(
                new Identifier("bossmusic", name)
        );

        client.getSoundManager().play(
                PositionedSoundInstance.master(event, 1.0f)
        );
    }
}
