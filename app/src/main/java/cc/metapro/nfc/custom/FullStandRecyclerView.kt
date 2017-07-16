package cc.metapro.nfc.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FullStandRecyclerView : RecyclerView {

    private var mEmptyView: View? = null
    private var mObserver: RecyclerView.AdapterDataObserver

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        class ItemObserver : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (adapter.itemCount == 0) {
                    mEmptyView?.visibility = View.VISIBLE
                    visibility = View.GONE
                } else {
                    mEmptyView?.visibility = View.GONE
                    visibility = View.VISIBLE
                }
            }
        }
        mObserver = ItemObserver()
    }

    fun setEmptyView(viewID: Int) {
        val view = LayoutInflater.from(context).inflate(viewID, rootView as ViewGroup, false)
        setEmptyView(view)
    }

    fun setEmptyView(view: View) {
        if (mEmptyView != null) throw UnsupportedOperationException("Already has empty view!")
        (rootView as ViewGroup).addView(view)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mObserver)
        mObserver.onChanged()
    }
}