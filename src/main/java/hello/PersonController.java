package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PersonController {

    private ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    private MongoOperations mongoOps =
            (MongoOperations) ctx.getBean("mongoTemplate");

    @Autowired
    public PersonController() {
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public List<Person> getPersons(){
        return mongoOps.findAll(Person.class);
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public Person getPerson(@RequestParam(value="id") String id, @RequestParam(value="firstName", required = false) String firstName, @RequestParam(value="lastName", required = false) String lastName){
        return mongoOps.findById(id, Person.class);
    }

    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public Person insertPerson(@RequestBody Person input){
        mongoOps.save(input);
        return input;
    }

    @RequestMapping(value = "/person", method = RequestMethod.DELETE)
    public Boolean removePerson(@RequestParam(value="id") String id){
        Query q = new Query(Criteria.where("_id").is(id));
        mongoOps.remove(q, Person.class);
        return true;
    }
}
