package coms309.people;

public class Organization {
  private String name;
  
  private String adminsName;

  private String location;

  public Organization() {

  }

  public Organization(String name, String adminsName, String location) {
    this.name = name;
    this.adminsName = adminName;
    this.location = location;
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

  public String toString() {
    return name + ", " + adminName + ", " + location; 
  }

}


