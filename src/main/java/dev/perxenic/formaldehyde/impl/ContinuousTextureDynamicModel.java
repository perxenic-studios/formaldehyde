package dev.perxenic.formaldehyde.impl;

import dev.perxenic.formaldehyde.Formaldehyde;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
    private static final Material MATERIAL =
            new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("block/dirt"));
    private static final ModelProperty<BlockPos> BLOCK_POS_PROPERTY = new ModelProperty<>();

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
        List<BakedQuad> quads = new ArrayList<>();

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(MATERIAL.sprite());
        vc.setShade(true);
        vc.setDirection(Direction.WEST);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(MATERIAL.sprite().getU0(), MATERIAL.sprite().getV0())
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(MATERIAL.sprite().getU0(), MATERIAL.sprite().getV1())
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(MATERIAL.sprite().getU1(), MATERIAL.sprite().getV1())
                .setNormal(-1f, 0f, 0f);

        vc.addVertex(0, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(MATERIAL.sprite().getU1(), MATERIAL.sprite().getV0())
                .setNormal(-1f, 0f, 0f);

        quads.add(vc.bakeQuad());

        return quads;
    }
}
