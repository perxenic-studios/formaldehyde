package dev.perxenic.formaldehyde.impl;

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
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContinuousTextureDynamicModel implements IDynamicBakedModel {
    private static final ModelProperty<BlockPos> BLOCK_POS_PROPERTY = new ModelProperty<>();

    private final boolean useAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean usesBlockLight;

    private final TextureAtlasSprite particle;

    private final BakedQuad[][] downQuads;
    private final BakedQuad[][] upQuads;
    private final BakedQuad[][] northQuads;
    private final BakedQuad[][] eastQuads;
    private final BakedQuad[][] southQuads;
    private final BakedQuad[][] westQuads;

    private final FacewiseTileDimensions facewiseTileDimensions;

    private final ItemOverrides overrides;

    public ContinuousTextureDynamicModel(
            boolean useAmbientOcclusion,
            boolean isGui3d,
            boolean usesBlockLight,
            TextureAtlasSprite particle,
            BakedQuad[][] downQuads,
            BakedQuad[][] upQuads,
            BakedQuad[][] northQuads,
            BakedQuad[][] eastQuads,
            BakedQuad[][] southQuads,
            BakedQuad[][] westQuads,
            FacewiseTileDimensions facewiseTileDimensions,
            ItemOverrides overrides
    ) {
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particle = particle;
        this.downQuads = downQuads;
        this.upQuads = upQuads;
        this.northQuads = northQuads;
        this.eastQuads = eastQuads;
        this.southQuads = southQuads;
        this.westQuads = westQuads;

        this.facewiseTileDimensions = facewiseTileDimensions;

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
        if (pos == null) pos = new BlockPos(0, 0, 0);

        if (side == Direction.DOWN) return List.of(
                downQuads
                    [Math.floorMod(pos.getX(), facewiseTileDimensions.downU)]
                    [Math.floorMod(-pos.getZ(), facewiseTileDimensions.downV)]
            );

        else if (side == Direction.UP) return List.of(
                upQuads
                    [Math.floorMod(pos.getX(), facewiseTileDimensions.upU)]
                    [Math.floorMod(pos.getZ() - 1, facewiseTileDimensions.upV)]
        );

        else if (side == Direction.NORTH) return List.of(
                northQuads
                    [Math.floorMod(-pos.getX() - 1, facewiseTileDimensions.northU)]
                    [Math.floorMod(-pos.getY(), facewiseTileDimensions.northV)]
        );

        else if (side == Direction.EAST) return List.of(
                eastQuads
                    [Math.floorMod(-pos.getZ(), facewiseTileDimensions.eastU)]
                    [Math.floorMod(-pos.getY(), facewiseTileDimensions.eastV)]
        );

        else if (side == Direction.SOUTH) return List.of(
                southQuads
                    [Math.floorMod(pos.getX(), facewiseTileDimensions.southU)]
                    [Math.floorMod(-pos.getY(), facewiseTileDimensions.southV)]
        );

        else if (side == Direction.WEST) return List.of(
                westQuads
                    [Math.floorMod(pos.getZ() - 1, facewiseTileDimensions.westU)]
                    [Math.floorMod(-pos.getY(), facewiseTileDimensions.westV)]
        );

        return List.of();
    }
}
