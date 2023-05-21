package mechanics_billiards;

/**
 * @author aram.azatyan | 5/19/2023 5:17 PM
 */
public record ParticleInfo(Point startingPoint, double impulseX, double impulseY) {

    public ParticleInfo {
        if (startingPoint == null) throw new IllegalArgumentException("null value passed");
    }

    @Override
    public String toString() {
        return startingPoint.toString() + ", impulseX = " + impulseX + ", impulseY = " + impulseY + "]";
    }
}
