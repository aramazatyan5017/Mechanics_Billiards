package mechanics_billiards;

/**
 * @author aram.azatyan | 5/19/2023 5:16 PM
 */
public record Point(double x, double y) {
    @Override
    public String toString() {
        return "[x = " + x + ", y = " + y + "]";
    }
}
