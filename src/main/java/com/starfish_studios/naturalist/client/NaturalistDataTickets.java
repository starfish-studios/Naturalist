package com.starfish_studios.naturalist.client;

import software.bernie.geckolib.constant.dataticket.DataTicket;

public class NaturalistDataTickets {
    public static final DataTicket<Boolean> IS_BABY = DataTicket.create("is_baby", Boolean.class);
    public static final DataTicket<String> ENTITY_NAME = DataTicket.create("entity_name", String.class);
    public static final DataTicket<Boolean> IS_SLEEPING = DataTicket.create("is_sleeping", Boolean.class);
    public static final DataTicket<Boolean> IS_ANGRY = DataTicket.create("is_angry", Boolean.class);
    public static final DataTicket<Boolean> IS_EATING = DataTicket.create("is_eating", Boolean.class);
    public static final DataTicket<Boolean> HAS_MANE = DataTicket.create("has_mane", Boolean.class);
    public static final DataTicket<Boolean> IS_AGGRESSIVE = DataTicket.create("is_aggressive", Boolean.class);
    public static final DataTicket<Integer> VARIANT_ID = DataTicket.create("variant_id", Integer.class);
    public static final DataTicket<String> VARIANT_NAME = DataTicket.create("variant_name", String.class);
}
