package util;

public class Pagination {
    public final int page;      // 1-based
    public final int size;      // items per page
    public final int total;     // total items
    public final int totalPages;

    public Pagination(int page, int size, int total) {
        this.page = Math.max(1, page);
        this.size = Math.max(1, size);
        this.total = Math.max(0, total);
        this.totalPages = (int)Math.ceil(total / (double)this.size);
    }

    public int offset() { return (page - 1) * size; }
    public boolean hasPrev() { return page > 1; }
    public boolean hasNext() { return page < totalPages; }
}
