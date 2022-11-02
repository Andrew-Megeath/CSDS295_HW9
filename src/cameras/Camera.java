package cameras;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Camera {

	private final ScreenShot trueScreenshot;
	private final List<ScreenShot> data;
	private final Boolean isSide;

	private Camera(ScreenShot trueScreenshot, boolean isSide) {
		this.trueScreenshot = trueScreenshot;
		this.data = new ArrayList<>(List.of(trueScreenshot));
		this.isSide = isSide;
	}

	public boolean isSide() {
		return isSide;
	}

	public ScreenShot getTrueScreenshot() {
		return trueScreenshot;
	}

	public List<ScreenShot> getData() {
		return new ArrayList<>(data);
	}

	public void addData(ScreenShot newS) throws ChangeDetectedException, CameraShiftedException {
		ScreenShot filteredS = newS;

		if(isSide()) {
			filteredS = ScreenShot.removeFloat(newS);
		} else if(ScreenShot.isShifted(trueScreenshot, filteredS)){
			throw new CameraShiftedException();
		}

		if(ScreenShot.countDifferences(trueScreenshot, filteredS) > 0) {
			throw Camera.getExceptionBuilder()
					.setBefore(trueScreenshot)
					.setAfter(newS)
					.build();
		}

		data.add(newS);
	}

	public static Builder getBuilder() {
		return new Builder();
	}

	public static final class Builder {
		private ScreenShot data = null;
		private Boolean isSide = null;

		public Builder setScreenShot(ScreenShot data) {
			ScreenShot.validate(data);
			this.data = data;
			return this;
		}

		public Builder setSide(Boolean side) {
			isSide = side;
			return this;
		}

		public Camera build() {
			Objects.requireNonNull(data);
			Objects.requireNonNull(isSide);
			return new Camera(data, isSide);
		}
	}

	static ChangeDetectedException.Builder getExceptionBuilder() {
		return new ChangeDetectedException.Builder();
	}

	public static final class CameraShiftedException extends Exception {

		public CameraShiftedException() {
			super("Error: camera shift detected from the data");
		}

	}

	public static final class ChangeDetectedException extends Exception {

		private final ScreenShot before;
		private final ScreenShot after;

		private ChangeDetectedException(ScreenShot before, ScreenShot after) {
			super("Error: changes detected from the data: \n" + before.toString() + "\n to " + after.toString() + "\n");
			this.before = before;
			this.after = after;
		}

		public ScreenShot getBefore() {
			return before;
		}

		public ScreenShot getAfter() {
			return after;
		}

		static final class Builder {

			private ScreenShot before;
			private ScreenShot after;

			public Builder setBefore(ScreenShot before) {
				this.before = before;
				return this;
			}

			public Builder setAfter(ScreenShot after) {
				this.after = after;
				return this;
			}

			public ChangeDetectedException build() {
				return new ChangeDetectedException(this.after, this.before);
			}
		}

	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Camera) {
			return this.getTrueScreenshot() == ((Camera) other).getTrueScreenshot()
					&& this.isSide() == ((Camera) other).isSide();
		}
		else return false;
	}

}
