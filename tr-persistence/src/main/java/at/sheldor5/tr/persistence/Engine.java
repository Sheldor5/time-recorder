package at.sheldor5.tr.persistence;

/**
 *
 * @param <T> Type.
 * @param <I> Identifier.
 */
public interface Engine<T, I> {

  T create(T t);

  T read(I i);

  void update(T t);

  void delete(I i);

}
