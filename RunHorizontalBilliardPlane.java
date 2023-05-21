package mechanics_billiards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author aram.azatyan | 5/21/2023 1:06 PM
 */
public class RunHorizontalBilliardPlane {

    public static void main(String[] args) {
        int numberOfReflections = 200;
        int L = 5;


        List<ParticleInfo> points = HorizontalBilliardStadiumSimulation.simulateHorizontalBilliardStadium(
                new Point(-0.5, 0),
                1, -3, L, numberOfReflections
        );

        List<Point> lineIntersectPoints = HorizontalBilliardStadiumSimulation.getLineIntersectPoints();
        int[] bucketArray = new int[L];
        lineIntersectPoints.forEach(p -> {
            if (p.x() == L) {
                bucketArray[L - 1]++;
            } else {
                bucketArray[(int) p.x()]++;
            }
        });
        System.out.println(Arrays.toString(bucketArray));

        //write to the respective file
        File file = new File("task3.txt");
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            for (ParticleInfo info : points) {
                writer.write(info.startingPoint().x() + ":" + info.startingPoint().y());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
