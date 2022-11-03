package cameras;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Represents a single image captured by a Camera
 */
public class ScreenShot {

	private final Boolean[][] pixels; //the grid of pixels in the image

	/**
	 * Creates a ScreenShot with the given pixels
	 * @param pixels The grid of pixels
	 */
	private ScreenShot(Boolean[][] pixels) {
		this.pixels = pixels;
	}

	/**
	 * Returns the grid of pixels
	 * @return The grid of pixels
	 */
	public Boolean[][] getPixels() {
		return pixels;
	}

	/**
	 * Removes any entities from the image that appear to defy gravity
	 * @param s The ScreenShot to cleanse
	 * @return The cleansed ScreenShot
	 */
	public static ScreenShot removeFloat(ScreenShot s) {
		Boolean[][] newPixels = Arrays.stream(s.getPixels())
				.map(row -> {
					long countTrue = Arrays.stream(row).takeWhile(pix -> pix).count();
					return IntStream.range(0, row.length).mapToObj(i -> i < countTrue).toList()
							.toArray(Boolean[]::new);
				}).toList().toArray(Boolean[][]::new);
		return of(newPixels);
	}

	/**
	 * Checks if the given ScreenShots match after performing the specified row and column shifts
	 * @param before The original image
	 * @param after The new image
	 * @param rowShift The number of rows to shift by
	 * @param colShift The number of columns to shift by
	 * @return Whether the ScreenShots match after shifting
	 */
	private static boolean isShiftedBy(ScreenShot before, ScreenShot after, int rowShift, int colShift){
		return (rowShift != 0 || colShift != 0) && after.equals(shiftColBy(shiftRowBy(before, rowShift), colShift));
	}

	/**
	 * Checks if the given ScreenShots match after performing any possible row and columns shifts
	 * @param before The original image
	 * @param after The new image
	 * @return Whether the ScreenShots match after shifting
	 */
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

	/**
	 * Shifts the rows of the given ScreenShot by the given amount
	 * @param screenShot The ScreenShot to shift
	 * @param k The number of rows to shift by
	 * @return The shift ScreenShot
	 */
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

	/**
	 * Shifts the columns of the given ScreenShot by the given amount
	 * @param screenShot The ScreenShot to shift
	 * @param k The number of columns to shift by
	 * @return The shift ScreenShot
	 */
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

	/**
	 * Counts the number of differing pixels between the given ScreenShots
	 * @param before The original image
	 * @param after The new image
	 * @return The number of differing pixels
	 */
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

	/**
	 * Asserts that the given ScreenShot has no null values or rows with incorrect lengths
	 * @param s The ScreenShot to examine
	 */
	public static void validate(ScreenShot s){
		Boolean[][] pixels = s.pixels;

		Objects.requireNonNull(pixels);

		Arrays.stream(pixels).forEach(row -> Objects.requireNonNull(row, "Null row found in array!"));

		int rowLength = Arrays.stream(pixels).findAny().orElse(new Boolean[]{}).length;

		assert Arrays.stream(pixels).allMatch(row -> row.length == rowLength) : "Rows are not of same length!";
	}

	/**
	 * Creates a ScreenShot with the given pixels
	 * @param pixels The ScreenShot's pixels
	 * @return The new ScreenShot
	 */
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
