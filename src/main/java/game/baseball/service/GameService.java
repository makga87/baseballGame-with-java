package game.baseball.service;

import java.util.ArrayList;
import java.util.List;

import game.baseball.data.BaseballNoGenerator;
import game.baseball.domain.Attacker;
import game.baseball.domain.BaseballGame;
import game.baseball.domain.Defender;
import game.baseball.domain.GameResult;
import game.baseball.domain.Umpire;
import game.baseball.view.InputView;
import game.baseball.view.ResultView;

public class GameService {

	private BaseballGame baseballGame;

	private Attacker attacker;
	private Defender defender;
	private Umpire umpire;

	private final int NO_LENGTH_LIMIT = 3;

	private GameService(BaseballGame baseballGame) {
		this.baseballGame = baseballGame;
		initializePlayer();
	}

	public static GameService of(BaseballGame baseballGame) {
		return new GameService(baseballGame);
	}

	public void gameStart() {

		while (baseballGame.isKeepGoing()) {

			attacker.shoot(InputView.inputNo());

			GameResult gameResult = umpire.judge(attacker.getAttackNos(), defender.getDefenceNos());

			ResultView.print(gameResult);

			goingToKeepGoing(gameResult);
		}
	}

	private void initializePlayer() {
		this.attacker = Attacker.init();
		this.defender = Defender.from(generateNo());
		this.umpire = Umpire.init();
	}

	private BaseballNoGenerator generateNo() {
		return () -> {
			List<Integer> baseballNos = new ArrayList();
			while (baseballNos.size() < NO_LENGTH_LIMIT) {
				int baseballNo = (int) ((Math.random() * 9) + 1);
				if (!baseballNos.contains(baseballNo)) {
					baseballNos.add(baseballNo);
				}
			}
			return baseballNos;
		};
	}

	private void goingToKeepGoing(GameResult gameResult) {
		if (gameResult.isOut(NO_LENGTH_LIMIT)) {
			baseballGame.keepGoing(InputView.inputWhetherToContinue());
			initializePlayer();
		}
	}
}
