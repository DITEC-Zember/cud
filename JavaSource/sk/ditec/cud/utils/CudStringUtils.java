package sk.ditec.cud.utils;

public class CudStringUtils {

    public static String trunkToSize(String s, int size) {
        if (s == null || s.length() <= size)
            return s;

        return s.substring(0, size - 3) + "...";
    }
}
