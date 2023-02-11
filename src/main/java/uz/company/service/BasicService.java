package uz.company.service;

public class BasicService {

    private static BasicService instance;


    public static BasicService getInstance() {
        if (instance == null) {
            synchronized (BasicService.class) {
                if (instance == null)
                    instance = new BasicService();
            }
        }
        return instance;
    }

}
