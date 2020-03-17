public enum Direction {
    UP (1),
    DOWN (-1),
    RIGHT (2),
    LEFT (-2);

    private int value;

    Direction(int value) {
	this.value = value;
    }

    public int getValue(){
	return value;
    }
}
