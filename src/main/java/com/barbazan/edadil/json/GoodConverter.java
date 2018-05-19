package com.barbazan.edadil.json;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodCategory;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GoodConverter implements JsonSerializer<Good>, JsonDeserializer<Good> {

    public JsonElement serialize(Good src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("name", src.getName());
        object.add("goodCategory", context.serialize(src.getGoodCategory(), GoodCategory.class));
        return object;
    }

    public Good deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        GoodCategory goodCategory = context.deserialize(object.get("goodCategory"), GoodCategory.class);
        return new Good(id, name, goodCategory);
    }
}
