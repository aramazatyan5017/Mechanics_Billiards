package mechanics_billiards;

import java.util.ArrayList;
import java.util.List;


/**
 * @author aram.azatyan | 5/21/2023 1:00 AM
 */
public class HorizontalBilliardStadiumSimulation {

    private static final List<Point> lineIntersectPoints;

    static {
        lineIntersectPoints = new ArrayList<>();
    }

    public static List<ParticleInfo> simulateHorizontalBilliardStadium(Point initialPoint, double initialImpulseX,
                                          double initialImpulseY, double L, int numberOfReflections) {
        if (L == 0) return HorizontalPlaneSimulation.simulateHorizontalCircleBilliard(
                initialPoint, initialImpulseX, initialImpulseY, numberOfReflections);
        if (initialPoint == null) throw new IllegalArgumentException("null argument passed");
        if (numberOfReflections < 0) throw new IllegalArgumentException("negative number of reflections");
        if (L < 0) throw new IllegalArgumentException("negative L passed");
        if (initialImpulseX == 0 && initialImpulseY == 0) throw new IllegalArgumentException("particle isn't moving");

        List<ParticleInfo> list = new ArrayList<>(numberOfReflections + 1);
        list.add(new ParticleInfo(initialPoint, initialImpulseX, initialImpulseY));
        for (int i = 0; i < numberOfReflections; i++) {
            ParticleInfo newInfo = getNextReflectionInfo(initialPoint, initialImpulseX, initialImpulseY, L);
            list.add(newInfo);
            initialPoint = newInfo.startingPoint();
            initialImpulseX = newInfo.impulseX();
            initialImpulseY = newInfo.impulseY();
        }
        return list;

    }

    public static List<Point> getLineIntersectPoints() {
        return lineIntersectPoints;
    }

