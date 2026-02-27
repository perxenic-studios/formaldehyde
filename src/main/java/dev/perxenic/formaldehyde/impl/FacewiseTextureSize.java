package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class FacewiseTextureSize {
    public final int down;
    public final int up;
    public final int north;
    public final int east;
    public final int south;
    public final int west;

    public FacewiseTextureSize(int down, int up, int north, int east, int south, int west) {
        this.down = down;
        this.up = up;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public FacewiseTextureSize(JsonObject object) throws JsonParseException {
        try {
            down = object.getAsJsonPrimitive("down").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'down' is not present or not a valid integer", e);
        }

        try {
            up = object.getAsJsonPrimitive("up").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'up' is not present or not a valid integer", e);
        }

        try {
            north = object.getAsJsonPrimitive("north").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'north' is not present or not a valid integer", e);
        }

        try {
            east = object.getAsJsonPrimitive("east").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'east' is not present or not a valid integer", e);
        }

        try {
            south = object.getAsJsonPrimitive("south").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'south' is not present or not a valid integer", e);
        }

        try {
            west = object.getAsJsonPrimitive("west").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'west' is not present or not a valid integer", e);
        }
    }

    public JsonObject toJson() {
        var json = new JsonObject();

        json.addProperty("down", down);
        json.addProperty("up", up);
        json.addProperty("north", north);
        json.addProperty("east", east);
        json.addProperty("south", south);
        json.addProperty("west", west);

        return json;
    }
}
