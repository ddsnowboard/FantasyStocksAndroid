package com.ddsnowboard.fantasystocksandroid;

import android.widget.ListView;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class FloorDrawerHandler {
    private final ListView drawer;

    public FloorDrawerHandler(ListView drawer) {
        this.drawer = drawer;
        // So here we need to make all the stuff that handles ListViews (which are different than
        // RecyclerViews) with the adapters and the arrays and all that jazz.
        // Then we have to make sure that we are logged in, then send out an AsyncTask to read
        // all the floors, and fill in the list. We also have to set up the OnClickListener to change
        // the fragments. An interesting problem will be how to get a hold on the fragments from
        // out here. Hmmm... This isn't hard, there has to be an elegant way to keep that all together.
        // I bet if I read the code some more, it'll be obvious. But anyway, that's what I have to do.
        // Another troubling addition is that I'll have to write that onclicklistener code and the
        // broadcast receiver code in parallel because they need each other. We'll see how that goes.
    }
}
