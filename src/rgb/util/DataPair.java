package rgb.util;

public class DataPair<T, U> {
	public DataPair(T first, U second) {
		this.first = first;
		this.second = second;
	}
	
	public T first;
	public U second;
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			DataPair<T, U> other = (DataPair<T, U>) obj;
			return this.first.equals(other.first) &&
					this.second.equals(other.second);
		}
		return false;
	}
}