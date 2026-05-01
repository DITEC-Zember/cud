package sk.ditec.cud.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SkupinaPrijemcov {
    CRD("CRD"),
    ExportLokaciiCRD("ExportLokaciiCRD"),
    RINF("RINF"),
    CUD("CUD");

    private String name;
    private static final Map<String, SkupinaPrijemcov> ENUM_MAP;

    SkupinaPrijemcov (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    static {
        Map<String, SkupinaPrijemcov> map = new ConcurrentHashMap<String, SkupinaPrijemcov>();
        for (SkupinaPrijemcov skupina : SkupinaPrijemcov.values()) {
            map.put(skupina.getName().toLowerCase(), skupina);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static SkupinaPrijemcov get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
