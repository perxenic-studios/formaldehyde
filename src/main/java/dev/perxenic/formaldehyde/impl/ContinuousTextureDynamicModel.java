package dev.perxenic.formaldehyde.impl;

import dev.perxenic.formaldehyde.helper.QuadBaker;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContinuousTextureDynamicModel implements IDynamicBakedModel {
    private static final ModelProperty<BlockPos> BLOCK_POS_PROPERTY = new ModelProperty<>();

    private final boolean useAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean usesBlockLight;

    private final TextureAtlasSprite particle;

    private final TextureAtlasSprite north;
    private final float northUStep;
    private final float northVStep;

    private final TextureAtlasSprite east;
    private final float eastUStep;
    private final float eastVStep;

    private final TextureAtlasSprite south;
    private final float southUStep;
    private final float southVStep;

    private final TextureAtlasSprite west;
    private final float westUStep;
    private final float westVStep;

    private final TextureAtlasSprite up;
    private final float upUStep;
    private final float upVStep;

    private final TextureAtlasSprite down;
    private final float downUStep;
    private final float downVStep;

    private final DirectionTileSize directionTileSize;

    private final ItemOverrides overrides;

    public ContinuousTextureDynamicModel(
            boolean useAmbientOcclusion,
            boolean isGui3d,
            boolean usesBlockLight,
            TextureAtlasSprite particle,
            TextureAtlasSprite north,
            TextureAtlasSprite east,
            TextureAtlasSprite south,
            TextureAtlasSprite west,
            TextureAtlasSprite up,
            TextureAtlasSprite down,
            DirectionTextureSize directionTextureSize,
            DirectionTileSize directionTileSize,
            ItemOverrides overrides
    ) {
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particle = particle;

        this.north = north;
        this.northUStep = (north.getU1() - north.getU0()) / directionTextureSize.north;
        this.northVStep = (north.getV1() - north.getV0()) / directionTextureSize.north;

        this.east = east;
        this.eastUStep = (east.getU1() - east.getU0()) / directionTextureSize.east;
        this.eastVStep = (east.getV1() - east.getV0()) / directionTextureSize.east;

        this.south = south;
        this.southUStep = (south.getU1() - south.getU0()) / directionTextureSize.south;
        this.southVStep = (south.getV1() - south.getV0()) / directionTextureSize.south;

        this.west = west;
        this.westUStep = (west.getU1() - west.getU0()) / directionTextureSize.west;
        this.westVStep = (west.getV1() - west.getV0()) / directionTextureSize.west;

        this.up = up;
        this.upUStep = (up.getU1() - up.getU0()) / directionTextureSize.up;
        this.upVStep = (up.getV1() - up.getV0()) / directionTextureSize.up;

        this.down = down;
        this.downUStep = (down.getU1() - down.getU0()) / directionTextureSize.down;
        this.downVStep = (down.getV1() - down.getV0()) / directionTextureSize.down;

        this.directionTileSize = directionTileSize;

        this.overrides = overrides;
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
        BlockPos pos = modelData.get(BLOCK_POS_PROPERTY);

        List<BakedQuad> quads = new ArrayList<>();

        if (side == null) return quads;

        if (side == Direction.NORTH) {
            float u0 = north.getU0() + northUStep * Math.floorMod(-pos.getX() - 1, directionTileSize.northU);
            float v0 = north.getV0() + northVStep * Math.floorMod(-pos.getY(), directionTileSize.northV);

            quads.add(QuadBaker.northQuad(
                    north,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + northUStep,
                    v0, v0 + northVStep
            ));
        }

        if (side == Direction.EAST) {
            float u0 = east.getU0() + eastUStep * Math.floorMod(-pos.getZ(), directionTileSize.eastU);
            float v0 = east.getV0() + eastVStep * Math.floorMod(-pos.getY(), directionTileSize.eastV);

            quads.add(QuadBaker.eastQuad(
                    east,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + eastUStep,
                    v0, v0 + eastVStep
            ));
        }

        if (side == Direction.SOUTH) {
            float u0 = south.getU0() + southUStep * Math.floorMod(pos.getX(), directionTileSize.southU);
            float v0 = south.getV0() + southVStep * Math.floorMod(-pos.getY(), directionTileSize.southV);

            quads.add(QuadBaker.southQuad(
                    south,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + southUStep,
                    v0, v0 + southVStep
            ));
        }

        if (side == Direction.WEST) {
            float u0 = west.getU0() + westUStep * Math.floorMod(pos.getZ() - 1, directionTileSize.westU);
            float v0 = west.getV0() + westVStep * Math.floorMod(-pos.getY(), directionTileSize.westV);

            quads.add(QuadBaker.westQuad(
                    west,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + westUStep,
                    v0, v0 + westVStep
            ));
        }

        if (side == Direction.UP) {
            float u0 = up.getU0() + upUStep * Math.floorMod(pos.getX(), directionTileSize.upU);
            float v0 = up.getV0() + upVStep * Math.floorMod(pos.getZ() - 1, directionTileSize.upV);

            quads.add(QuadBaker.upQuad(
                    up,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + upUStep,
                    v0, v0 + upVStep
            ));
        }

        if (side == Direction.DOWN) {
            float u0 = down.getU0() + downUStep * Math.floorMod(-pos.getX(), directionTileSize.downU);
            float v0 = down.getV0() + downVStep * Math.floorMod(pos.getZ(), directionTileSize.downV);

            quads.add(QuadBaker.downQuad(
                    down,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + downUStep,
                    v0, v0 + downVStep
            ));
        }

        return quads;
    }
}
