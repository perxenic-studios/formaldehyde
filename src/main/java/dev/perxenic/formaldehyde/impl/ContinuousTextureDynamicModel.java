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

    private final boolean useAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean usesBlockLight;

    private final TextureAtlasSprite particle;

    private final TextureAtlasSprite north;
    private final float northUStep;
    private final float northVStep;

    private final TextureAtlasSprite south;
    private final float southUStep;
    private final float southVStep;

    private final TextureAtlasSprite west;
    private final float westUStep;
    private final float westVStep;

    private final TextureAtlasSprite east;
    private final float eastUStep;
    private final float eastVStep;

    private final TextureAtlasSprite down;
    private final float downUStep;
    private final float downVStep;

    private final TextureAtlasSprite up;
    private final float upUStep;
    private final float upVStep;

    private final DirectionTileSize directionTileSize;

    private final ItemOverrides overrides;

    public ContinuousTextureDynamicModel(
            boolean useAmbientOcclusion,
            boolean isGui3d,
            boolean usesBlockLight,
            TextureAtlasSprite particle,
            TextureAtlasSprite north,
            TextureAtlasSprite south,
            TextureAtlasSprite west,
            TextureAtlasSprite east,
            TextureAtlasSprite down,
            TextureAtlasSprite up,
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

        this.south = south;
        this.southUStep = (south.getU1() - south.getU0()) / directionTextureSize.south;
        this.southVStep = (south.getV1() - south.getV0()) / directionTextureSize.south;

        this.west = west;
        this.westUStep = (west.getU1() - west.getU0()) / directionTextureSize.west;
        this.westVStep = (west.getV1() - west.getV0()) / directionTextureSize.west;

        this.east = east;
        this.eastUStep = (east.getU1() - east.getU0()) / directionTextureSize.east;
        this.eastVStep = (east.getV1() - east.getV0()) / directionTextureSize.east;

        this.down = down;
        this.downUStep = (down.getU1() - down.getU0()) / directionTextureSize.down;
        this.downVStep = (down.getV1() - down.getV0()) / directionTextureSize.down;

        this.up = up;
        this.upUStep = (up.getU1() - up.getU0()) / directionTextureSize.up;
        this.upVStep = (up.getV1() - up.getV0()) / directionTextureSize.up;

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

        if (side == Direction.NORTH)
            quads.add(northQuad(
                    Math.floorMod(-pos.getX(), directionTileSize.northU),
                    Math.floorMod(pos.getY(), directionTileSize.northV)
            ));
        if (side == Direction.SOUTH)
            quads.add(southQuad(
                    Math.floorMod(-pos.getX(), directionTileSize.southU),
                    Math.floorMod(pos.getY(), directionTileSize.southV)
            ));

        if (side == Direction.WEST)
            quads.add(westQuad(
                    Math.floorMod(pos.getZ(), directionTileSize.westU),
                    Math.floorMod(pos.getY(), directionTileSize.westV)
            ));
        if (side == Direction.EAST)
            quads.add(eastQuad(
                    Math.floorMod(pos.getZ(), directionTileSize.eastU),
                    Math.floorMod(pos.getY(), directionTileSize.eastV)
            ));

        if (side == Direction.DOWN)
            quads.add(downQuad(
                    Math.floorMod(pos.getZ(), directionTileSize.downU),
                    Math.floorMod(pos.getX(), directionTileSize.downV)
            ));
        if (side == Direction.UP)
            quads.add(upQuad(
                    Math.floorMod(pos.getZ(), directionTileSize.upU),
                    Math.floorMod(pos.getX(), directionTileSize.upV)
            ));

        return quads;
    }

    public BakedQuad northQuad(int uOffset, int vOffset) {
        var u0 = north.getU0() + northUStep * uOffset;
        var u1 = u0 + northUStep;

        var v0 = north.getV0() + northVStep * vOffset;
        var v1 = v0 + northVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(north);
        vc.setShade(true);
        vc.setDirection(Direction.NORTH);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(1, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(1, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(0, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, 0f, -1f);

        vc.addVertex(0, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, 0f, -1f);

        return vc.bakeQuad();
    }

    public BakedQuad southQuad(int uOffset, int vOffset) {
        // Flip texture horizontally for south quad
        var u0 = south.getU0() + southUStep * (directionTileSize.southU - uOffset - 1);
        var u1 = u0 + southUStep;

        var v0 = south.getV0() + southVStep * vOffset;
        var v1 = v0 + southVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(south);
        vc.setShade(true);
        vc.setDirection(Direction.SOUTH);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(0, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(1, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(1, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, 0f, 1f);

        return vc.bakeQuad();
    }

    public BakedQuad westQuad(int uOffset, int vOffset) {
        var u0 = west.getU0() + westUStep * uOffset;
        var u1 = u0 + westUStep;

        var v0 = west.getV0() + westVStep * vOffset;
        var v1 = v0 + westVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(west);
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

    public BakedQuad eastQuad(int uOffset, int vOffset) {
        // Flip texture horizontally for east quad
        var u0 = east.getU0() + eastUStep * (directionTileSize.eastU - uOffset - 1);
        var u1 = u0 + eastUStep;


        var v0 = east.getV0() + eastVStep * vOffset;
        var v1 = v0 + eastVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(east);
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

    public BakedQuad downQuad(int uOffset, int vOffset) {
        // Flip texture horizontally for down quad
        var u0 = down.getU0() + downUStep * (directionTileSize.downU - uOffset - 1);
        var u1 = u0 + downUStep;

        var v0 = down.getV0() + downVStep * vOffset;
        var v1 = v0 + downVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(down);
        vc.setShade(true);
        vc.setDirection(Direction.DOWN);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, -1f, 0f);

        vc.addVertex(0, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, -1f, 0f);

        vc.addVertex(1, 0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, -1f, 0f);

        vc.addVertex(1, 0, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, -1f, 0f);

        return vc.bakeQuad();
    }

    public BakedQuad upQuad(int uOffset, int vOffset) {
        var u0 = up.getU0() + upUStep * uOffset;
        var u1 = u0 + upUStep;

        var v0 = up.getV0() + upVStep * vOffset;
        var v1 = v0 + upVStep;

        var vc = new QuadBakingVertexConsumer();

        vc.setSprite(up);
        vc.setShade(true);
        vc.setDirection(Direction.UP);
        vc.setTintIndex(0);
        vc.setHasAmbientOcclusion(useAmbientOcclusion());

        vc.addVertex(0, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(0, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v1)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(1, 1, 1)
                .setColor(0xFFFFFFFF)
                .setUv(u1, v0)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(1, 1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(u0, v0)
                .setNormal(0f, 0f, 1f);

        return vc.bakeQuad();
    }
}
