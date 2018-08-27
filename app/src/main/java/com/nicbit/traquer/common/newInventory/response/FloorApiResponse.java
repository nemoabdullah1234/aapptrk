package com.nicbit.traquer.common.newInventory.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.stryker.Models.newModels.BaseResponse;

import java.util.List;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class FloorApiResponse extends BaseResponse {


    @SerializedName("data")
    @Expose
    private FloorData data;

    public FloorData getData() {
        return data;
    }

    public void setData(FloorData data) {
        this.data = data;
    }

    public class FloorData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("floors")
        @Expose
        private List<Floor> floors = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Floor> getFloors() {
            return floors;
        }

        public void setFloors(List<Floor> floors) {
            this.floors = floors;
        }

    }


    public class Floor {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
