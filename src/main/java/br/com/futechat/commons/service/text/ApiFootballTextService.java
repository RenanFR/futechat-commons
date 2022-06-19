package br.com.futechat.commons.service.text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public String getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName) {
		StringBuilder finalTextLeagueTopScorers = new StringBuilder();
		finalTextLeagueTopScorers.append("Artilheiros da " + leagueName + " no ano " + seasonYear + ":\n");
		List<Pair<String, Integer>> topScorers = apiFootballService.getLeagueTopScorersForTheSeason(seasonYear,
				leagueName);
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
			soccerMatchText.append(match.homeTeam() + (match.homeScore() == null ? " "
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

}
