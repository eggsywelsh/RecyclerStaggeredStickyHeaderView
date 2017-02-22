package com.eggsy.recyclerstaggeredstickyheader;

import android.support.annotation.NonNull;

/**
 * Created by eggsy on 17-1-6.
 */

public interface MainContract {

    interface View {

        void refreshLocalPictures();

    }

    abstract class Presenter<V> {

        protected V mViewProxy;

        public void detachView() {
            this.mViewProxy = null;
        }

        public void attachView(@NonNull V view) {
            this.mViewProxy = view;
        }

        public abstract void loadLocalPicture();

        public abstract int findBeforeTitlePosition(int coordinatePosition);

        public abstract int findAfterTitlePosition(int coordinatePosition);

    }

}
