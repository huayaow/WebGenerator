package model;

import alg.Configuration;

public class Algorithm {
	// raw data
	private String alg_version ;
	private String alg_config ;

	// format data
	public Configuration pso_config ;

	public Algorithm() {}

	public void setAlg_version(String alg) {
		this.alg_version = alg ;
	}
	public void setAlg_config(String config) {
		this.alg_config = config ;
	}

	public String getAlg_version() {
		return alg_version ;
	}
	public String getAlg_config() {
		return alg_config ;
	}

	// 
	public void FormatData() {
		if(alg_version.equals("PSO")) {
			String[] temp = alg_config.split(";");
			int r = Integer.valueOf(temp[0]).intValue();
			int s = Integer.valueOf(temp[1]).intValue();
			int t = Integer.valueOf(temp[2]).intValue();
			double w = Double.valueOf(temp[3]).doubleValue();
			double f = Double.valueOf(temp[4]).doubleValue();
			pso_config = new Configuration(r,s,t,w,f);
		}
	}
}