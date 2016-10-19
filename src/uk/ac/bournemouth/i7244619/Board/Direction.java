package uk.ac.bournemouth.i7244619.Board;

import java.util.Random;

public enum Direction {
	LEFT, UP, RIGHT, DOWN;

	public Direction getOppositeDirection() {
		switch (this) {
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
			default:
				return null;
		}
	}

	public static Direction getRandomDirection(Random random) {

		switch (random.nextInt(4)) {

			case 0:
				return Direction.LEFT;
			case 1:
				return Direction.UP;
			case 2:
				return Direction.RIGHT;
			case 3:
				return Direction.DOWN;
			default:
				return null;
		}

	}
}
