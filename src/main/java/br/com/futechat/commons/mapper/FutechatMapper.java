package br.com.futechat.commons.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.futechat.commons.api.model.ApiFootballLeague;
import br.com.futechat.commons.api.model.ApiFootballLeagueResponse;
import br.com.futechat.commons.api.model.ApiFootballPlayer;
import br.com.futechat.commons.api.model.ApiFootballPlayersResponse;
import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballTeam;
import br.com.futechat.commons.api.model.ApiFootballTeamsResponse;
import br.com.futechat.commons.api.model.ApiFootballTransfersResponse;
import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.model.Team;
import br.com.futechat.commons.model.Transfer;

@Mapper(componentModel = "spring")
public abstract class FutechatMapper {

	@Mapping(target = "player.name", expression = "java(apiFootballResponse.response().get(0).player().name())")
	@Mapping(target = "player.apiFootballId", expression = "java(apiFootballResponse.response().get(0).player().id())")
	@Mapping(target = "transfers", source = "response", qualifiedByName = "fromApiFootballTransfersResponseListToTransferList")
	public abstract PlayerTransferHistory fromApiFootballTransfersResponseToPlayerTransferHistory(
			ApiFootballResponse<ApiFootballTransfersResponse> apiFootballResponse);
	
	@Named("fromApiFootballTransfersResponseListToTransferList")
	List<Transfer> fromApiFootballTransfersResponseListToTransferList(
			List<ApiFootballTransfersResponse> apiFootballTransfersResponseList) {
		List<Transfer> transfers = apiFootballTransfersResponseList.get(0).transfers().stream()
				.map(apiFootballTransfer -> new Transfer(apiFootballTransfer.date(), apiFootballTransfer.type(),
						apiFootballTransfer.teamsInvolved().get("in").name(),
						apiFootballTransfer.teamsInvolved().get("out").name()))
				.collect(Collectors.toList());
		return transfers;
	}
	
	@Mapping(source = "player.id", target = "apiFootballId")
	@Mapping(source = "player.birth.place", target = "placeOfBirth")
	@Mapping(source = "player.birth.country", target = "countryOfBirth")
	@Mapping(target = "birth", expression="java(java.time.LocalDate.parse(player.birth().date(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "team.id", target = "team.apiFootballId")
	@Mapping(target = "team.id", ignore = true)
	@Mapping(source = "team.name", target = "team.name")
	@Mapping(source = "player.name", target = "name")
	public abstract Player fromApiFootballPlayerAndTeamToPlayer(ApiFootballPlayer player, ApiFootballTeam team);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "id", target = "apiFootballId")
	@Mapping(target = "birth", expression="java(java.time.LocalDate.parse(player.birth().date(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
	@Mapping(source = "player.birth.place", target = "placeOfBirth")
	@Mapping(source = "player.birth.country", target = "countryOfBirth")
	public abstract Player fromApiFootballPlayerToPlayer(ApiFootballPlayer player);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "player.id", target = "apiFootballId")
	@Mapping(source = "player.height", target = "height")
	@Mapping(source = "player.nationality", target = "nationality")
	@Mapping(source = "player.name", target = "name")
	@Mapping(source = "player.photo", target = "photo")
	@Mapping(source = "player.weight", target = "weight")
	@Mapping(target = "birth", expression="java(java.time.LocalDate.parse(response.player().birth().date(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
	@Mapping(source = "player.birth.place", target = "placeOfBirth")
	@Mapping(source = "player.birth.country", target = "countryOfBirth")
	@Mapping(target = "team.name", expression="java(apiFootballPlayersResponse.statistics().get(0).team().name())")
	@Mapping(target = "team.apiFootballId", expression="java(apiFootballPlayersResponse.statistics().get(0).team().id())")
	@Mapping(target = "team.logo", expression="java(apiFootballPlayersResponse.statistics().get(0).team().logo())")
	public abstract Player fromApiFootballPlayersResponseToPlayer(ApiFootballPlayersResponse response);
	
	public abstract PlayerEntity fromPlayerToPlayerEntity(Player player);
	
	public abstract List<PlayerEntity> fromPlayerToPlayerEntityList(List<Player> players);
	
	public abstract List<Player> fromPlayerEntityToPlayerList(List<PlayerEntity> entities);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "id", target = "apiFootballId")
	public abstract League fromApiFootballLeagueToLeague(ApiFootballLeague apiFootballLeague);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "league.id", target = "apiFootballId")
	@Mapping(source = "country.name", target = "country")
	@Mapping(source = "league.name", target = "name")
	@Mapping(source = "league.type", target = "type")
	@Mapping(source = "league.logo", target = "logo")
	public abstract League fromApiFootballLeagueResponseToLeague(ApiFootballLeagueResponse apiFootballLeagueResponse);
	
	@Mapping(target = "id", ignore = true)
	public abstract LeagueEntity fromLeagueToLeagueEntity(League league);
	
	public abstract List<LeagueEntity> fromLeagueToLeagueEntityList(List<League> leagueList);
	
	public abstract League fromLeagueEntityToLeague(LeagueEntity leagueEntity);
	
	public abstract List<League> fromLeagueEntityToLeagueList(List<LeagueEntity> list);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "team.id", target = "apiFootballId")
	@Mapping(source = "team.name", target = "name")
	@Mapping(source = "team.code", target = "code")
	@Mapping(source = "team.country", target = "country")
	@Mapping(source = "team.logo", target = "logo")
	@Mapping(source = "team.founded", target = "founded")
	@Mapping(source = "venue.name", target = "stadium")
	@Mapping(source = "venue.capacity", target = "stadiumCapacity")
	@Mapping(source = "venue.image", target = "stadiumImage")
	@Mapping(source = "venue.id", target = "stadiumApiFootballId")
	@Mapping(source = "venue.address", target = "stadiumAddress")
	public abstract Team fromApiFootballTeamsResponseToTeam(ApiFootballTeamsResponse apiFootballTeamsResponse);
	
	public abstract List<Team> fromApiFootballTeamsResponseToTeamList(List<ApiFootballTeamsResponse> apiFootballTeamsResponse);
	
	@Mapping(target = "league.teams", ignore = true)
	public abstract Team fromTeamEntityToTeam(TeamEntity teamEntity);
	
	public abstract List<Team> fromTeamEntityToTeamList(List<TeamEntity> list);
	
	public abstract TeamEntity fromTeamToTeamEntity(Team team);
	
	public abstract List<TeamEntity> fromTeamToTeamEntityList(List<Team> list);
}
