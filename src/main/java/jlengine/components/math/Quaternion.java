package jlengine.components.math;

import jlengine.components.ComponentBase;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Quaternion extends ComponentBase {
    public static Vector3f QuaternionToEuler(Quaternionf quaternion) {
        quaternion.normalize();

        float w = quaternion.w;
        float x = quaternion.x;
        float y = quaternion.y;
        float z = quaternion.z;

        float yaw = (float) Math.atan2(2.0f * (w * z + x * y), 1.0f - 2.0f * (y * y + z * z));
        float pitch = (float) Math.asin(2.0f * (w * x - y * z));
        float roll = (float) Math.atan2(2.0f * (w * y + x * z), 1.0f - 2.0f * (x * x + y * y));

        return new Vector3f(yaw, roll, pitch);
    }

    public static Quaternionf EulerToQuaternion(Vector3f vector) {
        float yaw = (float) Math.toRadians(vector.x);
        float pitch = (float) Math.toRadians(vector.y);
        float roll = (float) Math.toRadians(vector.z);

        return new Quaternionf().rotationXYZ(pitch, yaw, roll);
    }
}
