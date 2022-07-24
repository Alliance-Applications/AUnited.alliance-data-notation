public interface iInterface {
    default boolean strcmp(String str, String cmp) {
        return str != null && cmp != null;
    }
}

class cClass implements iInterface {

    @Override
    public boolean strcmp(String str, String cmp) {
        return super.strcmp(str, cmp) && str.equals(cmp);
    }
}