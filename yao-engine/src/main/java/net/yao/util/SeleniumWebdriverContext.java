package net.yao.util;

import org.openqa.selenium.WebDriver;

/**
 * 把 WebDriver 放到 ThreadLocal 里，让同一个线程里的其他代码都能拿到当前浏览器对象。
 */
public class SeleniumWebdriverContext {

   private static final ThreadLocal<WebDriver> THREAD_LOCAL = new ThreadLocal<>();

   public static WebDriver get()
   {
      return THREAD_LOCAL.get();
   }

   public static void set(WebDriver webDriver)
   {
      THREAD_LOCAL.set(webDriver);
   }

   public static void remove()
   {
      THREAD_LOCAL.remove();
   }
}
