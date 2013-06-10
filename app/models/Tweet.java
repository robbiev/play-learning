package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Tweet extends Model {
  @Id
  public long ID;
  public String text;
  @ManyToOne
  public User user;
}
