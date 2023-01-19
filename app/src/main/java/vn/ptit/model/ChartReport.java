package vn.ptit.model;

import java.math.BigDecimal;

public class ChartReport {
	private String label[];
	private float data[];

	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public ChartReport() {
		// TODO Auto-generated constructor stub
	}

	public String[] getLabel() {
		return label;
	}

	public void setLabel(String[] label) {
		this.label = label;
	}

	public ChartReport(String[] label, float[] data) {
		super();
		this.label = label;
		this.data = data;
	}

}
