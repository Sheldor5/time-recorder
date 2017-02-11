package at.sheldor5.tr.api.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtils {

  public static byte[] getBytes(final UUID uuid) {
    if (uuid == null) {
      return null;
    }
    final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  public static UUID getUuid(byte[] bytes) {
    if (bytes == null || bytes.length != 16) {
      return null;
    }
    final ByteBuffer bb = ByteBuffer.wrap(bytes);
    long mostSigBits = bb.getLong();
    long leastSigBits = bb.getLong();
    return new UUID(mostSigBits, leastSigBits);
  }

  public static byte[] getRandomUuidBytes() {
    return getBytes(UUID.randomUUID());
  }
}