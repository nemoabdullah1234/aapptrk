package com.nicbit.traquer.stryker.Models.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.stryker.inventory.frigdoor.ApiTempModel;

import java.util.List;

/**
 * Created by niteshgoel on 8/25/17.
 */

public class TempApiResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<List<ApiTempModel>> tempDataList;

    public List<List<ApiTempModel>> getTempDataList() {
        return tempDataList;
    }

    public void setTempDataList(List<List<ApiTempModel>> tempDataList) {
        this.tempDataList = tempDataList;
    }

    public ApiTempModel getTempOneRecord() {
        ApiTempModel tempModel = null;
        if (tempDataList.size() > 0) {
            List<ApiTempModel> apiTempModels = tempDataList.get(0);
            if (apiTempModels.size() > 0) {
                tempModel = apiTempModels.get(0);
            }
        }
        return tempModel;
    }

}
