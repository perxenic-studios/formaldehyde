package dev.perxenic.formaldehyde.helper;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

public class QuadBaker {
    public static BakedQuad quad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1,
            Direction direction,
            float[] vertices,
            float[] normal
    ) {
        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(sprite);
        vc.setShade(shade);
        vc.setDirection(direction);
        vc.setTintIndex(tintIndex);
        vc.setHasAmbientOcclusion(ambientOcclusion);

        vc.addVertex(vertices[0], vertices[1], vertices[2])
                .setColor(color)
                .setUv(u0, v0)
                .setNormal(normal[0], normal[1], normal[2]);

        vc.addVertex(vertices[3], vertices[4], vertices[5])
                .setColor(color)
                .setUv(u0, v1)
                .setNormal(normal[0], normal[1], normal[2]);

        vc.addVertex(vertices[6], vertices[7], vertices[8])
                .setColor(color)
                .setUv(u1, v1)
                .setNormal(normal[0], normal[1], normal[2]);

        vc.addVertex(vertices[9], vertices[10], vertices[11])
                .setColor(color)
                .setUv(u1, v0)
                .setNormal(normal[0], normal[1], normal[2]);

        return vc.bakeQuad();
    }

    public final static float[] DOWN_VERTICES = {
            1, 0, 0,
            1, 0, 1,
            0, 0, 1,
            0, 0, 0,
    };

    public final static float[] DOWN_NORMAL = {0, -1, 0};

    public static BakedQuad downQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.DOWN,
                DOWN_VERTICES,
                DOWN_NORMAL
        );
    }

    public final static float[] UP_VERTICES = {
            0, 1, 0,
            0, 1, 1,
            1, 1, 1,
            1, 1, 0,
    };

    public final static float[] UP_NORMAL = {0, 1, 0};

    public static BakedQuad upQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.UP,
                UP_VERTICES,
                UP_NORMAL
        );
    }

    public final static float[] NORTH_VERTICES = {
            1, 1, 0,
            1, 0, 0,
            0, 0, 0,
            0, 1, 0
    };

    public final static float[] NORTH_NORMAL = {0, 0, -1};

    public static BakedQuad northQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.NORTH,
                NORTH_VERTICES,
                NORTH_NORMAL
        );
    }

    public final static float[] SOUTH_VERTICES = {
            0, 1, 1,
            0, 0, 1,
            1, 0, 1,
            1, 1, 1
    };

    public final static float[] EAST_VERTICES = {
            1, 1, 1,
            1, 0, 1,
            1, 0, 0,
            1, 1, 0,
    };

    public final static float[] EAST_NORMAL = {1, 0, 0};

    public static BakedQuad eastQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.EAST,
                EAST_VERTICES,
                EAST_NORMAL
        );
    }

    public final static float[] SOUTH_NORMAL = {0, 0, 1};

    public static BakedQuad southQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.SOUTH,
                SOUTH_VERTICES,
                SOUTH_NORMAL
        );
    }

    public final static float[] WEST_VERTICES = {
            0, 1, 0,
            0, 0, 0,
            0, 0, 1,
            0, 1, 1
    };

    public final static float[] WEST_NORMAL = {-1, 0, 0};

    public static BakedQuad westQuad(
            TextureAtlasSprite sprite,
            boolean shade,
            int tintIndex,
            boolean ambientOcclusion,
            int color,
            float u0, float u1,
            float v0, float v1
    ) {
        return quad(
                sprite, shade, tintIndex, ambientOcclusion, color,
                u0, u1, v0, v1,
                Direction.WEST,
                WEST_VERTICES,
                WEST_NORMAL
        );
    }
}
