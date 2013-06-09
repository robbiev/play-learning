package models;

import play.data.validation.Constraints.Required;

public class Input {
  @Required(message="Tell us which user you'd like to track")
  public String text;
}
