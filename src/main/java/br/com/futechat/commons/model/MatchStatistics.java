package br.com.futechat.commons.model;

public record MatchStatistics(Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots, Integer blockedShots,
		Integer shotsInsidebox, Integer shotsOutsidebox, Integer fouls, Integer cornerKicks, Integer offsides,
		String ballPossession, Integer yellowCards, Integer redCards, Integer goalkeeperSaves, Integer totalPasses,
		Integer passesAccurate, String passesPercentage) {
}