package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Bass;
import com.starfish_studios.naturalist.common.entity.Bear;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.common.entity.Boar;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Catfish;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
import com.starfish_studios.naturalist.common.entity.Deer;
import com.starfish_studios.naturalist.common.entity.Dragonfly;
import com.starfish_studios.naturalist.common.entity.Duck;
import com.starfish_studios.naturalist.common.entity.Elephant;
import com.starfish_studios.naturalist.common.entity.Firefly;
import com.starfish_studios.naturalist.common.entity.Giraffe;
import com.starfish_studios.naturalist.common.entity.Hippo;
import com.starfish_studios.naturalist.common.entity.Lion;
import com.starfish_studios.naturalist.common.entity.Lizard;
import com.starfish_studios.naturalist.common.entity.LizardTail;
import com.starfish_studios.naturalist.common.entity.Rhino;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.entity.Snake;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import com.starfish_studios.naturalist.common.entity.Vulture;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.List;
import java.util.function.Supplier;

public class NaturalistEntityAttributes {
        private record Entry(Supplier<? extends EntityType<? extends LivingEntity>> type,
                        Supplier<AttributeSupplier.Builder> builder) {
        }

        private static final List<Entry> ENTRIES = List.of(
                        new Entry(NaturalistEntityTypes.SNAIL, Snail::createAttributes),
                        new Entry(NaturalistEntityTypes.BEAR, Bear::createAttributes),
                        new Entry(NaturalistEntityTypes.BUTTERFLY, Butterfly::createAttributes),
                        new Entry(NaturalistEntityTypes.FIREFLY, Firefly::createAttributes),
                        new Entry(NaturalistEntityTypes.SNAKE, Snake::createAttributes),
                        new Entry(NaturalistEntityTypes.CORAL_SNAKE, Snake::createAttributes),
                        new Entry(NaturalistEntityTypes.RATTLESNAKE, Snake::createAttributes),
                        new Entry(NaturalistEntityTypes.DEER, Deer::createAttributes),
                        new Entry(NaturalistEntityTypes.BLUEJAY, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.CANARY, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.CARDINAL, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.ROBIN, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.FINCH, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.SPARROW, Bird::createAttributes),
                        new Entry(NaturalistEntityTypes.CATERPILLAR, Caterpillar::createAttributes),
                        new Entry(NaturalistEntityTypes.RHINO, Rhino::createAttributes),
                        new Entry(NaturalistEntityTypes.LION, Lion::createAttributes),
                        new Entry(NaturalistEntityTypes.ELEPHANT, Elephant::createAttributes),
                        new Entry(NaturalistEntityTypes.ZEBRA, AbstractHorse::createBaseHorseAttributes),
                        new Entry(NaturalistEntityTypes.GIRAFFE, Giraffe::createAttributes),
                        new Entry(NaturalistEntityTypes.HIPPO, Hippo::createAttributes),
                        new Entry(NaturalistEntityTypes.VULTURE, Vulture::createAttributes),
                        new Entry(NaturalistEntityTypes.BOAR, Boar::createAttributes),
                        new Entry(NaturalistEntityTypes.DRAGONFLY, Dragonfly::createAttributes),
                        new Entry(NaturalistEntityTypes.CATFISH, Catfish::createAttributes),
                        new Entry(NaturalistEntityTypes.ALLIGATOR, Alligator::createAttributes),
                        new Entry(NaturalistEntityTypes.BASS, Bass::createAttributes),
                        new Entry(NaturalistEntityTypes.LIZARD, Lizard::createAttributes),
                        new Entry(NaturalistEntityTypes.LIZARD_TAIL, LizardTail::createAttributes),
                        new Entry(NaturalistEntityTypes.TORTOISE, Tortoise::createAttributes),
                        new Entry(NaturalistEntityTypes.DUCK, Duck::createAttributes));



}
