package com.sdpd.companion.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.sdpd.companion.data.remote.FirebaseAnalyticsSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class AnalyticsRepository {
    private static final String TAG = "AnalyticsRepository";

    FirebaseAnalyticsSource firebaseAnalyticsSource;

    @Inject
    public AnalyticsRepository(FirebaseAnalyticsSource firebaseAnalyticsSource) {
        this.firebaseAnalyticsSource = firebaseAnalyticsSource;
    }

    public void updateAnalytics(String type, String groupName, Long time) {
        firebaseAnalyticsSource.updateAnalytics(type, groupName, time);
    }

    public Flowable<Map<String, Object>> fetchGroups(String type) {
        return firebaseAnalyticsSource.fetchGroups(type).map(snapshot -> {
            Map<String, Object> groups = new HashMap();
            for (DataSnapshot child: snapshot.getChildren()){
                groups.put(child.getKey(), child.getValue());
            }
            return groups;
        });
    }
}
