package com.example.mhbuser.Classes;

import java.util.List;

public class CDashBoardData {

    private String sHallMarquee;
    private String sUid;
    private String sHallMarqueeName;
    private String sManagerFirstName;
    private String sManagerLastName;
    private String sManagerProfileImageUri;
    private String sEmail;
    private String sPhoneNo;
    private String sCity;
    private String sLocation;
    private String sSpotLights;
    private String sMusic;
    private String sAc_Heater;
    private String sParking;
    private List<String> sLHallEntranceDownloadImagesUri;
    private String sFavourite;

    public CDashBoardData() {
    }

    public CDashBoardData(String sHallMarqueeName, String sManagerFirstName, String sManagerLastName, String sManagerProfileImageUri, String sEmail, String sPhoneNo, String sCity, String sLocation, String sSpotLights, String sMusic, String sAc_Heater, String sParking, List<String> sLHallEntranceDownloadImagesUri) {
        this.sHallMarqueeName = sHallMarqueeName;
        this.sManagerFirstName = sManagerFirstName;
        this.sManagerLastName = sManagerLastName;
        this.sManagerProfileImageUri = sManagerProfileImageUri;
        this.sEmail = sEmail;
        this.sPhoneNo = sPhoneNo;
        this.sCity = sCity;
        this.sLocation = sLocation;
        this.sSpotLights = sSpotLights;
        this.sMusic = sMusic;
        this.sAc_Heater = sAc_Heater;
        this.sParking = sParking;
        this.sLHallEntranceDownloadImagesUri = sLHallEntranceDownloadImagesUri;
        this.sFavourite="no";
    }

    public String getsFavourite() {
        return sFavourite;
    }

    public void setsFavourite(String sFavourite) {
        this.sFavourite = sFavourite;
    }

    public String getsHallMarquee() {
        return sHallMarquee;
    }

    public void setsHallMarquee(String sHallMarquee) {
        this.sHallMarquee = sHallMarquee;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getsHallMarqueeName() {
        return sHallMarqueeName;
    }

    public void setsHallMarqueeName(String sHallMarqueeName) {
        this.sHallMarqueeName = sHallMarqueeName;
    }

    public String getsManagerFirstName() {
        return sManagerFirstName;
    }

    public String getsManagerLastName() {
        return sManagerLastName;
    }

    public String getsManagerProfileImageUri() {
        return sManagerProfileImageUri;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsManagerFirstName(String sManagerFirstName) {
        this.sManagerFirstName = sManagerFirstName;
    }

    public void setsManagerLastName(String sManagerLastName) {
        this.sManagerLastName = sManagerLastName;
    }

    public void setsManagerProfileImageUri(String sManagerProfileImageUri) {
        this.sManagerProfileImageUri = sManagerProfileImageUri;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public void setsPhoneNo(String sPhoneNo) {
        this.sPhoneNo = sPhoneNo;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public void setsLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public void setsSpotLights(String sSpotLights) {
        this.sSpotLights = sSpotLights;
    }

    public void setsMusic(String sMusic) {
        this.sMusic = sMusic;
    }

    public void setsAc_Heater(String sAc_Heater) {
        this.sAc_Heater = sAc_Heater;
    }

    public void setsParking(String sParking) {
        this.sParking = sParking;
    }

    public void setsLHallEntranceDownloadImagesUri(List<String> sLHallEntranceDownloadImagesUri) {
        this.sLHallEntranceDownloadImagesUri = sLHallEntranceDownloadImagesUri;
    }

    public String getsPhoneNo() {
        return sPhoneNo;
    }

    public String getsCity() {
        return sCity;
    }

    public String getsLocation() {
        return sLocation;
    }

    public String getsSpotLights() {
        return sSpotLights;
    }

    public String getsMusic() {
        return sMusic;
    }

    public String getsAc_Heater() {
        return sAc_Heater;
    }

    public String getsParking() {
        return sParking;
    }

    public List<String> getsLHallEntranceDownloadImagesUri() {
        return sLHallEntranceDownloadImagesUri;
    }
}
