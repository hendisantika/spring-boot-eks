package id.my.hendisantika.springbooteks.hello;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-eks
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/01/26
 * Time: 07.03
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping
public class NeonController {
    private final PlayingWithNeonRepository repository;

    public NeonController(PlayingWithNeonRepository repository) {
        this.repository = repository;
    }

    // Insert entry
    @PostMapping("/add")
    public PlayingWithNeon addEntry(@RequestBody NeonRequest request) {
        PlayingWithNeon entry = new PlayingWithNeon();
        entry.setName(request.getName());
        entry.setValue(request.getValue());
        return repository.save(entry);
    }

    // Get all entries
    @GetMapping("/all")
    public List<PlayingWithNeon> getAll() {
        return repository.findAll();
    }
}
