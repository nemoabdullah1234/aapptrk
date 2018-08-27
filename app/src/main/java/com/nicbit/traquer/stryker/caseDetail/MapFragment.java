package com.nicbit.traquer.stryker.caseDetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment {
    @BindView(R.id.webView)
    WebView webView;
    private boolean isLoadComplete = false;
    private static final String SHIPMENT_DETAILS = "shipmentdtails";
    private static final String CASE_DETAILS = "caseDetails";
    Shipment shipment;
    CaseDetails caseDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoadComplete = false;
        if (getArguments() != null) {
            shipment = getArguments().getParcelable(SHIPMENT_DETAILS);
            caseDetails = getArguments().getParcelable(CASE_DETAILS);
        }
    }

    public static MapFragment newInstance(Shipment shipment, CaseDetails caseDetails) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(SHIPMENT_DETAILS, shipment);
        args.putParcelable(CASE_DETAILS, caseDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        WebSettings settings = webView.getSettings();
        webView.setBackgroundColor(0x00000000);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        String mCaseNumber = getArguments().getString(StringUtils.CASE_NUMBER);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);

        webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webView.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default
        if (isLoadComplete) { // load offline
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (shipment != null && shipment.getMap() != null) {
            startWebView(shipment.getMap().getUrl());
            //startWebView("http://strykerqc.akwa.io/map/products");

        }
        return view;
    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
//        url="http://docs.google.com/gview?embedded=true&url="+url;

        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                DialogClass.showDialog(getActivity(), getResources().getString(R.string.please_wait));
                super.onLoadResource(view, url);
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                DialogClass.dismissDialog(getActivity());
                isLoadComplete = true;
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                DialogClass.dismissDialog(getActivity());
                super.onReceivedError(view, request, error);
            }
        });
        webView.loadUrl(url);

    }
}
