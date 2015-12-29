package com.org.actions.database;

/**
 * Created by ericbhatti on 12/29/15.
 */
public interface DataBaseFields {

     interface Markers {
        final String TABLE_NAME = "markers";
        final String _ID = "marker_id";
        final String MARKER_CLASS = "marker_class";
        final String MARKER_DISPLAY_NAME = "marker_display_name";
     }

     interface TimeSlots {
        final String TABLE_NAME = "timeslots";
        final String _ID = "timeslot_id";
        final String START_TIME = "start_time";
        final String END_TIME = "end_time";
     }

    interface Weekdays {
        final String TABLE_NAME = "weekdays";
        final String _ID = "weekday_id";
        final String WEEKDAY_NAME = "weekday_name";
    }

    interface Schedules {
        final String TABLE_NAME = "schedules";
        final String WEEKDAY_ID = "weekday_id";
        final String MARKER_ID = "marker_id";
        final String TIMESLOT_ID = "timeslot_id";
        final String SCHEDULED_CLASS = "scheduled_class";
    }

}