    private static Point findLeftSemiIntersection(Point startingPoint, double impulseX, double impulseY) {
        List<Point> intersectionPoints = new ArrayList<>();

        double opposite = impulseX * (-startingPoint.x()) + impulseY * (-startingPoint.y());
        double len = Math.pow(startingPoint.x(), 2) + Math.pow(startingPoint.y(), 2) - 1;
        double dir = opposite / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2));
        double temp = Math.pow(dir, 2) - (len / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2)));

        if (temp >= 0) {
            Point p1 = new Point(startingPoint.x() - impulseX * (-dir + Math.sqrt(temp)),
                    startingPoint.y() - impulseY * (-dir + Math.sqrt(temp)));
            if (temp == 0) {
                intersectionPoints.add(p1);
            } else {
                Point p2 = new Point(startingPoint.x() - impulseX * (-dir - Math.sqrt(temp)),
                        startingPoint.y() - impulseY * (-dir - Math.sqrt(temp)));
                intersectionPoints.add(p1);
                intersectionPoints.add(p2);
            }
        }

        if (intersectionPoints.isEmpty()) return null;
        if (intersectionPoints.size() == 1) {
            Point p1 = intersectionPoints.get(0);
            if (p1.x() == 0 && (p1.y() == 1 || p1.y() == -1)) return null;
            if (p1.x() > 0) return null;
            return p1;
        } else {
            Point p1 = intersectionPoints.get(0);
            Point p2 = intersectionPoints.get(1);

            if (p1.x() >= 0 && p2.x() >= 0) return null;
            Point p = getTheRightPoint(p1, p2, impulseX, impulseY, 0);
            if (p.x() < 0) return p;
            if (p.x() >= 0) return null;
        }
        return null;
    }

    private static Point findRightSemiIntersection(Point startingPoint,
                                                   double impulseX, double impulseY, double L) {
        List<Point> intersectionPoints = new ArrayList<>();

        double opposite = impulseX * (L - startingPoint.x()) + impulseY * (-startingPoint.y());
        double len = Math.pow(L - startingPoint.x(), 2) + Math.pow(startingPoint.y(), 2) - 1;
        double dir = opposite / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2));
        double temp = Math.pow(dir, 2) - (len / (Math.pow(impulseX, 2) + Math.pow(impulseY, 2)));

        if (temp >= 0) {
            Point p1 = new Point(startingPoint.x() - impulseX * (-dir + Math.sqrt(temp)),
                    startingPoint.y() - impulseY * (-dir + Math.sqrt(temp)));
            if (temp == 0) {
                intersectionPoints.add(p1);
            } else {
                Point p2 = new Point(startingPoint.x() - impulseX * (-dir - Math.sqrt(temp)),
                        startingPoint.y() - impulseY * (-dir - Math.sqrt(temp)));
                intersectionPoints.add(p1);
                intersectionPoints.add(p2);
            }
        }

        if (intersectionPoints.isEmpty()) return null;
        if (intersectionPoints.size() == 1) {
            Point p1 = intersectionPoints.get(0);
            if (p1.x() == L && (p1.y() == 1 || p1.y() == -1)) return null;
            if (p1.x() < L) return null;
            return p1;
        } else {
            Point p1 = intersectionPoints.get(0);
            Point p2 = intersectionPoints.get(1);

            if (p1.x() <= L && p2.x() <= L) return null;
            Point p = getTheRightPoint(p1, p2, impulseX, impulseY, L);
            if (p.x() > L) return p;
            if (p.x() <= L) return null;
        }
        return null;
    }

    private static Point findLineIntersection(Point startingPoint,
                                              double impulseX, double impulseY) {
        Point endingPoint = new Point(startingPoint.x() + impulseX, startingPoint.y() + impulseY);
        double k = (endingPoint.y() - startingPoint.y()) / (endingPoint.x() - startingPoint.x());
        double b = startingPoint.y() - (k * startingPoint.x());
        double x = 0;

        if (impulseX == 0 && (impulseY == 1 || impulseY == -1)) {
            Point p = new Point(startingPoint.x(), impulseY > 0 ? 1 : -1);
            lineIntersectPoints.add(p);
            return p;
        } else {
            if (impulseY > 0) {
                x = (1 - b) / k;
            } else {
                x = (-1 - b) / k;
            }
        }
        Point p = new Point(x, k * x + b);
        lineIntersectPoints.add(p);
        return p;
    }

    private static double distanceBetween(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }

    private static Point getTheRightPoint(Point p1, Point p2, double impulseX, double impulseY, double L) {
        if (impulseX == 0) {
            if (impulseY > 0) return p1.y() > 0 ? p1 : p2;
            else return p1.y() < 0 ? p1 : p2;
        } else if (impulseY == 0) {
            if (impulseX > 0) return p1.x() > L ? p1 : p2;
            else return p1.x() < L ? p1 : p2;
        } else {
            if (impulseX < 0) return p1.x() < p2.x() ? p1 : p2;
            else return p1.x() > p2.x() ? p1 : p2;
        }
    }

    private static ParticleInfo getNextReflectionInfo(Point currentStartingPoint,
                                                      double impulseX, double impulseY, double L) {
        Point p = findLeftSemiIntersection(currentStartingPoint, impulseX, impulseY);
        if (p != null) {
            return getNewInfoCircle(p, impulseX, impulseY, 0);
        }
        p = findRightSemiIntersection(currentStartingPoint, impulseX, impulseY, L);
        if (p != null) {
            return getNewInfoCircle(p, impulseX, impulseY, L);
        }
        p = findLineIntersection(currentStartingPoint, impulseX, impulseY);
        return new ParticleInfo(p, impulseX, -impulseY);
    }

    private static ParticleInfo getNewInfoCircle(Point intersection, double impulseX, double impulseY, double xc) {
        double newImpulseX = ((Math.pow(intersection.y(), 2) - Math.pow(intersection.x() - xc, 2)) * impulseX) - (2 * (intersection.x() - xc) * intersection.y() * impulseY);
        double newImpulseY = ((Math.pow(intersection.x() - xc,  2) - Math.pow(intersection.y(), 2)) * impulseY) - (2 * (intersection.x() - xc) * intersection.y() * impulseX);
        return new ParticleInfo(intersection, newImpulseX, newImpulseY);
    }
}
