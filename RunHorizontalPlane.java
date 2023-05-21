package mechanics_billiards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author aram.azatyan | 5/21/2023 12:39 PM
 */
public class RunHorizontalPlane {

    public static void main(String[] args) {

        int numberOfReflections = 30;
        double delta = 0.00000000000002;

        List<ParticleInfo> points = HorizontalPlaneSimulation.simulateHorizontalCircleBilliard(
                new Point(-0.5, 0.2), 2, 3.5, numberOfReflections);

        List<ParticleInfo> reversedPoints = HorizontalPlaneSimulation.
                reverseSimulateHorizontalCircleBilliard(points);

        //write to the respective file
        File file = new File("task1.txt");
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            for (ParticleInfo info : points) {
                writer.write(info.startingPoint().x() + ":" + info.startingPoint().y());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < reversedPoints.size(); i++) {
            double distance = distanceBetween(
                    points.get(points.size() - i - 1).startingPoint(), reversedPoints.get(i).startingPoint()
            );
            if (distance > delta) {
                System.out.println("After " + (i + 1) + " reflections, the paths deviate");
                return;
            }
        }

    }

    private static double distanceBetween(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
    }


}
