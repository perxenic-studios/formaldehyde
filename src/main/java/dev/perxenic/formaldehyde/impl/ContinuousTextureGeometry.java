package dev.perxenic.formaldehyde.impl;

import net.minecraft.MethodsReturnNonnullByDefault;
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
                spriteGetter.apply(context.getMaterial("down")),
                spriteGetter.apply(context.getMaterial("up")),
                spriteGetter.apply(context.getMaterial("north")),
                spriteGetter.apply(context.getMaterial("east")),
                spriteGetter.apply(context.getMaterial("south")),
                spriteGetter.apply(context.getMaterial("west")),
                directionTextureSize,
                directionTileSize,
                overrides
        );
    }
}
