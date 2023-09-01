package vjezbe.entitet;

import java.io.Serializable;

/**Rekord koji predstavlja dvoranu
 * @param name
 * @param room
 */
public record Dvorana(Long id, String name, String room) implements Serializable {
    @Override
    public String toString() {
        return name + " " + room;
    }
}