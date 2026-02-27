package dev.perxenic.formaldehyde.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.perxenic.formaldehyde.helper.JsonHelper;

public class FacewiseTileDimensions {
    public final int downU;
    public final int downV;

    public final int upU;
    public final int upV;

    public final int northU;
    public final int northV;

    public final int eastU;
    public final int eastV;

    public final int southU;
    public final int southV;

    public final int westU;
    public final int westV;

    public FacewiseTileDimensions(
            int downU, int downV,
            int upU, int upV,
            int northU, int northV,
            int eastU, int eastV,
            int southU, int southV,
            int westU, int westV
    ) {
        this.downU = downU;
        this.downV = downV;

        this.upU = upU;
        this.upV = upV;

        this.northU = northU;
        this.northV = northV;

        this.eastU = eastU;
        this.eastV = eastV;

        this.southU = southU;
        this.southV = southV;

        this.westU = westU;
        this.westV = westV;
    }

    public FacewiseTileDimensions(JsonObject object) throws JsonParseException {
        try {
            var down = object.getAsJsonArray("down");
            downU = down.get(0).getAsInt();
            downV = down.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'down' is not present or not a valid integer array", e);
        }

        try {
            var up = object.getAsJsonArray("up");
            upU = up.get(0).getAsInt();
            upV = up.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'up' is not present or not a valid integer array", e);
        }

        try {
            var north = object.getAsJsonArray("north");
            northU = north.get(0).getAsInt();
            northV = north.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'north' is not present or not a valid integer array", e);
        }

        try {
            var east = object.getAsJsonArray("east");
            eastU = east.get(0).getAsInt();
            eastV = east.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'east' is not present or not a valid integer array", e);
        }

        try {
            var south = object.getAsJsonArray("south");
            southU = south.get(0).getAsInt();
            southV = south.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'south' is not present or not a valid integer array", e);
        }

        try {
            var west = object.getAsJsonArray("west");
            westU = west.get(0).getAsInt();
            westV = west.get(1).getAsInt();
        } catch (Exception e) {
            throw new JsonParseException("Property 'west' is not present or not a valid integer array", e);
        }
    }

    public JsonObject toJson() {
        var json = new JsonObject();

        json.add("down", JsonHelper.intArray(downU, downV));
        json.add("up", JsonHelper.intArray(upU, upV));
        json.add("north", JsonHelper.intArray(northU, northV));
        json.add("east", JsonHelper.intArray(eastU, eastV));
        json.add("south", JsonHelper.intArray(southU, southV));
        json.add("west", JsonHelper.intArray(westU, westV));

        return json;
    }
}
