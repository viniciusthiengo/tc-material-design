package br.com.thiengo.tcmaterialdesign.network;

import org.json.JSONArray;

import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public interface Transaction {
    WrapObjToNetwork doBefore();

    void doAfter(JSONArray jsonArray);
}
