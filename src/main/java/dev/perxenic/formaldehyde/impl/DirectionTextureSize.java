package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class DirectionTextureSize {
    public final int north;
    public final int east;
    public final int south;
    public final int west;
    public final int up;
    public final int down;

    public DirectionTextureSize(int north, int east, int south, int west, int up, int down) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public DirectionTextureSize(JsonObject object) throws JsonParseException {
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

        try {
            up = object.getAsJsonPrimitive("up").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'up' is not present or not a valid integer", e);
        }

        try {
            down = object.getAsJsonPrimitive("down").getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'down' is not present or not a valid integer", e);
        }
    }
}
