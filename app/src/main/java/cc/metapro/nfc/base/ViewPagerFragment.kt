package cc.metapro.nfc.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

abstract class ViewPagerFragment : Fragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showWhenUserHere()
    }

    protected abstract fun showWhenUserHere()
}