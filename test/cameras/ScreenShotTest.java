package cameras;

import org.junit.*;
import static org.junit.Assert.*;

public class ScreenShotTest {

	@Test
	public void testShiftColBy() {
		ScreenShot s = ScreenShot.of(
				new Boolean[][]{
						{true, true, true},
						{true, false, false},
						{true, true, false}});
		assertEquals(ScreenShot.of(
				new Boolean[][]{
						{false, true, true},
						{false, true, false},
						{false, true, true}}), ScreenShot.shiftColBy(s, 1));
	}

	@Test
	public void testShiftRowBy() {
		ScreenShot s = ScreenShot.of(
				new Boolean[][]{
						{true, true, true},
						{true, false, false},
						{true, true, false}});
		assertEquals(ScreenShot.of(
				new Boolean[][]{
						{false, false, false},
						{true, true, true},
						{true, false, false}}), ScreenShot.shiftRowBy(s, 1));
	}

	@Test
	public void testIsShifted_noChange(){
		assertFalse("Error: Should be False", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, true, false},
						{true, true, true},
						{false, false, true}})));
	}

	@Test
	public void testIsShifted_packagesMoved() {
		assertFalse("Error: Should be False", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false},
						{true, true, false},
						{true, true, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, true, false},
						{true, true, true},
						{false, false, true}})));
	}

	@Test
	public void testIsShifted_colShiftLeft() {
		assertTrue("Error: Should be True", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false, false, false},
						{true, true, true, false, false},
						{true, true, true, true, false}}),
				ScreenShot.of(new Boolean[][]{
						{false, false, false, false, false},
						{true, false, false, false, false},
						{true, true, false, false, false}})));
	}

	@Test
	public void testIsShifted_colShiftRight() {
		assertTrue("Error: Should be True", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false, false, false},
						{true, true, true, false, false},
						{true, true, true, true, false}}),
				ScreenShot.of(new Boolean[][]{
						{false, false, false, false, true},
						{false, false, false, false, true},
						{false, false, false, false, true}})));
	}

	@Test
	public void testIsShifted_rowShiftDown() {
		assertTrue("Error: Should be True", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false, false},
						{true, true, true, false},
						{true, true, true, true},
						{false, true, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{false, false, false, false},
						{false, false, false, false},
						{true, false, false, false},
						{true, true, true, false}})));
	}

	@Test
	public void testIsShifted_rowShiftUp() {
		assertTrue("Error: Should be True", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false, false},
						{true, true, true, false},
						{true, true, true, true},
						{false, true, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, true, true, false},
						{true, true, true, true},
						{false, true, false, true},
						{false, false, false, false}})));
	}

	@Test
	public void testIsShifted_rowAndColShift(){
		assertTrue("Error: Should be True", ScreenShot.isShifted(
				ScreenShot.of(new Boolean[][]{
						{true, false, false, false},
						{false, true, true, false},
						{true, true, true, false},
						{false, true, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, true, false, false},
						{true, true, false, false},
						{true, false, true, false},
						{false, false, false, false}})));
	}

	@Test
	public void testRemoveFloat_present() {
		ScreenShot s = ScreenShot.of(new Boolean[][]{
				{true, false, true},
				{true, true, false},
				{false, false, false}});
		assertEquals(ScreenShot.of(new Boolean[][]{
				{true, false, false},
				{true, true, false},
				{false, false, false}}), ScreenShot.removeFloat(s));
	}

	@Test
	public void testRemoveFloat_notPresent() {
		ScreenShot s = ScreenShot.of(new Boolean[][]{
				{true, true, true},
				{true, true, false},
				{false, false, false}});
		assertEquals(ScreenShot.of(new Boolean[][]{
				{true, true, true},
				{true, true, false},
				{false, false, false}}), ScreenShot.removeFloat(s));
	}

	@Test
	public void testCountDifferences_identical() {
		assertEquals(0, ScreenShot.countDifferences(
				ScreenShot.of(new Boolean[][]{
						{true, true, true, false},
						{true, true, false, false},
						{false, false, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, true, true, false},
						{true, true, false, false},
						{false, false, false, true}})));
	}

	@Test
	public void testCountDifferences_someDifferent() {
		assertEquals(3, ScreenShot.countDifferences(
				ScreenShot.of(new Boolean[][]{
						{true, true, true, false},
						{true, true, false, false},
						{false, false, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{true, false, true, false},
						{true, true, true, false},
						{false, true, false, true}})));
	}

	@Test
	public void testCountDifferences_allDifferent() {
		assertEquals(12, ScreenShot.countDifferences(
				ScreenShot.of(new Boolean[][]{
						{true, true, true, false},
						{true, true, false, false},
						{false, false, false, true}}),
				ScreenShot.of(new Boolean[][]{
						{false, false, false, true},
						{false, false, true, true},
						{true, true, true, false}})));
	}

	@Test
	public void testEquals_equal(){
		ScreenShot s1 = ScreenShot.of(new Boolean[][]{
				{true, true},
				{true, false}});
		ScreenShot s2 = ScreenShot.of(new Boolean[][]{
				{true, true},
				{true, false}});
		assertEquals(s1, s2);
	}

	@Test
	public void testEquals_notEqual(){
		ScreenShot s1 = ScreenShot.of(new Boolean[][]{
				{true, true},
				{true, false}});
		ScreenShot s2 = ScreenShot.of(new Boolean[][]{
				{true, false},
				{true, false}});
		assertNotEquals(s1, s2);
	}

}
