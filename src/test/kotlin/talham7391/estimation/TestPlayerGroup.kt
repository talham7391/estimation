import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestPlayerGroup {

    @Test fun testPlayerAfter() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val g = PlayerGroup(p1, p2, p3, p4)

        assertEquals(p2, g.playerAfter(p1))
        assertEquals(p3, g.playerAfter(p2))
        assertEquals(p4, g.playerAfter(p3))
        assertEquals(p1, g.playerAfter(p4))
        assertEquals(p2, g.playerAfter(p1))

        assertNotEquals(p1, g.playerAfter(p1))
        assertNotEquals(p3, g.playerAfter(p1))
        assertNotEquals(p4, g.playerAfter(p1))

        assertNotEquals(p1, g.playerAfter(p3))
        assertNotEquals(p2, g.playerAfter(p3))
        assertNotEquals(p3, g.playerAfter(p3))
    }
}