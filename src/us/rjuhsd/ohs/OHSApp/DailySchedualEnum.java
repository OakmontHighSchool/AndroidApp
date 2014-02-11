package us.rjuhsd.ohs.OHSApp;

@SuppressWarnings("UnusedDeclaration")
public enum DailySchedualEnum {
	INTERVENTION(new int[][]{{7, 45}, {9, 5}, {9, 40}, {11, 11}, {0, 0}, {11, 41}, {12, 38}, {13, 8}, {14, 35}}),
	OFF(new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}});

	public int[] ss;
	public int[] e1;
	public int[] e2;
	public int[] inter;
	public int[] rally;
	public int[] lunch1;
	public int[] lunch2;
	public int[] e3;
	public int[] e4;
	public int[][] originalInput;
	DailySchedualEnum(int[][] args) {
		this.originalInput = args;
		this.ss = args[0];
		this.e1 = args[1];
		this.inter = args[2];
		this.e2 = args[3];
		this.rally = args[4];
		this.lunch1 = args[5];
		this.e3 = args[6];
		this.lunch2 = args[7];
		this.e4 = args[8];
	}
}
