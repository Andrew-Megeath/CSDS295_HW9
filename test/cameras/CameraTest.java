package cameras;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class CameraTest {

	@Test
	public void testCameraBuilding_normal() {
		ScreenShot s = ScreenShot.of(
				new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}});
		Camera cam = Camera.getBuilder().setScreenShot(s).setSide(true).build();
		assertTrue(cam.isSide());
		assertEquals(s, cam.getTrueScreenshot());
		assertEquals(List.of(s), cam.getData());
	}

	@Test(expected = NullPointerException.class)
	public void testCameraBuilding_degenerate() {
		Camera.getBuilder().setScreenShot(null).setSide(null).build();
	}

	@Test(expected = NullPointerException.class)
	public void testCameraBuilding_untouched() {
		Camera.getBuilder().build();
	}

	@Test
	public void testAddData_normal() throws Camera.ChangeDetectedException, Camera.CameraShiftedException {
		Camera sideCam = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}})).setSide(true).build();
		ScreenShot newS = ScreenShot.of(
				new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}});
		sideCam.addData(newS);
		assertEquals(newS, sideCam.getTrueScreenshot());
	}

	@Test
	public void testAddData_floatingPackage() throws Camera.ChangeDetectedException, Camera.CameraShiftedException {
		ScreenShot firstS = ScreenShot.of(new Boolean[][]{
				{true, false, false},
				{true, true, false},
				{true, true, true}});
		Camera sideCam = Camera.getBuilder().setScreenShot(firstS).setSide(true).build();
		sideCam.addData(ScreenShot.of(
				new Boolean[][]{
						{true, false, true},
						{true, true, false},
						{true, true, true}}));
		assertEquals(firstS, sideCam.getTrueScreenshot());
	}

	@Test(expected = Camera.CameraShiftedException.class)
	public void testAddData_cameraShift() throws Camera.ChangeDetectedException, Camera.CameraShiftedException {
		Camera sideCam = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}})).setSide(true).build();
		sideCam.addData(ScreenShot.of(
				new Boolean[][]{
						{true, true, false},
						{true, true, true},
						{false, false, false}}));
	}

	@Test(expected = Camera.ChangeDetectedException.class)
	public void testAddData_packagesMoved() throws Camera.ChangeDetectedException, Camera.CameraShiftedException {
		Camera sideCam = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}})).setSide(true).build();
		sideCam.addData(ScreenShot.of(
				new Boolean[][]{
						{true, true, false},
						{true, true, false},
						{true, true, true}}));
	}

	@Test
	public void testEquals_equal() {
		Camera cam1 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, true}})).setSide(true).build();
		Camera cam2 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, true}})).setSide(true).build();
		assertEquals(cam1, cam2);
	}

	@Test
	public void testEquals_differentSides() {
		Camera cam1 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, true}})).setSide(true).build();
		Camera cam2 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, true}})).setSide(false).build();
		assertNotEquals(cam1, cam2);
	}

	@Test
	public void testEquals_differentScreenShots() {
		Camera cam1 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, true}})).setSide(true).build();
		Camera cam2 = Camera.getBuilder().setScreenShot(ScreenShot.of(
				new Boolean[][]{
						{true, false},
						{true, false}})).setSide(true).build();
		assertNotEquals(cam1, cam2);
	}

}
