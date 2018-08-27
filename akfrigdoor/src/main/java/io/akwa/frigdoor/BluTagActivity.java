package io.akwa.frigdoor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import eu.blulog.blulib.Utils;
import eu.blulog.blulib.exceptions.BluException;
import eu.blulog.blulib.json.JSONArray;
import eu.blulog.blulib.json.JSONException;
import eu.blulog.blulib.json.JSONObject;
import eu.blulog.blulib.nfc.BackgroundExecutor;
import eu.blulog.blulib.nfc.NfcUtils;
import eu.blulog.blulib.tdl2.BlutagContent;
import eu.blulog.blulib.tdl2.BlutagHandler;
import eu.blulog.blulib.tdl2.DataDefinition;
import eu.blulog.blulib.tdl2.LogisticalProfile;
import eu.blulog.blulib.tdl2.LogisticalProfileManager;
import eu.blulog.blulib.tdl2.Recording;
import io.akwa.aksync.AppLog;
import io.akwa.aksync.SyncService;
import io.akwa.aksync.network.model.ApiBeaconModel;
import io.akwa.aksync.network.model.ApiLocationModel;
import io.akwa.aksync.network.model.ApiSensorModel;
import io.akwa.aktracking.LocationHandler;

public class BluTagActivity extends AppCompatActivity {

    //    private Toolbar mToolbar;
//    private WriterGetSerialsResponse temperatureSerialsData;
    //private StoreData storeData;
    private TextView startButton, stopButton;
    private LocationHandler locationHandler;
//    private AlgoType algoType;

    protected enum Operations {FINISH_RECORDING, READ_TEMPS, SHOW_TEMPS, NOTHING, RECOVER_AAR, START_RECORDING, SHORT_READ}

    ;

    private Operations operation = Operations.SHORT_READ;
    private ScrollView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blutag);

        locationHandler = new LocationHandler(this);


        contentView = (ScrollView) findViewById(R.id.content);
//        mToolbar = (Toolbar) findViewById(R.id.blutagtoolbar);
        startButton = (TextView) findViewById(R.id.startBtn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation = Operations.START_RECORDING;
                Toast.makeText(BluTagActivity.this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
            }
        });
        stopButton = (TextView) findViewById(R.id.stopBtn);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation = Operations.FINISH_RECORDING;
                Toast.makeText(BluTagActivity.this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
            }
        });
//        setSupportActionBar(mToolbar);
        Bundle b = getIntent().getExtras();
//        if (b != null)
//            temperatureSerialsData = b.getParcelable("STORE_DATA");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setType();
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        switch (id) {
//            case android.R.id.home:
//                this.finish();
//                return true;
//
//            case R.id.start_recording:
//                operation = Operations.START_RECORDING;
//                Toast.makeText(this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
//
//                return true;
//            case R.id.finish_recording:
//                operation = Operations.FINISH_RECORDING;
//                Toast.makeText(this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
//                return true;
//
//            case R.id.read_EEPROM:
//                if (DataDefinitionRepository.supportsEepromMemory()) {
//                    operation = Operations.READ_TEMPS;
//                    Toast.makeText(this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(this, "Your logger does is not equipped with EEPROM", Toast.LENGTH_LONG).show();
//                }
//                return true;
//
//            case R.id.recover_aar:
//                operation = Operations.RECOVER_AAR;
//                Toast.makeText(this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return true;
public void onPause() {
    super.onPause();
    NfcUtils.disableNfcForegroundDispatch(this);
}

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        NfcUtils.enableNfcForgroundDispatch(this);
        Intent intent = getIntent();
        onNewIntent(intent);
    }


    private AtomicBoolean busyOnProcessNFC = new AtomicBoolean(false);
    private BackgroundTagProcessor backgroundTagProcessor = null;
