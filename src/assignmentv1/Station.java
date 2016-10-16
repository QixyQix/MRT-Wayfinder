/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentv1;

import java.util.*;

/**
 *
 * @author QiXiang
 */
public class Station {
    private String stnCode;
    private String stnName;
    
    /**
     * Constructor
     * 
     * Station code e.g. CC1 NS5 NE17 CG2 etc.
     * @param stnCode
     * Station name e.g. Yew Tee, Choa Chu Kang etc.
     * @param stnName 
     */
        public Station(String stnCode, String stnName){
    this.stnCode = stnCode;
    this.stnName = stnName;
    }

    public String getStnCode() {
        return stnCode;
    }

    public void setStnCode(String stnCode) {
        this.stnCode = stnCode;
    }

    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }



    
    
}
