package com.training.storageservice.entity;

import com.training.storageservice.constant.RegexConstant;
import io.quarkus.runtime.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CovidReport {
    private String location;
    private String provinceState;
    private String countryName;
    private Date lastUpdate;
    private Long confirmedPeople;
    private Long deathPeople;
    private Long recoveredPeople;
    private Long activePeople;
    private Float incidentRate;
    private Float caseFatalityRatio;

    public static CovidReport convertFromRawData(String lineData) throws ParseException {
        String[] words = lineData.split(RegexConstant.REGEX_FOR_SPLITS_RAW_DATA_BY_COMMA, -1);
        CovidReport covidReport = CovidReport.builder()
                .location(words[1])
                .provinceState(words[2])
                .countryName(words[3])
                .build();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        covidReport.setLastUpdate(format.parse(words[4]));
        covidReport.setConfirmedPeople(StringUtil.isNullOrEmpty(words[7]) ? 0 : Long.parseLong(words[7]));
        covidReport.setDeathPeople(StringUtil.isNullOrEmpty(words[8]) ? 0 : Long.parseLong(words[8]));
        covidReport.setRecoveredPeople(StringUtil.isNullOrEmpty(words[9]) ? 0 : Long.parseLong(words[9]));
        long activePeople = 0;
        if(StringUtil.isNullOrEmpty(words[10])){
            activePeople = covidReport.getConfirmedPeople() - covidReport.getDeathPeople() - covidReport.getRecoveredPeople();
        }else{
            activePeople = Long.parseLong(words[10]);
        }
        covidReport.setActivePeople(activePeople);
        Float incidentRate = null;
        if(!StringUtil.isNullOrEmpty(words[12])){
            incidentRate = Float.parseFloat(words[12]);
        }
        covidReport.setIncidentRate(incidentRate);
        Float caseFatalityRatio = null;
        if(!StringUtil.isNullOrEmpty(words[13])){
            caseFatalityRatio = Float.parseFloat(words[13]);
        }
        covidReport.setCaseFatalityRatio(caseFatalityRatio);
        return covidReport;
    }

    public boolean checkDontHaveDetail(){
        if(StringUtil.isNullOrEmpty(this.getLocation())
            && StringUtil.isNullOrEmpty(this.getProvinceState())){
            return true;
        }
        return false;
    }
}
