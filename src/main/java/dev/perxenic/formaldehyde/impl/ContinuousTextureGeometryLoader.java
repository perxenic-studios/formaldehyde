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
public class ContinuousTextureGeometryLoader implements IGeometryLoader<ContinuousTextureGeometry> {

    public static final ContinuousTextureGeometryLoader INSTANCE = new ContinuousTextureGeometryLoader();

    public static final ResourceLocation ID = fhLoc("continuous_texture");

    private ContinuousTextureGeometryLoader() {}

    @Override
    public ContinuousTextureGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        return new ContinuousTextureGeometry();
    }
}
