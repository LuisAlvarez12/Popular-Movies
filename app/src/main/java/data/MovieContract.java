package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by luisalvarez on 2/11/17.
 */

public class MovieContract {

    public static final String uriAUTHORITY =
            "com.example.luisalvarez.popularmovies";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" +
            uriAUTHORITY);

    public static final class Items
            implements BaseColumns {

        public static final Uri ITEMS_CONTENT_URI =
                Uri.withAppendedPath(
                        MovieContract.CONTENT_URI,
                        "items");

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "com.example.luisalvarez.popularmovies.items";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "com.example.luisalvarez.popularmovies.items";

        public static final String[] PROJECTION_ALL =
                {_ID, NAME, BORROWER};

        public static final String SORT_ORDER_DEFAULT =
                NAME + " ASC";
    }

    }


    public static final class Favorites
            implements BaseColumns {

    }
}
