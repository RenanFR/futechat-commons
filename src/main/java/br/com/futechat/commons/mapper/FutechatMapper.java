package br.com.futechat.commons.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.futechat.commons.api.model.ApiFootballPlayer;
import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballTeam;
import br.com.futechat.commons.api.model.ApiFootballTransfersResponse;
import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.model.Transfer;

@Mapper(componentModel = "spring")
public abstract class FutechatMapper {

	@Mapping(target = "player.name", expression = "java(apiFootballResponse.response().get(0).player().name())")
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
	@Mapping(source = "player.name", target = "name")
	@Mapping(source = "player.height", target = "height")
	@Mapping(source = "team.name", target = "team.name")
	@Mapping(source = "team.code", target = "team.code")
	@Mapping(source = "team.country", target = "team.country")
	@Mapping(source = "team.founded", target = "team.founded")
	@Mapping(source = "team.id", target = "team.apiFootballId")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "team.id", ignore = true)
	public abstract Player fromApiFootballPlayerAndTeamToPlayer(ApiFootballPlayer player, ApiFootballTeam team);
	
	public abstract PlayerEntity fromPlayerToPlayerEntity(Player player);
	
	public abstract List<Player> fromPlayerEntityToPlayerList(List<PlayerEntity> entities);

}
