package bence.game2048;

public class CellValue {

	private int value;
	private boolean isNew = false;

	public int getValue() {
		return value;
	}

	public CellValue(int value) {
		this.value = value;
	}

	public CellValue(int value, boolean isNew) {
		this.value = value;
		this.isNew = isNew;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellValue other = (CellValue) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	public void setNew() {
		this.isNew = true;
	}

	public boolean isNew() {
		return isNew;
	}

	public void resetNew() {
		isNew = false;
	}
}
