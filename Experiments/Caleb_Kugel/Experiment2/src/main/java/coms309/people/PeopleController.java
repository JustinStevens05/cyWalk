package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

@RestController
public class PeopleController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    HashMap<String, Person> peopleList = new  HashMap<>();

    HashMap<Person, Walking> peopleWalking = new HashMap<>();

    HashMap<String, Organization> organizationList = new HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/people")
    public @ResponseBody HashMap<String,Person> getAllPersons() {
        return peopleList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/people")
    public @ResponseBody String createPerson(@RequestBody Person person) {
        System.out.println(person);
        peopleList.put(person.getFirstName(), person);
        return "New person "+ person.getFirstName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/people/{firstName}")
    public @ResponseBody Person getPerson(@PathVariable String firstName) {
        Person p = peopleList.get(firstName);
        return p;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/people/{firstName}")
    public @ResponseBody Person updatePerson(@PathVariable String firstName, @RequestBody Person p) {
        peopleList.replace(firstName, p);
        return peopleList.get(firstName);
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/people/{firstName}")
    public @ResponseBody HashMap<String, Person> deletePerson(@PathVariable String firstName) {
	deleteSteps(firstName);
	peopleList.remove(firstName);
        return peopleList;
    }

  @PostMapping("/people/{firstName}/addSteps")
  public @ResponseBody String addSteps(@PathVariable String firstName, @RequestBody Walking walk) {
    Person p = peopleList.get(firstName);
    peopleWalking.put(p, walk);
    return "Updated steps for " + firstName + ". with: " + walk.toString();
  }

  @PutMapping("/people/{firstName}/updateSteps")
  public @ResponseBody Walking updateSteps(@PathVariable String firstName, @RequestBody Walking walk) {
    Person p = peopleList.get(firstName);
    peopleWalking.get(p).append(walk);
    return peopleWalking.get(p);
  }

  @GetMapping("/people/{firstName}/getSteps")
  public @ResponseBody Walking getSteps(@PathVariable String firstName) {
    return peopleWalking.get(peopleList.get(firstName));
  }

  @DeleteMapping("/people/{firstName}/steps")
  public @ResponseBody HashMap<Person, Walking> deleteSteps(@PathVariable String firstName) {
    peopleWalking.remove(peopleList.get(firstName));
    return peopleWalking;
  }

  @PostMapping("/organization")
  public @ResponseBody Organization createOrganization(@RequestParam(required = true) Person creator, @RequestParam(required = true) String name, @RequestParam(required = true) String location) {
    Organization org = new Organization(name, creator.firstName, location);
    organizationList.put(name, org);
    return org;
  }

  @PutMapping("/organization/{organization}/add-user/{firstName}")
  public @ResponseBody Organization addUserToOrganization(@PathVariable String organization, @PathVariable String firstName) {
    Organization org = organizationList.get(organizarion);
    org.addPerson(organizationList.get(firstName));
    return org;
  }

  @GetMapping("/organization/{organization}")
  public @ResponseBody Organization getOrganization(@PathVariable String organization) {
    return organizationList.get(organization);
  }

  @DeleteMapping("/organization/{organization}")
  public @ResponseBody Organization deleteOrganization(@PathVariable String organization) {
    return organizationList.remove(organization);
  }


}

