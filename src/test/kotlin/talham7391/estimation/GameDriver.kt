package talham7391.estimation

class GameDriver {
    private val p1 = Player()
    private val p2 = Player()
    private val p3 = Player()
    private val p4 = Player()
    private val players = listOf(p1, p2, p3, p4)
    private val group = PlayerGroup(p1, p2, p3, p4)
    private val game = Estimation(group)
}