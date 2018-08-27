package io.akwa.frigdoor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import eu.blulog.blulib.exceptions.BluException;
import eu.blulog.blulib.json.JSONObject;
import eu.blulog.blulib.nfc.BackgroundExecutor;
import eu.blulog.blulib.nfc.NfcUtils;
import eu.blulog.blulib.tdl2.BlutagContent;
import eu.blulog.blulib.tdl2.BlutagHandler;
import eu.blulog.blulib.tdl2.DataDefinition;
import eu.blulog.blulib.tdl2.Recording;
import io.akwa.aksync.AppLog;
import io.akwa.aksync.SyncService;
import io.akwa.aksync.network.model.ApiBreachInfoModel;
import io.akwa.aksync.network.model.ApiLocationModel;
import io.akwa.aksync.network.model.ApiSensorModel;
import io.akwa.aksync.network.model.ApiTempModel;
import io.akwa.aktracking.LocationHandler;


public class FrigdorHomeActivity extends AppCompatActivity {

    public static int reportCount;
    private TagData tagData;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private FrameLayout container_body;
    private ImageView scanDefaultImg;
    public FragmentManager fragmentManager;
    private LocationHandler locationHandler;
    //    private SignInParcelable signInParcelable;
    private String callingFunction = "";
    private boolean isReport;
//    private ScanPresenter mActionsListener;
//    private StopScanPresenter mStopScanPresenter;


    protected enum Operations {FINISH_RECORDING, READ_TEMPS, SHOW_TEMPS, NOTHING, RECOVER_AAR, START_RECORDING, SHORT_READ}

    private Operations operation = Operations.SHORT_READ;
    private AtomicBoolean busyOnProcessNFC = new AtomicBoolean(false);
    private BackgroundTagProcessor backgroundTagProcessor = null;
    private String companyName = "Novartis";

    public void setPager() {
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frigdor_home);
        fragmentManager = getSupportFragmentManager();
        locationHandler = new LocationHandler(this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
//        tabLayout.addTab(tabLayout.newTab().setText("History"));

        container_body = (FrameLayout) findViewById(R.id.container_body);
        container_body.setVisibility(View.GONE);
        scanDefaultImg = (ImageView) findViewById(R.id.scanDefaultImg);
        tagData = new TagData();

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            signInParcelable = extras.getParcelable("SignInParcelable");
//            if (signInParcelable != null) {
//                tagData = signInParcelable.getTagData();
//                isReport = signInParcelable.getReport();
//                callingFunction = signInParcelable.getCallingFunction();
//            }
//
//        }
        setPager();
        showFragment();

    }

