package xyz.stasiak.cobudget.entry.query;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.stasiak.cobudget.common.UserId;

import java.security.Principal;
import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/api/entry")
@RestController
class EntryQueryController {

    private final EntryQueryService queryService;

    @GetMapping
    ResponseEntity<Set<EntryReadModel>> getEntriesByDate(@RequestParam String from, @RequestParam String to, Principal principal) {

        var userId = new UserId(principal.getName());
        var fromDate = LocalDate.parse(from);
        var toDate = LocalDate.parse(to);

        return ResponseEntity.ok(queryService.getByDate(fromDate, toDate, userId));
    }

}