//
//        }
//
//    }


    @Override
    public void onNewIntent(Intent intent) {
        Log.w("operation", operation.name());
        // When an NFC tag is being written, call the write tag function when an intent is
        // received that says the tag is within range of the device and ready to be written to

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
            return;
        intent.removeExtra(NfcAdapter.EXTRA_TAG);
        if (!busyOnProcessNFC.compareAndSet(false, true)) { //If busyOnProcessNFC is true to return and set busyOnProcessNFC to true
            if (operation != Operations.SHORT_READ)
                BlutagHandler.get().processNextTag(tag); //new in 1.6.x
            return;
        }

        Log.i("start new intent", intent.toString());

        switch (operation) {
            case RECOVER_AAR:
                backgroundTagProcessor =
                        new BackgroundTagProcessor(this, R.string.downloading_nfc_data, R.string.dont_remove_tag, tag) {
                            @Override
                            protected void postExecute(String status) {
                                if (status == null) {
                                    Toast.makeText(context, "AAR recovered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText((Activity) context, R.string.operation_not_completed_try_again, Toast.LENGTH_SHORT).show();
                                }
                                BackgroundExecutor be = new BackgroundExecutor() {
                                    @Override
                                    protected void postExecute(String status) {
                                        busyOnProcessNFC.set(false);
                                        operation = Operations.SHORT_READ;
                                        backgroundTagProcessor = null;

                                    }

                                    @Override
                                    protected void backgroundWork() {
                                        try {
                                            Thread.sleep(WAIT_TIME);
                                        } catch (InterruptedException e) {
                                        }

                                    }
                                };
                                be.execute(null, null);
                                backgroundTagProcessor = null;
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                BlutagHandler.get().recoverAar(tag);
                            }
                        };
                break;
            case FINISH_RECORDING:
                backgroundTagProcessor =
                        new BackgroundTagProcessor(this, R.string.downloading_nfc_data, R.string.dont_remove_tag, tag) {
                            @Override
                            protected void postExecute(String status) {
                                if (status == null) {
                                    if (BlutagContent.get().getRecordings().size() > 0) {
                                        Recording recording = BlutagContent.get().getRecordings().get(0);
                                        recording.setRegistrationFinishDate(new Date());
                                        Toast.makeText(context, R.string.recording_was_finished, Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(context, R.string.operation_not_completed_try_again, Toast.LENGTH_SHORT).show();
                                }
                                BackgroundExecutor be = new BackgroundExecutor() {
                                    @Override
                                    protected void postExecute(String status) {
                                        busyOnProcessNFC.set(false);
                                        operation = Operations.SHORT_READ;
                                        backgroundTagProcessor = null;
                                        Log.i("nfc", "setting not busy ");

                                    }

                                    @Override
                                    protected void backgroundWork() {
                                        try {
                                            Thread.sleep(WAIT_TIME);
                                        } catch (InterruptedException e) {
                                        }

                                    }
                                };
                                be.execute(null, null);
                                backgroundTagProcessor = null;
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                BlutagHandler.get().finishRecording(tag);
                            }
                        };
                break;
            case SHORT_READ:

                backgroundTagProcessor =
                        new BackgroundTagProcessor(this, R.string.downloading_nfc_data, R.string.dont_remove_tag, tag) {
                            @Override
                            protected void postExecute(String status) {
                                if (status == null) {
                                    contentView.removeAllViews();
                                    showGenericInfo();
                                    Toast.makeText(context, R.string.operation_successfully_completed, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                                }
                                BackgroundExecutor be = new BackgroundExecutor() {
                                    @Override
                                    protected void postExecute(String status) {
                                        busyOnProcessNFC.set(false);
                                    }

                                    @Override
                                    protected void backgroundWork() {
                                        try {
                                            Thread.sleep(WAIT_TIME);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                };
                                be.execute(null, null);
                                backgroundTagProcessor = null;
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                Log.i(" started", this.toString());


                                BlutagHandler.get().readBlutag(tag);
                                if (BlutagContent.get().getRecordings().size() > 0
                                        && BlutagContent.get().getRecordings().get(0).getRegistrationStartDate().getTime() < Recording.START_BY_BUTTON)
                                    BlutagContent.get().getRecordings().get(0).computeStatistics();


                                Log.i("finished", this.toString());
                            }
                        };
                break;
            case START_RECORDING:
                backgroundTagProcessor =
                        new BackgroundTagProcessor(this, R.string.downloading_nfc_data, R.string.dont_remove_tag, tag,
                                BackgroundTagProcessor.TagOperation.StartNewRecording) {
                            @Override
                            protected void postExecute(String status) {
                                if (status == null) {
                                    //uaktualnij info o blutagu
                                    Toast.makeText(context, R.string.new_recording_created, Toast.LENGTH_SHORT).show();
                                    getGenericInfo();
                                } else if (status.equals(BluException.RECORDING_ALREADY_STARTED)) {
                                    Toast.makeText(context, "Recording started already", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(context, R.string.operation_not_completed_try_again, Toast.LENGTH_SHORT).show();

                                BackgroundExecutor be = new BackgroundExecutor() {
                                    @Override
                                    protected void postExecute(String status) {
                                        operation = Operations.SHORT_READ;
                                        busyOnProcessNFC.set(false);
                                    }

                                    @Override
                                    protected void backgroundWork() {
                                        try {
                                            Thread.sleep(WAIT_TIME);
                                        } catch (InterruptedException e) {
                                        }

                                    }
                                };
                                be.execute(null, null);
                                backgroundTagProcessor = null;
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                startNewRecording(tag);
                            }
                        };
                break;
            case READ_TEMPS:
                backgroundTagProcessor =
                        new BackgroundTagProcessor(this, R.string.downloading_nfc_data, R.string.dont_remove_tag, tag, ProgressDialog.STYLE_HORIZONTAL) {
                            @Override
                            protected void postExecute(String status) {
                                if (status == null) {
                                    Toast.makeText(context, R.string.operation_successfully_completed, Toast.LENGTH_SHORT).show();
                                    BackgroundExecutor be = new BackgroundExecutor() {

                                        @Override
                                        protected void postExecute(String status) {
                                            operation = Operations.SHORT_READ;
                                            busyOnProcessNFC.set(false);
                                        }

                                        @Override
                                        protected void backgroundWork() {
                                            try {
                                                Thread.sleep(WAIT_TIME);
                                            } catch (InterruptedException e) {
                                            }
                                        }
                                    };
                                    be.execute(null, null);
                                    backgroundTagProcessor = null;
                                } else {
                                    askDoRetry();
                                }
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                BlutagHandler.get().readTempsFromEeprom(tag, 0);
                            }
                        };
                break;
            default:
                ;
        }
        Log.i("post start new intent", intent.toString());
        if (backgroundTagProcessor != null)
            backgroundTagProcessor.execute(tag);
    }

//    public void locKTag(WriterGetSerialsResponse temperatureSerialsData, String chipId) {
//
//        String token =SharePreference.getToken();
//
//
//        String[] stringParts = temperatureSerialsData.getSku().split("#");
//        String uniqueNumber = stringParts[2];
//
//        DialogClass.showDialog(this, getResources().getString(R.string.wait));
//        ApiHandler apiHandler = new ApiHandler();
//        apiHandler.setApiListener(this);
//        apiHandler.doLockTagRequest(algoType, temperatureSerialsData.getTemperatureSettings(), uniqueNumber, chipId, token);
//    }

//    private void setType() {
//        SharedPreferences sharedPref = this.getSharedPreferences("com.igenune.writer.pref", Context.MODE_PRIVATE);
//        int highScore = sharedPref.getInt(StringUtil.TAG_TYPE, 0);
//        switch (highScore) {
//            case 0:
//                algoType = AlgoType.NoCrypt;
//                break;
//            case 1:
//                algoType = AlgoType.SimpleCrypt;
//                break;
//            case 2:
//                algoType = AlgoType.PKICrypt;
//                break;
//            case 3:
//                algoType = AlgoType.BlueLog;
//                break;
//        }
//
//    }


    protected void setupOfLogisticalPropertiesUsingMyOwnProfile(JSONObject logisticalData) throws BluException {
        String[] stringParts = "1#2#1234#".split("#");
        String uniqueNumber = stringParts[2];
        String myProfileName = uniqueNumber;
        //get instance of logistical profile manager
        LogisticalProfileManager lpm = LogisticalProfileManager.get(this);

        LogisticalProfile myProfile = null;
        //find "MyProfile" profile if previous run of this method created it
        for (LogisticalProfile profile : LogisticalProfileManager.get(this).getProfiles()) {
            if (myProfileName.equals(profile.getProfileName())) {
                myProfile = profile;
                break;
            }
        }

        if (myProfile == null) { //if "MyProfile" was not found
            //create new logistical profile
            myProfile = new LogisticalProfile(myProfileName);

            //get collection logistical entries
            ArrayList<LogisticalProfile.LogisticalEntry> logisticalEntries = myProfile.getLogisticalEntries();

            //add new logistical entry using predefined logistical property name
            LogisticalProfile.LogisticalEntry le = new LogisticalProfile.LogisticalEntry("Data");         //add new logistical entry using user defined property name
            le.setPredefined(false);
            le.setDefValue("sku");
            logisticalEntries.add(le);

            le = new LogisticalProfile.LogisticalEntry("Client Id");
            le.setPredefined(false);
//            String clientID = IGenuineApplication.getSharePreference(BluTagActivity.this).getString(StringUtil.CLIENT_ID);
            le.setDefValue("clientID");
            logisticalEntries.add(le);

            //add new profile to logistical profile manager
            lpm.getProfiles().add(myProfile);

            //store permanently new state of logistical profile manager including new created MyProfile for future use
            lpm.writeToStore(this);
        }
        //populate each logistical property of "MyProfile" profile with its default value
        if (myProfile.getLogisticalEntries() != null) {
            for (LogisticalProfile.LogisticalEntry entry : myProfile.getLogisticalEntries()) {
                try {
                    logisticalData.put(entry.getPropName(), entry.getDefValue());
                } catch (JSONException e) {
                    throw new BluException(e);
                }
            }
        }

    }

//    protected void setupOfLogisticalPropertiesUsingMyOwnProfile(JSONObject logisticalData) throws BluException {
//        String myProfileName = storeData.getUniqueNo();
//        //get instance of logistical profile manager
//        LogisticalProfileManager lpm = LogisticalProfileManager.get(this);
//
//        LogisticalProfile myProfile = null;
//        //find "MyProfile" profile if previous run of this method created it
//        for (LogisticalProfile profile : LogisticalProfileManager.get(this).getProfiles()) {
//            if (myProfileName.equals(profile.getProfileName())) {
//                myProfile = profile;
//                break;
//            }
//        }
//
//        if (myProfile == null) { //if "MyProfile" was not found
//            //create new logistical profile
//            myProfile = new LogisticalProfile(myProfileName);
//
//            //get collection logistical entries
//            ArrayList<LogisticalProfile.LogisticalEntry> logisticalEntries = myProfile.getLogisticalEntries();
//
//            //add new logistical entry using predefined logistical property name
//            LogisticalProfile.LogisticalEntry le = new LogisticalProfile.LogisticalEntry("Batch");         //add new logistical entry using user defined property name
//            le.setPredefined(false);
//            le.setDefValue(storeData.getBatchNo());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("Product Name");
//            le.setPredefined(false);
//            le.setDefValue(storeData.getProductName());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("Serial No.");
//            le.setPredefined(false);
//            le.setDefValue(storeData.getSerialNo());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("Unique No.");
//            le.setPredefined(false);
//            le.setDefValue(storeData.getUniqueNo());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("MFG Date");
//            le.setPredefined(false);
//            le.setDefValue(storeData.getMfgDate());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("EXP Date");
//            le.setPredefined(false);
//            le.setDefValue(storeData.getExpDate());
//            logisticalEntries.add(le);
//
//            le = new LogisticalProfile.LogisticalEntry("Client Id");
//            le.setPredefined(false);
//            String clientID = IGenuineApplication.getSharePreference(BluTagActivity.this).getString(StringUtil.CLIENT_ID);
//            le.setDefValue(clientID);
//            logisticalEntries.add(le);
//
//            //add new profile to logistical profile manager
//            lpm.getProfiles().add(myProfile);
//
//            //store permanently new state of logistical profile manager including new created MyProfile for future use
//            lpm.writeToStore(this);
//        }
//        //populate each logistical property of "MyProfile" profile with its default value
//        if (myProfile.getLogisticalEntries() != null) {
//            for (LogisticalProfile.LogisticalEntry entry : myProfile.getLogisticalEntries()) {
//                try {
//                    logisticalData.put(entry.getPropName(), entry.getDefValue());
//                } catch (JSONException e) {
//                    throw new BluException(e);
//                }
//            }
//        }
//
//    }

    protected void startNewRecording(Tag tag) throws BluException {
        Recording recording = new Recording();

        JSONObject logisticalData = new JSONObject();
        setupOfLogisticalPropertiesUsingMyOwnProfile(logisticalData);
//        if (temperatureSerialsData != null) {
//        } else {
//            BluTagActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(BluTagActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }

        Location location = BlutagContent.get().getLocation();
        if (location != null) {
            JSONObject posData = new JSONObject();
            posData.put("lat", location.getLatitude());
            posData.put("lon", location.getLongitude());
            posData.put("d", (new Date()).getTime() / 1000);
            JSONArray posDataArr = new JSONArray();
            posDataArr.put(posData);

            logisticalData.put("$$pos", posDataArr);
            Log.i("logisticalData", logisticalData.toString(1));
        }

        recording.setLogisticalData(logisticalData);

//        if (temperatureSerialsData.getTemperatureSettings() != null) {
//
//            recording.setMeasurementCycle(temperatureSerialsData.getTemperatureSettings().getTempCycle());
//
//            recording.setMinTemp(getTemp(temperatureSerialsData.getTemperatureSettings().getTempMin()));
//
//            recording.setMaxTemp(getTemp(temperatureSerialsData.getTemperatureSettings().getTempMax()));
//
//            recording.setStartRecordingDelay(temperatureSerialsData.getTemperatureSettings().getTempDelay());
//        } else {
//
//
//        }

        recording.setMeasurementCycle(600);

        recording.setMinTemp(150);

        recording.setMaxTemp(250);

        recording.setStartRecordingDelay(0);

        recording.setDecisionParam1(1);

        recording.setDecisionParam2(1);


        recording.setReadTempPin(0xFFFF);

        recording.setFinishRecordingPin(0xFFFF);

        recording.setActivationEnergy(83);


        Log.i("new recording", recording.getRecordingData().toString());

        BlutagHandler.get().startNewRecording(tag, recording);

    }

    private int getTemp(Integer data) {
        boolean isNegative = false;
        if (data < 0) {
            isNegative = true;
            data = Math.abs(data);
//            temperature = (float) data * 100 / (float) 100.00;
        }
        int length = (int) (Math.log10(data) + 1);

        if (length == 1 || length == 2) {
            data = Integer.parseInt(data + "0");
        }
        if (isNegative) {
            data = -data;
        }
        return data;
    }

    public void getGenericInfo() {
        BlutagContent blutagContent = BlutagContent.get();
        if (blutagContent.getHardware() == 0)
            return;
        String chipId = Long.toString(blutagContent.getBlueTagId());
//        DataDefinition dataDefinition = blutagContent.getDataDefinition();
//        String propertyName;
//        String propertyValueStr;
//        long propertyValue;
//
//        long expDate = 0;
//        for (DataDefinition.DataDefinitionEntry<DataDefinition.GenericInfoType>
//                genericEntry : dataDefinition.getGenericInfo()) {
//            if (genericEntry.getDescription() < 0)
//                continue;
//            propertyName = genericEntry.getProperty().name();
//            propertyValue = blutagContent.getGenericData().getLong(propertyName);
//            expDate = 0;
//            if (propertyName.equals("loggerExpirationDate")) {
//                expDate = propertyValue;
//            }
//        }
//        Recording recording = blutagContent.getRecordings().get(0);
//        dataDefinition = recording.getDataDefinition();
//        long startDate = 0;
//        for (DataDefinition.DataDefinitionEntry<DataDefinition.RecordingInfoType>
//                recordingEntry : dataDefinition.getDeserialRecordingInfo()) {
//            if (recordingEntry.getDescription() < 0)
//                continue;
//            propertyName = recordingEntry.getProperty().name();
//            propertyValue = recording.getRecordingData().getLong(propertyName);
//            if (propertyName.equals("registrationStartDate")) {
//                startDate = propertyValue;
//            }
//        }
//        storeData.getTemperatureSettings().tempExpDate = expDate;
//        storeData.getTemperatureSettings().tempRecordStartTime = startDate;
//        locKTag(temperatureSerialsData, chipId);


    }


    public void showGenericInfo() {

        View rootView = contentView;

        TwoColumnTable table = new TwoColumnTable(rootView.getContext());

        BlutagContent blutagContent = BlutagContent.get();

        if (blutagContent.getHardware() == 0)
            return;

        table.addRow(new TwoColumnTable.Row(getString(R.string.blueTagId, getString(R.string.nfc_device_name)),
                Long.toHexString(blutagContent.getBlueTagId())));

        table.addRow(new TwoColumnTable.Row(getString(R.string.firmware),
                Long.toString(blutagContent.getFirmware())));

        table.addRow(new TwoColumnTable.Row(getString(R.string.hardware),
                Long.toString(blutagContent.getHardware())));

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        DataDefinition dataDefinition = blutagContent.getDataDefinition();
        String propertyName;
        String propertyValueStr;
        long propertyValue;

        for (DataDefinition.DataDefinitionEntry<DataDefinition.GenericInfoType>
                genericEntry : dataDefinition.getGenericInfo()) {
            if (genericEntry.getDescription() < 0)
                continue;
            propertyName = genericEntry.getProperty().name();
            propertyValue = blutagContent.getGenericData().getLong(propertyName);
            if (genericEntry.getType() == DataDefinition.DataType.DATE)
                propertyValueStr = dateFormat.format(new Date(propertyValue * 1000));
            else
                propertyValueStr = Long.toString(propertyValue);

            table.addRow(new TwoColumnTable.Row(getString(genericEntry.getDescription()),
                    propertyValueStr));


        }

        if (dataDefinition.getGenericInfoEntry(DataDefinition.GenericInfoType.utilizedDaysCount) != null) {
            long days;
            long lastPeriodStartDate = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.lastRecordingStartDate.name());
            long utilizedDaysCount = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.utilizedDaysCount.name());
            if (lastPeriodStartDate == 0)
                days = 0;
            else
                days = ((new Date().getTime()) / 1000 -
                        lastPeriodStartDate)
                        / (60 * 60 * 24);

            table.addRow(new TwoColumnTable.Row(getString(R.string.utilizedDaysCount),
                    Long.toString(utilizedDaysCount + days)));
        }

        if (dataDefinition.getGenericInfoEntry(DataDefinition.GenericInfoType.timeToLive) != null) {
            long timeToLive = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.timeToLive.name());
            long heartbeatDuration = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.heartbeatDuration.name());
            long lastPeriodStartDate = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.lastRecordingStartDate.name());
            long lastPeriodDuration = 0;
            if (lastPeriodStartDate > 0)
                lastPeriodDuration = (new Date()).getTime() / 1000 - lastPeriodStartDate;
            table.addRow(new TwoColumnTable.Row(getString(R.string.time_to_live),
                    Utils.secondsToInterval((int) (timeToLive * heartbeatDuration - lastPeriodDuration))));
        }

        if (blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.utilizedRecordingsCount.name()) > 0) {


            Recording recording = blutagContent.getRecordings().get(0);

            JSONObject logisticData = recording.getLogisticalData();
            Iterator<String> iter = logisticData.keys();
            String propName;
            String propValue;
            while (iter.hasNext()) {
                propName = iter.next();
                if (propName.equals("$$pos"))
                    continue;
                if ((propValue = logisticData.optString(propName)) != null) {
                    int resourceID = getResources().getIdentifier(propName, "string", this.getPackageName());
                    if (resourceID != 0)
                        propName = getString(resourceID);
                    table.addRow(new TwoColumnTable.Row(propName, propValue));

                }
            }


            dataDefinition = recording.getDataDefinition();

            for (DataDefinition.DataDefinitionEntry<DataDefinition.RecordingInfoType>
                    recordingEntry : dataDefinition.getDeserialRecordingInfo()) {
                if (recordingEntry.getDescription() < 0)
                    continue;
                propertyName = recordingEntry.getProperty().name();
                propertyValue = recording.getRecordingData().getLong(propertyName);
                if (recordingEntry.getType() == DataDefinition.DataType.DATE)
                    if (propertyValue > 0)
                        propertyValueStr = dateFormat.format(new Date(propertyValue * 1000));
                    else
                        propertyValueStr = "";
                else if (recordingEntry.getProperty() == DataDefinition.RecordingInfoType.minTemp ||
                        recordingEntry.getProperty() == DataDefinition.RecordingInfoType.maxTemp)
                    propertyValueStr = devideByTen((int) propertyValue) + getString(R.string.temperature_unit);
                else
                    propertyValueStr = Long.toString(propertyValue);

                table.addRow(new TwoColumnTable.Row(getString(recordingEntry.getDescription()),
                        propertyValueStr));


            }
            if (dataDefinition.getDeserialRecordingInfoEntry(DataDefinition.RecordingInfoType.pinsInfo) != null) {
                long pinsUsed = recording.getRecordingData().getLong(DataDefinition.RecordingInfoType.pinsInfo.name());
                table.addRow(new TwoColumnTable.Row(getString(R.string.readTempPinUsed),
                        (pinsUsed & 0x02) == 0x02 ?
                                getString(R.string.yes) : getString(R.string.no)));

                table.addRow(new TwoColumnTable.Row(getString(R.string.finishRecordingPinUsed),
                        (pinsUsed & 0x01) == 0x01 ?
                                getString(R.string.yes) : getString(R.string.no)));
            }
        }
        contentView.removeAllViews();
        contentView.addView(table.build());

        syncData();

    }

    private void syncData() {

        Location location = locationHandler.getLastKnownLocation();
        AppLog.i("last location---" + location.getLatitude());

//        Location location = BlutagContent.get().getLocation();
//        AppLog.i("last location--from blu-"+location.getLatitude());

        Recording recording = BlutagContent.get().getRecordings().get(0);
        Float[] temp = null;
        if (recording.getTemperatures() != null && recording.getTemperatures().length > 0) {
            short[] temperatureArray = recording.getTemperatures();
            temp = new Float[temperatureArray.length];
            for (int i = 0; i < temperatureArray.length; i++) {
                temp[i] = Float.parseFloat(toStringDevideByTen(temperatureArray[i]));
            }


        }


        long currentTime = System.currentTimeMillis();
        SharedPreferences sharedPref = this.getSharedPreferences("stryker", Context.MODE_PRIVATE);
        String code = sharedPref.getString("code", "");
        String projectId = "us-east-1_zI0af0OBy";//intent.getStringExtra(Key.IntentKey.PROJECT_ID);
        String clientId = "012179919676";//intent.getStringExtra(Key.IntentKey.CLIENT_ID);
        ArrayList<ApiLocationModel> apiLocationModels = new ArrayList<>();

        ApiLocationModel apiLocationModel = new ApiLocationModel();
        apiLocationModel.setacc(location.getAccuracy());
        apiLocationModel.setalt(location.getAltitude());
        apiLocationModel.setdir(location.getBearing());
        apiLocationModel.setlat(location.getLatitude());
        apiLocationModel.setlon(location.getLongitude());
        apiLocationModel.setprv("android_" + location.getProvider());
        apiLocationModel.setspd(location.getSpeed());
        apiLocationModel.setts(currentTime);
        apiLocationModel.setHt(currentTime);
        apiLocationModel.setDid(code);
        apiLocationModel.setClientid(clientId);
        apiLocationModel.setProjectid(projectId);
        apiLocationModel.setPkid(code + currentTime);
//        apiLocationModel.setTemp(temp);
        ArrayList<ApiSensorModel> beacons = new ArrayList<>();
        ApiBeaconModel beaconModel = new ApiBeaconModel();
        beaconModel.setUuid(Long.toString(BlutagContent.get().getBlueTagId()));
        beacons.add(beaconModel);

        apiLocationModel.setsensors(beacons);
        apiLocationModels.add(apiLocationModel);
        SyncService syncService = new SyncService(this);
        syncService.publishData(null, null, apiLocationModels);


    }

    public static String toStringDevideByTen(int data) {

        boolean isNegative = false;
        if (data < 0) {
            isNegative = true;
            data = Math.abs(data);
//            temperature = (float) data * 100 / (float) 100.00;
        }

        String temperature = Integer.toString(data / 10) + "." + Integer.toString(data % 10);
        if (isNegative) {
            temperature = "-" + temperature;
        }
        return temperature;
    }

    static protected String devideByTen(int data) {
        boolean isNegative = false;
        if (data < 0) {
            isNegative = true;
            data = Math.abs(data);
//            temperature = (float) data * 100 / (float) 100.00;
        }

        String temperature = Integer.toString(data / 10) + "." + Integer.toString(data % 10);
        if (isNegative) {
            temperature = "-" + temperature;
        }
        return temperature;

    }

    public void askDoRetry() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Operation not completed");
        alert.setMessage("Operation completed in " + BlutagHandler.get().getPercentageReadData() + "%. Do you want to continue reading of EEPROM?");

//        final View v = getLayoutInflater().inflate(R.layout.dialog_generic, null);
//        alert.setView(v);

        alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backgroundTagProcessor = null;
                BlutagHandler.get().setResumeRead(true);
                busyOnProcessNFC.set(false);
                Toast.makeText(BluTagActivity.this, getString(R.string.putBlutagInRange, getString(R.string.nfc_device_name)), Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                operation = Operations.NOTHING;
                backgroundTagProcessor = null;
                BlutagHandler.get().setResumeRead(false);
                busyOnProcessNFC.set(false);
            }
        });
        alert.show();
    }
}