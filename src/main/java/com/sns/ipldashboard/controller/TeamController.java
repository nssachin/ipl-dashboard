package com.sns.ipldashboard.controller;

import com.sns.ipldashboard.model.Match;
import com.sns.ipldashboard.model.Team;
import com.sns.ipldashboard.repository.MatchRepository;
import com.sns.ipldashboard.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("team")
@CrossOrigin
public class TeamController {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/{teamName}")
    public ResponseEntity<Team> getTeam(@PathVariable String teamName) {
        Team team;
        Optional<Team> teamOptional = teamRepository.findByTeamName(teamName);
        if (teamOptional.isPresent()) {
            team = teamOptional.get();
            List<Match> matches = matchRepository.findLatestMatchesByTeam(teamName, 4);
            team.setMatches(matches);
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(team);
    }

    @GetMapping("/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return this.matchRepository.getMatchesByTeamBetweenDates(
                teamName,
                startDate,
                endDate);
    }
}
