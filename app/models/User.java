package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class User extends Model {
  @Id
  public long ID;
  public String name;
  @OneToMany(mappedBy="user")
  public List<Tweet> tweets;
  
  
  public static Finder<Long, User> find() {
    return new Finder<Long, User>(Long.class, User.class);
  }
}
