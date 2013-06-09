package controllers;

import play.Configuration;
import play.Logger;
import play.Logger.ALogger;
import play.Play;
import play.libs.F.Option;
import play.libs.OAuth;
import play.libs.OAuth.ConsumerKey;
import play.libs.OAuth.RequestToken;
import play.libs.OAuth.ServiceInfo;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Strings;

public class Twitter extends Controller {
  private static final ALogger log = Logger.of("application");
  static final ConsumerKey CONSUMER_KEY;

  static {
    Configuration conf = Play.application().configuration();
    String key = conf.getString("twitter.consumer_key");
    String secret = conf.getString("twitter.consumer_secret");
    CONSUMER_KEY = new ConsumerKey(key, secret);
  }
  
  private static final ServiceInfo SERVICE_INFO = new ServiceInfo("https://api.twitter.com/oauth/request_token",
                                                                  "https://api.twitter.com/oauth/access_token",
                                                                  "https://api.twitter.com/oauth/authorize", 
                                                                  CONSUMER_KEY);
  
  private static final OAuth TWITTER = new OAuth(SERVICE_INFO);
  
  public static Result auth() {
    String verifier = request().getQueryString("oauth_verifier");
    if (Strings.isNullOrEmpty(verifier)) {
      String url = routes.Twitter.auth().absoluteURL(request());
      RequestToken requestToken = TWITTER.retrieveRequestToken(url);
      saveRequestTokenToSession(requestToken);
      String redirectURL = TWITTER.redirectUrl(requestToken.token);
      return redirect(redirectURL);
    } else {
      RequestToken requestToken = getToken().get();
      RequestToken accessToken = TWITTER.retrieveAccessToken(requestToken, verifier);
      saveRequestTokenToSession(accessToken);
      return redirect(routes.Application.index());
    }
  }

  private static void saveRequestTokenToSession(RequestToken requestToken) {
    session("token", requestToken.token);
    session("secret", requestToken.secret);
  }

  static Option<RequestToken> getToken() {
    if (session().containsKey("token")) {
      return Option.Some(new RequestToken(session("token"), session("secret")));
    }
    return Option.None();
  }
}