package cc.metapro.nfc.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import com.jude.swipbackhelper.SwipeBackHelper

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private val TAG = javaClass.simpleName
    private lateinit var mDetector: GestureDetectorCompat
    private var mVelocityTracker: VelocityTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetector = GestureDetectorCompat(this, this)
        mDetector.setOnDoubleTapListener(this)
        SwipeBackHelper.onCreate(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwipeBackHelper.onDestroy(this)
    }

    override fun onShowPress(p0: MotionEvent?) {
        Log.d(TAG, "onShowPress")
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapUp")
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDown")
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(TAG, "onFling")
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(TAG, "onScroll")
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        Log.d(TAG, "onLongPress")
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTap")
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTapEvent")
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapConfirmed")
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event)
        val index = event?.actionIndex
        val action = event?.actionMasked
        val pointerId = index?.let { event.getPointerId(it) }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mVelocityTracker!!.clear()
                mVelocityTracker!!.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker!!.addMovement(event)
                mVelocityTracker!!.computeCurrentVelocity(1000)

                Log.d(TAG, "X velocity: " + mVelocityTracker!!.xVelocity)
                Log.d(TAG, "Y velocity: " + mVelocityTracker!!.yVelocity)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker!!.recycle()
            }
        }

        return super.onTouchEvent(event)
    }
}