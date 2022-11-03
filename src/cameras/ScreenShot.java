/*
 * An example to demonstrate the testing of private methods by means of an inner class.
 */
package cameras;

/* import javax.annotation.Generated;
 * does not work with Cobertura due to annotation argument.
 * Use CoverageIgnore instead.
 */

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A class under test
 */
public class ScreenShot {

	private final Boolean[][] pixels;

	private ScreenShot(Boolean[][] pixels) {
		this.pixels = pixels;
	}

	public Boolean[][] getPixels() {
		return pixels;
	}

	public static ScreenShot removeFloat(ScreenShot s) {
		Boolean[][] newPixels = Arrays.stream(s.getPixels())
				.map(row -> {
					long countTrue = Arrays.stream(row).takeWhile(pix -> pix).count();
					return IntStream.range(0, row.length).mapToObj(i -> i < countTrue).toList()
							.toArray(Boolean[]::new);
				}).toList().toArray(Boolean[][]::new);
		return of(newPixels);
	}

	private static boolean isShiftedBy(ScreenShot before, ScreenShot after, int rowShift, int colShift){
		return (rowShift != 0 || colShift != 0) && after.equals(shiftColBy(shiftRowBy(before, rowShift), colShift));
	}

	public static boolean isShifted(ScreenShot before, ScreenShot after) {
		int rowCount = before.getPixels().length;
		int colCount = before.getPixels()[0].length;

		for(int i = -rowCount+1; i < rowCount; i++) {
			for(int j = -colCount+1; j < colCount; j++) {
				if(isShiftedBy(before, after, i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ScreenShot shiftRowBy(ScreenShot screenShot, int k) {
		return of(IntStream.range(0, screenShot.getPixels().length)
				.mapToObj(i -> {
					if(i - k >= 0 && i - k < screenShot.getPixels().length) {
						return Arrays.copyOf(screenShot.getPixels()[i - k], screenShot.getPixels()[i - k].length);
					}
					else {
						return IntStream.range(0, screenShot.getPixels()[0].length)
								.mapToObj(j -> false)
								.toArray(Boolean[]::new);
					}
				}).toArray(Boolean[][]::new));
	}

	public static ScreenShot shiftColBy(ScreenShot screenShot, int k) {
		return of(
				Arrays.stream(screenShot.getPixels())
						.map(row -> IntStream.range(0, row.length)
								.mapToObj(i -> {
									if(i - k >= 0 && i - k < row.length) {
										return row[i - k];
									}
									else return false;
								}).toArray(Boolean[]::new))
						.toArray(Boolean[][]::new));
	}

	public static int countDifferences(ScreenShot before, ScreenShot after) {
		int diffCount = 0;
		int rowCount = before.getPixels().length;
		int colCount = before.getPixels()[0].length;

		for(int i = 0; i < rowCount; i++) {
			for(int j = 0; j < colCount; j++) {
				if(!before.pixels[i][j].equals(after.pixels[i][j])){
					diffCount++;
				}
			}
		}

		return diffCount;
	}

	public static void validate(ScreenShot s){
		Boolean[][] pixels = s.pixels;

		Objects.requireNonNull(pixels);

		Arrays.stream(pixels).forEach(row -> Objects.requireNonNull(row, "Null row found in array!"));

		int rowLength = Arrays.stream(pixels).findAny().orElse(new Boolean[]{}).length;

		assert Arrays.stream(pixels).allMatch(row -> row.length == rowLength) : "Rows are not of same length!";
	}

	public static ScreenShot of(Boolean[][] pixels) {
		ScreenShot newScreenShot = new ScreenShot(pixels);
		try {
			validate(newScreenShot);
			return newScreenShot;
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof ScreenShot) {
			return Arrays.deepEquals(this.getPixels(), ((ScreenShot) other).getPixels());
		}
		else return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");

		for(int i = 0; i < pixels.length; i++) {
			for(int j = 0; j < pixels[0].length; j++) {
				sb.append(pixels[i][j] ? 'T' : 'F');
			}
			if(i < pixels.length - 1){
				sb.append("/");
			}
		}

		sb.append("}");
		return sb.toString();
	}

}
