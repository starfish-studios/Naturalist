import json

blacklist_substrings=["azure_froglass, crimson_froglass, verdant_froglass, alligator_egg, cattail, chrysalis, glow_goop, duckweed, teddy_bear, tortoise_egg"]

theitems = """naturalist:shellstone
naturalist:shellstone_stairs
naturalist:shellstone_slab
naturalist:shellstone_wall
naturalist:smooth_shellstone
naturalist:smooth_shellstone_stairs
naturalist:smooth_shellstone_slab
naturalist:smooth_shellstone_wall
naturalist:cut_shellstone
naturalist:cut_shellstone_stairs
naturalist:cut_shellstone_slab
naturalist:cut_shellstone_wall
naturalist:shellstone_bricks
naturalist:shellstone_brick_stairs
naturalist:shellstone_brick_slab
naturalist:shellstone_brick_wall""".split("\n")


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
