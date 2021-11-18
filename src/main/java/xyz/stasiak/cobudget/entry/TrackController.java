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
class TrackController {

    private final EntryRepository entryRepository;

    @PostMapping
    ResponseEntity<Entry> addEntry(@RequestBody EntryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {

        return UserId.get(jwt)
                .map(userId -> Entry.of(dto, userId.id()))
                .map(entryRepository::save)
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping
    ResponseEntity<Set<EntryReadModel>> getEntriesByDate(@RequestParam String from, @RequestParam String to, @AuthenticationPrincipal Jwt jwt) {
        return UserId.get(jwt)
                .map(userId -> entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to), userId.id()))
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }
}