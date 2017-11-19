package android.example.com.visualizerpreferences;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

// SOLVED (1) Implement OnSharedPreferenceChangeListener
public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener{


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);

        // SOLVED (3) Get the preference screen, get the number of preferences and iterate through
        // all of the preferences if it is not a checkbox preference, call the setSummary method
        // passing in a preference and the value of the preference

        // TODO: 19/11/2017 We are creating instance of PreferenceScreen (which consist of checkBox/s and list preferences)
        // todo: WHY in VisualizerActivity we are calling SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();

        for(int i = 0; i < prefScreen.getPreferenceCount(); i++){
            Preference p = prefScreen.getPreference(i);
                if(p instanceof ListPreference ){
                    // TODO: 19/11/2017 To get Value we need to call key and we have value associated with key ?
                    String valueChoosenByUser = sharedPreferences.getString(p.getKey(),"");
                    setPreferenceSummary(p,valueChoosenByUser);
                }
            }

        }


    // TODO (4) Override onSharedPreferenceChanged and, if it is not a checkbox preference,
    // call setPreferenceSummary on the changed preference
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if(null != p){
            // CheckBoxPreference will throw exception while trying to get boolean - that is why we need If statement
            if(!(p instanceof CheckBoxPreference)){
                // TODO: 19/11/2017 To get Value we need to call key and we have value associated with key ? Why def value we keep ?
                String valueChoosenByUser = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,valueChoosenByUser);
            }
        }

    }

    // SOLVED (2) Create a setPreferenceSummary which takes a Preference and String value as parameters.
    // This method should check if the preference is a ListPreference and, if so, find the label
    // associated with the value. You can do this by using the findIndexOfValue and getEntries methods
    // of Preference.
    private void setPreferenceSummary (Preference p, String value){

        // casting Preferences to ListPreferences
        ListPreference listPreference = (ListPreference) p;
        // take the index of selected by user value from ListPreferences
        int prefIndex = listPreference.findIndexOfValue(value);
        // index of value [0,1,2,....] - zabezpieczenie dodatkowe
        if(prefIndex >= 0){
            //set the summary of the entry/row connected to index with correct Label
            listPreference.setSummary(listPreference.getEntries()[prefIndex]);
        }
    }


    // TODO (5) Register and unregister the OnSharedPreferenceChange listener (this class) in
    // onCreate and onDestroy respectively.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}