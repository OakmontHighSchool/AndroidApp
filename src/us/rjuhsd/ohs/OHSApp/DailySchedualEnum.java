package us.rjuhsd.ohs.OHSApp;

public enum DailySchedualEnum {
	INTERVENTION(new int[][]{{7, 45}, {9, 5}, {9, 40}, {11, 11}, {11, 41}, {12, 38}, {13, 8}, {12, 38}, {14, 35}});

	public int[] ss;
	public int[] e1;
	public int[] e2;
	public int[] inter;
	public int[] elunch1;
	public int[] slunch2;
	public int[] e3l1;
	public int[] e3l2;
	public int[] e4;
	DailySchedualEnum(int[][] args) {
		this.ss = args[0];
		this.e1 = args[1];
		this.inter = args[2];
		this.e2 = args[3];
		this.elunch1 = args[4];
		this.slunch2 = args[5];
		this.e3l1 = args[6];
		this.e3l2 = args[7];
		this.e4 = args[8];
	}
}
