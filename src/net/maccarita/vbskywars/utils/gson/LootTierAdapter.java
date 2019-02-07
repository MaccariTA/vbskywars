package net.maccarita.vbskywars.utils.gson;


import com.google.gson.*;
import net.maccarita.vbskywars.arenas.loot.TierLevel;

import java.lang.reflect.Type;

/**
 * A Gson adapter for {@link TierLevel}
 */
public class LootTierAdapter implements JsonDeserializer<TierLevel>, JsonSerializer<TierLevel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TierLevel deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        TierLevel tierLevel;
        if (!json.isJsonObject()) {
            throw new JsonParseException("not a JSON object");
        }

        final JsonObject obj = (JsonObject) json;
        final JsonElement tier = obj.get("tier");

        if (!tier.isJsonPrimitive()) {
            throw new JsonParseException("tier is not a number");
        }
        switch (tier.getAsByte()) {
            case 1:
                tierLevel = jsonDeserializationContext.deserialize(json, TierLevel.TierLevel1.class);
                break;
            case 2:
                tierLevel = jsonDeserializationContext.deserialize(json, TierLevel.TierLevel2.class);
                break;
            default:
                throw new JsonParseException("tier is not an expected value.");
        }
        return tierLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(TierLevel tierLevel, Type type, JsonSerializationContext jsonSerializationContext) {

        final JsonObject obj = new JsonObject();
        obj.addProperty("tier", tierLevel.getTierLevel());
        return obj;

    }

}