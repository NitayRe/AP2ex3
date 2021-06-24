package view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import java.lang.Integer.min
import java.util.*
import kotlin.math.sqrt

/**
 * A generic joystick that can be swiped to different directions.
 */
class Joystick @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var borderRadius = 0f           // radius of the outer circle
    private var joystickRadius = 0f         // radius of the inner circle
    private var touchAreaRadius = 0f        // radius of the circle the joystick can move to
    private var center = PointF()           // center of the outer circle
    private var joystickCenter = PointF()   // center of the joystick (relative to center)

    // attributes
    private var borderColor = 0
    private var joystickColor = 0

    private val borderPaint = Paint()
    private val joystickPaint = Paint()

    // define interface for listener for joystick moved event
    public interface OnJoystickMovedListener {
        // this is called when joystick is moved
        // values of x and y are normalizes to range of -1 to 1
        public fun onJoystickMoved(x: Float, y: Float)
    }

    private var listener : OnJoystickMovedListener? = null

    init{
        // Load attributes
        context.withStyledAttributes(attrs, R.styleable.Joystick) {
            borderColor = getColor(R.styleable.Joystick_borderColor, 0)
            joystickColor = getColor(R.styleable.Joystick_JoystickColor, 0)
        }

        // init paint
        borderPaint.apply {
            style = Paint.Style.STROKE
            color = borderColor
            isAntiAlias = true
        }
        joystickPaint.apply {
            style = Paint.Style.FILL
            color = joystickColor
            isAntiAlias = true
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        borderRadius = 0.5f * min(width, height).toFloat()
        joystickRadius = 0.4f * borderRadius // joystick radius is 40% of the whole widget
        touchAreaRadius = borderRadius - joystickRadius
        center = PointF(width/2.0f, height/2.0f)
        joystickCenter = PointF()
    }

    override fun onDraw(canvas: Canvas) {
        // draw both the outer and inner circle
        canvas.drawCircle(center.x, center.y, borderRadius, borderPaint)
        canvas.drawCircle(center.x + joystickCenter.x, center.y + joystickCenter.y,
            joystickRadius, joystickPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> updateJoystickPosition(event.x, event.y)
            MotionEvent.ACTION_MOVE -> updateJoystickPosition(event.x, event.y)
            // when touch stopped, reset the joystick
            MotionEvent.ACTION_UP -> { joystickCenter = PointF() }
        }

        listener?.onJoystickMoved(joystickCenter.x / touchAreaRadius, joystickCenter.y / touchAreaRadius)

        // update display
        invalidate()
        return true
    }

    fun setOnJoystickMovedListener(l: OnJoystickMovedListener) {
        listener = l
    }

    // calculate distance between center to a point
    private fun distFromCenter(x: Float, y: Float) : Float {
        return sqrt((center.x - x) * (center.x - x) + (center.y - y) * (center.y - y))
    }

    // update the joystick position
    // gets the position of the cursor
    private fun updateJoystickPosition(x: Float, y: Float){
        // validate joystick in area
        var dist = distFromCenter(x, y)
        joystickCenter = if (dist <= touchAreaRadius) {
            PointF(x - center.x, y - center.y)
        } else {
            // make sure the joystick is in the same angle but in the valid range
            val resize = touchAreaRadius / dist
            PointF((x - center.x) * resize, (y - center.y) * resize)
        }
    }

}