    @Override
    public void onResume() {
        super.onResume();

//        displayView(0);

        NfcUtils.enableNfcForgroundDispatch(this);
        Intent intent = getIntent();
        onNewIntent(intent);
    }

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
                                    ArrayList<Recording> recordings = BlutagContent.get().getRecordings();
                                    if (recordings != null && recordings.size() > 0) {
                                        Recording recording = recordings.get(0);
                                        recording.setRegistrationFinishDate(new Date());
                                    }
//                                    stopRecording();
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
                                    setPager();
                                    showFragment();

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
                                setData();
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
                                    Toast.makeText(context, R.string.new_recording_created, Toast.LENGTH_SHORT);
                                } else if (status.equals(BluException.RECORDING_ALREADY_STARTED)) {
                                    Toast.makeText(context, "Recording started already", Toast.LENGTH_SHORT);
                                } else
                                    Toast.makeText(context, R.string.operation_not_completed_try_again, Toast.LENGTH_SHORT);

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
                                //  startNewRecording(tag);
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
//                                    askDoRetry();
                                }
                            }

                            @Override
                            protected void processTag(Tag tag) throws BluException {
                                BlutagHandler.get().readTempsFromEeprom(tag, 0);
                            }
                        };
                break;
            default:

        }
        Log.i("post start new intent", intent.toString());
        if (backgroundTagProcessor != null)
            backgroundTagProcessor.execute(tag);
    }


    private ArrayList<BreachInfo> updateBreaches(PriorityQueue<Recording.Breach> breaches) {
        int size = breaches.size();
        ArrayList<BreachInfo> breachInfos = new ArrayList<>();
        try {
            Class<Recording.Breach> breachClass = (Class<Recording.Breach>) Class.forName("eu.blulog.blulib.tdl2.Recording$Breach");
            Field duration = breachClass.getDeclaredField("duration");
            Field start = breachClass.getDeclaredField("start");
            Field end = breachClass.getDeclaredField("end");
            Field avgTemp = breachClass.getDeclaredField("avgTemp");
            Field breachType = breachClass.getDeclaredField("breachType");
            Field extremumTemp = breachClass.getDeclaredField("extremumTemp");

            duration.setAccessible(true);
            start.setAccessible(true);
            end.setAccessible(true);
            avgTemp.setAccessible(true);
            breachType.setAccessible(true);
            extremumTemp.setAccessible(true);
            for (int i = 0; i < size; i++) {
                BreachInfo breachInfo = new BreachInfo();
                Recording.Breach breach = breaches.poll();
                breachInfo.duration = (int) duration.get(breach);
                breachInfo.start = (Date) start.get(breach);
                breachInfo.end = (Date) end.get(breach);
                breachInfo.avgTemp = (double) avgTemp.get(breach);
                double extremumTemp1 = (double) extremumTemp.get(breach);
                breachInfo.minMaxTemp = BluTagInfo.toStringDevideByTen((int) extremumTemp1);
                breachInfo.minMaxTempNumber = BluTagInfo.devideByTen((int) extremumTemp1);
                Recording.BreachType breachTypeEnum = (Recording.BreachType) breachType.get(breach);
                String type = breachTypeEnum.name().charAt(0) + "" + (breachTypeEnum.name().substring(1)).toLowerCase();
                breachInfo.breachType = type;
                breachInfos.add(breachInfo);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return breachInfos;
    }


    public void setData() {

        BluTagInfo bluTagInfo = new BluTagInfo();
        ApiTempModel apiTempModel = new ApiTempModel();
        apiTempModel.setType("tempTag");

        BlutagContent blutagContent = BlutagContent.get();
        if (blutagContent.getHardware() == 0)
            return;

        bluTagInfo.setId(Long.toString(blutagContent.getBlueTagId()));

//
        apiTempModel.setUid(bluTagInfo.getId());

        bluTagInfo.setHardware("" + blutagContent.getHardware());
        tagData.chipNo = bluTagInfo.getId();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        DataDefinition dataDefinition = blutagContent.getDataDefinition();
        String propertyName = "";
        String propertyValueStr = "";
        long propertyValue = 0;

        for (DataDefinition.DataDefinitionEntry<DataDefinition.GenericInfoType>
                genericEntry : dataDefinition.getGenericInfo()) {
            if (genericEntry.getDescription() < 0)
                continue;
            propertyName = genericEntry.getProperty().name();
            propertyValue = blutagContent.getGenericData().getLong(propertyName);
            if (propertyName.equals("loggerExpirationDate")) {
                bluTagInfo.setEpochTempExpDate(propertyValue);
//
                apiTempModel.setExpDate(propertyValue*1000);
                propertyValueStr = dateFormat.format(new Date(propertyValue * 1000));
                bluTagInfo.setExpirationDate(propertyValueStr);
            }
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

            bluTagInfo.setTotalDuration("" + Long.toString(utilizedDaysCount + days));
//
            apiTempModel.setTotalDuration(utilizedDaysCount + days);
        }

        ArrayList<BreachInfo> breachInfos = new ArrayList<>();
        if (dataDefinition.getGenericInfoEntry(DataDefinition.GenericInfoType.timeToLive) != null) {
            long timeToLive = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.timeToLive.name());
            long heartbeatDuration = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.heartbeatDuration.name());
            long lastPeriodStartDate = blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.lastRecordingStartDate.name());
            long lastPeriodDuration = 0;
            if (lastPeriodStartDate > 0)
                lastPeriodDuration = (new Date()).getTime() / 1000 - lastPeriodStartDate;
            // table.addRow(new TwoColumnTable.Row(getString(R.string.time_to_live),
            //          Utils.secondsToInterval((int) (timeToLive * heartbeatDuration - lastPeriodDuration))));


            if (blutagContent.getGenericData().getLong(DataDefinition.GenericInfoType.utilizedRecordingsCount.name()) > 0) {

                Recording recording = blutagContent.getRecordings().get(0);

                //data i.e is writen in the tag (TagData)
                Recording.Statistics statistics = recording.getStatistics();
                breachInfos = updateBreaches(statistics.getBreaches());
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
                        if (resourceID == 0) {
                            if (propName.equalsIgnoreCase("Data")) {
                                tagData.sku = String.valueOf(propValue);
                                String[] split = tagData.sku.split("#");
//                                tagData.productName = split[0];
//                                tagData.serialNo = split[1];
//                                tagData.uniqueID = split[2];
//                                tagData.companyName = split[3];
//                                if (split.length > 4) {
//                                    tagData.expDate = split[4];
//                                } else {
//                                    tagData.expDate = "none";
//                                }
                                if (split.length > 5) {
                                    for (int i = 5; i < split.length; i++) {
                                        String attribute = split[i];
                                        String[] newSplit = attribute.split(":");
                                        tagData.attribute.add(new Attribute(newSplit[0], newSplit[1]));
                                    }
                                }
//                                PrefUtils.setCompanyName(tagData.companyName);
                            } else if (propName.equalsIgnoreCase("Client Id"))
                                tagData.clientID = String.valueOf(propValue);

                        }

                    }
                }

                if (recording.getTemperatures() != null && recording.getTemperatures().length > 0) {
                    short[] temperatureArray = recording.getTemperatures();
                    Float[] temp = new Float[temperatureArray.length];
                    for (int i = 0; i < temperatureArray.length; i++) {
                        temp[i] = Float.parseFloat(BluTagInfo.toStringDevideByTen(temperatureArray[i]));
                    }
                    bluTagInfo.temp = temp;
                    bluTagInfo.setLastRecordedTemperature("" + temp[temp.length - 1] + getString(R.string.temperature_unit));
                    bluTagInfo.setLastRecordedTemp(temp[temp.length - 1]);

//
                    apiTempModel.temp=temp;
                    apiTempModel.setLastRecordedTemp(temp[temp.length - 1]);

                    for (int i = 0; i < recording.getTemperatures().length; i++)


                        dataDefinition = recording.getDataDefinition();

                    for (DataDefinition.DataDefinitionEntry<DataDefinition.RecordingInfoType>
                            recordingEntry : dataDefinition.getDeserialRecordingInfo()) {
                        if (recordingEntry.getDescription() < 0)
                            continue;
                        propertyName = recordingEntry.getProperty().name();
                        propertyValue = recording.getRecordingData().getLong(propertyName);


                        if (recordingEntry.getProperty() == (DataDefinition.RecordingInfoType.registrationStartDate)) {
                            if (recordingEntry.getType() == DataDefinition.DataType.DATE)
                                if (propertyValue > 0) {
                                    bluTagInfo.setEpochTempRecordStartTime(propertyValue);
                                    propertyValueStr = formatDate(new Date(propertyValue * 1000));
                                    bluTagInfo.setStartTime(propertyValueStr);
//
                                    apiTempModel.setStartTime(propertyValue*1000);

                                    //dateFormat.format(new Date(propertyValue * 1000));
//                                    addItemInList("Start date", "" + propertyValueStr);
                                } else {
//                                    addItemInList("Start date", "NA");
                                }

                        } else if (recordingEntry.getProperty() == (DataDefinition.RecordingInfoType.registrationFinishDate)) {
                            if (recordingEntry.getType() == DataDefinition.DataType.DATE)
                                if (propertyValue > 0) {
                                    propertyValueStr = formatDate(new Date(propertyValue * 1000));
                                    bluTagInfo.setEndTime(propertyValueStr);
                                    apiTempModel.setEndTime(propertyValue*1000);
//                                    addItemInList("End date", "" + propertyValueStr);
                                } else {
//                                    addItemInList("End date", "NA");

                                }

                        } else if (recordingEntry.getProperty() == DataDefinition.RecordingInfoType.minTemp) {
                            propertyValueStr = BluTagInfo.toStringDevideByTen((int) propertyValue) + getString(R.string.temperature_unit);
                            bluTagInfo.setMinTemperature(propertyValueStr);
                            bluTagInfo.setMinTemp(BluTagInfo.devideByTen((int) propertyValue));
//
                            apiTempModel.setMinTemp(bluTagInfo.getMinTemp());
                            //    addItemInList(propertyName, "" + propertyValueStr);

                        } else if (recordingEntry.getProperty() == DataDefinition.RecordingInfoType.maxTemp) {
                            propertyValueStr = BluTagInfo.toStringDevideByTen((int) propertyValue) + getString(R.string.temperature_unit);
                            bluTagInfo.setMaxTemperature(propertyValueStr);
                            bluTagInfo.setMaxTemp(BluTagInfo.devideByTen((int) propertyValue));

                            apiTempModel.setMaxTemp(bluTagInfo.getMaxTemp());

                            //    addItemInList(propertyName, "" + propertyValueStr);

                        } else if (recordingEntry.getProperty() == DataDefinition.RecordingInfoType.measurementCycle) {
                            bluTagInfo.setMeasurementCycle(propertyValue);

                            apiTempModel.setCycle(propertyValue);

                            //    addItemInList(propertyName, "" + propertyValueStr);

                        }


                    }
                }

                if (statistics.getMinTemp() == 10000) {
                    bluTagInfo.setMinRecordedTemperature(0 + getString(R.string.temperature_unit));
                    bluTagInfo.setMaxRecordedTemperature(0 + getString(R.string.temperature_unit));
                    bluTagInfo.setMaxRecordedTemp(0);
                    bluTagInfo.setMinRecordedTemp(0);

                    apiTempModel.setMinRecordedTemp(0);
                    apiTempModel.setMaxRecordedTemp(0);

                } else {
                    bluTagInfo.setMinRecordedTemperature(BluTagInfo.toStringDevideByTen(statistics.getMinTemp()) + getString(R.string.temperature_unit));
                    bluTagInfo.setMaxRecordedTemperature(BluTagInfo.toStringDevideByTen(statistics.getMaxTemp()) + getString(R.string.temperature_unit));
                    bluTagInfo.setMaxRecordedTemp(BluTagInfo.devideByTen(statistics.getMaxTemp()));
                    bluTagInfo.setMinRecordedTemp(BluTagInfo.devideByTen(statistics.getMinTemp()));

                    apiTempModel.setMinRecordedTemp(bluTagInfo.getMinRecordedTemp());
                    apiTempModel.setMaxRecordedTemp(bluTagInfo.getMaxRecordedTemp());
                }
                if (statistics != null) {
                    bluTagInfo.setAverageTemperature("" + statistics.getAvgTemp());
                    apiTempModel.setAvgTemp(statistics.getAvgTemp());
                    if (statistics.getBreachesCount() > 0) {
                        bluTagInfo.setBreachCount(statistics.getBreachesCount());
                        bluTagInfo.setBreachDuration(statistics.getBreachesDuration());

                        apiTempModel.setBreachCount(bluTagInfo.getBreachCount());
                        apiTempModel.setBreachDuration(bluTagInfo.getBreachDuration());
                        apiTempModel.setKineticMeanTemp(statistics.getMeanKineticTemp());


                    }

                }
            }

        }
        tagData.bluTagInfo = bluTagInfo;
        tagData.bluTagInfo.setBreachInfos(breachInfos);

        ArrayList<ApiBreachInfoModel> breachInfoList= new ArrayList<>();

        for (BreachInfo breachInfo: breachInfos){
            ApiBreachInfoModel apiBreachInfoModel = new ApiBreachInfoModel();
            apiBreachInfoModel.setAvgTemp(breachInfo.getAvgTemp());
            apiBreachInfoModel.setBreachType(breachInfo.getBreachType());
            apiBreachInfoModel.setDuration(breachInfo.getDuration());
            apiBreachInfoModel.setMinMaxTemp(breachInfo.getMinMaxTempNumber());
            apiBreachInfoModel.setStart(breachInfo.getStart().getTime());
            apiBreachInfoModel.setEnd(breachInfo.getEnd().getTime());
            breachInfoList.add(apiBreachInfoModel);

        }

        apiTempModel.setBreachInfos(breachInfoList);
        Log.i("start new intent", bluTagInfo.toString());
        syncData(apiTempModel);


    }


    private void syncData(ApiTempModel apiTempModel) {

        Location location = locationHandler.getLastKnownLocation();
        AppLog.i("last location---" + location.getLatitude());

//        Location location = BlutagContent.get().getLocation();
//        AppLog.i("last location--from blu-"+location.getLatitude());

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
        ArrayList<ApiSensorModel> tempInfos = new ArrayList<>();
        tempInfos.add(apiTempModel);
        apiLocationModel.setsensors(tempInfos);
        apiLocationModels.add(apiLocationModel);
        SyncService syncService = new SyncService(this);
        syncService.publishData(null, null, apiLocationModels);

    }

    public static String formatDate(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy H:mm a");
        String str = ft.format(date).replace("am", "AM").replace("pm", "PM");
        return str;
    }

    public void hideTabs() {
        if (scanDefaultImg != null && tabLayout != null && viewPager != null && container_body != null) {
            scanDefaultImg.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            container_body.setVisibility(View.VISIBLE);
        }

    }


    public void showTabs() {
        scanDefaultImg.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    private void showFragment() {
//        setTitle(getString(R.string.frigdor_app_name));
        container_body.setVisibility(View.VISIBLE);
        showTabs();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public TagData getTagData() {
        return tagData;
    }


//    public void displayView(int position) {
//        Fragment fragment = null;
//        String fragmentTag = null;
//        if (position != 0 || position != 2)
//            hideTabs();
//
//        if (position != 0)
//            switch (position) {
//
//                case 1:
//                    setTitle(getString(R.string.my_scan_history));
//                    fragment = new BluScanHistoryFragment();
//                    break;
//                case 2:
//                    setTitle(getString(R.string.replacement_history));
//                    fragment = new ReplacementHistoryFragment();
//                    break;
//                case 3:
//                    sendFeedBack();
//                    break;
//                case 4:
//                    setTitle(getString(R.string.frigdor));
//                    fragment = new FrigdorAboutFragment();
//                    break;
//                case 5:
//
//                    if (isLogin) {
//                        logout();
//
//                        break;
//                    } else {
//                        Intent intent = new Intent(this, FrigdorLoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    }
//
//                default:
//                    setTitle(getString(R.string.frigdor));
//                    fragment = new FrigdorAboutFragment();
//                    break;
//            }
//
//        if (fragment != null) {
//            fragmentTag = fragment.getClass().getName();
//            Fragment currentVisibleFragment = fragmentManager.findFragmentByTag(fragmentTag);
//            if (fragmentManager.getBackStackEntryCount() == 0) {
//                addFragment(fragment, fragmentTag);
//            } else {
//                if (!(currentVisibleFragment != null && currentVisibleFragment.isVisible())) {
//                    addFragment(fragment, fragmentTag);
//                }
//
//            }
//
//        }
//    }

//    public void addFragment(Fragment fragment, String fragmentTag) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_body, fragment, fragmentTag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

//    private void showScan() {
//        nTag = null;
//        desFire = null;
//        setTitle(getString(R.string.frigdor_app_name));
//        startNewScan();
//    }


//    @Override
//    protected void updateTitle(Fragment f) {
//        String name = f.getClass().getName();
//        if (name != null) {
//            if (name.equals(BluScanHistoryFragment.class.getName())) {
//                setTitle(getString(R.string.my_scan_history));
//            } else if (name.equals(ReplacementHistoryFragment.class.getName())) {
//                setTitle(getString(R.string.replacement_history));
//            } else if (name.equals(FrigdorAboutFragment.class.getName())) {
//                setTitle(getString(R.string.frigdor));
//            }
//        }
//    }

    private void startNewScan() {
//        setTitle(getString(R.string.frigdor_app_name));
        if (fragmentManager.getBackStackEntryCount() == 1) {
            fragmentManager.popBackStack();
        }
        scanDefaultImg.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        container_body.setVisibility(View.GONE);
    }

//    @Override
//    public void onBackPressed() {
//
//        if (getDrawerFragment().isDrawerOpen()) {
//            getDrawerFragment().closeDrawer();
//        } else {
//            if (scanDefaultImg.getVisibility() == View.VISIBLE)
//                finish();
//            else if (tabLayout.getVisibility() == View.VISIBLE) {
//                startNewScan();
//            } else {
//                if (fragmentManager.getBackStackEntryCount() > 1) {
//                    fragmentManager.popBackStack();
//                } else if (fragmentManager.getBackStackEntryCount() == 1) {
//                    startNewScan();
//                }
//            }
//        }
//
//    }

}
