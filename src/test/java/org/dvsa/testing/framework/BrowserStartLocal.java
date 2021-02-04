package org.dvsa.testing.framework;

import Injectors.World;
import com.browserstack.local.Local;

import java.util.HashMap;

public class BrowserStartLocal {
    World world;
    public void browserLocal(World world) throws Exception {
        this.world = new World();
        Local bsLocal = new Local();
        HashMap<String, String> bsLocalArgs = new HashMap<>();
        bsLocalArgs.put("key", world.configuration.config.getString("browserStackKey"));

        bsLocal.start(bsLocalArgs);

        System.out.println(bsLocal.isRunning());
    }
}
