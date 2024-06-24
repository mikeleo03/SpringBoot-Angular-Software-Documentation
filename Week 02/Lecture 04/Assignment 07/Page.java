import java.util.List;

public class Page<T> {
    private List<T> content;     // Items on the current page
    private int size;            // Number of items per page
    private int pageNumber;      // Current page number
    private int totalPages;      // Total number of pages
    private long totalItems;     // Total number of items

    public Page(List<T> content, int size, int pageNumber, long totalItems) {
        this.content = content;
        this.size = size;
        this.pageNumber = pageNumber;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
    }

    public List<T> getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", size=" + size +
                ", pageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                ", totalItems=" + totalItems +
                '}';
    }

    public boolean hasNext() {
        return pageNumber < totalPages;
    }

    public boolean hasPrevious() {
        return pageNumber > 1;
    }
}