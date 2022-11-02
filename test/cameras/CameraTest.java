package cameras;

import org.junit.*;
import static org.junit.Assert.*;

public class CameraTest {
	@Test
	public void testCameraBuilding() {
		Camera.getBuilder()
				.setScreenShot(ScreenShot.of(
						new Boolean[][]{{true, false, false},
								{true, true, false},
								{true, true, true}})).setSide(true).build();
	}

	@Test(expected = NullPointerException.class)
	public void testDegenCameraBuilding() {
		Camera.getBuilder()
				.setScreenShot(null)
				.setSide(null).build();
	}

	@Test(expected = NullPointerException.class)
	public void testUntouchedCameraBuilding() {
		Camera.getBuilder().build();Camera.getBuilder().build();
	}

	@Test
	public void testCameraAddNormal() {
		Camera sideCam = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{{true, false, false},
						{true, true, false},
						{true, true, true}})).setSide(true).build();
		try {
			sideCam.addData(ScreenShot.of(
					new Boolean[][]{{true, false, false},
							{true, true, false},
							{true, true, true}}));
			sideCam.addData(ScreenShot.of(
					new Boolean[][]{{true, true, false},
							{true, true, true},
							{false, false, false}}));
		}
		catch(Exception e) {
			fail("Error: New Screenshot be added smoothly!");
		}
	}

	@Test(expected = Camera.ChangeDetectedException.class)
	public void testCameraAddFail() throws Camera.ChangeDetectedException, Camera.CameraShiftedException {
		Camera sideCam = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{{true, false, false},
						{true, true, false},
						{true, true, true}})).setSide(true).build();
		sideCam.addData(ScreenShot.of(
				new Boolean[][]{{true, true, false},
						{true, true, false},
						{true, true, true}}));
		sideCam.addData(ScreenShot.of(
				new Boolean[][]{{true, true, false},
						{true, true, false},
						{true, true, true}}));
		//fail("Error: New Screenshot should not be added smoothly!");
	}

}
