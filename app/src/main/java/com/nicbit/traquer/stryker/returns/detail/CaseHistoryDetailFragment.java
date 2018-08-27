package com.nicbit.traquer.stryker.returns.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.common.comments.ItemCommentActivity;
import com.nicbit.traquer.common.utils.SignatureActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.ItemRequest;
import com.nicbit.traquer.stryker.Models.ItemUsedRequest;
import com.nicbit.traquer.stryker.Models.history.ReaderGetCaseHistoryDetailsResponse;
import com.nicbit.traquer.stryker.Models.history.ReviewerDetail;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.CaseHistoryItemAdapter;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.inventory.ScanFragment;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.util.TypefaceTextView;
import com.nicbit.traquer.stryker.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.akwa.akcore.BeaconData;
import io.akwa.akproximity.kontakt.KontaktBeaconScannerManual;

public class CaseHistoryDetailFragment extends ScanFragment implements CaseHistoryDetailContract.View, CaseHistoryItemAdapter.ItemClickListener, KontaktBeaconScannerManual.KontaktBeaconListener {


    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.saveBtn)
    TypefaceTextView saveBtn;
    private static final String PATH = Environment.getExternalStorageDirectory() + "/" + StringUtils.APP_NAME + "/Images";
    TextView txtDoctorName, txtLocation, txtPhone, txtNumber, txtFax, txtSurgeryDetails, txtDueBack, txtName, lblName, lblNumber;
    private CaseHistoryDetailContract.UserActionsListener mActionsListener;
    private String mCaseNumber;
    private Integer mIsSubmitted;
    CaseHistoryItemAdapter mAdapter;
    boolean isScanning = false;
    KontaktBeaconScannerManual kontaktBeaconScannerManual;
    Set<BeaconData> kontakBeacons;
    private boolean isScanClicked = false;
    private File signatureFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mCaseNumber = getArguments().getString(StringUtils.CASE_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_history_new, container, false);
        ButterKnife.bind(this, view);
        txtSurgeryDetails = (TextView) view.findViewById(R.id.txtSurgeryName);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtFax = (TextView) view.findViewById(R.id.txtFax);
        txtDoctorName = (TextView) view.findViewById(R.id.txtDocterName);
        txtDueBack = (TextView) view.findViewById(R.id.txtDueBack);
        txtName = (TextView) view.findViewById(R.id.txtName);
        lblName = (TextView) view.findViewById(R.id.lblName);
        lblNumber = (TextView) view.findViewById(R.id.lblNumber);
        txtNumber = (TextView) view.findViewById(R.id.txtNumber);
        txtLocation = (TextView) view.findViewById(R.id.txtLocation);
        mActionsListener = new CaseHistoryDetailPresenter(this);
        getCasesHistory();
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = (String) txtPhone.getTag();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
        return view;

    }

    @OnClick(R.id.saveBtn)
    void onSaveClick() {
        openPopUpWindow();
    }

    private void setItemStatus() {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        ItemUsedRequest request = getRequestData();
        mActionsListener.setItemUsedStatus(request);
    }

    private ItemUsedRequest getRequestData() {
        ItemUsedRequest request = new ItemUsedRequest();
        request.setCaseNo(mCaseNumber);
        request.setIsSubmitted(mIsSubmitted);
        List<Item> list = mAdapter.getItemList();
        List<ItemRequest> newRequestList = new ArrayList<>();
        for (Item item : list) {
            ItemRequest newItem = new ItemRequest();
            newItem.setComment(item.getL3());
            newItem.setSkuId(item.getSkuId());
            newItem.setUsedStatus("" + item.getL5());
            newRequestList.add(newItem);
        }
        request.setItems(newRequestList);
        return request;
    }

    private void getCasesHistory() {
        DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
        mActionsListener.getCaseHistoryDetail(mCaseNumber);
    }

    @Override
    public void onCaseHistoryDetailResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                ReaderGetCaseHistoryDetailsResponse responseData = response.getData().getReaderGetCaseHistoryDetailsResponse();
                if (responseData != null) {
                    setDoctorDetails(responseData.getCaseDetails());
                    setAdapter(responseData.getItems());
                    setShipmentDetails(responseData.getCaseDetails());
                    setReviewerDetails(responseData.getReviewerDetails());
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getCasesHistory();
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }

    private void setReviewerDetails(ReviewerDetail reviewerDetails) {
        if (!reviewerDetails.getReviewerFirstName().isEmpty()) {
            txtName.setVisibility(View.VISIBLE);
            lblName.setVisibility(View.VISIBLE);
            lblNumber.setVisibility(View.VISIBLE);
            txtNumber.setVisibility(View.VISIBLE);
            txtName.setText(reviewerDetails.getReviewerFirstName() + " " + reviewerDetails.getReviewerLastName());
            txtNumber.setText(reviewerDetails.getReviewerCountryCode() + " " + reviewerDetails.getReviewerMobile());
        } else {
            txtName.setVisibility(View.GONE);
            lblName.setVisibility(View.GONE);
            lblNumber.setVisibility(View.GONE);
            txtNumber.setVisibility(View.GONE);
            txtName.setText(reviewerDetails.getReviewerFirstName() + " " + reviewerDetails.getReviewerLastName());
        }
    }

    @Override
    public void onItemUsedStatusResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                Toast.makeText(getActivity(), R.string.status_update, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            setItemStatus();
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onSurgeryReportResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(getActivity());
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                ReviewerDetail reviewerDetail = response.getData().getReaderSubmitSurgeryReportResponse();
                setReviewerDetails(reviewerDetail);
                deleteSignature();
                Toast.makeText(getActivity(), R.string.status_update, Toast.LENGTH_SHORT).show();

            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(getActivity(), getResources().getString(R.string.check_internet_connection));
        }
    }

    private void deleteSignature() {
        if (signatureFile != null && signatureFile.exists()) {
            signatureFile.delete();
        }
    }


    public void setDoctorDetails(CaseDetails caseDetails) {
        if (caseDetails != null) {
            txtDoctorName.setText(caseDetails.getL1());
            txtSurgeryDetails.setText(caseDetails.getL2() + " | " + caseDetails.getL8());
            txtLocation.setText(caseDetails.getL3());
            txtDueBack.setText(caseDetails.getL6());

            if (caseDetails.getL4().isEmpty()) {
                txtPhone.setVisibility(View.GONE);
            } else {
                txtPhone.setVisibility(View.VISIBLE);
                txtPhone.setText(caseDetails.getL4());

            }
            if (caseDetails.getL5().isEmpty()) {
                txtFax.setVisibility(View.GONE);
            } else {
                txtFax.setVisibility(View.VISIBLE);
                txtFax.setText("Fax: " + caseDetails.getL5());

            }
            txtPhone.setTag(caseDetails.getL4());
        }
    }

    public void setAdapter(List<Item> itemList) {

        mAdapter = new CaseHistoryItemAdapter(getActivity(), itemList, this);
        mAdapter.setIsFirstTime(false);
        listView.setAdapter(mAdapter);

    }

    public void setShipmentDetails(CaseDetails caseDetails) {

    }

    private void openPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.reviewer_details_popup, null);
        final PopupWindow popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText medtFirstName = (EditText) layout.findViewById(R.id.edtFirstName);
        final EditText medtLastName = (EditText) layout.findViewById(R.id.edtLastName);
        final EditText edtPhoneNo = (EditText) layout.findViewById(R.id.edtPhoneNo);
        final EditText mbtnCountryCode = (EditText) layout.findViewById(R.id.btn_country_code);
        TextView mBtnSubmit = (TextView) layout.findViewById(R.id.btn_submit);
        TextView mBtnSignature = (TextView) layout.findViewById(R.id.btn_signature);
        TextView mBtnCancel = (TextView) layout.findViewById(R.id.btnCancel);

        mBtnSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignatureActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = medtFirstName.getText().toString();
                String lastName = medtLastName.getText().toString();
                String countryCode = mbtnCountryCode.getText().toString();
                String countryPhone = edtPhoneNo.getText().toString();
                if (firstName.isEmpty()) {

                    Toast.makeText(getActivity(), "Please write the first Name.", Toast.LENGTH_LONG).show();
                } else if (lastName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please write the last Name.", Toast.LENGTH_LONG).show();

                } else if (countryCode.isEmpty()) {
                    Toast.makeText(getActivity(), "Please write the country code.", Toast.LENGTH_LONG).show();

                } else if (countryPhone.isEmpty()) {
                    Toast.makeText(getActivity(), "Please write the phone number.", Toast.LENGTH_LONG).show();

                } else if (!Util.validateCountryCodeAndPhoneNumber(countryCode, countryPhone)) {
                    Toast.makeText(getActivity(), "Phone number is not valid", Toast.LENGTH_LONG).show();
                } else {
                    if (countryCode.contains("+"))
                        countryCode = countryCode.replace("+", "");
                    ReviewerDetail reviewerDetail = new ReviewerDetail();
                    reviewerDetail.setCaseNo(mCaseNumber);
                    reviewerDetail.setReviewerFirstName(firstName);
                    reviewerDetail.setReviewerLastName(lastName);
                    reviewerDetail.setReviewerMobile(countryPhone);
                    reviewerDetail.setReviewerCountryCode(countryCode);
                    signatureFile = new File(getDirectory(), "Signature.jpg");
                    if (signatureFile != null && signatureFile.exists()) {
                        reviewerDetail.setSignatureFile(signatureFile);
                    }
                    mActionsListener.submitSurgeryReport(reviewerDetail);
                    DialogClass.showDialog(getActivity(), getActivity().getString(R.string.please_wait));
                    popupWindow.dismiss();
                }

            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }

    private File getDirectory() {
        File folder = new File(PATH);
        if (!folder.exists())
            folder.mkdirs();
        return folder;
    }


    @OnClick(R.id.iv_sync)
    public void onScanClick() {
        if (isScanning) {
            kontaktBeaconScannerManual.stopRanginBeacon();
            isScanning = false;
            isScanClicked = false;
            stopAnimation();
        } else {
            isScanClicked = true;
            checkBluetoothOnOff();
        }
    }

    public void startScan() {
        mAdapter.notifyDataSetChanged();
        kontakBeacons = new HashSet<>();
        kontaktBeaconScannerManual = new KontaktBeaconScannerManual(getActivity(), this);
        kontaktBeaconScannerManual.startScanning();
        isScanning = true;
        startAnimation();
    }

    @Override
    public void onItemClicked(Item item) {
        Intent i = new Intent(getActivity(), ItemQuantityActivity.class);
        i.putExtra(StringUtils.IntentKey.IS_EDIT, true);
        i.putExtra(StringUtils.IntentKey.SKU_ID, item.getSkuIdAsInt());
        i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
        getActivity().startActivity(i);
    }

    @Override
    public void onItemCommentClicked(Item item) {
        Intent i = new Intent(getActivity(), ItemCommentActivity.class);
        i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
        i.putExtra(StringUtils.SKU_ID, item.getSkuId());
        i.putExtra(StringUtils.IntentKey.L2, item.getL2());
        getActivity().startActivity(i);
    }

    @Override
    public void onDestroy() {
        if (kontaktBeaconScannerManual != null) {
            kontaktBeaconScannerManual.stopRanginBeacon();
        }
        super.onDestroy();

    }

    public void onBluetoothChange(boolean isOn) {
        if (isOn) {
            changeIconState(true);
            if (isScanClicked) {
                startScan();
            }
        } else {
            changeIconState(false);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        checkBluetoothOnOff();
    }

    @Override
    public void onBeaconDetected(BeaconData iBeacon) {
        kontakBeacons.add(iBeacon);
        int minor = iBeacon.getMinor();
        Log.i("beacon", minor + "");
        mAdapter.setIsFirstTime(true);
        mAdapter.onBeaconFound(iBeacon);
    }
}
