package dev.perxenic.formaldehyde.helper;

import com.google.gson.JsonArray;

public class JsonHelper {
    public static JsonArray intArray(int... values) {
        var array = new JsonArray();

        for (int value : values)
            array.add(value);

        return array;
    }
}
