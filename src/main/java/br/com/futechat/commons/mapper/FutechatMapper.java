package br.com.futechat.commons.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballTransfersResponse;
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

}
