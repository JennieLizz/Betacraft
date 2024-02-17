package jlengine.utils;

public class StringUtils {
  public static boolean Contains(String[] array, String value) {
    for (String s : array) {
      if (s.equals(value)) {
        return true;
      }
    }
    return false;
  }
}
