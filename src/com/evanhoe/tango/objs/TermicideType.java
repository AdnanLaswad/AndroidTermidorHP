package com.evanhoe.tango.objs;

public class TermicideType {
	
    private String TermicideTypeCode;
    private String TermicideTypeName;
    private double DilutionRatio;
    private double InjectionCountFactor;
    
    
	public TermicideType(String termicideTypeCode, String termicideTypeName,
			double dilutionRatio, double injectionCountFactor) {
		super();
		TermicideTypeCode = termicideTypeCode;
		TermicideTypeName = termicideTypeName;
		DilutionRatio = dilutionRatio;
		InjectionCountFactor = injectionCountFactor;
	}


	public String getTermicideTypeCode() {
		return TermicideTypeCode;
	}


	public void setTermicideTypeCode(String termicideTypeCode) {
		TermicideTypeCode = termicideTypeCode;
	}


	public String getTermicideTypeName() {
		return TermicideTypeName;
	}


	public void setTermicideTypeName(String termicideTypeName) {
		TermicideTypeName = termicideTypeName;
	}


	public double getDilutionRatio() {
		return DilutionRatio;
	}


	public void setDilutionRatio(double dilutionRatio) {
		DilutionRatio = dilutionRatio;
	}


	public double getInjectionCountFactor() {
		return InjectionCountFactor;
	}


	public void setInjectionCountFactor(double injectionCountFactor) {
		InjectionCountFactor = injectionCountFactor;
	}
	
	
	

}
