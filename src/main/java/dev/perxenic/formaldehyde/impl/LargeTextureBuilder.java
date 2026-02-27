package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeTextureBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
    private FacewiseTextureSize _texSizes;
    private FacewiseTileDimensions _tileDims;

    public LargeTextureBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(
                LargeTextureGeometryLoader.ID,
                parent,
                existingFileHelper,
                false
        );
    }

    public LargeTextureBuilder setTextureSizes(FacewiseTextureSize texSizes) {
        _texSizes = texSizes;
        return this;
    }

    public LargeTextureBuilder setTextureSizes(int down, int up, int north, int east, int south, int west) {
        _texSizes = new FacewiseTextureSize(down, up, north, east, south, west);
        return this;
    }

    public LargeTextureBuilder setTileDimensions(FacewiseTileDimensions tileDims) {
        _tileDims = tileDims;
        return this;
    }

    public LargeTextureBuilder setTileDimensions(
            int downU, int downV,
            int upU, int upV,
            int northU, int northV,
            int eastU, int eastV,
            int southU, int southV,
            int westU, int westV
    ) {
        _tileDims = new FacewiseTileDimensions(
                downU, downV,
                upU, upV,
                northU, northV,
                eastU, eastV,
                southU, southV,
                westU, westV
        );
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        if (json.has("elements"))
            throw new IllegalStateException("Large texture model currently does not support custom model elements!");

        if (_texSizes == null)
            throw new IllegalStateException("Texture sizes were never assigned!");
        if (_tileDims == null)
            throw new IllegalStateException("Tile dimensions were never assigned!");

        json.add("texture_size", _texSizes.toJson());
        json.add("tile_size", _tileDims.toJson());

        return json;
    }
}
