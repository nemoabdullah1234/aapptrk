package com.nicbit.traquer.common.newInventory.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.common.newInventory.zone.Product;
import com.nicbit.traquer.stryker.Models.newModels.BaseResponse;

import java.util.List;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class ZoneApiResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Thing {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("major")
        @Expose
        private Integer major;
        @SerializedName("minor")
        @Expose
        private Integer minor;
        @SerializedName("type")
        @Expose
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Integer getMajor() {
            return major;
        }

        public void setMajor(Integer major) {
            this.major = major;
        }

        public Integer getMinor() {
            return minor;
        }

        public void setMinor(Integer minor) {
            this.minor = minor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class Zones {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("productList")
        @Expose
        private List<Product> productList = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Product> getProductList() {
            return productList;
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

    }


    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("zones")
        @Expose
        private List<Zones> zones;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Zones> getZones() {
            return zones;
        }

        public void setZones(List<Zones> zones) {
            this.zones = zones;
        }

    }


}
