package coms309.people;

import java.util.ArrayList;
import java.util.List;

public class Organization {
  private String name;
  
  private String adminsName;

  private String location;

  private List<Person> people;

  public Organization() {
    people = new ArrayList<>();
  }

  public Organization(String name, String adminsName, String location) {
    this.name = name;
    this.adminsName = adminsName;
    this.location = location;
    people = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public String getAdminsName() {
    return adminsName;
  }

  public String getLocation() {
    return location;
  }

  public void addPerson(Person p1) {
    people.add(p1);
  }

  public List<Person> getPeople() {
    return people;
  }

  public String toString() {
    return name + ", " + adminsName + ", " + location; 
  }

}


