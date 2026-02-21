package dev.perxenic.formaldehyde.impl;

import dev.perxenic.formaldehyde.Formaldehyde;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContinuousTextureDynamicModel implements IDynamicBakedModel {
    private static final ModelProperty<BlockPos> BLOCK_POS_PROPERTY = new ModelProperty<>();

    private final int BLOCK_WIDTH_X = 3;
    private final int BLOCK_WIDTH_Y = 2;

    private final int TEX_BLOCK_SIZE = 4;

    private final float minU;
    private final float maxU;
    private final float uStep;
    private final float minV;
    private final float maxV;
    private final float vStep;

    private final boolean useAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean usesBlockLight;
    private final TextureAtlasSprite particle;
    private final ItemOverrides overrides;

    public ContinuousTextureDynamicModel(
            boolean useAmbientOcclusion,
            boolean isGui3d,
            boolean usesBlockLight,
            TextureAtlasSprite particle,
            ItemOverrides overrides
    ) {
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particle = particle;
        this.overrides = overrides;
        minU = particle.getU0();
        uStep = (particle.getU1() - minU) / TEX_BLOCK_SIZE;
        maxU = minU + uStep * BLOCK_WIDTH_X;

        minV = particle.getV0();
        vStep = (particle.getV1() - minV) / TEX_BLOCK_SIZE;
        maxV = minV + vStep * BLOCK_WIDTH_Y;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return useAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return usesBlockLight;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    // Add BlockPos to extra model data
    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        return modelData.derive().with(BLOCK_POS_PROPERTY, pos).build();
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState blockState,
            @Nullable Direction side,
            RandomSource randomSource,
            ModelData modelData,
            @Nullable RenderType renderType
    ) {
        // TODO: Temporary logging message, needs to be removed at some point
        if (!modelData.has(BLOCK_POS_PROPERTY)) Formaldehyde.LOGGER.error("Block pos property not present!");
        BlockPos pos = modelData.get(BLOCK_POS_PROPERTY);

        List<BakedQuad> quads = new ArrayList<>();

        quads.add(northQuad(Math.floorMod(pos.getX(), BLOCK_WIDTH_X), Math.floorMod(pos.getY(), BLOCK_WIDTH_Y)));
        quads.add(southQuad(Math.floorMod(pos.getX(), BLOCK_WIDTH_X), Math.floorMod(pos.getY(), BLOCK_WIDTH_Y)));

        quads.add(eastQuad(Math.floorMod(pos.getZ(), BLOCK_WIDTH_X), Math.floorMod(pos.getY(), BLOCK_WIDTH_Y)));
        quads.add(westQuad(Math.floorMod(pos.getZ(), BLOCK_WIDTH_X), Math.floorMod(pos.getY(), BLOCK_WIDTH_Y)));

        return quads;
    }

    public BakedQuad northQuad(int uOffset, int vOffset) {
        var u0 = minU + uStep * uOffset;
        var u1 = u0 + uStep;

        var v0 = minV + vStep * vOffset;
        var v1 = v0 + vStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(particle);
        vc.setShade(true);
        vc.setDirection(Direction.NORTH);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(1, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(1, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(0, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(0, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, 0f, 1f);

        return vc.bakeQuad();
    }

    public BakedQuad southQuad(int uOffset, int vOffset) {
        var u0 = maxU - uStep * (BLOCK_WIDTH_X - uOffset - 1);
        var u1 = u0 - uStep;

        var v0 = minV + vStep * vOffset;
        var v1 = v0 + vStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(particle);
        vc.setShade(true);
        vc.setDirection(Direction.SOUTH);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(0, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(1, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(1, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, 0f, -1f);

        return vc.bakeQuad();
    }

    public BakedQuad eastQuad(int uOffset, int vOffset) {

        var u0 = maxU - uStep * (BLOCK_WIDTH_X - uOffset - 1);
        var u1 = u0 - uStep;

        var v0 = minV + vStep * vOffset;
        var v1 = v0 + vStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(particle);
        vc.setShade(true);
        vc.setDirection(Direction.EAST);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(1, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(1f, 0f, 0f);

        vc.addVertex(1, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(1f, 0f, 0f);

        vc.addVertex(1, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(1f, 0f, 0f);

        vc.addVertex(1, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(1f, 0f, 0f);

        return vc.bakeQuad();
    }

    public BakedQuad westQuad(int uOffset, int vOffset) {

        var u0 = minU + uStep * uOffset;
        var u1 = u0 + uStep;

        var v0 = minV + vStep * vOffset;
        var v1 = v0 + vStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(particle);
        vc.setShade(true);
        vc.setDirection(Direction.WEST);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(-1f, 0f, 0f);

        return vc.bakeQuad();
    }
}
