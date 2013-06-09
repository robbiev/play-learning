package controllers;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import play.Logger;
import play.Logger.ALogger;
import play.mvc.WebSocket;

public class TweetsFromBackground implements Runnable {
  private static final ALogger log = Logger.of("application");
  private final Set<WebSocket.Out<String>> sockets = new HashSet<>();
  private final ReadWriteLock socketLock = new ReentrantReadWriteLock();
  private final ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);

  public TweetsFromBackground() {
    newScheduledThreadPool.scheduleAtFixedRate(this, 1, 2, TimeUnit.SECONDS);
  }
  
  public void add(WebSocket.Out<String> out) {
    Lock lock = socketLock.writeLock();
    lock.lock();
    try {
      this.sockets.add(out);
    } finally {
      lock.unlock();
    }
  }

  public void remove(WebSocket.Out<String> out) {
    Lock lock = socketLock.writeLock();
    lock.lock();
    try {
      this.sockets.remove(out);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void run() {
    Lock lock = socketLock.readLock();
    lock.lock();
    try {
      for(WebSocket.Out<String> socket : sockets) {
        socket.write("meh");
      }
    } finally {
      lock.unlock();
    }
  }
}