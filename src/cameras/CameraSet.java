package cameras;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents a group of Cameras that includes a front, side, and top Camera
 */
public class CameraSet {

	private final Camera frontCam; //the camera facing the front of the cargo
	private final Camera sideCam;  //the camera facing the side of the cargo
	private final Camera topCam;   //the camera facing the top of the cargo

	/**
	 * Creates a CameraSet with the given Cameras
	 * @param frontCam The Camera facing the front of the cargo
	 * @param sideCam The Camera facing the side of the cargo
	 * @param topCam The Camera facing the top of the cargo
	 */
	private CameraSet(Camera frontCam, Camera sideCam, Camera topCam) {
		this.frontCam = frontCam;
		this.sideCam = sideCam;
		this.topCam = topCam;
	}

	/**
	 * Returns the Camera facing the front of the cargo
	 * @return The Camera facing the front of the cargo
	 */
	public Camera getFrontCam() {
		return frontCam;
	}

	/**
	 * Returns the Camera facing the side of the cargo
	 * @return The Camera facing the side of the cargo
	 */
	public Camera getSideCam() {
		return sideCam;
	}

	/**
	 * Returns the Camera facing the top of the cargo
	 * @return The Camera facing the top of the cargo
	 */
	public Camera getTopCam() {
		return topCam;
	}

	/**
	 * Adds the given ScreenShots to the CameraSet's Camera
	 * @param frontShot The ScreenShot to add to the front Camera
	 * @param sideShot The ScreenShot to add to the side Camera
	 * @param topShot The ScreenShot to add to the top Camera
	 */
	public void addData(ScreenShot frontShot, ScreenShot sideShot, ScreenShot topShot) {
		try {
			getFrontCam().addData(frontShot);
			getSideCam().addData(sideShot);
			getTopCam().addData(topShot);
		}
		catch(Camera.ChangeDetectedException | Camera.CameraShiftedException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Creates a new CameraSet builder object
	 * @return The builder object
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Mutable builder object used to initialize a CameraSet
	 */
	public static final class Builder {

		private int[][] packages; //a grid containing the height of each package stack
		private Integer height;   //the height of the tallest package stack

		/**
		 * Sets the height of the tallest package stack
		 * @param height The height of the tallest package stack
		 * @return Self
		 */
		public Builder setHeight(int height) {
			if(height < 0){
				throw new IllegalArgumentException("Height cannot be negative");
			}
			this.height = height;
			return this;
		}

		/**
		 * Sets the package grid
		 * @param packages The package grid
		 * @return Self
		 */
		public Builder setPackages(int[][] packages) {
			validate(packages);
			this.packages = packages;
			return this;
		}

		/**
		 * Checks that heights of the package stacks are within the required range
		 * @param packages The package grid
		 */
		private void validate(int[][] packages) {
			if(height == null){
				throw new UnsupportedOperationException("Height must be entered before packages");
			}
			for(int[] aPackage : packages) {
				for(int stackHeight : aPackage) {
					verifyPackageStackInRange(stackHeight);
				}
			}
		}

		/**
		 * Checks that the given package stack height is within the required range
		 * @param stackHeight The package stack height to check
		 */
		private void verifyPackageStackInRange(int stackHeight) {
			if(stackHeight < 0){
				throw new IllegalArgumentException("Container stack heights cannot be negative");
			}else if(stackHeight > height){
				throw new IllegalArgumentException("Container stack heights cannot exceed the maximum height");
			}
		}

		/**
		 * Creates a new CameraSet from the builder object's data
		 * @return The new CameraSet
		 */
		public CameraSet build() {
			ScreenShot front = ScreenShot.of(IntStream.range(0, packages[0].length)
					.map(i -> Arrays.stream(packages)
							.mapToInt(row -> row[i])
							.max()
							.orElse(0))
					.mapToObj(count -> IntStream.range(0, height).mapToObj(i -> i < count).toArray(Boolean[]::new))
					.toArray(Boolean[][]::new));

			ScreenShot side = ScreenShot.of((Arrays.stream(packages)
					.mapToInt(arr -> Arrays.stream(arr).max().orElse(0)))
					.mapToObj(count -> IntStream.range(0, height).mapToObj(i -> i < count).toArray(Boolean[]::new))
					.toArray(Boolean[][]::new));

			ScreenShot top = ScreenShot.of(Arrays.stream(packages)
					.map(row -> Arrays.stream(row).mapToObj(i -> i > 0).toArray(Boolean[]::new))
					.toArray(Boolean[][]::new));

			return new CameraSet(
					Camera.getBuilder().setScreenShot(front).setSide(true ).build(),
					Camera.getBuilder().setScreenShot(side).setSide(true).build(),
					Camera.getBuilder().setScreenShot(top).setSide(false).build());
		}

	}

}
