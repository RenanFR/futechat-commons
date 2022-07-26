package br.com.futechat.commons.service.text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.futechat.commons.exception.FixtureNotFoundException;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.SourceApi;

@Service(SourceApi.API_FOOTBALL_NAME)
public class ApiFootballTextService implements FutechatTextService {

    private static final String DATE_TIME_PATTERN = "MM/yyyy";

    private ApiFootballService apiFootballService;

    @Autowired
    public ApiFootballTextService(ApiFootballService apiFootballService) {
        this.apiFootballService = apiFootballService;
    }

    @Override
    public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
        return apiFootballService.getPlayerHeight(playerName, teamName, countryName);
    }

    @Override
    public String getPlayerTransferHistory(String playerName, String teamName) {
        StringBuilder finalTextWithTransferHistory = new StringBuilder();
        finalTextWithTransferHistory.append("Transferências do " + playerName + "\n");
        PlayerTransferHistory playerTransferHistory = apiFootballService.getPlayerTransferHistory(playerName, teamName);
        String transfersText = playerTransferHistory.transfers().stream()
                .map(transfer -> transfer.date().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) + ": "
                        + transfer.teamOut() + "->" + transfer.teamIn() + " (" + transfer.type() + ")")
                .collect(Collectors.joining("\n"));
        finalTextWithTransferHistory.append(transfersText);
        return finalTextWithTransferHistory.toString();
    }

    @Override
    public String getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName, String countryName) {
        StringBuilder finalTextLeagueTopScorers = new StringBuilder();
        finalTextLeagueTopScorers.append("Artilheiros da " + leagueName + " no ano " + seasonYear + ":\n");
        List<Pair<String, Integer>> topScorers = apiFootballService.getLeagueTopScorersForTheSeason(seasonYear,
                leagueName,
                countryName);
        Comparator<Pair<String, Integer>> pairComparator = (Pair<String, Integer> pairOne,
                                                            Pair<String, Integer> pairTwo) -> pairOne.getValue1().compareTo(pairTwo.getValue1());
        String goalScorerText = topScorers.stream().sorted(pairComparator.reversed())
                .map(scorerAndGoalsEntry -> scorerAndGoalsEntry.getValue0() + " -> " + scorerAndGoalsEntry.getValue1())
                .collect(Collectors.joining("\n"));
        finalTextLeagueTopScorers.append(goalScorerText);
        return finalTextLeagueTopScorers.toString();
    }

    @Override
    public String getSoccerMatches(Optional<String> leagueName, Optional<String> countryName, Optional<LocalDate> schedule) {
        List<Match> matches = apiFootballService.getSoccerMatches(leagueName, countryName, schedule);
        String soccerMatchesText = matches.stream().map(match -> {
            StringBuilder soccerMatchText = new StringBuilder();
            soccerMatchText.append(match.apiFootballId() + ": " + match.homeTeam() + (match.homeScore() == null ? " "
                    : "(" + match.homeScore() + ") ") + "X " + match.awayTeam() + (match.awayScore() == null ? " "
                    : "(" + match.awayScore() + ") "));
            soccerMatchText.append("-> " + match.schedule().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            List<String> matchInProgressStatusList = List.of("Second Half", "First Half, Kick Off", "Halftime",
                    "Second Half, 2nd Half Started", "Extra Time", "Penalty In Progress");
            if (matchInProgressStatusList.contains(match.status())) {
                soccerMatchText.append(" -> ");
                String matchEventListText = match.events().stream()
                        .map(event -> event.elapsedTime() + "/" + event.type()
                                + (event.playerName() == null ? "" : "/" + event.playerName()))
                        .collect(Collectors.joining(" | ", "[", "]"));
                soccerMatchText.append(matchEventListText);
            }
            return soccerMatchText.toString();
        }).reduce("Partidas encontradas:\n", (firstMatch, otherMatch) -> {
            return firstMatch + "\n" + otherMatch;
        });
        return soccerMatchesText;
    }

    @Override
    public String getFixtureStatistics(Integer idOfTheFixture) {
        try {
            Match fixtureStatisticsMatch = apiFootballService.getFixtureStatistics(idOfTheFixture);
            StringBuilder fixtureStatisticsText = new StringBuilder();
            fixtureStatisticsText
                    .append(fixtureStatisticsMatch.homeTeam() + " X "
                            + fixtureStatisticsMatch.awayTeam() + ":\n");
            fixtureStatisticsText.append("Chutes no gol | " + fixtureStatisticsMatch.homeTeamStatistics().shotsOnGoal()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().shotsOnGoal());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText
                    .append("Chutes fora do gol | " + fixtureStatisticsMatch.homeTeamStatistics().shotsOffGoal() + " | "
                            + fixtureStatisticsMatch.awayTeamStatistics().shotsOffGoal());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Total de chutes | " + fixtureStatisticsMatch.homeTeamStatistics().totalShots()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().totalShots());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Chutes bloqueados | " + fixtureStatisticsMatch.homeTeamStatistics().blockedShots()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().blockedShots());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText
                    .append("Finalizações dentro | " + fixtureStatisticsMatch.homeTeamStatistics().shotsInsidebox() + " | "
                            + fixtureStatisticsMatch.awayTeamStatistics().shotsInsidebox());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText
                    .append("Finalizações fora | " + fixtureStatisticsMatch.homeTeamStatistics().shotsOutsidebox() + " | "
                            + fixtureStatisticsMatch.awayTeamStatistics().shotsOutsidebox());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Faltas | " + fixtureStatisticsMatch.homeTeamStatistics().fouls() + " | "
                    + fixtureStatisticsMatch.awayTeamStatistics().fouls());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Escanteios | " + fixtureStatisticsMatch.homeTeamStatistics().cornerKicks() + " | "
                    + fixtureStatisticsMatch.awayTeamStatistics().cornerKicks());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Impedimentos | " + fixtureStatisticsMatch.homeTeamStatistics().offsides() + " | "
                    + fixtureStatisticsMatch.awayTeamStatistics().offsides());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Posse de bola | " + fixtureStatisticsMatch.homeTeamStatistics().ballPossession()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().ballPossession());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Cartões amarelos | " + fixtureStatisticsMatch.homeTeamStatistics().yellowCards()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().yellowCards());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Cartões vermelhos | " + fixtureStatisticsMatch.homeTeamStatistics().redCards()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().redCards());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText
                    .append("Defesas do goleiro | " + fixtureStatisticsMatch.homeTeamStatistics().goalkeeperSaves() + " | "
                            + fixtureStatisticsMatch.awayTeamStatistics().goalkeeperSaves());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Passes totais | " + fixtureStatisticsMatch.homeTeamStatistics().totalPasses()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().totalPasses());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append("Passes certos | " + fixtureStatisticsMatch.homeTeamStatistics().passesAccurate()
                    + " | " + fixtureStatisticsMatch.awayTeamStatistics().passesAccurate());
            fixtureStatisticsText.append("\n");
            fixtureStatisticsText.append(
                    "Porcentagem de passes certos | " + fixtureStatisticsMatch.homeTeamStatistics().passesPercentage()
                            + " | " + fixtureStatisticsMatch.awayTeamStatistics().passesPercentage());
            return fixtureStatisticsText.toString();
        } catch (FixtureNotFoundException fixtureNotFoundException) {
            return fixtureNotFoundException.getMessage();
        }
    }

}
