package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to CyWalk! Login through Params to Get Started";
    }

    @GetMapping("/username={name}&password={user}")
    public String welcome(@PathVariable String name, @PathVariable String user) {
        return "Welcome to CyWalk " + name + "! Get Started Walking!";
    }

    @GetMapping("/username={name}&password=admin")
    public String welcome(@PathVariable String name) {
        return "Welcome to the Admin Page " + name + "!";
    }
}
