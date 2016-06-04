// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.app2.app2t.domain;

import com.app2.app2t.domain.ParameterDetail;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ParameterDetail_Roo_Json {
    
    public String ParameterDetail.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ParameterDetail.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ParameterDetail ParameterDetail.fromJsonToParameterDetail(String json) {
        return new JSONDeserializer<ParameterDetail>()
        .use(null, ParameterDetail.class).deserialize(json);
    }
    
    public static String ParameterDetail.toJsonArray(Collection<ParameterDetail> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ParameterDetail.toJsonArray(Collection<ParameterDetail> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ParameterDetail> ParameterDetail.fromJsonArrayToParameterDetails(String json) {
        return new JSONDeserializer<List<ParameterDetail>>()
        .use("values", ParameterDetail.class).deserialize(json);
    }
    
}
