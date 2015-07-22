package com.android.game_007;

import android.util.Log;

import com.android.game_007.Player.Symbol;

public class Board {
	CellState[][] states;
	int dimension;
	private int count;

	private boolean isFull() {
		return count == dimension * dimension;
	}

	int getCount() {
		return count;
	}

	Board copy() {
		Board copy = new Board(dimension);
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				copy.states[i][j] = new CellState(states[i][j]);
			}
		}
		copy.count = count;
		return copy;
	}

	void printStates() {
		StringBuffer buffer;
		for (int i = 0; i < dimension; i++) {
			buffer = new StringBuffer();
			for (int j = 0; j < dimension; j++) {
				buffer.append(states[i][j] + ",");
			}
			Log.d(GameActivity.TAG, buffer.toString());
		}
	}

	public Board(int size) {
		states = new CellState[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				states[i][j] = new CellState(false, Symbol.CROSS);
			}
		}
		dimension = size;
	}

	public boolean setState(int x, int y, Symbol sym) {
		if (states[x][y].occupied)
			return false;

		states[x][y].symbol = sym;
		states[x][y].occupied = true;
		count++;
		return true;
	}

	boolean clearPosition(int x, int y) {
		states[x][y].occupied = false;
		count--;
		return true;
	}

	private boolean checkRow(Symbol symbol, int index) {
		for (int i = 0; i < dimension; i++) {
			if (!states[index][i].occupied || states[index][i].symbol != symbol) {
				return false;
			}
		}
		return true;
	}

	private boolean checkCol(Symbol symbol, int index) {
		for (int i = 0; i < dimension; i++) {
			if (!states[i][index].occupied || states[i][index].symbol != symbol) {
				return false;
			}
		}
		return true;
	}

	private boolean checkDiag(Symbol symbol) {
		int i = 0;
		for (; i < dimension; i++) {
			if (!states[i][i].occupied || states[i][i].symbol != symbol) {
				break;
			}
		}
		if (i == dimension) {
			return true;
		}
		for (i = 0; i < dimension; i++) {
			if (!states[i][dimension - i - 1].occupied
					|| states[i][dimension - i - 1].symbol != symbol) {
				break;
			}
		}
		return i == dimension;
	}

	States getState(Symbol player, Symbol other) {
		for (int i = 0; i < dimension; i++) {
			if (checkRow(player, i) || checkCol(player, i)) {
				return States.WIN;
			}
			if (checkRow(other, i) || checkCol(other, i)) {
				return States.LOSE;
			}
		}
		if (checkDiag(player)) {
			return States.WIN;
		}
		if (checkDiag(other)) {
			return States.LOSE;
		}
		if (isFull()) {
			return States.DRAW;
		}
		return States.PLAYING;
	}

}