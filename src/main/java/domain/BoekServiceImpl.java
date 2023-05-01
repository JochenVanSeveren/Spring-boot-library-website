package domain;

public class BoekServiceImpl implements BoekService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello %s!", ((name != null) ? name : ""));
    }


}
