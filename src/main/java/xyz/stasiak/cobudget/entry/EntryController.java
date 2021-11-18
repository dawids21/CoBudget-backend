package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.common.UserIdNotFound;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/api/entry")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class EntryController {

    private final EntryRepository entryRepository;

    @PostMapping
    ResponseEntity<Entry> addEntry(@RequestBody EntryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var entry = Entry.of(dto, userId.id());

        return ResponseEntity.ok(entryRepository.save(entry));
    }

    @GetMapping
    ResponseEntity<Set<EntryReadModel>> getEntriesByDate(@RequestParam String from, @RequestParam String to, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var entries = entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to), userId.id());

        return ResponseEntity.ok(entries);
    }
}