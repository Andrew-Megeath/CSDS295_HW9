package cameras;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CameraSet {

	private final Camera frontCam;
	private final Camera sideCam;
	private final Camera topCam;

	private CameraSet(Camera frontCam, Camera sideCam, Camera topCam) {
		this.frontCam = frontCam;
		this.sideCam = sideCam;
		this.topCam = topCam;
	}

	public Camera getFrontCam() {
		return frontCam;
	}

	public Camera getSideCam() {
		return sideCam;
	}

	public Camera getTopCam() {
		return topCam;
	}

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

	public static Builder getBuilder() {
		return new Builder();
	}

	public static final class Builder {

		private int[][] packages;
		private Integer height;

		public Builder setHeight(int height) {
			if(height < 0){
				throw new IllegalArgumentException("Height cannot be negative");
			}
			this.height = height;
			return this;
		}

		public Builder setPackages(int[][] packages) {
			validate(packages);
			this.packages = packages;
			return this;
		}

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

		private void verifyPackageStackInRange(int stackHeight) {
			if(stackHeight < 0){
				throw new IllegalArgumentException("Container stack heights cannot be negative");
			}else if(stackHeight > height){
				throw new IllegalArgumentException("Container stack heights cannot exceed the maximum height");
			}
		}

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
