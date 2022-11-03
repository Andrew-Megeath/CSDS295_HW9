package cameras;

import org.junit.*;
import static org.junit.Assert.*;

public class CameraSetTest {

	private CameraSet newSet;
	private Camera newFront;
	private Camera newSide;
	private Camera newTop;

	@Before
	public void init() {
		newSet = CameraSet.getBuilder()
				.setHeight(5).setPackages(new int[][]{
						{1, 2, 3, 4},
						{0, 4, 1, 5},
						{1, 2, 0, 1}}).build();
		ScreenShot frontShot = ScreenShot.of(
				new Boolean[][]{
						{true, false, false, false, false},
						{true, true, true, true, false},
						{true, true, true, false, false},
						{true, true, true, true, true}}
		);
		ScreenShot sideShot = ScreenShot.of(
				new Boolean[][]{
						{true, true, true, true, false},
						{true, true, true, true, true},
						{true, true, false, false, false}}
		);
		ScreenShot topShot = ScreenShot.of(
				new Boolean[][]{
						{true, true, true, true},
						{false, true, true, true},
						{true, true, false, true}}
		);
		newFront = Camera.getBuilder().setScreenShot(frontShot).setSide(true).build();
		newSide = Camera.getBuilder().setScreenShot(sideShot).setSide(true).build();
		newTop = Camera.getBuilder().setScreenShot(topShot).setSide(false).build();
	}



	@Test
	public void testCameraSetBuilding_frontCam() {
		assertEquals(newFront, newSet.getFrontCam());
	}

	@Test
	public void testCameraSetBuilding_sideCam() {
		assertEquals(newSide, newSet.getSideCam());
	}

	@Test
	public void testCameraSetBuilding_topCam() {
		assertEquals(newTop, newSet.getTopCam());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCameraSetBuilding_packagesEnteredBeforeHeight(){
		CameraSet.getBuilder().setPackages(new int[][]{{2}}).setHeight(2).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCameraSetBuilding_negativeHeight(){
		CameraSet.getBuilder().setHeight(-2).setPackages(new int[][]{{2}}).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCameraSetBuilding_negativeStackHeight(){
		CameraSet.getBuilder().setHeight(2).setPackages(new int[][]{
				{2, -3},
				{0, 1}}).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCameraSetBuilding_stackHeightExceedsMaxHeight(){
		CameraSet.getBuilder().setHeight(3).setPackages(new int[][]{
				{2, 3},
				{0, 4}}).build();
	}

	@Test
	public void testAddData_noChange() {
		newSet.addData(ScreenShot.of(
				new Boolean[][]{
						{true, false, false, false, false},
						{true, true, true, true, false},
						{true, true, true, false, false},
						{true, true, true, true, true}}
		), ScreenShot.of(
				new Boolean[][]{
						{true, true, true, true, false},
						{true, true, true, true, true},
						{true, true, false, false, false}}
		), ScreenShot.of(
				new Boolean[][]{
						{true, true, true, true},
						{false, true, true, true},
						{true, true, false, true}}
		));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddData_invalidScreenShot() {
		newSet.addData(ScreenShot.of(
				new Boolean[][]{
						{true, false, false, false, false},
						{true, true, true, true, false},
						{true, true, true, false, false},
						{true, true, true, true, true}}
		), ScreenShot.of(
				new Boolean[][]{
						{true, true, true, true, false},
						{true, true, true, true, true},
						{true, true, false, false, false}}
		), ScreenShot.of(
				new Boolean[][]{
						{true, true, true, false},
						{false, true, true, true},
						{true, true, false, true}}
		));
	}

}
