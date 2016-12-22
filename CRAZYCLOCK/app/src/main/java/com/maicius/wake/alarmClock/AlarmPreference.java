package com.maicius.wake.alarmClock;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

/**
 * The RingtonePreference does not have a way to get/set the current ringtone so
 * we override onSaveRingtone and onRestoreRingtone to get the same behavior.
 */
public class AlarmPreference extends RingtonePreference {
    private Uri mAlert;

    public AlarmPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSaveRingtone(Uri ringtoneUri) {
        setAlert(ringtoneUri);
    }

    @Override
    protected Uri onRestoreRingtone() {
        if (RingtoneManager.isDefault(mAlert)) {
            return RingtoneManager.getActualDefaultRingtoneUri(getContext(),
                    RingtoneManager.TYPE_ALARM);
        }
        return mAlert;
    }

    public void setAlert(Uri alert) {
        mAlert = alert;
        if (alert != null) {
            final Ringtone r = RingtoneManager.getRingtone(getContext(), alert);
            if (r != null) {
                setSummary(r.getTitle(getContext()));
            }
        } else {
            setSummary(R.string.silent_alarm_summary);
        }
    }

    public Uri getAlert() {
        return mAlert;
    }
}
