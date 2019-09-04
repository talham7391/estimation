package talham7391.estimation

import kotlin.math.abs

internal object Utils {

    fun computePlayerScore(target: Int, actual: Int): Int {
        var score = abs(target - actual) * -1
        if (score == 0) {
            score = if (target == 0) 13 else target
        }
        return score
    }
}