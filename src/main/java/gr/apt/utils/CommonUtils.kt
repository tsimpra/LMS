package gr.apt.utils;

import java.util.Collection;

public class CommonUtils {
    public static <T> boolean isNeitherNullNorEmpty(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isNeitherNullNorBlank(String aString) {
        return aString != null && !aString.isBlank();
    }
}
