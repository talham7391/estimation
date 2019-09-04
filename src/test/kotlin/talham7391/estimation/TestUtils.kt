package talham7391.estimation

import kotlin.test.Test
import kotlin.test.assertEquals

class TestUtils {

    @Test fun testComputePlayerScore() {
        assertEquals(-1, Utils.computePlayerScore(4, 5))
        assertEquals(-1, Utils.computePlayerScore(4, 3))
        assertEquals(-4, Utils.computePlayerScore(3, 7))
        assertEquals(-3, Utils.computePlayerScore(3, 0))
        assertEquals(1, Utils.computePlayerScore(1, 1))
        assertEquals(3, Utils.computePlayerScore(3, 3))
        assertEquals(13, Utils.computePlayerScore(13, 13))
        assertEquals(-5, Utils.computePlayerScore(0, 5))
        assertEquals(13, Utils.computePlayerScore(0, 0))
    }
}