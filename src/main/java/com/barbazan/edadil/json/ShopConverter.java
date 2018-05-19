package com.barbazan.edadil.json;

import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopCategory;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ShopConverter implements JsonSerializer<Shop>, JsonDeserializer<Shop> {

    public JsonElement serialize(Shop src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("name", src.getName());
        object.add("shopCategory", context.serialize(src.getShopCategory(), ShopCategory.class));
        return object;
    }

    public Shop deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        ShopCategory shopCategory = context.deserialize(object.get("shopCategory"), ShopCategory.class);
        return new Shop(id, name, shopCategory);
    }
}
