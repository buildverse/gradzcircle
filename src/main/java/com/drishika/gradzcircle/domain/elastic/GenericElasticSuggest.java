package com.drishika.gradzcircle.domain.elastic;

public class GenericElasticSuggest{
    private String value;
    private String display;
   

    public GenericElasticSuggest(String value, String display ){
        this.value = value;
        this.display = display;
    }

    public String getValue(){
        return value;
    }

    public String getDisplay(){
        return display;
    }

   
}