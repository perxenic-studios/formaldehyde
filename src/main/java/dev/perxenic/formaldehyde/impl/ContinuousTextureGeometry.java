package dev.perxenic.formaldehyde.impl;

import com.mojang.datafixers.util.Function9;
import dev.perxenic.formaldehyde.helper.QuadBaker;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContinuousTextureGeometry implements IUnbakedGeometry<ContinuousTextureGeometry> {
    private final DirectionTextureSize directionTextureSize;
    private final DirectionTileSize directionTileSize;

    public ContinuousTextureGeometry(DirectionTextureSize directionTextureSize, DirectionTileSize directionTileSize) {
        this.directionTextureSize = directionTextureSize;
        this.directionTileSize = directionTileSize;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        return new ContinuousTextureDynamicModel(
                context.useAmbientOcclusion(),
                context.isGui3d(),
                context.useBlockLight(),
                spriteGetter.apply(context.getMaterial("particle")),
                bakeFaceQuads(
                        directionTileSize.downU, directionTileSize.downV,
                        spriteGetter.apply(context.getMaterial("down")),
                        directionTextureSize.down,
                        context.useAmbientOcclusion(),
                        QuadBaker::downQuad
                ),
                bakeFaceQuads(
                        directionTileSize.upU, directionTileSize.upV,
                        spriteGetter.apply(context.getMaterial("up")),
                        directionTextureSize.up,
                        context.useAmbientOcclusion(),
                        QuadBaker::upQuad
                ),
                bakeFaceQuads(
                        directionTileSize.northU, directionTileSize.northV,
                        spriteGetter.apply(context.getMaterial("north")),
                        directionTextureSize.north,
                        context.useAmbientOcclusion(),
                        QuadBaker::northQuad
                ),
                bakeFaceQuads(
                        directionTileSize.eastU, directionTileSize.eastV,
                        spriteGetter.apply(context.getMaterial("east")),
                        directionTextureSize.east,
                        context.useAmbientOcclusion(),
                        QuadBaker::eastQuad
                ),
                bakeFaceQuads(
                        directionTileSize.southU, directionTileSize.southV,
                        spriteGetter.apply(context.getMaterial("south")),
                        directionTextureSize.south,
                        context.useAmbientOcclusion(),
                        QuadBaker::southQuad
                ),
                bakeFaceQuads(
                        directionTileSize.westU, directionTileSize.westV,
                        spriteGetter.apply(context.getMaterial("west")),
                        directionTextureSize.west,
                        context.useAmbientOcclusion(),
                        QuadBaker::westQuad
                ),
                directionTileSize,
                overrides
        );
    }

    public static BakedQuad[][] bakeFaceQuads(
            int uTiles, int vTiles,
            TextureAtlasSprite sprite,
            int tSize,
            boolean ao,
            Function9<TextureAtlasSprite, Boolean, Integer, Boolean, Integer, Float, Float, Float, Float, BakedQuad> quadFunc
    ) {
        BakedQuad[][] quads = new BakedQuad[uTiles][vTiles];

        var uStep = (sprite.getU1() - sprite.getU0()) / tSize;
        var vStep = (sprite.getV1() - sprite.getV0()) / tSize;

        for (var u = 0; u < uTiles; u++) {
            float u0 = sprite.getU0() + uStep * u;

            for (var v = 0; v < vTiles; v++) {
                float v0 = sprite.getV0() + vStep * v;

                quads[u][v] = quadFunc.apply(
                        sprite,
                        true,
                        0,
                        ao,
                        0xFFFFFFFF,
                        u0, u0 + uStep,
                        v0, v0 + vStep
                );
            }
        }

        return quads;
    }
}
