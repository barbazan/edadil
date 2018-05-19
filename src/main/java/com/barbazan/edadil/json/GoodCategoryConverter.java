package com.barbazan.edadil.json;

import com.barbazan.edadil.beans.GoodCategory;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GoodCategoryConverter implements JsonSerializer<GoodCategory>, JsonDeserializer<GoodCategory> {

    public JsonElement serialize(GoodCategory src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("name", src.getName());
        return object;
    }

    public GoodCategory deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        return new GoodCategory(id, name);
    }
}