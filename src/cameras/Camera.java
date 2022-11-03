package cameras;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Camera watching the dock
 */
public class Camera {

	private final ScreenShot trueScreenshot; //the latest image captured by the camera
	private final List<ScreenShot> data; 	 //all images captured by the camera
	private final Boolean isSide; 			 //true if the camera faces the front or side of the cargo

	/**
	 * Creates a Camera with the given properties
	 * @param trueScreenshot The first image captured by the camera
	 * @param isSide True if the camera faces the front or side of the cargo
	 */
	private Camera(ScreenShot trueScreenshot, boolean isSide) {
		this.trueScreenshot = trueScreenshot;
		this.data = new ArrayList<>(List.of(trueScreenshot));
		this.isSide = isSide;
	}

	/**
	 * Returns true if the camera faces the front or side of the cargo
	 * @return True if the camera faces the front or side of the cargo
	 */
	public boolean isSide() {
		return isSide;
	}

	/**
	 * Returns the latest image captured by the camera
	 * @return The latest image captured by the camera
	 */
	public ScreenShot getTrueScreenshot() {
		return trueScreenshot;
	}

	/**
	 * Returns a list of all images captured by the camera
	 * @return A list of all images captured by the camera
	 */
	public List<ScreenShot> getData() {
		return new ArrayList<>(data);
	}

	/**
	 * Adds the given ScreenShot to Camera's list of data
	 * @param newS The new ScreenShot to add
	 * @throws ChangeDetectedException If a container movement is detected in the new ScreenShot
	 * @throws CameraShiftedException If a camera shift is detected in the new ScreenShot
	 */
	public void addData(ScreenShot newS) throws ChangeDetectedException, CameraShiftedException {
		ScreenShot filteredS = newS;

		if(isSide()) {
			filteredS = ScreenShot.removeFloat(filteredS);
		}

		if(ScreenShot.isShifted(trueScreenshot, filteredS)){
			throw new CameraShiftedException();
		}

		if(ScreenShot.countDifferences(trueScreenshot, filteredS) > 0) {
			throw Camera.getExceptionBuilder()
					.setBefore(trueScreenshot)
					.setAfter(filteredS)
					.build();
		}

		data.add(newS);
	}

	/**
	 * Creates a new Camera builder object
	 * @return A new Camera builder object
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Mutable object used to initialize a Camera
	 */
	public static final class Builder {

		private ScreenShot data = null; //the first image captures by the camera
		private Boolean isSide = null;  //true if the camera faces the front or side of the cargo

		/**
		 * Sets the first image captures by the camera
		 * @param data The first image captures by the camera
		 * @return Self
		 */
		public Builder setScreenShot(ScreenShot data) {
			ScreenShot.validate(data);
			this.data = data;
			return this;
		}

		/**
		 * Sets whether the camera faces the front/side of the cargo or the top
		 * @param side Whether the camera faces the front/side of the cargo or the top
		 * @return Self
		 */
		public Builder setSide(Boolean side) {
			isSide = side;
			return this;
		}

		/**
		 * Creates a new Camera from the builder object's data
		 * @return The new Camera
		 */
		public Camera build() {
			Objects.requireNonNull(data);
			Objects.requireNonNull(isSide);
			return new Camera(data, isSide);
		}

	}

	/**
	 * Creates a new ChangeDetectedException builder object
	 * @return The builder object
	 */
	static ChangeDetectedException.Builder getExceptionBuilder() {
		return new ChangeDetectedException.Builder();
	}

	/**
	 * Thrown when a container movement is detected
	 */
	public static final class ChangeDetectedException extends Exception {

		private final ScreenShot before; //the image before the movement occurred
		private final ScreenShot after;  //the image after the movement occurred

		/**
		 * Creates a ChangeDetectedException with the given properties
		 * @param before The image before the movement occurred
		 * @param after The image after the movement occurred
		 */
		private ChangeDetectedException(ScreenShot before, ScreenShot after) {
			super("Error: changes detected from the data: \n" + before.toString() + "\n to " + after.toString() + "\n");
			this.before = before;
			this.after = after;
		}

		/**
		 * Returns the image before the movement occurred
		 * @return The image before the movement occurred
		 */
		public ScreenShot getBefore() {
			return before;
		}

		/**
		 * Returns the image after the movement occurred
		 * @return The image after the movement occurred
		 */
		public ScreenShot getAfter() {
			return after;
		}

		/**
		 * Mutable builder object used to initialize a ChangeDetectedException
		 */
		static final class Builder {

			private ScreenShot before; //the image before the movement occurred
			private ScreenShot after;  //the image after the movement occurred

			/**
			 * Sets the image before the movement occurred
			 * @param before The image before the movement occurred
			 * @return Self
			 */
			public Builder setBefore(ScreenShot before) {
				this.before = before;
				return this;
			}

			/**
			 * Sets the image after the movement occurred
			 * @param after The image after the movement occurred
			 * @return Self
			 */
			public Builder setAfter(ScreenShot after) {
				this.after = after;
				return this;
			}

			/**
			 * Creates a new ChangeDetectedException from the builder object's data
			 * @return The new ChangeDetectedException
			 */
			public ChangeDetectedException build() {
				return new ChangeDetectedException(this.after, this.before);
			}

		}

	}

	/**
	 * Thrown when a camera shift is detected
	 */
	public static final class CameraShiftedException extends Exception {

		/**
		 * Creates a CameraShiftedException
		 */
		public CameraShiftedException() {
			super("Error: camera shift detected from the data, please fix immediately");
		}

	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Camera) {
			return this.getTrueScreenshot().equals(((Camera) other).getTrueScreenshot())
					&& this.isSide() == ((Camera) other).isSide();
		}
		else return false;
	}

	@Override
	public String toString() {
		return String.format("Camera {side = %s, current = %s}", isSide, trueScreenshot);
	}

}
