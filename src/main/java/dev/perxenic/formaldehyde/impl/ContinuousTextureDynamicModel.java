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

    private final TextureAtlasSprite northSprite;
    private final float northUStep;
    private final float northVStep;

    private final TextureAtlasSprite eastSprite;
    private final float eastUStep;
    private final float eastVStep;

    private final TextureAtlasSprite southSprite;
    private final float southUStep;
    private final float southVStep;

    private final TextureAtlasSprite westSprite;
    private final float westUStep;
    private final float westVStep;

    private final TextureAtlasSprite upSprite;
    private final float upUStep;
    private final float upVStep;

    private final TextureAtlasSprite downSprite;
    private final float downUStep;
    private final float downVStep;

    private final DirectionTileSize directionTileSize;

    private final ItemOverrides overrides;

    public ContinuousTextureDynamicModel(
            boolean useAmbientOcclusion,
            boolean isGui3d,
            boolean usesBlockLight,
            TextureAtlasSprite particle,
            TextureAtlasSprite northSprite,
            TextureAtlasSprite eastSprite,
            TextureAtlasSprite southSprite,
            TextureAtlasSprite westSprite,
            TextureAtlasSprite upSprite,
            TextureAtlasSprite downSprite,
            DirectionTextureSize directionTextureSize,
            DirectionTileSize directionTileSize,
            ItemOverrides overrides
    ) {
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particle = particle;

        this.northSprite = northSprite;
        this.northUStep = (northSprite.getU1() - northSprite.getU0()) / directionTextureSize.north;
        this.northVStep = (northSprite.getV1() - northSprite.getV0()) / directionTextureSize.north;

        this.eastSprite = eastSprite;
        this.eastUStep = (eastSprite.getU1() - eastSprite.getU0()) / directionTextureSize.east;
        this.eastVStep = (eastSprite.getV1() - eastSprite.getV0()) / directionTextureSize.east;

        this.southSprite = southSprite;
        this.southUStep = (southSprite.getU1() - southSprite.getU0()) / directionTextureSize.south;
        this.southVStep = (southSprite.getV1() - southSprite.getV0()) / directionTextureSize.south;

        this.westSprite = westSprite;
        this.westUStep = (westSprite.getU1() - westSprite.getU0()) / directionTextureSize.west;
        this.westVStep = (westSprite.getV1() - westSprite.getV0()) / directionTextureSize.west;

        this.upSprite = upSprite;
        this.upUStep = (upSprite.getU1() - upSprite.getU0()) / directionTextureSize.up;
        this.upVStep = (upSprite.getV1() - upSprite.getV0()) / directionTextureSize.up;

        this.downSprite = downSprite;
        this.downUStep = (downSprite.getU1() - downSprite.getU0()) / directionTextureSize.down;
        this.downVStep = (downSprite.getV1() - downSprite.getV0()) / directionTextureSize.down;

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
            float u0 = northSprite.getU0() + northUStep * Math.floorMod(-pos.getX() - 1, directionTileSize.northU);
            float v0 = northSprite.getV0() + northVStep * Math.floorMod(-pos.getY(), directionTileSize.northV);

            quads.add(QuadBaker.northQuad(
                    northSprite,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + northUStep,
                    v0, v0 + northVStep
            ));
        }

        if (side == Direction.EAST) {
            float u0 = eastSprite.getU0() + eastUStep * Math.floorMod(-pos.getZ(), directionTileSize.eastU);
            float v0 = eastSprite.getV0() + eastVStep * Math.floorMod(-pos.getY(), directionTileSize.eastV);

            quads.add(QuadBaker.eastQuad(
                    eastSprite,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + eastUStep,
                    v0, v0 + eastVStep
            ));
        }

        if (side == Direction.SOUTH) {
            float u0 = southSprite.getU0() + southUStep * Math.floorMod(pos.getX(), directionTileSize.southU);
            float v0 = southSprite.getV0() + southVStep * Math.floorMod(-pos.getY(), directionTileSize.southV);

            quads.add(QuadBaker.southQuad(
                    southSprite,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + southUStep,
                    v0, v0 + southVStep
            ));
        }

        if (side == Direction.WEST) {
            float u0 = westSprite.getU0() + westUStep * Math.floorMod(pos.getZ() - 1, directionTileSize.westU);
            float v0 = westSprite.getV0() + westVStep * Math.floorMod(-pos.getY(), directionTileSize.westV);

            quads.add(QuadBaker.westQuad(
                    westSprite,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + westUStep,
                    v0, v0 + westVStep
            ));
        }

        if (side == Direction.UP) {
            float u0 = upSprite.getU0() + upUStep * Math.floorMod(pos.getX(), directionTileSize.upU);
            float v0 = upSprite.getV0() + upVStep * Math.floorMod(pos.getZ() - 1, directionTileSize.upV);

            quads.add(QuadBaker.upQuad(
                    upSprite,
                    true,
                    0,
                    useAmbientOcclusion(),
                    0xFFFFFFFF,
                    u0, u0 + upUStep,
                    v0, v0 + upVStep
            ));
        }

        if (side == Direction.DOWN) {
            float u0 = downSprite.getU0() + downUStep * Math.floorMod(-pos.getX(), directionTileSize.downU);
            float v0 = downSprite.getV0() + downVStep * Math.floorMod(pos.getZ(), directionTileSize.downV);

            quads.add(QuadBaker.downQuad(
                    downSprite,
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
