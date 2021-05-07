package org.dvsa.testing.framework.Utils.common;

import activesupport.number.Int;
import activesupport.string.Str;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class World {

    private static final String id = Str.randomWord(10, 20).concat(String.valueOf(Int.random(100,1000000)));

    public String getId() {
        return id;
    }

    private Map<String, Object> state = new HashMap<>();

    public <T> World put(@NotNull String key, @NotNull T value) {
        state.put(key, value);
        return this;
    }

    public <T> T get(@NotNull String key) {
        return (T) state.get(key);
    }

    public boolean containsKey(String key){
        return state.containsKey(key);
    }

}
