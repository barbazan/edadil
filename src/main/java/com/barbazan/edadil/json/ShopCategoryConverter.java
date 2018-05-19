package com.barbazan.edadil.json;

import com.barbazan.edadil.beans.ShopCategory;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ShopCategoryConverter implements JsonSerializer<ShopCategory>, JsonDeserializer<ShopCategory> {

    public JsonElement serialize(ShopCategory src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("name", src.getName());
        return object;
    }

    public ShopCategory deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        return new ShopCategory(id, name);
    }
}