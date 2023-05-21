package mechanics_billiards;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aram.azatyan | 5/19/2023 5:16 PM
 */
public class HorizontalPlaneSimulation {

    public static List<ParticleInfo> simulateHorizontalCircleBilliard(Point initialPoint, double initialImpulseX,
                                                     double initialImpulseY, int numberOfReflections) {
        if (initialPoint == null) throw new IllegalArgumentException("null argument passed");
        if (numberOfReflections < 0) throw new IllegalArgumentException("negative number of reflections");
        if (initialImpulseX == 0 && initialImpulseY == 0) throw new IllegalArgumentException("particle isn't moving");
        if (distanceBetween(initialPoint, new Point(0, 0)) > 1) throw new IllegalArgumentException("particle is not inside the circle");

        List<ParticleInfo> list = new ArrayList<>(numberOfReflections + 1);
        list.add(new ParticleInfo(initialPoint, initialImpulseX, initialImpulseY));
        for (int i = 0; i < numberOfReflections; i++) {
            ParticleInfo newInfo = getNextReflectionInfo(initialPoint, initialImpulseX, initialImpulseY);
            list.add(newInfo);
            initialPoint = newInfo.startingPoint();
            initialImpulseX = newInfo.impulseX();
            initialImpulseY = newInfo.impulseY();
        }
        return list;
    }

    /**
     * Always returns points.size() - 1 points, since points also contains the initial points,
     * whereas the reversed simulation will contain only the reflection points
     */
    public static List<ParticleInfo> reverseSimulateHorizontalCircleBilliard(List<ParticleInfo> points) {
        if (points == null) throw new IllegalArgumentException("null argument passed");
        if (points.isEmpty()) throw new IllegalArgumentException("empty dataset passed");
        if (points.size() == 1) return points;

        ParticleInfo lastInfo = points.get(points.size() - 1);
        Point current = lastInfo.startingPoint();
        Point next = findIntersection(current, lastInfo.impulseX(), lastInfo.impulseY());
        Point middlePoint = new Point((current.x() + next.x()) / 2, (current.y() + next.y()) / 2);

        List<ParticleInfo> reversed = simulateHorizontalCircleBilliard(
                middlePoint, -lastInfo.impulseX(), -lastInfo.impulseY(), points.size() - 1);
        reversed.remove(0);
        return reversed;
    }

    private static Point findIntersection(Point startingPoint, double impulseX, double impulseY) {
        double opposite = impulseX * (-startingPoint.x()) + impulseY * (-startingPoint.y());
        double len = Math.pow(startingPoint.x(), 2) + Math.pow(startingPoint.y(), 2) - 1;
        double dir = opposite / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2));
        double temp = Math.pow(dir, 2) - (len / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2)));

        Point p1 = new Point(startingPoint.x() - impulseX * (-dir + Math.sqrt(temp)),
                startingPoint.y() - impulseY * (-dir + Math.sqrt(temp)));
        Point p2 = new Point(startingPoint.x() - impulseX * (-dir - Math.sqrt(temp)),
                startingPoint.y() - impulseY * (-dir - Math.sqrt(temp)));

        return getTheRightPoint(p1, p2, impulseX, impulseY);
    }

    private static Point getTheRightPoint(Point p1, Point p2, double impulseX, double impulseY) {
        if (impulseX == 0) {
            if (impulseY > 0) return p1.y() > 0 ? p1 : p2;
            else return p1.y() < 0 ? p1 : p2;
        } else if (impulseY == 0) {
            if (impulseX > 0) return p1.x() > 0 ? p1 : p2;
            else return p1.x() < 0 ? p1 : p2;
        } else {
            if (impulseX < 0) return p1.x() < p2.x() ? p1 : p2;
            else return p1.x() > p2.x() ? p1 : p2;
        }
    }

    private static ParticleInfo getNextReflectionInfo(Point currentStartingPoint, double impulseX, double impulseY) {
        Point intersectionPoint = findIntersection(currentStartingPoint, impulseX, impulseY);
        return getNewInfo(intersectionPoint, impulseX, impulseY);
    }

    private static ParticleInfo getNewInfo(Point intersection, double impulseX, double impulseY) {
        double newImpulseX = ((Math.pow(intersection.y(), 2) - Math.pow(intersection.x(), 2)) * impulseX) - (2 * intersection.x() * intersection.y() * impulseY);
        double newImpulseY = ((Math.pow(intersection.x(), 2) - Math.pow(intersection.y(), 2)) * impulseY) - (2 * intersection.x() * intersection.y() * impulseX);
        return new ParticleInfo(intersection, newImpulseX, newImpulseY);
    }

    private static double distanceBetween(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }

    public static void main(String[] args) {
        int numberOfReflections = 1000000;
        List<ParticleInfo> points = simulateHorizontalCircleBilliard(
                new Point(-0.5, 0),
                2, 3, numberOfReflections);
        List<ParticleInfo> reversedPoints = reverseSimulateHorizontalCircleBilliard(points);

        for (int i = 0; i < reversedPoints.size(); i++) {
            System.out.println(distanceBetween(
                    points.get(points.size() - i - 1).startingPoint(), reversedPoints.get(i).startingPoint()
            ));
        }
    }
}
