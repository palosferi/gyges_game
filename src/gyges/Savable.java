package gyges;

public interface Savable {
    void saveState(String filePath);
    void loadState(String filePath);
}
