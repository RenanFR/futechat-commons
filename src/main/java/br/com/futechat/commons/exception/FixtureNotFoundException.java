package br.com.futechat.commons.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FixtureNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String homeTeam;
    private String awayTeam;
    private LocalDate matchDate;

    public FixtureNotFoundException(String homeTeam, String awayTeam, LocalDate matchDate) {
        super("A partida entre " + homeTeam + " e " + awayTeam + " na data " + matchDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " nao foi encontrada, talvez nao esteja disponivel em nossa base, iremos trabalhar nisso");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
    }

}
