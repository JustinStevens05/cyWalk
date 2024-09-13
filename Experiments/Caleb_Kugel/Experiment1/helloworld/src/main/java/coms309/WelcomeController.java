package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@RestController
class WelcomeController {
  
    HashMap<String, Integer> viewCounter = new HashMap<>();  

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
	if (viewCounter.containsKey(name)) {
	  viewCounter.put(name, viewCounter.get(name) + 1);
	}
	else {
	  viewCounter.put(name , 1);
	}
        return "Hello and welcome to COMS 309: " + name;
    }

    @GetMapping("/{name}/views")
    public String getViews(@PathVariable String name) {
      return name + " has visited this site " + viewCounter.get(name) + " times.";
    }

    @GetMapping("/newPage") 
    public String newPage() {
      return "This is a new page that was created";
    }

    
}
