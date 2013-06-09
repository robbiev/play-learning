package controllers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import play.libs.F.Function;
import play.libs.F.Option;
import play.libs.OAuth.RequestToken;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
  private static final ALogger log = Logger.of("application");
  
	public static Result index() {
	  Option<RequestToken> token = Twitter.getToken();
	  if (token.isDefined()) {
	    return async(WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
	        .sign(new play.libs.OAuth.OAuthCalculator(Twitter.CONSUMER_KEY, token.get()))
	        .get()
	        .map(new Function<Response, Result>(){
	          @Override
	          public Result apply(Response jsonResponse) throws Throwable {
	            List<String> tweets = new ArrayList<>();
	            for(JsonNode node : jsonResponse.asJson()) {
	              tweets.add(node.get("text").asText());
	            }
	            return ok(views.html.index.render(tweets));
	          }
	        }));
	  }
		return redirect(routes.Twitter.auth());
	}
}
