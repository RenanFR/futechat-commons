package br.com.futechat.commons.model;

import java.util.List;

public record PlayerTransferHistory(Player player, List<Transfer> transfers) {

}
