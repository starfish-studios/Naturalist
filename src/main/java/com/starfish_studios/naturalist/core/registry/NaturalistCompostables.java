package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;

public class NaturalistCompostables {
    public static void register() {
        CommonPlatformHelper.registerCompostable(0.65F, NaturalistItems.SNAIL_SHELL.get());
    }
}

