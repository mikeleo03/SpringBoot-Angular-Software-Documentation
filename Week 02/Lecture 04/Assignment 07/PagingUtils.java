import java.util.List;

public class PagingUtils {

    /**
     * Creates a Page object for the specified page number and size.
     *
     * @param items      the list of items to paginate
     * @param pageNumber the page number (1-based)
     * @param size       the number of items per page
     * @param <T>        the type of items
     * @return a Page object containing the items for the specified page
     */
    public static <T> Page<T> getPage(List<T> items, int pageNumber, int size) {
        int totalItems = items.size();
        int fromIndex = (pageNumber - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<T> pageContent = items.subList(fromIndex, toIndex);
        return new Page<>(pageContent, size, pageNumber, totalItems);
    }
}
