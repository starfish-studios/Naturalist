import json

blacklist_substrings=["azure_froglass, crimson_froglass, verdant_froglass, alligator_egg, cattail, chrysalis, glow_goop, duckweed, teddy_bear, tortoise_egg"]

theitems = """naturalist:limestone
naturalist:limestone_stairs
naturalist:limestone_slab
naturalist:limestone_wall
naturalist:smooth_limestone
naturalist:smooth_limestone_stairs
naturalist:smooth_limestone_slab
naturalist:smooth_limestone_wall
naturalist:cut_limestone
naturalist:cut_limestone_stairs
naturalist:cut_limestone_slab
naturalist:cut_limestone_wall
naturalist:limestone_bricks
naturalist:limestone_brick_stairs
naturalist:limestone_brick_slab
naturalist:limestone_brick_wall""".split("\n")


for itemname in theitems:
    for blacklist_substring in blacklist_substrings:
        if blacklist_substring in itemname:
            continue
        thejson = {
                "type": "minecraft:block",
                "pools": [
                        {
                                "bonus_rolls": 0.0,
                                "entries": [
                                        {
                                                "type": "minecraft:item",
                                                "name": itemname
                                        }
                                ],
                                "rolls": 1.0
                        }
                ]
        }
        f = open(itemname.replace("naturalist:","") + ".json", "w+")
        f.write(json.dumps(thejson, indent=4) + "\n")

f.close()
