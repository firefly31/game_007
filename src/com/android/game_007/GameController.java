package com.android.game_007;

import com.android.game_007.Player.Symbol;

public class GameController {
	private Player[] players;
	Board board;

	private GameController(Player player1, Player player2, int dimension) {
		players = new Player[2];
		players[0] = player1;
		players[1] = player2;
		board = new Board(dimension);
	}

	public static GameController getInstance(Player player1, Player player2,
			int dimension) {
		if (player1.symbol.equals(player2.symbol)) {
			return null;
		}
		return new GameController(player1, player2, dimension);
	}

	public States getState(Player player) {
		Player other = getOtherPlayer(player);
		return board.getState(player.symbol, other.symbol);
	}

	Player getOtherPlayer(Player player) {
		return players[0].symbol == player.symbol ? players[1] : players[0];
	}
}

enum States {
	WIN, LOSE, DRAW, PLAYING,
}

class CellState {
	boolean occupied;
	Symbol symbol;

	public CellState(boolean occupied, Symbol symbol) {
		this.occupied = occupied;
		this.symbol = symbol;
	}

	public CellState(CellState cellState) {
		occupied = cellState.occupied;
		symbol = cellState.symbol;
	}

	public String toString() {
		if (occupied) {
			return symbol.toString();
		}
		return "EMPTY";
	}
}
