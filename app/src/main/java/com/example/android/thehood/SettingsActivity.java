package com.example.android.thehood;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_radius_key)));
            ///bindRadiusSummaryToValue();
    // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
    // updated when the preference changes.
    // TODO: Add preference
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
//    private void bindRadiusSummaryToValue(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Preference preference = findPreference(getString(R.string.pref_radius_key));
//        //preference.setOnPreferenceChangeListener(this);
//        String units;
//        String unitType = prefs.getString(
//                this.getString(R.string.pref_units_key),
//                this.getString(R.string.pref_units_imperial));
//        if (unitType == this.getString(R.string.pref_units_imperial)){
//            units = " miles";
//        }
//        else {
//            units = " kilometres";
//        }
//        preference.setOnPreferenceChangeListener(this);
//        onPreferenceChange(preference, PreferenceManager
//                .getDefaultSharedPreferences(preference.getContext())
//                .getString(preference.getKey(), "") + units);
//
//    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
// For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}