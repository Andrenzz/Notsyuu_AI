package id.notsyuu.assistant

import org.junit.Assert.*
import org.junit.Test

class BubbleGeometryTest {
    @Test fun snapsToNearestSafeEdge() { assertEquals(4,BubbleGeometry.snapX(30,56,393,4));assertEquals(333,BubbleGeometry.snapX(290,56,393,4)) }
    @Test fun tinyWindowNeverProducesNegativePosition() { assertEquals(0,BubbleGeometry.snapX(0,56,20,4)) }
    @Test fun dismissRequiresBothCoordinatesNearTarget() { assertTrue(BubbleGeometry.inDismissZone(190f,710f,195f,720f,55f));assertFalse(BubbleGeometry.inDismissZone(30f,720f,195f,720f,55f));assertFalse(BubbleGeometry.inDismissZone(195f,500f,195f,720f,55f)) }
}
