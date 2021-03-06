package cd4017be.automation.shaft;

public interface IHeatReservoir {
	public float T();
	public float C();
	public void addHeat(float dQ);
	public static interface IHeatStorage {
		public IHeatReservoir getHeat(byte side);
		public float getHeatRes(byte side);
	}
}
