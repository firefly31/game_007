package com.android.game_007;

import com.android.game_007.Player.Symbol;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {

	private GridView gridview;
	private static final int DEFAULT_GRID_SIZE = 3;
	private Player computerPlayer, humanPlayer;
	private GameController controller;
	static final String TAG = "game_007";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		computerPlayer = new ComputerPlayer(Symbol.CIRCLE);
		humanPlayer = new Player(Symbol.CROSS);

		gridview = (GridView) findViewById(R.id.gridview1);
		resetGame();
		gridview.setOnItemClickListener(new ClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showToast(getResources().getString(R.string.toast_new_game));
		switch (item.getItemId()) {
		case R.id.action_new_game:
			resetGame();
			break;
		default:
			break;
		}
		return true;
	}

	private void resetGame() {
		controller = GameController.getInstance(humanPlayer, computerPlayer,
				DEFAULT_GRID_SIZE);
		int size = DEFAULT_GRID_SIZE * DEFAULT_GRID_SIZE;
		String[] strings = new String[size];
		for (int i = 0; i < size; i++) {
			strings[i] = "";
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_layout, strings);
		gridview.invalidate();
		gridview.setNumColumns(DEFAULT_GRID_SIZE);
		gridview.setAdapter(adapter);
	}

	private boolean isGameOver() {
		States computerState = controller.board.getState(computerPlayer.symbol,
				humanPlayer.symbol);
		return computerState != States.PLAYING;
	}

	private boolean showWinToast() {
		States computerState = controller.board.getState(computerPlayer.symbol,
				humanPlayer.symbol);
		String message = null;
		switch (computerState) {
		case DRAW:
			message = getResources().getString(R.string.game_draw);
			break;
		case WIN:
			message = getResources().getString(R.string.game_loose);
			break;
		case LOSE:
			message = getResources().getString(R.string.game_win);
			break;
		default:
			return false;
		}
		showToast(message);
		return true;
	}

	private int getArrayPos(int x, int y) {
		return x * DEFAULT_GRID_SIZE + y;
	}

	private int[] getPosFromArrayIndex(int idx) {
		int[] out = new int[2];
		out[0] = idx / DEFAULT_GRID_SIZE;
		out[1] = idx % DEFAULT_GRID_SIZE;
		return out;
	}

	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, 100000).show();
	}

	private class ClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			if (isGameOver()) {
				return;
			}
			int[] pos = getPosFromArrayIndex(position);
			CellState state = controller.board.states[pos[0]][pos[1]];
			if (state.occupied) {
				return;
			}
			TextView tv = (TextView) view.findViewById(R.id.txt);
			updateView(tv, humanPlayer.symbol.toString());
			controller.board.setState(pos[0], pos[1], humanPlayer.symbol);
			if (!isGameOver()) {
				int[] nextPos = computerPlayer.getNextMove(controller.board,
						humanPlayer.symbol);
				if (nextPos != null) {
					int idx = getArrayPos(nextPos[0], nextPos[1]);
					tv = (TextView) adapterView.getChildAt(idx);
					updateView(tv, computerPlayer.symbol.toString());
				}
			}
			if (isGameOver()) {
				showWinToast();
			}
		}
		private void updateView(TextView textView, String text) {
			textView.setText(text);
			textView.setBackgroundColor(getResources().getColor(
					R.color.cell_color_selected));
		}

	}
}
