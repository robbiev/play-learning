package controllers;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import play.Logger;
import play.Logger.ALogger;

public class TweetsFromBackground implements Runnable {
  private static final ALogger log = Logger.of("application");

  private final ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);

  public TweetsFromBackground() {
    newScheduledThreadPool.scheduleAtFixedRate(this, 1, 2, TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    //log.debug("MEH");
  }
  
}
