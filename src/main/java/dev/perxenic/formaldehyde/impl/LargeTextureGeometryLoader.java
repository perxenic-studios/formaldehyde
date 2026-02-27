package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import javax.annotation.ParametersAreNonnullByDefault;

import static dev.perxenic.formaldehyde.Formaldehyde.fhLoc;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeTextureGeometryLoader implements IGeometryLoader<LargeTextureGeometry> {

    public static final LargeTextureGeometryLoader INSTANCE = new LargeTextureGeometryLoader();

    public static final ResourceLocation ID = fhLoc("large_texture");

    private LargeTextureGeometryLoader() {}

    @Override
    public LargeTextureGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        if (!jsonObject.has("texture_size"))
            throw new JsonParseException("Property 'texture_size' was not present");
        if (!jsonObject.has("tile_size"))
            throw new JsonParseException("Property 'tile_size' was not present");
        if (!jsonObject.get("texture_size").isJsonObject())
            throw new JsonParseException("Property 'texture_size' was not a json object");
        if (!jsonObject.get("tile_size").isJsonObject())
            throw new JsonParseException("Property 'tile_size' was not a json object");

        return new LargeTextureGeometry(
                new FacewiseTextureSize(jsonObject.get("texture_size").getAsJsonObject()),
                new FacewiseTileDimensions(jsonObject.get("tile_size").getAsJsonObject())
        );
    }
}
