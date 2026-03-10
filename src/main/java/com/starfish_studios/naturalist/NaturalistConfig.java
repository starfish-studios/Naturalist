package com.starfish_studios.naturalist;

import eu.midnightdust.lib.config.MidnightConfig;

public class NaturalistConfig extends MidnightConfig {

//    @Entry(category = "server") public static boolean spawnFarmAnimalsInSwamps = false;
//    @Entry(category = "server") public static boolean spawnFarmAnimalsInSavannas = false;

    @Comment(category = "mobRemoval") public static Comment mobRemoval;
    @Comment(category = "mobRemoval") public static Comment spacer;

    @Entry(category = "mobRemoval") public static boolean alligatorRemoved = false;
    @Entry(category = "mobRemoval") public static boolean bassRemoved = false;
    @Entry(category = "mobRemoval") public static boolean bearRemoved = false;
    @Entry(category = "mobRemoval") public static boolean bluejayRemoved = false;
    @Entry(category = "mobRemoval") public static boolean boarRemoved = false;
    @Entry(category = "mobRemoval") public static boolean butterflyRemoved = false;
    @Entry(category = "mobRemoval") public static boolean canaryRemoved = false;
    @Entry(category = "mobRemoval") public static boolean cardinalRemoved = false;
//    @Entry(category = "mobRemoval") public static boolean caterpillarRemoved = false;
    @Entry(category = "mobRemoval") public static boolean catfishRemoved = false;
    @Entry(category = "mobRemoval") public static boolean coralSnakeRemoved = false;
    @Entry(category = "mobRemoval") public static boolean deerRemoved = false;
    @Entry(category = "mobRemoval") public static boolean dragonflyRemoved = false;
    @Entry(category = "mobRemoval") public static boolean duckRemoved = false;
    @Entry(category = "mobRemoval") public static boolean elephantRemoved = false;
    @Entry(category = "mobRemoval") public static boolean finchRemoved = false;
    @Entry(category = "mobRemoval") public static boolean fireflyRemoved = false;
    @Entry(category = "mobRemoval") public static boolean forestFoxRemoved = false;
    @Entry(category = "mobRemoval") public static boolean forestRabbitRemoved = false;
    @Entry(category = "mobRemoval") public static boolean giraffeRemoved = false;
    @Entry(category = "mobRemoval") public static boolean hippoRemoved = false;
    @Entry(category = "mobRemoval") public static boolean lionRemoved = false;
    @Entry(category = "mobRemoval") public static boolean lizardRemoved = false;
    @Entry(category = "mobRemoval") public static boolean rattlesnakeRemoved = false;
    @Entry(category = "mobRemoval") public static boolean rhinoRemoved = false;
    @Entry(category = "mobRemoval") public static boolean robinRemoved = false;
    @Entry(category = "mobRemoval") public static boolean snailRemoved = false;
    @Entry(category = "mobRemoval") public static boolean snakeRemoved = false;
    @Entry(category = "mobRemoval") public static boolean sparrowRemoved = false;
    @Entry(category = "mobRemoval") public static boolean tortoiseRemoved = false;
    @Entry(category = "mobRemoval") public static boolean vultureRemoved = false;
    @Entry(category = "mobRemoval") public static boolean zebraRemoved = false;


    @Comment(category = "mobConfig") public static Comment biomeConfig;
    @Comment(category = "mobConfig") public static Comment blacklistConfig;
    @Comment(category = "mobConfig") public static Comment spacer2;

    @Entry(category = "mobConfig") public static int alligatorSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int alligatorSpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int alligatorSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int bassSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bassSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bassSpawnMaxGroupSize = 6;

    @Entry(category = "mobConfig") public static int bearSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bearSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bearSpawnMaxGroupSize = 2;

    @Entry(category = "mobConfig") public static int bluejaySpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bluejaySpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int bluejaySpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int boarSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int boarSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int boarSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int butterflySpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int butterflySpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int butterflySpawnMaxGroupSize = 6;

    @Entry(category = "mobConfig") public static int canarySpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int canarySpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int canarySpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int cardinalSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int cardinalSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int cardinalSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int catfishSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int catfishSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int catfishSpawnMaxGroupSize = 2;

    @Entry(category = "mobConfig") public static int coralSnakeSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int coralSnakeSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int coralSnakeSpawnMaxGroupSize = 1;

    @Entry(category = "mobConfig") public static int deerSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int deerSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int deerSpawnMaxGroupSize = 5;

    @Entry(category = "mobConfig") public static int dragonflySpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int dragonflySpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int dragonflySpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int duckSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int duckSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int duckSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int elephantSpawnWeight = 5;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int elephantSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int elephantSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int finchSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int finchSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int finchSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int fireflySpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int fireflySpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int fireflySpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int forestFoxSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int forestFoxSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int forestFoxSpawnMaxGroupSize = 2;

    @Entry(category = "mobConfig") public static int forestRabbitSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int forestRabbitSpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int forestRabbitSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int giraffeSpawnWeight = 5;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int giraffeSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int giraffeSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int hippoSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int hippoSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int hippoSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int lionSpawnWeight = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int lionSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int lionSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int lizardSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int lizardSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int lizardSpawnMaxGroupSize = 1;

    @Entry(category = "mobConfig") public static int rattlesnakeSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int rattlesnakeSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int rattlesnakeSpawnMaxGroupSize = 1;

    @Entry(category = "mobConfig") public static int rhinoSpawnWeight = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int rhinoSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int rhinoSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int robinSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int robinSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int robinSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int snailSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int snailSpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int snailSpawnMaxGroupSize = 3;

    @Entry(category = "mobConfig") public static int snakeSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int snakeSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int snakeSpawnMaxGroupSize = 1;

    @Entry(category = "mobConfig") public static int sparrowSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int sparrowSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int sparrowSpawnMaxGroupSize = 4;

    @Entry(category = "mobConfig") public static int tortoiseSpawnWeight = 10;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int tortoiseSpawnMinGroupSize = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int tortoiseSpawnMaxGroupSize = 1;

    @Entry(category = "mobConfig") public static int vultureSpawnWeight = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int vultureSpawnMinGroupSize = 3;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int vultureSpawnMaxGroupSize = 5;

    @Entry(category = "mobConfig") public static int zebraSpawnWeight = 1;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int zebraSpawnMinGroupSize = 2;
    @Entry(category = "mobConfig",isSlider = true, min = 1, max = 8) public static int zebraSpawnMaxGroupSize = 6;



}
