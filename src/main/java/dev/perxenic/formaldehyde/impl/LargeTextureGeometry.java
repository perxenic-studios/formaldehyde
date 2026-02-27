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
public class LargeTextureGeometry implements IUnbakedGeometry<LargeTextureGeometry> {
    private final FacewiseTextureSize facewiseTextureSize;
    private final FacewiseTileDimensions facewiseTileDimensions;

    public LargeTextureGeometry(FacewiseTextureSize facewiseTextureSize, FacewiseTileDimensions facewiseTileDimensions) {
        this.facewiseTextureSize = facewiseTextureSize;
        this.facewiseTileDimensions = facewiseTileDimensions;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        return new LargeTextureDynamicModel(
                context.useAmbientOcclusion(),
                context.isGui3d(),
                context.useBlockLight(),
                spriteGetter.apply(context.getMaterial("particle")),
                bakeFaceQuads(
                        facewiseTileDimensions.downU, facewiseTileDimensions.downV,
                        spriteGetter.apply(context.getMaterial("down")),
                        facewiseTextureSize.down,
                        context.useAmbientOcclusion(),
                        QuadBaker::downQuad
                ),
                bakeFaceQuads(
                        facewiseTileDimensions.upU, facewiseTileDimensions.upV,
                        spriteGetter.apply(context.getMaterial("up")),
                        facewiseTextureSize.up,
                        context.useAmbientOcclusion(),
                        QuadBaker::upQuad
                ),
                bakeFaceQuads(
                        facewiseTileDimensions.northU, facewiseTileDimensions.northV,
                        spriteGetter.apply(context.getMaterial("north")),
                        facewiseTextureSize.north,
                        context.useAmbientOcclusion(),
                        QuadBaker::northQuad
                ),
                bakeFaceQuads(
                        facewiseTileDimensions.eastU, facewiseTileDimensions.eastV,
                        spriteGetter.apply(context.getMaterial("east")),
                        facewiseTextureSize.east,
                        context.useAmbientOcclusion(),
                        QuadBaker::eastQuad
                ),
                bakeFaceQuads(
                        facewiseTileDimensions.southU, facewiseTileDimensions.southV,
                        spriteGetter.apply(context.getMaterial("south")),
                        facewiseTextureSize.south,
                        context.useAmbientOcclusion(),
                        QuadBaker::southQuad
                ),
                bakeFaceQuads(
                        facewiseTileDimensions.westU, facewiseTileDimensions.westV,
                        spriteGetter.apply(context.getMaterial("west")),
                        facewiseTextureSize.west,
                        context.useAmbientOcclusion(),
                        QuadBaker::westQuad
                ),
                facewiseTileDimensions,
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
