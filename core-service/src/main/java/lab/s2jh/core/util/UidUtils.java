package lab.s2jh.core.util;

import java.util.UUID;

public class UidUtils {
    public static String UID(Class<?> clazz) {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String UID() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
