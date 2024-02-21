package jlengine.utils;

public class JLLog {
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

    static final long m_StartTime = System.currentTimeMillis();
    static final String Start = "JLog - ";
    static final String End = "------------------------";
    static boolean m_logsDisabled;
    public boolean showTime;
    Class<?> m_sf;
    String m_ssf;

    public static String GetStackTraceString(Throwable e) {
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
            sb.append(GetStackTraceString(cause));
        }

        return sb.toString();
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

    public static void SFPrint(Object out) {
        String r = COLORS.ANSI_RESET;

        long m_elapsedTime = System.currentTimeMillis() - m_StartTime;

        System.out.println(r + "@" + Start + "INFO" + " +@ " + m_elapsedTime);
        System.out.println(out);
        System.out.println(r + End);
    }

    /**
     * Prints to the Console.
     *
     * @param out        The object to output.
     * @param type       Type of output. (INFO, WARNING, ERROR)
     * @param fDisableSf Disables the output of the sender Class name. (Only works
     *                   when GiveSentFrom() has been called with the parent Class)
     */
    public void Print(Object out, TYPE type, boolean fDisableSf, Throwable e) {
        String r = COLORS.ANSI_RESET;
        String yc = COLORS.ANSI_YELLOW;
        String rc = COLORS.ANSI_RED;

        long m_elapsedTime = System.currentTimeMillis() - m_StartTime;

        switch (type) {
            case INFO:
                if (showTime)
                    System.out.println(r + "@" + Start + "INFO" + " +@ " + m_elapsedTime);
                else
                    System.out.println(r + "@" + Start + "INFO");
                break;
            case WARNING:
                if (showTime)
                    System.out.println(r + yc + "^" + Start + "WARNING" + " +@ " + m_elapsedTime);
                else
                    System.out.println(r + yc + "^" + Start + "WARNING");
                break;
            case ERROR:
                if (showTime)
                    System.out.println(r + rc + "!" + Start + "ERROR" + " +@ " + m_elapsedTime);
                else
                    System.out.println(r + rc + "!" + Start + "ERROR");
                break;
        }

        System.out.println(out);

        switch (type) {
            case INFO:
                System.out.println(r + End);
                break;
            case WARNING:
                System.out.println(r + yc + End);
                break;
            case ERROR:
                System.out.println(r + rc + End);
                if (e != null)
                    System.out.println(GetStackTraceString(e));
                break;
        }

        if (fDisableSf || m_sf == null)
            return;

        System.out.println(m_ssf);
    }

    /**
     * Allows the parent Class to be used in the log.
     *
     * @param sf Shows the class the print was from.
     */
    public void AllowSentFrom(Class<?> sf) {
        m_sf = sf;
        m_ssf = sf.getName();
    }

    public enum TYPE {
        INFO,
        WARNING,
        ERROR
    }

    /**
     * Allows you to change the color of the string.
     * (Can be used multiple times in a string)
     * <p>
     * Ex. String ex = COLORS.ANSI_RED + "Red" + COLORS.ANSI_BLUE + "Blue";
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
}