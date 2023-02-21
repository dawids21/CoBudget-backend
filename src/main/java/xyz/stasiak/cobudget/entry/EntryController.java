package xyz.stasiak.cobudget.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.common.UserId;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/entry")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class EntryController {

    private final EntryRepository entryRepository;

    @PostMapping
    ResponseEntity<Entry> addEntry(@RequestBody EntryWriteModel dto, Principal principal) {

        var userId = new UserId(principal.getName());

        var entry = Entry.of(dto, userId.id());

        return ResponseEntity.ok(entryRepository.save(entry));
    }

}