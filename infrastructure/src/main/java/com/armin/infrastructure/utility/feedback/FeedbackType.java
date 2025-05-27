package com.armin.infrastructure.utility.feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum FeedbackType {
    //accident
    LIGHT_ACCIDENT("AUTO"),
    HEAVY_ACCIDENT("AUTO"),
    //traffic
    HEAVY_JAM("AUTO"),
    LIGHT_JAM("AUTO"),
    MEDIUM_JAM("AUTO"),
    //way incident
    PIT("AUTO"),
    LANDSLIDES("AUTO"),
    BLOCKED_WAY("AUTO"),
    ICY_ROAD("AUTO"),
    FLOODED_ROAD("AUTO"),
    UNDER_CONSTRUCTION("AUTO"),
    //police
    POLICE("AUTO"),
    UNDER_COVER_POLICE("AUTO"),
    //camera
    SPEED_CAMERA("MANUALS"),
    TRAFFIC_LIGHT_CAMERA("MANUALS"),
    LOW_EMISSION_ZONE_CAMERA("MANUALS"),
    //bump
    BUMP("MANUALS"),
    //
    SLIPPERY_ROAD("AUTO"),
    FOGGY_ROAD("AUTO"),
    CAR_CHAINS_NEEDED("AUTO"),
    //add place
    GAS_STATION("MANUALS"),
    CNG_STATION("MANUALS"),
    PARKING("MANUALS"),
    //map issue
    DEAD_END("MANUALS"),
    TWO_WAY("MANUALS"),
    NO_ENTRY("MANUALS"),
    NO_CAR_WAY("MANUALS"),
    NO_U_TURN("MANUALS"),
    NO_RIGHT_TURN("MANUALS"),
    NO_LEFT_TURN("MANUALS"),
    DIRT_ROAD("MANUALS"),
    NEW_WAY("MANUALS"),
    NO_WAY("MANUALS"),
    WAY_NAME("MANUALS"),
    SPEED_LIMITED_30("MANUALS"),
    SPEED_LIMITED_40("MANUALS"),
    SPEED_LIMITED_50("MANUALS"),
    SPEED_LIMITED_60("MANUALS"),
    SPEED_LIMITED_70("MANUALS"),
    SPEED_LIMITED_80("MANUALS"),
    SPEED_LIMITED_85("MANUALS"),
    SPEED_LIMITED_90("MANUALS"),
    SPEED_LIMITED_95("MANUALS"),
    SPEED_LIMITED_100("MANUALS"),
    SPEED_LIMITED_110("MANUALS"),
    SPEED_LIMITED_120("MANUALS"),
    //
    POI_CLOSED("MANUALS"),
    DUPLICATE_POI("MANUALS"),
    NO_POI_FOUND("MANUALS"),
    //
    ADD_MISSING_PLACE("MANUALS");

    String processType;

    FeedbackType(String processType) {
        this.processType = processType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    private static final List<FeedbackType> autos = new ArrayList<>();
    private static final List<FeedbackType> manuals = new ArrayList<>();

    static {
        for (FeedbackType type : FeedbackType.values()) {
            if (type.getProcessType().equals("AUTO"))
                autos.add(type);
            if (type.getProcessType().equals("MANUALS"))
                manuals.add(type);
        }
    }

    public static boolean existedInAutos(FeedbackType type) {
        return autos.contains(type);
    }

    public static boolean existedInManuals(FeedbackType type) {
        return manuals.contains(type);
    }

    public static List<FeedbackType> getAllManualsTypes() {
        return manuals;
    }


}
