package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Input;

import oauth.signpost.http.HttpParameters;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Option;
import play.libs.Json;
import play.libs.OAuth.OAuthCalculator;
import play.libs.OAuth.RequestToken;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
  private static final String FLASH_CURRENT_INPUT = "input";
  private static final ALogger log = Logger.of("application");
  private static final Form<Input> inputForm = Form.form(Input.class);
  
	public static Result index() {
	  Option<RequestToken> token = Twitter.getToken();
	  if (token.isDefined()) {
	    if (flash().containsKey(FLASH_CURRENT_INPUT)) {
	      HttpParameters params = new HttpParameters();
	      params.put("screen_name", flash(FLASH_CURRENT_INPUT));
	      OAuthCalculator oAuthCalculator = new play.libs.OAuth.OAuthCalculator(Twitter.CONSUMER_KEY, token.get());
	      oAuthCalculator.setAdditionalParameters(params);
	      return async(WS.url("https://api.twitter.com/1.1/statuses/user_timeline.json")
	          .sign(oAuthCalculator)
	          .setQueryParameter("screen_name", flash(FLASH_CURRENT_INPUT))
	          .get()
	          .map(new Function<Response, Result>(){
	            @Override
	            public Result apply(Response jsonResponse) throws Throwable {
	              List<String> tweets = new ArrayList<>();
	              for(JsonNode node : jsonResponse.asJson()) {
	                tweets.add(node.get("text").asText());
	              }
	              Input input = new Input();
	              input.text = flash(FLASH_CURRENT_INPUT);
	              Form<Input> form = inputForm.fill(input);
	              return ok(views.html.index.render(tweets, form));
	            }
	          }));
	    }
	    return ok(views.html.index.render(Arrays.asList("default"), inputForm));
	  }
		return redirect(routes.Twitter.auth());
	}
	
	public static Result load() {
	  Form<Input> form = inputForm.bindFromRequest();
	  if (form.hasErrors()) {
	    flash("error", "Please correct the form below.");
	    return badRequest(views.html.index.render(Arrays.<String>asList(), form));
	  }
	  flash("success", "Success!");
	  flash(FLASH_CURRENT_INPUT, form.get().text);
	  return redirect(routes.Application.index());
	}
}