package com.sns.ipldashboard.controller;

import com.sns.ipldashboard.model.Match;
import com.sns.ipldashboard.model.Team;
import com.sns.ipldashboard.repository.MatchRepository;
import com.sns.ipldashboard.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("team")
@CrossOrigin
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

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
}
