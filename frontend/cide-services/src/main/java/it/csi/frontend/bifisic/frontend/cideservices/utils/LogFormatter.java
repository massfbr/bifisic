package it.csi.frontend.bifisic.frontend.cideservices.utils;

public class LogFormatter {

  /**
   * @param className
   * @param methodName
   * @param msg
   * @return formatted text
   */
  public static String format(String className, String methodName, String msg) {
	StringBuffer sb = new StringBuffer();
	sb.append("[").append(className).append("::").append(methodName).append("] ").append(msg);
	return sb.toString();
  }
}
