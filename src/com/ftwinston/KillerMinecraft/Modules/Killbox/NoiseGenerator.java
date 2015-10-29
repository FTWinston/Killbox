package com.ftwinston.KillerMinecraft.Modules.Killbox;

import java.util.Random;

public class NoiseGenerator
{
	public static double[] generateScaledNoise(Random r, double fixedMaxVal, double minVal)
	{
		double shapeDistortion = r.nextDouble() * fixedMaxVal * 0.8 + 0.15;
        double bumpiness = r.nextInt(1001) / 1666.6666667; // maximum value should be 0.6

        double[] data = perlinNoise(r, 256, 1, bumpiness, new int[] { 64, 32, 16, 8 }, false);

        double min, max;
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        double val;
        for (int i = 0; i < data.length; i++)
        {
            val = data[i];
            if (val < min)
                min = val;
            if (val > max)
                max = val;
        }

        // scale such that max - min = verticalRange,
        // and offset such that such that (min + max)/2 = ground level ... min should be >= minGroundHeight
        double scale = shapeDistortion / (max - min);
        double offset = fixedMaxVal - (max + min) * scale / 2.0;
        min = min * scale + offset;
        if (min < minVal)
            scale += minVal - min;

        for (int i = 0; i < data.length; i++)
        	data[i] = data[i] * scale + offset;
        return data;
	}
	
	public static double[] perlinNoise(Random r, int range, double amplitude, double persistance, int[] spacing, boolean fixedEnds)
    {
        double[] output = new double[range];
        int flatteningStartLeft = Math.max(1, (int)(range * 0.15)), flatteningStartRight = range - flatteningStartLeft - 1;
        
        for (int o = 0; o < spacing.length; o++)
        {
            double[] noise = generateNoise(r, range, spacing[o], !fixedEnds);

            if (fixedEnds)
                for (int i = 0; i < range; i++)
                {
                    double endScale;

                    // this scales the ends down linearly, which can have a sharp corner.
                    // can we get a smoother transition

                    if (i <= flatteningStartLeft)
                        endScale = (double)i / flatteningStartLeft;
                    else if (i >= flatteningStartRight)
                        endScale = (double)(range - i - 1) / flatteningStartLeft;
                    else
                        endScale = 1;

                    output[i] += noise[i] * amplitude * endScale;
                }
            else
                for (int i = 0; i < range; i++)
                    output[i] += noise[i] * amplitude;

            amplitude *= persistance;
        }
        return output;
    }
	
	private static double[] generateNoise(Random r, int range, int smoothness, boolean wrapEnds)
    {
        double[] output = new double[range];
        if (wrapEnds && (range % smoothness != 0))
            return output; // For wrapping noise, the range must divide by smoothness

        // generate the key points we will interpolate between
        double[] keyPoints = new double[range / smoothness + (wrapEnds ? 3 : 4)];
        for (int i = 0; i < keyPoints.length; i++)
            keyPoints[i] = r.nextDouble() * 2 - 1;

        if (wrapEnds)
        {
            keyPoints[keyPoints.length - 1] = keyPoints[2];
            keyPoints[keyPoints.length - 2] = keyPoints[1];
            keyPoints[keyPoints.length - 3] = keyPoints[0];
        }

        int key = 0;

        // now interpolate all the points - if its right on a key point, substitute it in
        for (int i = 0; i < range; i++)
        {
            if (i % smoothness == 0)
            {
                key++;
                output[i] = keyPoints[key];
                continue;
            }

            double P = keyPoints[key + 2] - keyPoints[key + 1] - keyPoints[key - 1] + keyPoints[key];
            double Q = keyPoints[key - 1] - keyPoints[key] - P;
            double R = keyPoints[key + 1] - keyPoints[key - 1];
            double S = keyPoints[key];

            double mu = ((double)(i - (key - 1) * smoothness)) / smoothness;

            output[i] = P * mu * mu * mu + Q * mu * mu + R * mu + S;
        }

        return output;
    }
}
