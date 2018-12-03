package dataconservationnitrr.swcmonitoring;

import android.content.Context;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;

/**
 * Created by Ketan-PC on 4/6/2018.
 */

public class Cloud_storage {
   private Storage storage;
    Context context;

    public Cloud_storage(Context context) {

        this.context= context;

        try {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId("clay-197816")
                    .setCredentials(GoogleCredentials.fromStream(context.getAssets().open
                            ("Files/clayuploads.json"))).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Storage getStorage() {
        return storage;
    }
}
