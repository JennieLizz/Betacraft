package betacraft.utils;

public class JLog {
  /*
   * public static void main(String[] args) {
   * JLog j = new JLog();
   * j.showTime = true;
   * 
   * while (true) {
   * String str = "Hello, World!";
   * String fstr = "";
   * for (char c : str.toCharArray()) {
   * int rr = (int) (Math.random() * 9);
   * String rc = switch (rr) {
   * case 0 -> JLog.COLORS.ANSI_BLACK;
   * case 1 -> JLog.COLORS.ANSI_BLUE;
   * case 2 -> JLog.COLORS.ANSI_CYAN;
   * case 3 -> JLog.COLORS.ANSI_GREEN;
   * case 4 -> JLog.COLORS.ANSI_GREEN;
   * case 5 -> JLog.COLORS.ANSI_PURPLE;
   * case 6 -> JLog.COLORS.ANSI_RED;
   * case 7 -> JLog.COLORS.ANSI_WHITE;
   * case 8 -> JLog.COLORS.ANSI_YELLOW;
   * default -> JLog.COLORS.ANSI_BLACK;
   * };
   * fstr += rc + c;
   * }
   * 
   * jl.Print(fstr, JLog.TYPE.INFO, false, null);
   * }
   * }
   */

  static boolean m_logsDisabled;
  static long m_StartTime = System.currentTimeMillis();
  public boolean showTime;
  Class<?> m_sf;
  String m_ssf;

  /**
   * Allows you to change the color of the string.
   * (Can be used multiple times in a string)
   * 
   * Ex. String ex = COLORS.ANSI_RED + "Red" + COLORS.ANSI_BLUE + "Blue";
   *
   */
  public static class COLORS {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
  }

  public enum TYPE {
    INFO,
    WARNING,
    ERROR
  }

  static final String Start = "JLog - ";
  static final String End = "------------------------";

  /**
   * Prints to the Console.
   *
   * @param text       - The text to output.
   * @param type       - Type of output. (INFO, WARNING, ERROR)
   * @param fDisableSf - Disables the output of the sender Class name. (Only works
   *                   when GiveSentFrom() has been called with the parent Class)
   * @return None
   */
  public void Print(Object out, TYPE type, boolean fDisableSf, Throwable e) {
    String m_r = COLORS.ANSI_RESET;
    String m_w = COLORS.ANSI_YELLOW;
    String m_e = COLORS.ANSI_RED;

    long m_elapsedTime = System.currentTimeMillis() - m_StartTime;

    switch (type) {
      case INFO:
        if (showTime)
          System.out.println(m_r + "@" + Start + "INFO" + " +@ " + m_elapsedTime);
        else
          System.out.println(m_r + "@" + Start + "INFO");
        break;
      case WARNING:
        if (showTime)
          System.out.println(m_r + m_w + "^" + Start + "WARNING" + " +@ " + m_elapsedTime);
        else
          System.out.println(m_r + m_w + "^" + Start + "WARNING");
        break;
      case ERROR:
        if (showTime)
          System.out.println(m_r + m_e + "!" + Start + "ERROR" + " +@ " + m_elapsedTime);
        else
          System.out.println(m_r + m_e + "!" + Start + "ERROR");
        break;
    }

    System.out.println(out);

    switch (type) {
      case INFO:
        System.out.println(m_r + End);
        break;
      case WARNING:
        System.out.println(m_r + m_w + End);
        break;
      case ERROR:
        System.out.println(m_r + m_e + End);
        if (e != null)
          System.out.println(getStackTraceString(e));
        break;
    }

    if (fDisableSf || m_sf == null)
      return;

    System.out.println(m_ssf);
  }

  public static String getStackTraceString(Throwable e) {
    StringBuilder sb = new StringBuilder();
    sb.append(e.toString());
    sb.append("\n");

    String indent = "	";

    StackTraceElement[] stack = e.getStackTrace();
    if (stack != null) {
      for (StackTraceElement stackTraceElement : stack) {
        sb.append(indent);
        sb.append("\tat ");
        sb.append(stackTraceElement.toString());
        sb.append("\n");
      }
    }

    Throwable cause = e.getCause();
    if (cause != null) {
      sb.append(indent);
      sb.append("Caused by: ");
      sb.append(getStackTraceString(cause));
    }

    return sb.toString();
  }

  /**
   * Allows the parent Class to be used in the log.
   * 
   * @param sf
   */
  public void AllowSentFrom(Class<?> sf) {
    m_sf = sf;
    m_ssf = sf.getName();
  }

  public static void DisableAllLogs() {
    m_logsDisabled = true;
  }

  public static void EnableAllLogs() {
    m_logsDisabled = false;
  }

  public static void ToggleAllLogs() {
    m_logsDisabled = !m_logsDisabled;
  }
}
