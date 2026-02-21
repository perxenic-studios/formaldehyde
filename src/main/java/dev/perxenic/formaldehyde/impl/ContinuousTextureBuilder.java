package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContinuousTextureBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
    public ContinuousTextureBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(
                ContinuousTextureGeometryLoader.ID,
                parent,
                existingFileHelper,
                false
        );
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        return super.toJson(json);
    }
}
