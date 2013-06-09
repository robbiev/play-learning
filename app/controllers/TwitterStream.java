package controllers;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;

public class TwitterStream {
  private static final TweetsFromBackground t = new TweetsFromBackground();

  public static WebSocket<String> index() {
    return new WebSocket<String>() {
      public void onReady(WebSocket.In<String> in, final WebSocket.Out<String> out) {
        in.onMessage(new Callback<String>() {
           public void invoke(String event) {
             System.out.println(event);  
           } 
        });
        
        in.onClose(new Callback0() {
           public void invoke() {
             t.remove(out);
           }
        });

        t.add(out);
      }
      
    };
  }

}
