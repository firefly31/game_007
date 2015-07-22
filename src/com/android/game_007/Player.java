package com.android.game_007;

import android.util.Log;

public class Player {
	Symbol symbol;

	public int[] getNextMove(Board board, Symbol other) {
		return null;
	}

	public Player(Symbol sym) {
		symbol = sym;
	}

	enum Symbol {
		CROSS("X"), CIRCLE("O");
		private final String label;

		private Symbol(final String text) {
			this.label = text;
		}

		public String toString() {
			return label;
		}

	}
}

class ComputerPlayer extends Player {
	public ComputerPlayer(Symbol sym) {
		super(sym);
	}

	public int[] getNextMove(Board board, Symbol other) {
		Board newBoard = board.copy();
		Move move = findNextMove(newBoard, symbol, other, newBoard.getCount());
		Log.d(GameActivity.TAG, "Playing Move " + move);
		board.setState(move.x, move.y, symbol);
		return new int[] { move.x, move.y };
	}

	private class Move {
		States state;
		int x, y;

		public Move(States s, int x, int y) {
			this.state = s;
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return this.x + "," + this.y + " " + this.state;
		}
	}

	private Move findNextMove(Board board, Symbol symbol, Symbol other,
			int iteration) {
		Move lastDrawMove = null;
		board.printStates();
		for (int i = 0; i < board.dimension; i++) {
			for (int j = 0; j < board.dimension; j++) {
				if (board.setState(i, j, symbol)) {
					States state = board.getState(symbol, other);
					if (lastDrawMove == null) {
						lastDrawMove = new Move(state, i, j);
					}
					if (iteration == board.dimension * board.dimension - 1) {
						board.clearPosition(i, j);
						return new Move(state, i, j);
					}
					if (state == States.WIN) {
						board.clearPosition(i, j);
						return new Move(state, i, j);
					}
					if (state == States.LOSE) {
						board.clearPosition(i, j);
						continue;
					}
					if (state == States.DRAW) {
						board.clearPosition(i, j);
						lastDrawMove = new Move(States.DRAW, i, j);
						continue;
					}
					Move enemyMove = findNextMove(board, other, symbol,
							iteration + 1);
					board.clearPosition(i, j);
					if (enemyMove == null) {
						board.printStates();
						Log.d(GameActivity.TAG, "No Move found " + other);
						return null;
					}
					if (enemyMove.state == States.WIN) {
						continue;
					}
					if (enemyMove.state == States.LOSE) {
						return new Move(States.WIN, i, j);
					}
					if (enemyMove.state == States.DRAW) {
						lastDrawMove = new Move(States.DRAW, i, j);
						continue;
					}
					return new Move(enemyMove.state, i, j);
				}
			}
		}
		if (lastDrawMove != null && lastDrawMove.state == States.PLAYING) {
			lastDrawMove.state = States.LOSE;
		}
		return lastDrawMove;
	}
}