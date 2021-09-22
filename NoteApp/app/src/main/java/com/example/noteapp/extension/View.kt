import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

private const val FADE_DURATION = 500L
private const val ALPHA_VISIBLE = 1.0f
private const val ALPHA_GONE = 0.0f
private const val ZERO_MARGIN = 0

private val accelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.fadeOut() {
    animate()
        .alpha(ALPHA_GONE)
        .setDuration(FADE_DURATION)
        .setInterpolator(accelerateDecelerateInterpolator)
        .withEndAction {
            makeGone()
        }
}

fun View.fadeIn() {
    animate()
        .alpha(ALPHA_VISIBLE)
        .setDuration(FADE_DURATION)
        .setInterpolator(accelerateDecelerateInterpolator)
        .withEndAction {
            makeVisible()
        }
}

fun View.getDrawable(@DrawableRes drawableResId: Int): Drawable? =
    context.getDrawable(drawableResId)

fun View.setZeroMargins() =
    (layoutParams as? ViewGroup.MarginLayoutParams)
        ?.apply {
            setMargins(
                ZERO_MARGIN,
                ZERO_MARGIN,
                ZERO_MARGIN,
                ZERO_MARGIN
            )
        }?.also {
            layoutParams = it
        }
