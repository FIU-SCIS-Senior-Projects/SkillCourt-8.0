package fiu.com.skillcourt.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import fiu.com.skillcourt.interfaces.FragmentListener;

/**
 * Created by pedrocarrillo on 9/10/16.
 */

public class BaseFragment extends Fragment {

    protected FragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (FragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

}
